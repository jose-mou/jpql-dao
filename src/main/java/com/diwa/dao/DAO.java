package com.diwa.dao;

import com.diwa.dao.search.ScrollResult;
import com.diwa.dao.shared.aggregate.Aggregate;
import com.diwa.dao.shared.criteria.Criteria;
import com.diwa.dao.shared.entity.FetchJoin;
import com.diwa.dao.shared.search.SearchInfo;
import com.diwa.dao.shared.search.SearchResult;
import org.hibernate.Query;
import org.hibernate.transform.ResultTransformer;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Interfaz que han de implementar las diferentes implementaciones del DAO.
 * <p>
 * A la hora de acceder a los objetos de DAO, estos deberán de ser accedidos a través de la interfaz.
 * <p>
 * Para declarar un objeto de tipo DAO hay que indicar tanto la Clase del dominio a la que corresponde el DAO (T), como
 * la clase que corresponde a la primary key de la entidad (PK).
 *
 * <code>
 *      DAO< Persona, Integer > dao = new DAOImpl< Persona, Integer >();
 *      List< Persona > personas = dao.findAll();
 * </code>
 *
 * @param <T> Clase de la entidad del dominio a la que representa el DAO. Esta clase ha de estar anotada en el dominio.
 * @param <PK> Clase a la que corresponde la primary key de la entidad del dominio a la que representa. La primary key
 *            ha de implementar la interfaz <code>Serializable</code>.
 */
public interface DAO<T, PK extends Serializable> {

    /**
     * Inserta/Actualiza en la BD la información sobre la entidad. Al ejecutar esta sentencia se lanza el evento INSERT
     * en el caso de que el elemento sea nuevo o MERGE en el caso de que el elemento se actualice.
     *
     * Si el objeto no tiene asignado un identificador se persistirá y se le asignará un identificador, en caso
     * contrario se actualizará.
     *
     * @param entity Entidad del dominio que va a ser insertada/actualizada.
     */
    T save(T entity);

    /**
     * Inserta/Actualiza en la BD la información sobre cada uno de los objetos que componen la lista indicada.
     *
     * @param entities Conjunto de entidades del dominio que va a ser insertada/actualizada.
     */
    Collection<T> save(Collection<T> entities);

    /**
     * Actualiza en la BD el atributo indicado con el valor pasado para aquellos registros que cumplan con los criterios
     * de búsqueda.
     * <p>
     * NOTA: Cuando esta sentencia es llamada no se lanza ningún evento JPA.
     *
     * @param field Atributo que va a ser actualizado
     * @param value Valor al cual se va a actualizar el atributo de los objetos
     * @param criteria Conjunto de criterios que han de cumplir los registros que van a ser actualizados
     */
    void update(String field, Serializable value, Criteria... criteria);

    /**
     * Actualiza en la BD los atributos indicado con los correspondientes valores asociados para aquellos registros que
     * cumplan con los criterios de búsqueda.
     *
     * @param attribute Par formado por el nombre del atributo y el valor con el que se va a actualizar el atributo
     * @param searchInfo SearchInfo que contiene los criterios de búsqueda que han de cumplir los registros que van a
     *            ser actualizados
     */
    void update(Map<String, Serializable> attribute, SearchInfo searchInfo);

    /**
     * Elimina una entidad de la BD. Cuando este método es llamado se lanza el evento DELETE de JPA.
     * <p>
     * Este método carga la entidad que coincide con el identificador indicado y posteriormente llama al método
     * delete(T). @see {@link DAO#delete(Serializable)}.
     *
     * @param id Clave primaria de la entidad a borrar.
     */
    void delete(PK id);

    /**
     * Elimina una entidad de la BD. Si la entidad no se encuentra almacenada en la BD, primero se almacena y luego se
     * elimina de la BD. Cuando este método es llamado se lanza el evento DELETE de JPA.
     *
     * @param entity Entidad del dominio a eliminar.
     */
    void delete(T entity);

    /**
     * Elimina cada una de las entidades que pertenecen al conjunto indicado. Cuando este método es llamado se lanza el
     * evento DELETE de JPA para cada una de al entidades que pertenecen al conjunto.
     *
     * @param entities Conjunto de entidades de dominio que van a ser eliminadas de la BD.
     */
    void delete(Collection<T> entities);

