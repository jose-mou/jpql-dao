package com.diwa.dao;

import com.diwa.dao.shared.aggregate.Aggregate;
import com.diwa.dao.shared.criteria.Criteria;
import com.diwa.dao.shared.criteria.PersonalCriteria;
import com.diwa.dao.shared.criteria.conditional.*;
import com.diwa.dao.shared.criteria.logical.GroupLogical;
import com.diwa.dao.shared.criteria.logical.Logical;
import com.diwa.dao.shared.criteria.logical.NotLogical;
import com.diwa.dao.shared.entity.*;
import com.diwa.dao.shared.order.OrderBy;
import com.diwa.dao.shared.order.OrderDirection;
import com.diwa.dao.shared.search.SearchInfo;
import com.diwa.dao.utils.DaoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;

/**
 * Implementación de la interfaz DAO. @see DAO.
 * <p>
 * Implementa las consultas contra la BD mediante JPQL. Éste es el lenguaje utilizado por JPA para realizar las
 * consultas a BD.
 */
public class JPQLGenerator {

    /**
     * Número máximo de iteraciones que es tendrán en cuenta a la hora de generar los joins.
     */
    private static final int MAX_JOIN_BUCLE_ITERATION = 20;

    /**
     * Logger.
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static JPQLGenerator instance;

    private JPQLGenerator(){
    }

    public static synchronized JPQLGenerator getInstance(){
        if (instance == null){
            instance = new JPQLGenerator();
        }
        return instance;
    }

    /**
     * {@inheritdoc}
     */
    public JPQLResult update(Class<?> type, final Map<String, Serializable> attribute, final SearchInfo searchInfo) {
        StringBuilder cadena = new StringBuilder("UPDATE ");
        cadena.append(DaoUtils.getEntityName(type));
        cadena.append(String.format(" %s SET ", Criteria.DEFAULT_ENTITY_ALIAS));
        Map<String, Serializable> params = new HashMap<String, Serializable>();

        for (Entry<String, Serializable> att : attribute.entrySet()) {
            cadena.append(Criteria.DEFAULT_ENTITY_ALIAS).append(".");
            cadena.append(att.getKey());
            cadena.append(" = :");
            StringBuilder varname = generateVarName(att.getKey(), att.getValue());
            cadena.append(varname);
            params.put(varname.toString(), att.getValue());
            cadena.append(",");
        }
        cadena.deleteCharAt(cadena.length() - 1);
        Map<String, Serializable> parameters = appendWhereClause(cadena, searchInfo);
        parameters.putAll(params);
        return new JPQLResult(cadena.toString(), parameters);
    }

    /**
     * {@inheritdoc}
     */
    public JPQLResult count(Class<?> type, final SearchInfo searchInfo) {
        SearchInfo auxInfo = searchInfo;
        StringBuilder query = new StringBuilder("SELECT COUNT(e)");
        if (!searchInfo.getFetches().isEmpty()) {
            auxInfo = new SearchInfo();
            auxInfo.setDistinct(searchInfo.isDistinct());
            auxInfo.setEntities(searchInfo.getEntities());
            auxInfo.setOrders(searchInfo.getOrders());
            auxInfo.setOffset(searchInfo.getOffset());
            auxInfo.setPageSize(searchInfo.getPageSize());
            for (Criteria c : searchInfo.getCriterias()) {
                if (!(c instanceof PersonalCriteria && !((PersonalCriteria) c).isApplyInCount())) {
                    auxInfo.addCriteria(c);
                }
            }
        }
        query.append(generateFromClause(type, auxInfo));
        Map<String, Serializable> params = appendWhereClause(query, auxInfo);
        return new JPQLResult(query.toString(), params);
    }


    /**
     * {@inheritdoc}
     */
    public JPQLResult aggregate(Class<?> type, Aggregate aggregate, String field, SearchInfo searchInfo) {
        StringBuilder query = new StringBuilder("SELECT ");
        query.append(aggregate.getOperator());
        if (searchInfo.isDistinct()) {
            query.append("(DISTINCT");
        }
        query.append(String.format("(%s.", Criteria.DEFAULT_ENTITY_ALIAS));
        query.append(field);
        query.append(")");
        if (searchInfo.isDistinct()) {
            query.append(")");
        }
        query.append(generateFromClause(type, searchInfo));
        Map<String, Serializable> params = appendWhereClause(query, searchInfo);
        return new JPQLResult(query.toString(), params);
    }

