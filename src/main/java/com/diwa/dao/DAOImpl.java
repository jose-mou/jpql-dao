package com.diwa.dao;

import com.diwa.dao.search.ScrollResult;
import com.diwa.dao.shared.aggregate.Aggregate;
import com.diwa.dao.shared.criteria.Criteria;
import com.diwa.dao.shared.criteria.conditional.ValueComparison;
import com.diwa.dao.shared.entity.FetchJoin;
import com.diwa.dao.shared.search.SearchInfo;
import com.diwa.dao.shared.search.SearchResult;
import com.diwa.dao.utils.DaoUtils;
import org.hibernate.*;
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
     * Clase de la entidad a la que pertenece el DAO.
     */
    private Class<T> type;

    /**
     * JPQL query generator.
     */
    private JPQLGenerator generator;

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
        this.type = getPersistentClass();
        generator = JPQLGenerator.getInstance();
    }

    /**
     * Constructor por defecto.
     */
    public DAOImpl(final Class<T> type, SessionFactory sessionFactory) {
        this.type = type;
        generator = JPQLGenerator.getInstance();
        this.sessionFactory = sessionFactory;
    }

    /**
     * Constructor a partir de la entidad del dominio a la que accede el DAO.
     * 
     * @param type Clase que corresponde a la entidad del dominio a la que accede el DAO.
     */
    public DAOImpl(final Class<T> type) {
        this(type, null);
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
        JPQLResult jpqlResult = generator.update(type, attribute, searchInfo);
        Query query = createQuery(jpqlResult.getQuery());
        addCriteriaParams(query, jpqlResult.getParameters());
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
        return count(new SearchInfo());
    }

    /**
     * {@inheritdoc}
     */
    public Long count(final SearchInfo searchInfo) {
        JPQLResult jpqlResult = generator.count(type, searchInfo);
        Query query = createQuery(jpqlResult.getQuery());
        addCriteriaParams(query, jpqlResult.getParameters());
        return (Long) query.uniqueResult();
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
        JPQLResult jpqlResult = generator.aggregate(type, aggregate, field, searchInfo);
        Query query = createQuery(jpqlResult.getQuery());
        addCriteriaParams(query, jpqlResult.getParameters());
        return (K) query.uniqueResult();
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
        JPQLResult jpqlResult = generator.find(type, searchInfo);
        Query query = createQuery(jpqlResult.getQuery());
        addCriteriaParams(query, jpqlResult.getParameters());

        Long total = count(searchInfo);
        int offset = searchInfo.getOffset();
        int pageSize = searchInfo.getPageSize();

        // TODO Volver a la versión anterior, esta provoca fallos al llegar al final del listado
        if (offset != -1 && pageSize != -1) {
            query.setFirstResult(offset);
            query.setMaxResults(pageSize);
        }
        return new SearchResult<T>(query.list(), total);
    }

    /**
     * {@inheritdoc}
     */
    @SuppressWarnings("unchecked")
    public List<T> findWithoutCount(final SearchInfo searchInfo) {
        JPQLResult jpqlResult = generator.find(type, searchInfo);
        Query query = createQuery(jpqlResult.getQuery());
        addCriteriaParams(query, jpqlResult.getParameters());

        int offset = searchInfo.getOffset();
        int pageSize = searchInfo.getPageSize();

        if (offset != -1 && pageSize != -1) {
            query.setFirstResult(offset);
            query.setMaxResults(pageSize);
        }
        return query.list();
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
        JPQLResult jpqlResult = generator.find(type, searchInfo);
        Query query = createQuery(jpqlResult.getQuery());
        addCriteriaParams(query, jpqlResult.getParameters());
        return (T) query.uniqueResult();
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
        JPQLResult jpqlResult = generator.find(type, si);
        Query query = createQuery(jpqlResult.getQuery());
        addCriteriaParams(query, jpqlResult.getParameters());
        if (transformer != null) {
            query.setResultTransformer(transformer);
        }

        return query.scroll();
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
     * <code>return sessionFactory.getCurrentSession();</code>
     * 
     * @return La sesión actual.
     */
    protected Session getSession() {
        if (sessionFactory == null) {
            logger.error("Not found SessionFactory bean");
        }
        try {
            return sessionFactory.getCurrentSession();
        } catch (HibernateException e){
            return sessionFactory.openSession();
        }
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