    /**
     * Permite ejecutar una query en lenguaje JPQL a través del DAO.
     *
     * @param query Sentencia que va a ser ejecutada en la BD.
     * @param params Conjunto de parámetros para generar la sentencia correcta
     */
    void executeUpdate(String query, Map<String, Serializable> params);

    /**
     * Devuelve un objeto previamente almacenado en la BD o null si no existe.
     *
     * @param id Clave Primaria del objeto
     * @return Objeto almacenado en la BD
     */
    T read(PK id);

    /**
     * Carga un objeto previamente almacenado en la BD aplicando los fetches indicados, de tal manera que en la misma
     * consulta se traen los objetos indicados en los feches.
     *
     * @param id Clave Primaria del objeto
     * @param fetch Fetches que van a se aplicados a la hora de cargar el objeto. Disminuye el número de accesos a la
     *            BD.
     * @return Objeto almacenado en la BD
     */
    T read(PK id, FetchJoin... fetch);

    /**
     * Refresca una entidad que ha podido cambiar. Cuando este método es llamado se sincroniza la sesión con la BD, a su
     * vez se lanza el evento REFRESH de JPA.
     *
     * @param entity Objeto a refrescar
     * @return Objeto recargado
     */
    T refresh(T entity);

    /**
     * Carga la entidad que corresponde a la PK indicada y entonces llama al método refresh(T). @see
     * {@link DAO#refresh(Serializable)}.
     *
     * @param id Identificador del objeto
     * @return Objeto recargado
     */
    T refresh(PK id);

    /**
     * Refresca una colección de entidades que han podido cambiar. @see
     * {@link DAO#refresh(Serializable)}.
     *
     * @param entities Colección de objetos que han podido cambiar
     * @return Colección de objetos recargados
     */
    Collection<T> refresh(Collection<T> entities);

    /**
     * Escribe en la BD todas las operaciones que se encuentren pendientes y limpia la sesión.
     */
    void flushAndClear();

    /**
     * Cuenta el número de entidades que hay en la BD, para la entidad del dominio asociada al DAO.
     *
     * @return Numero de entidades que hay para la entidad del dominio asociada al DAO
     */
    Long count();

    /**
     * Cuenta el número de entidades que hay en la BD, para la entidad del dominio asociada al DAO y que coinciden con
     * los criterios indicados.
     *
     * @param criteria Criterios de búsqueda que han de cumplir las entidades de la BD
     * @return Numero de entidades que hay para la entidad del dominio asociada al DAO y que cumplen los criterios
     *         indicados.
     */
    Long count(Criteria... criteria);

    /**
     * Cuenta el número de entidades que hay en la BD, para la entidad del dominio asociada al DAO y que coinciden con
     * los criterios que contiene el searchInfo.
     *
     * @param searchInfo Contiene un conjunto de criterios de búsquedas
     * @return Numero de entidades que hay para la entidad del dominio asociada al DAO y que cumplen los criterios
     *         indicados en el objeto searchInfo.
     */
    Long count(SearchInfo searchInfo);

    /**
     * Permite la ejecución de funciones estadísticas(SUM, MIN, MAX, AVG) de SQL. los criterios de búsquedas indicados
     * establecerán los filtros de los registros a los que aplicarán la función.
     *
     * @param aggregate Agregado/función que va a ser aplicada al atributo indicado
     * @param field Atributo al que se aplicará el agregado
     * @param searchInfo Criterios de búsqueda
     * @return Numero resultante de aplicar la función/agregado al atributos indicado para los registros que cumplan con
     *         las condiciones de búsqueda.
     */
    Number aggregate(Aggregate aggregate, String field, SearchInfo searchInfo);

    /**
     * Permite la ejecución de funciones estadísticas(SUM, MIN, MAX, AVG) de SQL. los criterios de búsquedas indicados
     * establecerán los filtros de los registros a los que aplicarán la función. En este caso se devolverá un objeto de
     * la clase indicada, realizando el casting correspondiente,
     *
     * @param aggregate Agregado/función que va a ser aplicada al atributo indicado
     * @param field Atributo al que se aplicará el agregado
     * @param searchInfo Criterios de búsqueda
     * @return Numero resultante de aplicar la función/agregado al atributos indicado para los registros que cumplan con
     *         las condiciones de búsqueda.
     */
    <K extends Serializable> K aggregate(Aggregate aggregate, String field, SearchInfo searchInfo, Class<K> clazz);