    /**
     * {@inheritdoc}
     */
    public JPQLResult find(Class<?> type, final SearchInfo searchInfo) {
        StringBuilder query = new StringBuilder("SELECT ");
        if (searchInfo.isDistinct()) {
            query.append("DISTINCT ");
        }
        query.append(Criteria.DEFAULT_ENTITY_ALIAS);
        query.append(generateFromClause(type, searchInfo));
        Map<String, Serializable> params = appendWhereClause(query, searchInfo);
        query.append(generateOrderClause(searchInfo));
        return new JPQLResult(query.toString(), params);
    }

    /**
     * Componemos el FROM de la sentencia JPQL que va a ser ejecutada.
     * 
     * @param searchInfo Conjunto de joins/criterios que han sido utilizados en la query.
     * @return Cadena JPQL con el FROM de la sentencia que va a ser ejecutada.
     */
    protected StringBuilder generateFromClause (Class<?> type, final SearchInfo searchInfo) {
        // Componemos el from para la entidad base, sobre la que realizamos la query
        StringBuilder query = new StringBuilder(" FROM ");
        query.append(DaoUtils.getEntityName(type));
        // La entidad base siempre se nombrará con el alias e
        query.append(" AS ").append(Criteria.DEFAULT_ENTITY_ALIAS);

        List<Entity> entities = searchInfo.getEntities();
        List<JoinEntity> joins = new ArrayList<JoinEntity>();

        // En este for separamos las entidades en JoinEntity o DomainEntity. Además generamos las entidades de dominio
        // ya que són las primeras en la consulta.
        for (Entity e : entities) {
            if (e instanceof JoinEntity) {
                joins.add((JoinEntity) e);
            } else if (e instanceof DomainEntity) {
                generateDomainClause((DomainEntity) e, query);
            }
        }

        // Ordenamos los joins para que el orden sea correcto
        joins = orderJoins(joins);
        // Generamos los joins correspondientes
        for (JoinEntity je : joins) {
            generateJoinClause(query, je);
        }

        // Generamos los fetchs
        for (FetchJoin f : searchInfo.getFetches()) {
            generateJoinClause(query, f);
        }
        return query;
    }

    /**
     * Ordena los joins. Se generará una lista en el orden correcto de procesado.
     */
    protected List<JoinEntity> orderJoins(final List<JoinEntity> joins) {
        List<JoinEntity> pendingProcess = new ArrayList<JoinEntity>(joins);
        List<JoinEntity> orderJoins = new ArrayList<JoinEntity>(joins.size());
        int count = 0;
        while (!pendingProcess.isEmpty() && count < MAX_JOIN_BUCLE_ITERATION) {
            for (Iterator<JoinEntity> it = pendingProcess.listIterator(); it.hasNext();) {
                JoinEntity je = it.next();
                Entity e = je.getEntity();
                if (e == null || e instanceof DomainEntity || orderJoins.contains(e)) {
                    // Si no tiene entidad, si pertenece a una entidad del dominio (ya añadida), si se ha añadido la
                    // entidad al dominio.
                    orderJoins.add(je);
                    it.remove();
                }
            }
            count++;
        }
        return orderJoins;
    }

    /**
     * Genera la calusula correspondiente a las entidades.
     */
    protected StringBuilder generateDomainClause (final DomainEntity de, final StringBuilder query) {
        query.append(", ");
        query.append(de.getClase().getSimpleName());
        query.append(" ");
        query.append(de.getAlias());
        return query;
    }

    /**
     * Añade a la cadena JPQL establecida, la cadena correspondiente al Join indicado.
     * 
     * @param query Cadena JPQL que contiene la consulta que va a lanzarse contra la BD.
     * @param join Join que va a ser asociado a la sentencia.
     */
    protected void generateJoinClause (final StringBuilder query, final Join join) {
        query.append(" ");
        query.append(join.getOperator().getOperator());
        if (join.getEntity() == null) {
            query.append(String.format(" %s.", Criteria.DEFAULT_ENTITY_ALIAS));
        } else {
            query.append(" ");
            query.append(join.getEntity().getAlias());
            query.append(".");
        }
        query.append(join.getName());
        query.append(" ");
        if (join instanceof Entity && ((Entity) join).getAlias() != null) {
            query.append(((Entity) join).getAlias());
        }
    }

