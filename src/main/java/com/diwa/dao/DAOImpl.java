package com.diwa.dao;

import com.diwa.dao.search.ScrollResult;
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
import com.diwa.dao.shared.search.SearchResult;
import com.diwa.dao.utils.DaoUtils;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityExistsException;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.Map.Entry;

/**
 * Implementación de la interfaz DAO. @see DAO.
 * <p>
 * Implementa las consultas contra la BD mediante JPQL. Éste es el lenguaje utilizado por JPA para realizar las
 * consultas a BD.
 * <p>
 * <code>
 *      DAO< Persona ,Integer > dao = new DAOImpl< Persona , Integer >();
 *      List< Persona > personas = dao.findAll();
 * </code>
 * 
 * @param <T> Clase de la entidad del dominio a la que representa el DAO. Esta clase ha de estar anotada en el dominio.
 * @param <PK> Clase a la que corresponde la primary key de la entidad del dominio a la que representa. La primary key
 *            ha de implementar la interfaz <code>Serializable</code>.
 */
public class DAOImpl<T, PK extends Serializable> implements DAO<T, PK> {

    /**
     * Número máximo de iteraciones que es tendrán en cuenta a la hora de generar los joins.
     */
    private static final int MAX_JOIN_BUCLE_ITERATION = 20;

    /**
     * Clase de la entidad a la que pertenece el DAO.
     */
    private Class<T> type;

    /**
     * SessionFactory al que se encuentra asociado el DAO.
     */
    private transient SessionFactory sessionFactory;

    /**
     * Indica si este DAO debe usar la cache de segundo nivel de Hibernate.
     */
    private boolean useCache = false;

    /**
     * Logger.
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Constructor por defecto.
     */
    public DAOImpl() {
        type = getPersistentClass();
    }

