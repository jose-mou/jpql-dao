package com.diwa.dao.shared.criteria.conditional;

import com.diwa.dao.shared.criteria.Criteria;
import com.diwa.dao.shared.entity.Entity;
import com.google.common.base.Objects;

/**
 * Modela los criterios de búsqueda IS NULL y IS NOT NULL. Este criterio será válido en aquellos atributos que sean nulos en BD. Corresponde a la
 * sentencia IS NULL y IS NOT NULL de SQL. En este caso modelamos IS NOT NULL con esta clase por simplicidad a la hora de generar la consulta.
 * <p>
 * Proporciona un conjunto de métodos estáticos para facilitar la generación del criterio, por el contrario, no se permite el acceso a los
 * constructores.
 * <p>
 * A continuación se muestra un ejemplo de como se crea un criterio is null:
 * <p>
 * <code>
 *        Criteria condicion = NullConditional.isNull("atributo");
 * </code>
 */
public class NullConditional extends Conditional {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor vacío necesario para serialización en GWT.
     */
    public NullConditional() {
    }

    /**
     * Constructor privado. Este no puede ser invocado para evitar operadores inválidos.
     * 
     * @param name
     *            Nombre del atributo que vamos a comparar
     * @param operator
     *            Operador correspondiente a la operación
     */
    private NullConditional(final String name, final ConditionalOperator operator) {
        super(name, operator);
    }

    /**
     * Constructor privado. Este no puede ser invocado para evitar operadores inválidos.
     * 
     * @param name
     *            Nombre del atributo que vamos a comparar
     * @param operator
     *            Operador correspondiente a la operación
     * @param entity
     *            Entidad a la que pertenece el atributo que vamos a comparar
     */
    private NullConditional(final String name, final ConditionalOperator operator, final Entity entity) {
        super(name, operator, entity);
    }

    /**
     * Genera un criterio IS NULL sobre el atributo indicado. Será satisfecho si el atributo indicado es nulo.
     * 
     * @param name
     *            Nombre del atributo que vamos a comparar.
     * @return Criterio IS NULL
     */
    public static NullConditional isNull(final String name) {
        return new NullConditional(name, ConditionalOperator.IS_NULL);
    }

    /**
     * Genera un criterio IS NULL sobre el atributo indicado. Será satisfecho si el atributo indicado es nulo.
     * 
     * @param name
     *            Nombre del atributo que vamos a comparar.
     * @param entity
     *            Entidad a la que pertenece el atributo que vamos a comparar
     * @return Criterio IS NULL
     */
    public static NullConditional isNull(final String name, final Entity entity) {
        return new NullConditional(name, ConditionalOperator.IS_NULL, entity);
    }

    /**
     * Genera un criterio IS NOT NULL sobre el atributo indicado. Será satisfecho si el atributo indicado tiene algún valor diferente de nulo.
     * 
     * @param name
     *            Nombre del atributo que vamos a comparar.
     * @return Criterio IS NOT NULL
     */
    public static NullConditional isNotNull(final String name) {
        return new NullConditional(name, ConditionalOperator.IS_NOT_NULL);
    }

    /**
     * Genera un criterio IS NOT NULL sobre el atributo indicado. Será satisfecho si el atributo indicado tiene algún valor diferente de nulo.
     * 
     * @param name
     *            Nombre del atributo que vamos a comparar.
     * @param entity
     *            Entidad a la que pertenece el atributo que vamos a comparar
     * @return Criterio IS NOT NULL
     */
    public static NullConditional isNotNull(final String name, final Entity entity) {
        return new NullConditional(name, ConditionalOperator.IS_NOT_NULL, entity);
    }

    /**
     * Sobrescrito el método toString. Este método será invocado en el cliente para generar las URLs asociadas a los cirterios de búsqueda. Ejemplo :
     * atributo-IS_NULL
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
        NullConditional other = (NullConditional) obj;
        return (super.equals(other) && getName().equals(other.getName()));
    }
}