    /**
     * Modifica la cadena que contiene la consulta que va a lanzarse contra la BD, añadiendo el JPQL correspondiente a
     * las condiciones de búsqueda establecidas. Devolverá los parámetros generados como consecuencia de los criterios
     * de búsqueda.
     * 
     * @param query Cadena JPQL que contiene la consulta que va a lanzarse contra la BD.
     * @param searchInfo Objeto <code>SearchInfo</code> que encapsula a los diferentes criterios establecidos en la
     *            consulta.
     * @return Parámetros que van a ser pasados a la Query y que contienen las variables de la sentencia JPQL junto a
     *         los valores con los que tenemos que comparar.
     */
    protected Map<String, Serializable> appendWhereClause (final StringBuilder query, final SearchInfo searchInfo) {
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        List<Criteria> criterias = searchInfo.getCriterias();
        if (!criterias.isEmpty()) {
            query.append(" WHERE ");
            for (Iterator<Criteria> it = criterias.listIterator(); it.hasNext();) {
                Criteria criteria = it.next();
                query.append(generateCriteriaClause(criteria, params));
                // Realizamos un AND de todos los criterios pasados
                if (it.hasNext()) {
                    query.append(" AND ");
                }
            }
        }
        return params;
    }

    /**
     * Genera una cadena JPQL asociada a los diferentes criterios de ordenación establecidos en la búsqueda. Esta cadena
     * generada corresponde a la parte del ORDER BY de una consulta a BD.
     * 
     * @param searchInfo Objeto <code>SearchInfo</code> que encapsula a los diferentes criterios establecidos en la
     *            consulta.
     * @return Cadena JPQL generada a partir de los criterios de ordenación establecidos.
     */
    protected StringBuilder generateOrderClause (final SearchInfo searchInfo) {
        StringBuilder clause = new StringBuilder();
        List<OrderBy> orders = searchInfo.getOrders();
        boolean orderedById = false;
        clause.append(" ORDER BY ");
        if (!orders.isEmpty()) {
            for (Iterator<OrderBy> it = orders.listIterator(); it.hasNext();) {
                OrderBy order = it.next();
                if (order.getEntity() == null) {
                    clause.append(Criteria.DEFAULT_ENTITY_ALIAS);
                } else {
                    clause.append(order.getEntity().getAlias());
                }
                clause.append(".");
                clause.append(order.getName());
                clause.append(" ");
                clause.append(order.getDirection());
                if (!orderedById && "id".equalsIgnoreCase(order.getName())) {
                    orderedById = true;
                }
                if (it.hasNext()) {
                    clause.append(", ");
                }
            }
            if (!orderedById) {
                clause.append(", ");
                addOrderByIdClause(clause);
            }
        } else {
            addOrderByIdClause(clause);
        }

        return clause;
    }

    /**
     * Añade el criterio de ordenación por identificador "id"
     * 
     * @param clausule la cadena JPQL que se esta generando a partir de los criterios de ordenación establecidos
     */
    protected void addOrderByIdClause (StringBuilder clausule) {
        clausule.append(Criteria.DEFAULT_ENTITY_ALIAS);
        clausule.append(".");
        clausule.append("id");
        clausule.append(" ");
        clausule.append(OrderDirection.ASC);
    }

    /**
     * Genera una cadena JPQL asociada a un criterio de búsqueda, ya sea de tipo lógico como condicional.
     * 
     * @param criteria Criterio de búsqueda que generará la cadena JPQL.
     * @param params Conjunto de parámetros que van a ser utilizados en la Query. A este conjunto se añadirán los
     *            parámetros generados a partir del criterio (E/S).
     * @return Cadena JPQL correspondiente al criterio.
     */
    protected StringBuilder generateCriteriaClause (final Criteria criteria, final Map<String, Serializable> params) {
        StringBuilder clausule = null;
        if (criteria instanceof Conditional) {
            clausule = generateConditionalClause((Conditional) criteria, params);
        } else if (criteria instanceof Logical) {
            clausule = generateLogicalClause((Logical) criteria, params);
        } else if (criteria instanceof PersonalCriteria) {
            clausule = generatePersonalCriteriaClause((PersonalCriteria) criteria, params);
        }
        return clausule;
    }