    /**
     * Constructor a partir de la entidad del dominio a la que accede el DAO.
     * 
     * @param type Clase que corresponde a la entidad del dominio a la que accede el DAO.
     */
    public DAOImpl(final Class<T> type) {
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    public Class<T> getPersistentClass() {
        if (this.type == null) {
            Object objeto = getClass().getGenericSuperclass();
            if (objeto instanceof ParameterizedType) {
                Type type2 = ((ParameterizedType) objeto).getActualTypeArguments()[0];
                if (type2 instanceof Class<?>) {
                    this.type = (Class<T>) type2;
                } else if (type2 instanceof ParameterizedType) {
                    this.type = (Class<T>) ((ParameterizedType) type2).getRawType();
                }
            }
        }
        return this.type;
    }

    /**
     * {@inheritdoc}
     */
    @SuppressWarnings("unchecked")
    public T save(final T entity) {
        T persistedEntity = entity;
        // If the entity has a null id, then use the persist method, otherwise use the merge method.
        if (!DaoUtils.hasId(entity)) {
            try {
                getSession().persist(entity);
            } catch (EntityExistsException e) {
                // if the entity exists then we call merge
                persistedEntity = (T) getSession().merge(entity);
            }
        } else {
            // if the entity exists then we call merge
            persistedEntity = (T) getSession().merge(entity);
        }
        return persistedEntity;
    }

    /**
     * {@inheritdoc}
     */
    public Collection<T> save(final Collection<T> entities) {
        Collection<T> aux = new ArrayList<T>(entities.size());
        for (T entity : entities) {
            aux.add(save(entity));
        }
        return aux;
    }

    /**
     * {@inheritdoc}
     */
    public void update(final String field, final Serializable value, final Criteria... criterias) {
        SearchInfo searchInfo = new SearchInfo();
        searchInfo.getCriterias().addAll(Arrays.asList(criterias));
        Map<String, Serializable> attribute = new HashMap<String, Serializable>(1);
        attribute.put(field, value);
        update(attribute, searchInfo);
    }

    /**
     * {@inheritdoc}
     */
    public void update(final Map<String, Serializable> attribute, final SearchInfo searchInfo) {
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
        // Añade los parámetros generados en el where
        params.putAll(appendWhereClausule(cadena, searchInfo));
        logger.debug("Query final: " + cadena.toString());
        Query query = createQuery(cadena.toString());
        addCriteriaParams(query, params);
        query.executeUpdate();
    }

    /**
     * {@inheritdoc}
     */
    public void delete(final PK id) {
        getSession().delete(read(id));
    }

    /**
     * {@inheritdoc}
     */
    @SuppressWarnings("unchecked")
    public void delete(final T entity) {
        if (entity != null) {
            T managedEntity = entity;
            if (getSession().contains(entity)) {
                managedEntity = (T) getSession().merge(entity);
            }
            getSession().delete(managedEntity);
        }
    }

    /**
     * {@inheritdoc}
     */
    public void delete(final Collection<T> entities) {
        for (T entity : entities) {
            delete(entity);
        }
    }

    /**
     * {@inheritdoc}
     */
    public void executeUpdate(final String query, final Map<String, Serializable> params) {
        Query q = createQuery(query.toString());
        addCriteriaParams(q, params);
        q.executeUpdate();
    }

    /**
     * {@inheritdoc}
     */
    public void flushAndClear() {
        getSession().flush();
        getSession().clear();
    }

    /**
     * {@inheritdoc}
     */
    @SuppressWarnings("unchecked")
    public T read(final PK id) {
        return (T) getSession().get(type, id);
    }

    /**
     * {@inheritdoc}
     */
    public T read(final PK id, final FetchJoin... fetch) {
        SearchInfo searchInfo = new SearchInfo();
        searchInfo.getFetches().addAll(Arrays.asList(fetch));
        String pkName = DaoUtils.getPrimaryKeyName(type);
        searchInfo.addCriteria(ValueComparison.eq(pkName, id));
        return findSingle(searchInfo);
    }

    /**
     * {@inheritdoc}
     */
    @SuppressWarnings("unchecked")
    public T refresh(final T entity) {
        Session session = getSession();
        T manageEntity = entity;
        if (!session.contains(entity)) {
            manageEntity = (T) session.merge(entity);
        }
        session.refresh(manageEntity);
        return manageEntity;
    }

    /**
     * {@inheritdoc}
     */
    @SuppressWarnings("unchecked")
    public T refresh(final PK id) {
        Session session = getSession();
        T entity = (T) session.get(this.type, id);
        session.refresh(entity);
        return entity;
    }

    /**
     * {@inheritdoc}
     */
    public Collection<T> refresh(final Collection<T> entities) {
        Collection<T> aux = new ArrayList<T>(entities.size());
        for (T entity : entities) {
            aux.add(refresh(entity));
        }
        return aux;
    }

    /**
     * {@inheritdoc}
     */
    public Long count() {
        String entityName = DaoUtils.getEntityName(type);
        StringBuilder cadena = new StringBuilder("SELECT count(*) FROM ");
        cadena.append(entityName);
        cadena.append(" AS ").append(Criteria.DEFAULT_ENTITY_ALIAS);
        logger.debug("Query final: " + cadena.toString());
        Query query = createQuery(cadena.toString());
        Long count = (Long) query.uniqueResult();
        return count;
    }

    /**
     * {@inheritdoc}
     */
    public Long count(final Criteria... criterias) {
        SearchInfo searchInfo = new SearchInfo();
        searchInfo.getCriterias().addAll(Arrays.asList(criterias));
        return count(searchInfo);
    }

    /**
     * {@inheritdoc}
     */
    public Long count(final SearchInfo searchInfo) {
        SearchInfo auxInfo = searchInfo;
        StringBuilder query = new StringBuilder("SELECT ");
        if (auxInfo.isDistinct()) {
            query.append("DISTINCT(COUNT(e)) ");
        } else {
            query.append("COUNT(e) ");
        }
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
        query.append(generateFromClausule(auxInfo));
        Map<String, Serializable> params = appendWhereClausule(query, auxInfo);
        logger.debug("Query final: " + query.toString());
        Query q = createQuery(query.toString());
        addCriteriaParams(q, params);
        Long count = (Long) q.uniqueResult();
        return count;
    }

    /**
     * {@inheritdoc}
     */
    public Number aggregate(final Aggregate aggregate, final String field, final SearchInfo searchInfo) {
        return aggregate(aggregate, field, searchInfo, Number.class);
    }

    /**
     * {@inheritdoc}
     */
    @SuppressWarnings("unchecked")
    public <K extends Serializable> K aggregate(Aggregate aggregate, String field, SearchInfo searchInfo, Class<K> clazz) {
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
        query.append(generateFromClausule(searchInfo));
        Map<String, Serializable> params = appendWhereClausule(query, searchInfo);
        logger.debug("Query final: " + query.toString());
        Query q = createQuery(query.toString());
        addCriteriaParams(q, params);
        K count = (K) q.uniqueResult();
        return count;
    }

    /**
     * {@inheritdoc}
     */
    public SearchResult<T> find(final Map<String, Serializable> propertyValue) {
        SearchInfo searchInfo = new SearchInfo();
        for (Entry<String, Serializable> entry : propertyValue.entrySet()) {
            searchInfo.addCriteria(ValueComparison.eq(entry.getKey(), entry.getValue()));
        }
        return find(searchInfo);
    }

    /**
     * {@inheritdoc}
     */
    public SearchResult<T> find(final String property, final Serializable value) {
        SearchInfo searchInfo = new SearchInfo();
        searchInfo.addCriteria(ValueComparison.eq(property, value));
        return find(searchInfo);
    }

    /**
     * {@inheritdoc}
     */
    public SearchResult<T> find(final Criteria... criteria) {
        SearchInfo searchInfo = new SearchInfo();
        searchInfo.getCriterias().addAll(Arrays.asList(criteria));
        return find(searchInfo);
    }

    /**
     * {@inheritdoc}
     */
    public SearchResult<T> findAll() {
        return find(new SearchInfo());
    }

    /**
     * {@inheritdoc}
     */
    @SuppressWarnings("unchecked")
    public SearchResult<T> find(final SearchInfo searchInfo) {
        StringBuilder query = new StringBuilder("SELECT ");
        if (searchInfo.isDistinct()) {
            query.append("DISTINCT ");
        }
        query.append(Criteria.DEFAULT_ENTITY_ALIAS).append(" ");
        query.append(generateFromClausule(searchInfo));
        Map<String, Serializable> params = appendWhereClausule(query, searchInfo);
        query.append(generateOrderClausule(searchInfo));
        Query q = createQuery(query.toString());
        addCriteriaParams(q, params);
        logger.debug("Query final: " + query.toString());

        Long total = count(searchInfo);
        int offset = searchInfo.getOffset();
        int pageSize = searchInfo.getPageSize();

        // TODO Volver a la versión anterior, esta provoca fallos al llegar al final del listado
        if (offset != -1 && pageSize != -1) {
            q.setFirstResult(offset);
            q.setMaxResults(pageSize);
        }
        return new SearchResult<T>(q.list(), total);
    }

    /**
     * {@inheritdoc}
     */
    @SuppressWarnings("unchecked")
    public List<T> findWithoutCount(final SearchInfo searchInfo) {
        StringBuilder query = new StringBuilder("SELECT ");
        if (searchInfo.isDistinct()) {
            query.append("DISTINCT ");
        }
        query.append(Criteria.DEFAULT_ENTITY_ALIAS).append(" ");
        query.append(generateFromClausule(searchInfo));
        Map<String, Serializable> params = appendWhereClausule(query, searchInfo);
        query.append(generateOrderClausule(searchInfo));
        Query q = createQuery(query.toString());
        addCriteriaParams(q, params);

        int offset = searchInfo.getOffset();
        int pageSize = searchInfo.getPageSize();

        if (offset != -1 && pageSize != -1) {
            q.setFirstResult(offset);
            q.setMaxResults(pageSize);
        }
        return q.list();
    }

    /**
     * {@inheritdoc}
     */
    public T findSingle(final Criteria... criterias) {
        SearchInfo searchInfo = new SearchInfo();
        searchInfo.getCriterias().addAll(Arrays.asList(criterias));
        return findSingle(searchInfo);
    }

    /**
     * {@inheritdoc}
     */
    @SuppressWarnings("unchecked")
    public T findSingle(final SearchInfo searchInfo) {
        StringBuilder query = new StringBuilder(String.format("SELECT DISTINCT(%s)", Criteria.DEFAULT_ENTITY_ALIAS));
        query.append(generateFromClausule(searchInfo));
        Map<String, Serializable> params = appendWhereClausule(query, searchInfo);
        logger.debug("Query final: " + query.toString());
        Query q = createQuery(query.toString());
        addCriteriaParams(q, params);
        return (T) q.uniqueResult();
    }

    /**
     * Componemos el FROM de la sentencia JPQL que va a ser ejecutada.
     * 
     * @param searchInfo Conjunto de joins/criterios que han sido utilizados en la query.
     * @return Cadena JPQL con el FROM de la sentencia que va a ser ejecutada.
     */
    protected StringBuilder generateFromClausule(final SearchInfo searchInfo) {
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
                generateDomainClausule((DomainEntity) e, query);
            }
        }

