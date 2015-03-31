package com.diwa.dao.shared.criteria.conditional;

import com.diwa.dao.shared.entity.Entity;
import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * Representa las comparaciones entre un atributo de una entidad y un valor indicado. Esta comparación permite la generación de los criterios =, !=,
 * >, >=, < y <=. Todos estos criterios serán aplicados entre atributo y el valor indicado.
 * <p>
 * Proporciona un conjunto de métodos estáticos para facilitar la generación del criterio, por el contrario, no se permite el acceso a los
 * constructores.
 * <p>
 * A continuación se muestra un ejemplo de como se crea un criterio de igualdad entre un atributo y un valor:
 * <p>
 * <code>
 *        Criteria condicion = ValueComparison.eq("at1", value);
 * </code>
 */
public class ValueComparison extends CaseSensitiveConditional {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Valor con el que vamos a comparar el atributo.
     */
    private Serializable value;

    /**
     * Constructor vacío necesario para serialización en GWT.
     */
    public ValueComparison() {
    }

    /**
     * Constructor privado. Genera una criterio de comparación entre el atributo y el valor indicado.
     * 
     * @param name
     *            Nombre del atributo
     * @param operator
     *            Operador correspondiente a la operación
     * @param value
     *            Valor con el que vamos a comparar el atributo
     */
    private ValueComparison(final String name, final ConditionalOperator operator, final Serializable value) {
        super(name, operator);
        this.value = value;
    }

    /**
     * Constructor privado. Genera una criterio de comparación entre el atributo y el valor indicado.
     * 
     * @param name
     *            Nombre del atributo
     * @param operator
     *            Operador correspondiente a la operación
     * @param value
     *            Valor con el que vamos a comparar el atributo.
     * @param entity
     *            Entidad a la que aplica el atributo
     */
    private ValueComparison(final String name, final ConditionalOperator operator, final Serializable value, final Entity entity) {
        super(name, operator, entity);
        this.value = value;
    }

    /**
     * Proporciona el valor con el que vamos a comparar el atributo.
     * 
     * @return Valor con el que vamos a comparar el atributo
     */
    public Serializable getValue() {
        return value;
    }

    /**
     * Establece/Modifica el valor con el que vamos a comparar el atributo.
     * 
     * @param value
     *            Valor con el que vamos a comparar el atributo
     */
    public void setValue(final Serializable value) {
        this.value = value;
    }

    /**
     * Genera un criterio de tipo '=' comparando un atributo de la entidad por defecto con el valor indicado.
     * 
     * @param name
     *            Nombre del atributo que vamos a comparar.
     * @param value
     *            Valor con el que vamos a comparar el atributo
     * @return Criterio de tipo name = :value
     */
    public static Conditional eq(final String name, final Serializable value) {
        return new ValueComparison(name, ConditionalOperator.EQ, value);
    }

    /**
     * Genera un criterio de tipo '=' comparando un atributo de la entidad indicada con el valor indicado.
     * 
     * @param name
     *            Nombre del atributo que vamos a comparar.
     * @param value
     *            Valor con el que vamos a comparar el atributo
     * @param entity
     *            Entidad a la que aplica el atributo
     * @return Criterio de tipo entity.name = :value
     */
    public static Conditional eq(final String name, final Serializable value, final Entity entity) {
        return new ValueComparison(name, ConditionalOperator.EQ, value, entity);
    }

    /**
     * Genera un criterio de tipo '!=' comparando un atributo de la entidad por defecto con el valor indicado.
     * 
     * @param name
     *            Nombre del atributo que vamos a comparar.
     * @param value
     *            Valor con el que vamos a comparar el atributo
     * @return Criterio de tipo name != :value
     */
    public static Conditional ne(final String name, final Serializable value) {
        return new ValueComparison(name, ConditionalOperator.NE, value);
    }

    /**
     * Genera un criterio de tipo '!=' comparando un atributo de la entidad indicada con el valor indicado.
     * 
     * @param name
     *            Nombre del atributo que vamos a comparar.
     * @param value
     *            Valor con el que vamos a comparar el atributo
     * @param entity
     *            Entidad a la que aplica el atributo
     * @return Criterio de tipo entity.name != :value
     */
    public static Conditional ne(final String name, final Serializable value, final Entity entity) {
        return new ValueComparison(name, ConditionalOperator.NE, value, entity);
    }