    /**
     * Genera una cadena JPQL asociada a una condición.
     * 
     * @param conditional Criterio de tipo condicional a partir del cual vamos a generar la cadena JPQL.
     * @param params Conjunto de parámetros que van a ser utilizados en la Query. A este conjunto se añadirán los
     *            parámetros generados a partir del criterio (E/S).
     * @return Cadena JPQL correspondiente al criterio condicional.
     */
    protected StringBuilder generateConditionalClause (final Conditional conditional, final Map<String, Serializable> params) {
        StringBuilder result = new StringBuilder();
        if (conditional.getEntity() == null) {
            result.append(Criteria.DEFAULT_ENTITY_ALIAS).append(".");
        } else {
            result.append(conditional.getEntity().getAlias());
            result.append(".");
        }
        result.append(conditional.getName());
        if (conditional instanceof CaseSensitiveConditional
                && !((CaseSensitiveConditional) conditional).isCaseSensitive()) {
            // En el caso de que el LIKE sea case sensitive realizamos un UPPER de los datos de la BD
            result.insert(0, "UPPER(");
            result.append(")");
        }
        result.append(" ");
        result.append(conditional.getOperator().getOperator());
        result.append(" ");
        if (conditional instanceof BetweenConditional) {
            BetweenConditional betweenConditional = (BetweenConditional) conditional;
            StringBuilder varName = generateVarName(betweenConditional.getName(), betweenConditional.getValue1());
            result.append(":");
            result.append(varName);
            params.put(varName.toString(), betweenConditional.getValue1());
            result.append(" AND ");
            StringBuilder varName2 = generateVarName(betweenConditional.getName(), betweenConditional.getValue2());
            result.append(":");
            result.append(varName2);
            params.put(varName2.toString(), betweenConditional.getValue2());
        } else if (conditional instanceof InConditional) {
            InConditional inConditional = (InConditional) conditional;
            StringBuilder varName = generateVarName(conditional.getName(), inConditional.getValues());
            if (inConditional.getValues().size() == 1) {
                // En el caso de sólo 1 elemento creamos un EQ
                result.replace(result.lastIndexOf(ConditionalOperator.IN.getOperator()), result.length() - 1,
                        ConditionalOperator.EQ.getOperator());
                result.append(":");
                result.append(varName);
                if (inConditional.isCaseSensitive()) {
                    params.put(varName.toString(), inConditional.getValues().get(0));
                } else {
                    params.put(varName.toString(), inConditional.getValues().get(0).toString().toUpperCase());
                }
            } else if (inConditional.getValues().size() > 1) {
                // Añadimos paréntesis aunque no lo diga la especificación de JPA 2.
                // Ver incidencia : https://hibernate.onjira.com/browse/HHH-7407
                result.append("(");
                result.append(":");
                result.append(varName);
                result.append(")");
                if (inConditional.isCaseSensitive()) {
                    params.put(varName.toString(), (Serializable) inConditional.getValues());
                } else {
                    ArrayList<String> upperList = new ArrayList<String>(inConditional.getValues().size());
                    for (Serializable e : inConditional.getValues()) {
                        upperList.add(e.toString().toUpperCase());
                    }
                    params.put(varName.toString(), upperList);
                }
            }
        } else if (conditional instanceof ValueComparison) {
            ValueComparison valueComparison = (ValueComparison) conditional;
            StringBuilder varName = generateVarName(valueComparison.getName(), valueComparison.getValue());
            result.append(":");
            result.append(varName);
            if (valueComparison.isCaseSensitive()) {
                params.put(varName.toString(), valueComparison.getValue());
            } else {
                params.put(varName.toString(), valueComparison.getValue().toString().toUpperCase());
            }
        } else if (conditional instanceof FieldComparison) {
            FieldComparison fieldComparison = (FieldComparison) conditional;
            String comparison;
            if (fieldComparison.getEntity2() == null) {
                comparison = Criteria.DEFAULT_ENTITY_ALIAS + ".";
            } else {
                comparison = fieldComparison.getEntity2().getAlias() + ".";
            }
            comparison = comparison + fieldComparison.getField();
            if (fieldComparison.isCaseSensitive()) {
                result.append(comparison);
            } else {
                result.append("UPPER(").append(comparison).append(")");
            }
        } else if (conditional instanceof LikeConditional) {
            LikeConditional likeConditional = (LikeConditional) conditional;
            StringBuilder varName = generateVarName(likeConditional.getName(), likeConditional.getValue());
            result.append(":");
            result.append(varName);
            String value = likeConditional.getValue();
            if (!likeConditional.isCaseSensitive()) {
                value = value.toUpperCase();
            }
            if (likeConditional.isLeftWildcard()) {
                value = "%" + value;
            }
            if (likeConditional.isRightWildcard()) {
                value = value + "%";
            }
            params.put(varName.toString(), value);
        }
        return result;
    }

