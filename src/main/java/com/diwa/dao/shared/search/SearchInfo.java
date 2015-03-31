package com.diwa.dao.shared.search;

import com.diwa.dao.shared.criteria.Criteria;
import com.diwa.dao.shared.entity.Entity;
import com.diwa.dao.shared.entity.FetchJoin;
import com.diwa.dao.shared.order.OrderBy;
import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase encargada de indicar los parámetros que serán utilizados a la hora de realizar las consultas a la BD.
 * <p>
 * Los parámetros que se pueden de definir son:
 * <ul>
 * <li>Criterios de búsqueda</li>
 * <li>Ordenes utilizados a la hora de mostrar los datos obtenidos</li>
 * <li>Entidades utilizadas en los criterios. La entidad sobre la que se realiza la consulta no hay que indicarla</li>
 * <li>Joins Fetchs que se utilizarán para optimizar el número de consultas a las BD.</li>
 * <li>Número máximo de elementos que van a ser mostrado</li>
 * <li>Posición del primer elemento que se va a mostrar</li>
 * </ul>
 * En el caso de que no se indique lo contrario, se mostrarán todos los datos que coincidan con los criterios de búsqueda.
 * <p>
 * El siguiente ejemplo indica como usar un objeto de la clase <code>SearchInfo</code> para realizar una búsqueda.
 * <p>
 * <code>SearchInfo search = new SearchInfo();<p>
 *       search.addCriteria(Comparison.eq("atributo", valor));<p>
 *       dao.find(search);
 * </code>
 */
public class SearchInfo implements Serializable {

    /**
     * serial version uid.
     */
    private static final long serialVersionUID = -4478354063425072004L;

    /**
     * Criterios utilizados para la búsqueda.
     */
    private List<Criteria> criterias = new ArrayList<Criteria>();

    /**
     * Ordenes utilizados para mostrar los resultados.
     */
    private List<OrderBy> orders = new ArrayList<OrderBy>();

    /**
     * Entidades a las que se harán referencia los atributos de la búsqueda.
     */
    private List<Entity> entities = new ArrayList<Entity>();

    /**
     * Fetches utilizados para optimizar los accesos a BD.
     */
    private List<FetchJoin> fetches = new ArrayList<FetchJoin>();

    /**
     * Número máximo de elementos que van a ser mostrados en el resultado (tamaño de la página).
     * <p>
     * Si este atributo es igual a -1 <code>(pageSize == -1)</code> se ignorará.
     */
    private int pageSize = -1;
    
    /**
     * Primer elemento a mostrar.
     */
    private int offset = -1;

    /**
     * Indica si se aplicará distinct en la query ejecutada.
     * <p>
     * Si este atributo es cierto, se realizará un DISTINCT de los resultados (no se tienen en cuenta los duplicados), en caso contrario, se tendrán
     * en cuenta todos los resultados, aunque se encuentren duplicados.
     * <p>
     * Por defecto se aplicará distinct.
     */
    private boolean distinct = true;

    /**
     * Constructor vacío necesario para serialización en GWT.
     */
    public SearchInfo() {
    }

    /**
     * Constructor a partir de otro SearchInfo.
     */
    /*
     * public SearchInfo(final SearchInfo searchInfo) { super(); this.criterias.addAll(searchInfo.getCriterias());
     * this.entities.addAll(searchInfo.getEntities()); this.fetches.addAll(searchInfo.getFetches()); this.orders.addAll(searchInfo.getOrders());
     * this.pageSize = searchInfo.getPageSize(); this.pageNumber = searchInfo.getPageNumber(); this.distinct = searchInfo.isDistinct(); }
     */