    /**
     * Genera un criterio de tipo '>' comparando un atributo de la entidad por defecto con el valor indicado.
     * 
     * @param name
     *            Nombre del atributo que vamos a comparar.
     * @param value
     *            Valor con el que vamos a comparar el atributo
     * @return Criterio de tipo name > :value
     */
    public static Conditional gt(final String name, final Serializable value) {
        return new ValueComparison(name, ConditionalOperator.GT, value);
    }

    /**
     * Genera un criterio de tipo '>' comparando un atributo de la entidad indicada con el valor indicado.
     * 
     * @param name
     *            Nombre del atributo que vamos a comparar.
     * @param value
     *            Valor con el que vamos a comparar el atributo
     * @param entity
     *            Entidad a la que aplica el atributo
     * @return Criterio de tipo entity.name > :value
     */
    public static Conditional gt(final String name, final Serializable value, final Entity entity) {
        return new ValueComparison(name, ConditionalOperator.GT, value, entity);
    }

/**
     * Genera un criterio de tipo '<' comparando un atributo de la entidad por defecto con el valor indicado.
     *
     * @param name Nombre del atributo que vamos a comparar.
     * @param value Valor con el que vamos a comparar el atributo
     * @return Criterio de tipo name < :value
     */
    public static Conditional lt(final String name, final Serializable value) {
        return new ValueComparison(name, ConditionalOperator.LT, value);
    }

/**
     * Genera un criterio de tipo '<' comparando un atributo de la entidad indicada con el valor indicado.
     *
     * @param name Nombre del atributo que vamos a comparar.
     * @param value Valor con el que vamos a comparar el atributo
     * @param entity Entidad a la que aplica el atributo
     * @return Criterio de tipo entity.name < :value
     */
    public static Conditional lt(final String name, final Serializable value, final Entity entity) {
        return new ValueComparison(name, ConditionalOperator.LT, value, entity);
    }

    /**
     * Genera un criterio de tipo '>=' comparando un atributo de la entidad por defecto con el valor indicado.
     * 
     * @param name
     *            Nombre del atributo que vamos a comparar.
     * @param value
     *            Valor con el que vamos a comparar el atributo
     * @return Criterio de tipo name >= :value
     */
    public static Conditional ge(final String name, final Serializable value) {
        return new ValueComparison(name, ConditionalOperator.GE, value);
    }

    /**
     * Genera un criterio de tipo '>=' comparando un atributo de la entidad indicada con el valor indicado.
     * 
     * @param name
     *            Nombre del atributo que vamos a comparar.
     * @param value
     *            Valor con el que vamos a comparar el atributo
     * @param entity
     *            Entidad a la que aplica el atributo
     * @return Criterio de tipo entity.name >= :value
     */
    public static Conditional ge(final String name, final Serializable value, final Entity entity) {
        return new ValueComparison(name, ConditionalOperator.GE, value, entity);
    }

    /**
     * Genera un criterio de tipo '<=' comparando un atributo de la entidad por defecto con el valor indicado.
     * 
     * @param name
     *            Nombre del atributo que vamos a comparar.
     * @param value
     *            Valor con el que vamos a comparar el atributo
     * @return Criterio de tipo name <= :value
     */
    public static Conditional le(final String name, final Serializable value) {
        return new ValueComparison(name, ConditionalOperator.LE, value);
    }

    /**
     * Genera un criterio de tipo '<=' comparando un atributo de la entidad indicada con el valor indicado.
     * 
     * @param name
     *            Nombre del atributo que vamos a comparar.
     * @param value
     *            Valor con el que vamos a comparar el atributo
     * @param entity
     *            Entidad a la que aplica el atributo
     * @return Criterio de tipo entity.name <= :value
     */
    public static Conditional le(final String name, final Serializable value, final Entity entity) {
        return new ValueComparison(name, ConditionalOperator.LE, value, entity);
    }

    /**
     * Sobrescrito el método toString. Este método será invocado en el cliente para generar las URLs asociadas a los cirterios de búsqueda. Ejemplo :
     * atributo_EQ_valor
     */
    @Override
    public String toString() {
        return getName() + QUERY_SEPARATOR + getOperator() + QUERY_SEPARATOR + getValue();
    }

    /**
     * @return the caseSensitive
     */
    public boolean isCaseSensitive() {
        return super.isCaseSensitive() || !(value instanceof String);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getOperator(), value);
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
        return (super.equals(other) && value.equals(((ValueComparison) obj).value));
    }

}