    /**
     * Genera la sentencia JPQL correspondiente al criterio personalizado.
     * 
     * @param criteria Criterio personalizado.
     * @return Cadena JPQL correspondiente al criterio.
     */
    protected StringBuilder generatePersonalCriteriaClause (final PersonalCriteria criteria, final Map<String, Serializable> params) {
        String query = criteria.getQuery();
        for (Entry<String, PersonalCriteria.ReplaceProperty> p : criteria.getProperties().entrySet()) {
            String nuevo;
            PersonalCriteria.ReplaceProperty newProperty = p.getValue();
            if (newProperty.getReferenceEntity() == null) {
                nuevo = String.format("%s.%s", Criteria.DEFAULT_ENTITY_ALIAS, newProperty.getTargetProperty());
            } else {
                nuevo = String.format("%s.%s", newProperty.getReferenceEntity().getAlias(),
                        newProperty.getTargetProperty());
            }
            query = query.replaceAll(p.getKey(), nuevo);
        }
        for (Entry<String, Serializable> p : criteria.getParameters().entrySet()) {
            if (query.contains(":" + p.getKey())) {
                params.put(p.getKey(), p.getValue());
            } else {
                logger.warn("Se pasa como parámetro una variable que no es utilizada en el criterio.");
            }
        }
        return new StringBuilder(query);
    }

    /**
     * Genera la cadena JPQL correspondiente el criterio lógico indicado.
     * 
     * @param logical Criterio lógico a partir del cual vamos a generar la cadena JPQL.
     * @param params Conjunto de parámetros que van a ser utilizados en la Query. A este conjunto se añadirán los
     *            parámetros generados a partir del criterio lógico (E/S).
     * @return Cadena JPQL correspondiente al criterio lógico.
     */
    protected StringBuilder generateLogicalClause (final Logical logical, final Map<String, Serializable> params) {
        StringBuilder clausule = new StringBuilder();
        if (logical instanceof NotLogical) {
            clausule.append("NOT (");
            clausule.append(generateCriteriaClause(((NotLogical) logical).getExpresion(), params));
            clausule.append(")");
        } else if (logical instanceof GroupLogical) {
            GroupLogical groupLogical = (GroupLogical) logical;
            Collection<Criteria> conditionals = groupLogical.getContitionals();
            if (conditionals != null) {
                if (conditionals.size() > 1) {
                    clausule.append("(");
                }
                for (Iterator<Criteria> it = conditionals.iterator(); it.hasNext();) {
                    Criteria criteria = it.next();
                    clausule.append(generateCriteriaClause(criteria, params));
                    if (it.hasNext()) {
                        clausule.append(" ");
                        clausule.append(groupLogical.getOperator().getOperator());
                        clausule.append(" ");
                    }
                }
                if (conditionals.size() > 1) {
                    clausule.append(")");
                }
            }
        }

        return clausule;
    }

    /**
     * Genera un nombre de variable para el atributo indicado y el valor con el que vamos a comparar. Para ello tenemos
     * en cuenta el hashCode del valor.
     * 
     * @param field Nombre del atributo
     * @param value Valor que tiene el atributo y con el cual vamos a comparar.
     * @return Nombre generado para la variable.
     */
    protected StringBuilder generateVarName(final String field, final Object value) {
        StringBuilder varName = new StringBuilder(field.replace(".", "_"));
        varName.append("_");
        if (value != null) {
            varName.append(Math.abs(value.hashCode()));
        }
        return varName;
    }

}