        // Ordenamos los joins para que el orden sea correcto
        joins = orderJoins(joins);
        // Generamos los joins correspondientes
        for (JoinEntity je : joins) {
            generateJoinClausule(query, je);
        }

        // Generamos los fetchs
        for (FetchJoin f : searchInfo.getFetches()) {
            generateJoinClausule(query, f);
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
    protected StringBuilder generateDomainClausule(final DomainEntity de, final StringBuilder query) {
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
    protected void generateJoinClausule(final StringBuilder query, final Join join) {
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
    protected Map<String, Serializable> appendWhereClausule(final StringBuilder query, final SearchInfo searchInfo) {
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        List<Criteria> criterias = searchInfo.getCriterias();
        if (!criterias.isEmpty()) {
            query.append(" WHERE ");
            for (Iterator<Criteria> it = criterias.listIterator(); it.hasNext();) {
                Criteria criteria = it.next();
                query.append(generateCriteriaClausule(criteria, params));
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
    protected StringBuilder generateOrderClausule(final SearchInfo searchInfo) {
        StringBuilder clausule = new StringBuilder();
        List<OrderBy> orders = searchInfo.getOrders();
        boolean orderedById = false;
        clausule.append(" ORDER BY ");
        if (!orders.isEmpty()) {
            for (Iterator<OrderBy> it = orders.listIterator(); it.hasNext();) {
                OrderBy order = it.next();
                if (order.getEntity() == null) {
                    clausule.append(Criteria.DEFAULT_ENTITY_ALIAS);
                } else {
                    clausule.append(order.getEntity().getAlias());
                }
                clausule.append(".");
                clausule.append(order.getName());
                clausule.append(" ");
                clausule.append(order.getDirection());
                if (!orderedById && "id".equalsIgnoreCase(order.getName())) {
                    orderedById = true;
                }
                if (it.hasNext()) {
                    clausule.append(", ");
                }
            }
            if (!orderedById) {
                clausule.append(", ");
                addOrderByIdClausule(clausule);
            }
        } else {
            addOrderByIdClausule(clausule);
        }

        return clausule;
    }

    /**
     * Añade el criterio de ordenación por identificador "id"
     * 
     * @param clausule la cadena JPQL que se esta generando a partir de los criterios de ordenación establecidos
     */
    protected void addOrderByIdClausule(StringBuilder clausule) {
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
    protected StringBuilder generateCriteriaClausule(final Criteria criteria, final Map<String, Serializable> params) {
        StringBuilder clausule = null;
        if (criteria instanceof Conditional) {
            clausule = generateConditionalClausule((Conditional) criteria, params);
        } else if (criteria instanceof Logical) {
            clausule = generateLogicalClausule((Logical) criteria, params);
        } else if (criteria instanceof PersonalCriteria) {
            clausule = generatePersonalCriteriaClausule((PersonalCriteria) criteria, params);
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
    protected StringBuilder generateConditionalClausule(final Conditional conditional,
            final Map<String, Serializable> params) {
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
                result.append(" :");
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
    protected StringBuilder generatePersonalCriteriaClausule(final PersonalCriteria criteria,
            final Map<String, Serializable> params) {
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
            query.replaceAll(p.getKey(), nuevo);
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
    protected StringBuilder generateLogicalClausule(final Logical logical, final Map<String, Serializable> params) {
        StringBuilder clausule = new StringBuilder();
        if (logical instanceof NotLogical) {
            clausule.append("NOT (");
            clausule.append(generateCriteriaClausule(((NotLogical) logical).getExpresion(), params));
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
                    clausule.append(generateCriteriaClausule(criteria, params));
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
     * Añade a la query que vamos a ejecutar, un conjunto de parámetros, que contienen el nombre de la variable de
     * parámetro y el valor con el que se va a comparar.
     * 
     * @param query Consulta que va a ser ejecutada
     * @param params Map que contiene los nombres de la variables utilizados en la query, asociados con el valor con el
     *            cual vamos a comparar.
     */
    protected void addCriteriaParams(final Query query, final Map<String, Serializable> params) {
        for (Entry<String, Serializable> entry : params.entrySet()) {
            if (entry.getValue() instanceof Collection<?>) {
                query.setParameterList(entry.getKey(), (Collection<?>) entry.getValue());
            } else {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
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

    /**
     * @return Clase que corresponde a la entidad del dominio a la que accede el DAO.
     */
    public Class<T> getType() {
        return type;
    }

    /**
     * Permite establecer/modificar la entidad del dominio a la que accede el DAO.
     * 
     * @param type Clase que corresponde a la entidad del dominio a la que accede el DAO.
     */
    public void setType(final Class<T> type) {
        this.type = type;
    }

    /**
     * {@inheritdoc}
     */
    @Override
    public ScrollResult<T> findScroll(SearchInfo si) {
        return new ScrollResult<T>(generateScrollResult(si, null));
    }

    /**
     * {@inheritdoc}
     */
    @Override
    public ScrollResult<Map<String, Serializable>> findMapScroll(SearchInfo si) {
        return new ScrollResult<Map<String, Serializable>>(generateScrollResult(si, Transformers.ALIAS_TO_ENTITY_MAP));
    }

    @Override
    public ScrollResult<Serializable> findScroll(SearchInfo si, ResultTransformer transformer) {
        return new ScrollResult<Serializable>(generateScrollResult(si, transformer));
    }

    /**
     * {@inheritdoc}
     */
    @Override
    public ScrollResult<T> findQueryScroll(Query q) {
        return new ScrollResult<T>(generateScrollResult(q, null));
    }

    @Override
    public ScrollResult<Map<String, Serializable>> findQueryMapScroll(Query si) {
        return new ScrollResult<Map<String, Serializable>>(generateScrollResult(si, Transformers.ALIAS_TO_ENTITY_MAP));
    }

    /**
     * {@inheritdoc}
     */
    @Override
    public ScrollResult<Serializable> findQueryScroll(Query query, ResultTransformer transformer) {
        return new ScrollResult<Serializable>(generateScrollResult(query, transformer));
    }

    /**
     *
     * @param query
     * @param transformer
     * @return
     */
    protected ScrollableResults generateScrollResult(final Query query, ResultTransformer transformer) {
        if (transformer != null) {
            query.setResultTransformer(transformer);
        }
        return query.scroll();
    }

    /**
     * Genera un ScrollableResults a partir de los criterios de búsquedas indicados en el SearchInfo. En el caso de que
     * se indique un resultTransformer se aplicará.
     */
    protected ScrollableResults generateScrollResult(final SearchInfo si, ResultTransformer transformer) {

        StringBuilder query = new StringBuilder("SELECT ");
        if (si.isDistinct()) {
            query.append(" DISTINCT ");
        }
        query.append(Criteria.DEFAULT_ENTITY_ALIAS).append(" ");
        query.append(generateFromClausule(si));
        Map<String, Serializable> params = appendWhereClausule(query, si);
        query.append(generateOrderClausule(si));

        Query q = createQuery(query.toString());
        addCriteriaParams(q, params);
        logger.debug("Query final: " + query.toString());
        if (transformer != null) {
            q.setResultTransformer(transformer);
        }

        return q.scroll();
    }

    /**
     * Crea una query de hibernate a partir del HQL.
     */
    protected Query createQuery(String queryString) {
        Session session = getSession();
        Query queryObject = session.createQuery(queryString);
        queryObject.setCacheable(useCache);
        return queryObject;
    }

    /**
     * <code>return sessionFactory.getCurrentSession();</code>
     * 
     * @return La sesión actual.
     */
    protected Session getSession() {
        if (sessionFactory == null) {
            logger.error("Existe más de un bean del tipo SessionFactory");
        }
        return sessionFactory.getCurrentSession();
    }

    /**
     * Indica si la entidad asociada al DAO utiliza cache de hibernate.
     */
    public boolean isUseCache() {
        return useCache;
    }

    /**
     * Establece si el DAO utiliza cache de hibernate.
     */
    public void setUseCache(boolean useCache) {
        this.useCache = useCache;
    }

    /**
     * @return the sessionFactory
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * @param sessionFactory the sessionFactory to set
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
