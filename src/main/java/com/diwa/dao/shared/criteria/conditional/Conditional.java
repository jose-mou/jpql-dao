package com.diwa.dao.shared.criteria.conditional;

import com.diwa.dao.shared.criteria.Criteria;
import com.diwa.dao.shared.entity.Entity;
import com.google.common.base.Objects;

/**
 * Clase abstracta de la que heredan todos los criterios de búsqueda. Contiene aquellos atributos que son comunes a todos los criterios de búsqueda:
 * <ul>
 * <li>Nombre del atributo que vamos a comparar.</li>
 * <li>Entidad a la que pertenece el atributo que vamos a comparar</li>
 * <li>Operador correspondiente a la condición de búsqueda</li>
 * </ul>
 */
public abstract class Conditional implements Criteria {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Nombre del atributo que vamos a comparar.
     */
    private String name;

    /**
     * Alias/Entidad a la que pertenece el atributo.
     */
    private Entity entity;

    /**
     * Operador correspondiente a la condición de búsqueda. Este atributo sólo puede ser establecido mediante el constructor, de tal forma que no
     * podrá ser modificado.
     */
    private ConditionalOperator operator;

    /**
     * Constructor vacío necesario para serialización en GWT.
     */
    public Conditional() {
    }

    /**
     * Constructor. Este constructor esta diseñado para se invocado por los criterios que implementan esta clase. No puede ser llamado de forma
     * directa.
     * 
     * @param name
     *            Nombre del atributo que vamos a comparar.
     * @param operator
     *            Operador correspondiente a la condición de búsqueda.
     */
    protected Conditional(final String name, final ConditionalOperator operator) {
        this(name, operator, null);
    }

    /**
     * Constructor. Este constructor esta diseñado para se invocado por los criterios que implementan esta clase. No puede ser llamado de forma
     * directa.
     * 
     * @param name
     *            Nombre del atributo que vamos a comparar.
     * @param operator
     *            Operador correspondiente a la condición de búsqueda.
     * @param entity
     *            Alias/Entidad a la que pertenece el atributo.
     */
    protected Conditional(final String name, final ConditionalOperator operator, final Entity entity) {
        this.name = name;
        this.operator = operator;
        this.entity = entity;
    }

    /**
     * @return Nombre del atributo que vamos a comparar.
     */
    public String getName() {
        return name;
    }

    /**
     * Establece/Modifica el Nombre del atributo que vamos a comparar.
     * 
     * @param name
     *            Nombre del atributo que vamos a comparar.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return Operador correspondiente a la condición de búsqueda.
     */
    public ConditionalOperator getOperator() {
        return operator;
    }

    /**
     * @return Alias/Entidad a la que pertenece el atributo.
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * Establece/Modifica el Alias/Entidad a la que pertenece el atributo.
     * 
     * @param entity
     *            Alias/Entidad a la que pertenece el atributo.
     */
    public void setEntity(final Entity entity) {
        this.entity = entity;
    }

    @Override
    public int hashCode() {
        if (getEntity() != null)
            return Objects.hashCode(getName(), getEntity().getAlias(), getOperator());
        else
            return Objects.hashCode(getName(), getOperator());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Conditional other = (Conditional) obj;
        if (entity == null) {
            if (other.entity != null)
                return false;
        } else if (!entity.equals(other.entity))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (operator != other.operator)
            return false;
        return true;
    }

}
