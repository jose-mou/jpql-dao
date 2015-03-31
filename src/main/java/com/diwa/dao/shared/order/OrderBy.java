package com.diwa.dao.shared.order;

import com.diwa.dao.shared.entity.Entity;
import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * Modela un criterio de orden en el que van a ser mostrados los datos de la consulta. Los datos se pueden ordenar de forma ascendente o descendente
 * por el valor de un atributo. Esta clase modela el <code>ORDER BY</code> de SQL. Permitiendo aplicarlo a los atributos de las entidades del dominio.
 * <p>
 * Para la creación de un criterio de ordenación <code>OrderBy</code> se puede hacer mediante los métodos estáticos de la clase. Estos métodos
 * permiten crearlos criterios de orden de forma rápida y sencilla. En el caso que no se le indique la entidad a la que pertenece el atributo, se
 * considerará que esta entidad es la entidad por defecto.
 * <p>
 * Un ejemplo de uso es el siguiente:
 * <p>
 * <ul>
 * <li>Criterio de orden ascendente sobre un atributo de la entidad consultada => OrderBy.asc("nombreAtributo");</li>
 * <li>Criterio de orden descendente sobre una entidad que se utiliza en la búsqueda => OrderBy.desc("nombreAtributo" , entityObject);
 * </ul>
 * Una vez creado el criterio de búsqueda hay que añadirlo al objeto <code>SearchInfo</code> utilizado para la búsqueda.
 */
public class OrderBy implements Serializable {

    /**
     * serial version uid.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Nombre del atributo por el que vamos a realizar la ordenación.
     */
    private String name;

    /**
     * Dirección de la ordenación. Esta puede ser en orden ascendente (ASC) o descendente (DESC).
     */
    private OrderDirection direction;

    /**
     * Entidad a la que corresponde el atributo por el que vamos a ordenar. Esta entidad puede ser tanto una entidad del dominio o un join.
     */
    private Entity entity = null;

    /**
     * Constructor vacío necesario para serialización en GWT.
     */
    public OrderBy() {
    }

    /**
     * Constructor para un orden que será aplicado a la entidad base o por defecto.
     * 
     * @param name
     *            Nombre del atributo por el que vamos a ordenar los resultados.
     * @param direction
     *            Dirección del orden aplicado.
     */
    public OrderBy(final String name, final OrderDirection direction) {
        this.name = name;
        this.direction = direction;
    }

    /**
     * Constructor para un orden que será aplicado a un atributo que pertenecerá a la entidad pasada.
     * 
     * @param name
     *            Nombre del atributo por el que vamos a ordenar los resultados.
     * @param direction
     *            Dirección del orden aplicado.
     * @param entity
     *            Entidad a la que corresponde el atributo por el que vamos a ordenar.
     */
    public OrderBy(final String name, final OrderDirection direction, final Entity entity) {
        this(name, direction);
        this.entity = entity;
    }

    /**
     * Genera un objeto <code>OrderBy</code> con dirección <strong>ascendente (ASC)</strong>, a partir del nobre del atributo sobre el que vamos a
     * ordenar. Este atributo ha de pertenecer a la entidad base o por la que realizamos la búsqueda.
     * 
     * @param name
     *            Nombre del atributo por el que vamos a ordenar los resultados.
     * @return Criterio de orden.
     */
    public static OrderBy asc(final String name) {
        return new OrderBy(name, OrderDirection.ASC);
    }

    /**
     * Genera un objeto <code>OrderBy</code> con dirección <strong>descendente (DESC)</strong>, a partir del nombre del atributo sobre el que vamos a
     * ordenar. Este atributo ha de pertenecer a la entidad base o por la que realizamos la búsqueda.
     * 
     * @param name
     *            Nombre del atributo por el que vamos a ordenar los resultados.
     * @return Criterio de orden.
     */
    public static OrderBy desc(final String name) {
        return new OrderBy(name, OrderDirection.DESC);
    }

    /**
     * Genera un objeto <code>OrderBy</code> con dirección <strong>ascendente (ASC)</strong>, a partir del nombre del atributo sobre el que vamos a
     * ordenar. Este atributo ha de pertenecer a la entidad pasada por el parámetro. Si la entidad indicada en el parámetro fuera nula se consideraría
     * la entidad base o por defecto.
     * 
     * @param name
     *            Nombre del atributo por el que vamos a ordenar los resultados.
     * @param entity
     *            Entidad a la que corresponde el atributo por el que vamos a ordenar.
     * @return Criterio de orden
     */
    public static OrderBy asc(final String name, final Entity entity) {
        return new OrderBy(name, OrderDirection.ASC, entity);
    }

    /**
     * Genera un objeto <code>OrderBy</code> con dirección <strong>descendente (DESC)</strong>, a partir del nobre del atributo sobre el que vamos a
     * ordenar. Este atributo ha de pertenecer a la entidad pasada por el parámetro. Si la entidad indicada en el parámetro fuera nula se consideraría
     * la entidad base o por defecto.
     * 
     * @param name
     *            Nombre del atributo por el que vamos a ordenar los resultados.
     * @param entity
     *            Entidad a la que corresponde el atributo por el que vamos a ordenar.
     * @return Criterio de orden
     */
    public static OrderBy desc(final String name, final Entity entity) {
        return new OrderBy(name, OrderDirection.DESC, entity);
    }

    /**
     * @return Nombre del atributo por el que vamos a realizar la ordenación.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            Nombre del atributo por el que vamos a realizar la ordenación.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Dirección de la ordenación. Esta puede ser en orden ascendente (ASC) o descendente (DESC).
     */
    public OrderDirection getDirection() {
        return direction;
    }

    /**
     * @return Entidad a la que corresponde el atributo por el que vamos a ordenar. Esta entidad puede ser tanto una entidad del dominio o un join.
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * @param entity
     *            the entity to set
     */
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    /**
     * @return true si la ordenación es ascendente.
     */
    public boolean isAscendente() {
        return direction == OrderDirection.ASC;
    }

    @Override
    public String toString() {
        return getEntity() + "." + getName() + " " + getDirection();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(direction, entity, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        OrderBy f = (OrderBy) obj;

        if (entity != null)
            return (direction.equals(f.direction) && entity.equals(f.entity));
        else
            return (direction.equals(f.direction) && name.equals(f.name));

    }

}