    /**
     * Devuelve si se permitirán o no duplicado en los valores mostrados.
     * 
     * @return Cierto en el caso de que no se permitan duplicados y false en caso contrario.
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * Establece si se tendrán en cuenta los duplicados o no en el listado resultante.
     * 
     * @param distinct
     *            Cierto en el caso de que no se permitan duplicados y false en caso contrario.
     */
    public void setDistinct(final boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * Devuelve el número máximo de elementos que serán mostrados en la página. Por defecto el valor de este atributo es -1, en este caso el
     * atributo será ignorado a la hora de realizar la búsqueda.
     * 
     * @return Número máximo de elementos devueltos
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Establece el número máximo de elementos que van a ser mostrados en la página.
     * 
     * @param pageSize
     *            Número máximo de elementos que van a ser devueltos en la consulta.
     */
    public void setPageSize(final int pageSize) {
        this.pageSize = pageSize;
    }
    
    /**
     * Devuelve la página a ser mostrada. La primera página es la 1. Por defecto el valor de este atributo es -1, en este caso el atributo será
     * ignorado a la hora de realizar la búsqueda.
     * 
     * @return Número de la página a mostrar.
     */
    public int getPageNumber() {
        return (offset / pageSize) + 1;
    }

    /**
     * Devuelve el primer elemento a mostrar.
     * 
     * @return Número de elemento a mostrar.
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Establece el primer elemento a mostrar.
     * 
     * @param firstResult Número de elemento a mostrar.
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }

    /**
     * Proporciona la lista de criterios que van a ser utilizados a la hora de realizar la búsqueda.
     * 
     * @return Criterios de búsqueda
     */
    public List<Criteria> getCriterias() {
        return criterias;
    }

    /**
     * Proporciona un listado de ordenes utilizados a la hora de mostrar los datos devueltos por la consulta. Estos ordenes serán utilizados en la
     * consulta SQL realizada al a BD
     * 
     * @return Ordenes utilizados a la hora de mostrar los datos
     */
    public List<OrderBy> getOrders() {
        return orders;
    }

    /**
     * Proporciona un listado de las entidades sobre las que se podrán utilizar los criterios de búsqueda. Estas entidades pueden ser tanto de
     * dominio como entidades resultantes de los joins.
     * 
     * @return Listados de entidades de la consulta
     */
    public List<Entity> getEntities() {
        return entities;
    }

    /**
     * Proporciona los Join Fetch utilizados para la optimización de la consulta a BD.
     * 
     * @return Conjunto de join fetch utilizados en la consulta
     */
    public List<FetchJoin> getFetches() {
        return fetches;
    }

    /**
     * Añade un o varios criterios de búsqueda. A la hora de realizar la consulta a BD, se realizará un AND de todos los criterios que se han
     * añadido al objeto <code>SearchInfo</code> utilizado para la búsqueda.
     * 
     * @param criteria
     *            Criterios variables, que serán añadidos para la búsqueda.
     */
    public void addCriteria(Criteria criteria) {
        criterias.add(criteria);
    }

    /**
     * Añade uno o varios ordenes utilizados para ordenar los datos de la consulta realizada. Los ordenes se aplicarán en el mismo orden en el que
     * fueron añadidos.
     * 
     * @param order
     *            Ordenes variables, que serán añadidos para la búsqueda.
     */
    public void addOrder(OrderBy order) {
        orders.add(order);
    }

    /**
     * Añade una entidad sobre las que se aplicarán los criterios de búsqueda. Se podrán añadir tanto entidades de dominio (
     * <code>DomainEntity</code>) como joins que no sean del tipo fetch (<code>JoinEntity</code>).
     * 
     * @param entity
     *            Entidades que se podrán utilizar en los criterios de búsqueda.
     */

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    /**
     * Añade un <code>FetchJoin</code> a la consulta realizada. Los fetchs sirven para disminuir el número de consultas realizadas sobre la BD.
     * 
     * @param fetch
     *            Fetchs que se utilizarán para optimizar las consultas sobre la BD.
     */
    public void addFetch(FetchJoin fetch) {
        fetches.add(fetch);
    }

    /**
     * @param criterias
     *            the criterias to set
     */
    public void setCriterias(List<Criteria> criterias) {
        this.criterias = criterias;
    }

    /**
     * @param orders
     *            the orders to set
     */
    public void setOrders(List<OrderBy> orders) {
        this.orders = orders;
    }

    /**
     * @param entities
     *            the entities to set
     */
    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    /**
     * @param fetches
     *            the fetches to set
     */
    public void setFetches(List<FetchJoin> fetches) {
        this.fetches = fetches;
    }

    @Override
    public int hashCode() {
        int hashCode = 31;
        if (criterias != null) {
            for (Criteria crit : criterias) {
                hashCode += Objects.hashCode(crit);
            }
        }
        if (entities != null) {
            for (Entity entity : entities) {
                hashCode += Objects.hashCode(entity);
            }
        }

        if (fetches != null) {
            for (FetchJoin fetch : fetches) {
                hashCode += Objects.hashCode(fetch);
            }
        }
        if (orders != null) {
            for (OrderBy order : orders) {
                hashCode += Objects.hashCode(order);
            }
        }

        hashCode += Objects.hashCode(offset);
        hashCode += Objects.hashCode(pageSize);

        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SearchInfo other = (SearchInfo) obj;
        if (criterias == null) {
            if (other.criterias != null)
                return false;
        } else if (!criterias.equals(other.criterias))
            return false;
        if (distinct != other.distinct)
            return false;
        if (entities == null) {
            if (other.entities != null)
                return false;
        } else if (!entities.equals(other.entities))
            return false;
        if (fetches == null) {
            if (other.fetches != null)
                return false;
        } else if (!fetches.equals(other.fetches))
            return false;
        if (orders == null) {
            if (other.orders != null)
                return false;
        } else if (!orders.equals(other.orders))
            return false;
        if (offset != other.offset)
            return false;
        if (pageSize != other.pageSize)
            return false;
        return true;
    }

    public boolean equalss(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SearchInfo other = (SearchInfo) obj;
        if (criterias == null) {
            if (other.criterias != null)
                return false;
        } else if (!criterias.equals(other.criterias))
            return false;
        if (distinct != other.distinct)
            return false;
        if (entities == null) {
            if (other.entities != null)
                return false;
        } else if (!entities.equals(other.entities))
            return false;
        if (fetches == null) {
            if (other.fetches != null)
                return false;
        } else if (!fetches.equals(other.fetches))
            return false;
        if (orders == null) {
            if (other.orders != null)
                return false;
        } else if (!orders.equals(other.orders))
            return false;
        if (offset != other.offset)
            return false;
        if (pageSize != other.pageSize)
            return false;
        return true;
    }
}
