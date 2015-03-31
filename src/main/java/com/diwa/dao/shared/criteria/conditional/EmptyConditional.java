package com.diwa.dao.shared.criteria.conditional;

import com.diwa.dao.shared.criteria.Criteria;
import com.diwa.dao.shared.entity.Entity;
import com.google.common.base.Objects;

/**
 * Modela los criterios de búsqueda empty de una colección de elementos. Este criterios se aplica únicamente a
 * colecciones de elementos asociadas a un elemento, es decir, @OneToMany o @ManyToMany. Este criterio se satisface
 * cuando la colección de elementos asociada es vacía.
 * <p>
 * El criterio de búsqueda corresponde a la anotación EMPTY de JPA y su correspondiente negación, pero éste no tiene una
 * representación directa en SQL, se podría representar la misma con un 'subselect' o un 'join' sobre la tabla que
 * representa a la entidad asociada.
 * <p>
 * Proporciona un conjunto de métodos estáticos para facilitar la generación del criterio, por el contrario, no se
 * permite el acceso a los constructores.
 * <p>
 * A continuación se muestra un ejemplo de como se crea un criterio empty:
 * <p>
 * <code>
 *        Criteria condicion = EmptyConditional.isEmpty("atributoColeccion");
 *</code>
 */
public class EmptyConditional extends Conditional {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor vacío necesario para serialización en GWT.
     */
    public EmptyConditional() {
    }

    /**
     * @param name Nombre del atributo que corresponde a la colección.
     * @param operator Operador correspondiente a la operación
     */
    private EmptyConditional(final String name, final ConditionalOperator operator) {
        super(name, operator);
    }

    /**
     * @param name Nombre del atributo que corresponde a la colección
     * @param operator Operador correspondiente a la operación
     * @param entity Entidad a la que pertenece el atributo(colección)
     */
    private EmptyConditional(final String name, final ConditionalOperator operator, final Entity entity) {
        super(name, operator, entity);
    }

    /**
     * Genera un criterio IS EMPTY que será satisfecho si la colección asociada es vacía.
     *
     * @param name Nombre del atributo que corresponde a la colección
     * @return Criterio IS EMPTY
     */
    public static EmptyConditional isEmpty(final String name) {
        return new EmptyConditional(name, ConditionalOperator.IS_EMPTY);
    }

    /**
     * Genera un criterio IS EMPTY que será satisfecho si la colección asociada es vacía.
     *
     * @param name Nombre del atributo que corresponde a la colección
     * @param entity Entidad a la que pertenece el atributo(colección)
     * @return Criterio IS EMPTY
     */
    public static EmptyConditional isEmpty(final String name, final Entity entity) {
        return new EmptyConditional(name, ConditionalOperator.IS_EMPTY, entity);
    }

    /**
     * Genera un criterio IS NOT EMPTY que será satisfecho si la colección asociada contiene al menos un elemento.
     *
     * @param name Nombre del atributo que corresponde a la colección
     * @return Criterio IS NOT EMPTY
     */
    public static EmptyConditional isNotEmpty(final String name) {
        return new EmptyConditional(name, ConditionalOperator.IS_NOT_EMPTY);
    }

    /**
     * Genera un criterio IS NOT EMPTY que será satisfecho si la colección asociada contiene al menos un elemento.
     *
     * @param name Nombre del atributo que corresponde a la colección
     * @param entity Entidad a la que pertenece el atributo(colección)
     * @return Criterio IS NOT EMPTY
     */
    public static EmptyConditional isNotEmpty(final String name, final Entity entity) {
        return new EmptyConditional(name, ConditionalOperator.IS_NOT_EMPTY, entity);
    }
    
    /**
     * Sobrescrito el método toString. Este método será invocado en el cliente para generar las URLs asociadas a los
     * cirterios de búsqueda. Ejemplo : atributo-IS_NOT_EMPTY
     */
    public String toString() {
        return getName() + Criteria.QUERY_SEPARATOR + getOperator();
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(getOperator(), getName());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EmptyConditional other = (EmptyConditional) obj;
        return (super.equals(other) && getName().equals(other.getName()));
    }
}