    /**
     * Lista las entidades que coincidan(han de ser iguales) con los valores de búsqueda.
     *
     * @param propertyValue Filtros de búsqueda con los que han de coincidir las entidades.
     * @return Lista de objetos que coinciden con los filtros establecidos.
     */
    SearchResult<T> find(Map<String, Serializable> propertyValue);

    /**
     * Lista las entidades que coincidan con el valor de búsqueda correspondientes al atributo de búsqueda.
     *
     * @param property Nombre del atributo por el que buscamos.
     * @param value Valor del atributos por el que filtramos.
     * @return Lista de objetos que coinciden con el filtro establecido.
     */
    SearchResult<T> find(String property, Serializable value);

    /**
     * Método que proporciona todos las entidades del dominio al que está asociado el DAO.
     *
     * @return Lista con todos las entidades
     */
    SearchResult<T> findAll();

    /**
     * Lista las entidades que hay en la BD, para la entidad del dominio asociada al DAO y que coinciden con los
     * criterios indicados.
     *
     * @param criteria Conjunto de criterios que es utilizarán para la búsqueda
     * @return Entidades que hay para la entidad del dominio asociada al DAO y que cumplen los criterios indicados.
     */
    SearchResult<T> find(Criteria... criteria);

    /**
     * Lista las entidades que hay en la BD, para la entidad del dominio asociada al DAO y que coinciden con los
     * criterios que contiene el searchInfo.
     *
     * @param searchInfo Contiene un conjunto de criterios de búsquedas
     * @return Entidades que hay para la entidad del dominio asociada al DAO y que cumplen los criterios indicados en el
     *         objeto searchInfo.
     */
    SearchResult<T> find(SearchInfo searchInfo);

	/**
     * Lista las entidades que hay en la BD, para la entidad del dominio asociada al DAO y que coinciden con los
     * criterios que contiene el searchInfo. No realiza el count.
     *
     * @param searchInfo Contiene un conjunto de criterios de búsquedas
     * @return Entidades que hay para la entidad del dominio asociada al DAO y que cumplen los criterios indicados en el
     *         objeto searchInfo.
     */
    List<T> findWithoutCount(SearchInfo searchInfo);

    /**
     * Devuelve un único objeto que coincida con los criterios de búsqueda. En el caso de que exista mas de un objeto o
     * ninguno coincida con los criterios de búsqueda lanzará una excepción.
     * {@link javax.persistence.Query#getSingleResult()}.
     *
     * @param criteria Conjunto de criterios que es utilizarán para la búsqueda.
     * @return Objeto que coincide con los criterios establecidos.
     */
    T findSingle(Criteria... criteria);

    /**
     * Devuelve un único objeto que coincida con los criterios de búsqueda. En el caso de que exista mas de un objeto o
     * ninguno coincida con los criterios de búsqueda lanzará una excepción.
     * {@link javax.persistence.Query#getSingleResult()}.
     *
     * @param searchInfo Contiene un conjunto de criterios de búsquedas.
     * @return Objeto que coincide con los criterios establecidos.
     */
    T findSingle(SearchInfo searchInfo);

    /**
     * Realiza búsqueda con scroll sobre la entidad asociada al DAO.
     */
    ScrollResult<T> findScroll(final SearchInfo si);

    /**
     * Realiza búsqueda con scroll aplicando un transformer Transformers.ALIAS_TO_ENTITY_MAP.
     */
    ScrollResult<Map<String, Serializable>> findMapScroll(final SearchInfo si);

    /**
     * Realiza búsqueda con scroll aplicando el transformer indicado. Para Transformers.ALIAS_TO_ENTITY_MAP ver
     * searchMapScroll.
     */
    ScrollResult<Serializable> findScroll(final SearchInfo si, ResultTransformer transformer);

    /**
     * Realiza búsqueda con scroll sobre la entidad asociada al DAO.
     */
    ScrollResult<T> findQueryScroll(final Query q);

    /**
     * Realiza búsqueda con scroll aplicando un transformer Transformers.ALIAS_TO_ENTITY_MAP.
     */
    ScrollResult<Map<String, Serializable>> findQueryMapScroll(final Query si);

    /**
     * Realiza búsqueda con scroll aplicando el transformer indicado. Para Transformers.ALIAS_TO_ENTITY_MAP ver
     * searchMapScroll.
     */
    ScrollResult<Serializable> findQueryScroll(final Query query, ResultTransformer transformer);
}
