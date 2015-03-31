package com.diwa.dao.shared.criteria.conditional;

import com.diwa.dao.shared.entity.Entity;
import com.google.common.base.Objects;

/**
 * Representa las comparaciones entre dos atributos. De esta forma se compararán los atributos de una misma entidad o de distintas entidades. En este
 * caso no comparamos un valor indicado con un atributo, sino que comparamos el valor de los dos atributos indicados.
 * <p>
 * Esta comparación permite la generación de los criterios =, !=, >, >=, < y <=. Todos estos criterios serán aplicados entre los dos atributos
 * indicados.
 * <p>
 * Proporciona un conjunto de métodos estáticos para facilitar la generación del criterio, por el contrario, no se permite el acceso a los
 * constructores.
 * <p>
 * A continuación se muestra un ejemplo de como se crea un criterio de igualdad entre atributos:
 * <p>
 * <code>
 *        Criteria condicion = FieldComparison.eq("at1", "at2");
 * </code>
 */
public class FieldComparison extends CaseSensitiveConditional {

    /**
     * Prefijo que se le añade a los FieldComparison para diferenciarlo del ValueComparison.
     */
    public final static char PREFIX_OPERATOR = 'F';

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Atributo con el que vamos a comparar. Este es el segundo atributo.
     */
    private String field;

    /**
     * Entidad a la que pertenece el atributo con el que vamos a comparar.
     */
    private Entity entity2;

    /**
     * Constructor vacío necesario para serialización en GWT.
     */
    public FieldComparison() {
    }

    /**
     * Constructor privado. Genera una criterio sobre dos atributos que pertenecen a la misma entidad.
     * 
     * @param name
     *            Nombre del primer atributo
     * @param operator
     *            Operador correspondiente a la operación
     * @param field2
     *            Atributo con el que vamos a comparar
     */
    private FieldComparison(final String name, final ConditionalOperator operator, final String field2) {
        super(name, operator);
        this.field = field2;
    }

    /**
     * Constructor privado. Genera una criterio sobre dos atributos que pertenecen a diferentes entidades.
     * 
     * @param entity1
     *            Entidad a la que pertenece el primer atributo
     * @param name
     *            Nombre del primer atributo
     * @param operator
     *            Operador correspondiente a la operación
     * @param entity2
     *            Entidad a la que pertenece el atributo field2
     * @param field2
     *            Atributo con el que vamos a comparar
     */
    private FieldComparison(final Entity entity1, final String name, final ConditionalOperator operator, final Entity entity2,
            final String field2) {
        super(name, operator, entity1);
        this.field = field2;
        this.entity2 = entity2;
    }

    /**
     * Proporciona el atributo con el que vamos a comparar.
     * 
     * @return Atributo con el que vamos a comparar.
     */
    public String getField() {
        return field;
    }

    /**
     * Proporciona la entidad a la que pertenece el atributo con el que vamos a comparar.
     * 
     * @return Entidad a la que pertenece el atributo con el que vamos a comparar
     */
    public Entity getEntity2() {
        return entity2;
    }

    /**
     * Establece el atributo con el que vamos a comparar.
     * 
     * @param field
     *            Atributo con el que vamos a comparar.
     */
    public void setField(final String field) {
        this.field = field;
    }

    /**
     * Establece la entidad a la que pertenece el atributo con el que vamos a comparar.
     * 
     * @param entity2
     *            Entidad a la que pertenece el atributo con el que vamos a comparar
     */
    public void setEntity2(final Entity entity2) {
        this.entity2 = entity2;
    }

    /**
     * Genera un criterio de tipo '=' comparando dos atributos de la entidad por defecto.
     * 
     * @param name
     *            Nombre del primer atributo
     * @param field2
     *            Nombre del segundo atributo
     * @return Criterio de tipo name = field2
     */
    public static Conditional eq(final String name, final String field2) {
        return new FieldComparison(name, ConditionalOperator.EQ, field2);
    }

    /**
     * Genera un criterio de tipo '=' comparando dos atributos de la entidad indicada.
     * 
     * @param entity
     *            Entidad a la que pertenecen los dos atributos
     * @param name
     *            Nombre del primer atributo
     * @param field2
     *            Nombre del segundo atributo
     * @return Criterio de tipo entity.name = entity.field2
     */
    public static Conditional eq(final Entity entity, final String name, final String field2) {
        return new FieldComparison(entity, name, ConditionalOperator.EQ, entity, field2);
    }

    /**
     * Genera un criterio de tipo '=' comparando dos atributos de diferentes entidades.
     * 
     * @param entity1
     *            Entidad a la que pertenece el primer atributo
     * @param name
     *            Nombre del primer atributo
     * @param entity2
     *            Entidad a la que pertenece el segundo atributo
     * @param field2
     *            Nombre del segundo atributo
     * @return Criterio de tipo entity1.name = entity2.field2
     */
    public static Conditional eq(final Entity entity1, final String name, final Entity entity2, final String field2) {
        return new FieldComparison(entity1, name, ConditionalOperator.EQ, entity2, field2);
    }

    /**
     * Genera un criterio de tipo '!=' comparando dos atributos de la entidad por defecto.
     * 
     * @param name
     *            Nombre del primer atributo
     * @param field2
     *            Nombre del segundo atributo
     * @return Criterio de tipo name != field2
     */
    public static Conditional ne(final String name, final String field2) {
        return new FieldComparison(name, ConditionalOperator.NE, field2);
    }

    /**
     * Genera un criterio de tipo '!=' comparando dos atributos de la entidad indicada.
     * 
     * @param entity
     *            Entidad a la que pertenecen los dos atributos
     * @param name
     *            Nombre del primer atributo
     * @param field2
     *            Nombre del segundo atributo
     * @return Criterio de tipo entity.name != entity.field2
     */
    public static Conditional ne(final Entity entity, final String name, final String field2) {
        return new FieldComparison(entity, name, ConditionalOperator.NE, entity, field2);
    }

    /**
     * Genera un criterio de tipo '!=' comparando dos atributos de diferentes entidades.
     * 
     * @param entity1
     *            Entidad a la que pertenece el primer atributo
     * @param name
     *            Nombre del primer atributo
     * @param entity2
     *            Entidad a la que pertenece el segundo atributo
     * @param field2
     *            Nombre del segundo atributo
     * @return Criterio de tipo entity1.name != entity2.field2
     */
    public static Conditional ne(final Entity entity1, final String name, final Entity entity2, final String field2) {
        return new FieldComparison(entity1, name, ConditionalOperator.NE, entity2, field2);
    }

    /**
     * Genera un criterio de tipo '>' comparando dos atributos de la entidad por defecto.
     * 
     * @param name
     *            Nombre del primer atributo
     * @param field2
     *            Nombre del segundo atributo
     * @return Criterio de tipo name > field2
     */
    public static Conditional gt(final String name, final String field2) {
        return new FieldComparison(name, ConditionalOperator.GT, field2);
    }

    /**
     * Genera un criterio de tipo '>' comparando dos atributos de la entidad indicada.
     * 
     * @param entity
     *            Entidad a la que pertenecen los dos atributos
     * @param name
     *            Nombre del primer atributo
     * @param field2
     *            Nombre del segundo atributo
     * @return Criterio de tipo entity.name > entity.field2
     */
    public static Conditional gt(final Entity entity, final String name, final String field2) {
        return new FieldComparison(entity, name, ConditionalOperator.GT, entity, field2);
    }

    /**
     * Genera un criterio de tipo '>' comparando dos atributos de diferentes entidades.
     * 
     * @param entity1
     *            Entidad a la que pertenece el primer atributo
     * @param name
     *            Nombre del primer atributo
     * @param entity2
     *            Entidad a la que pertenece el segundo atributo
     * @param field2
     *            Nombre del segundo atributo
     * @return Criterio de tipo entity1.name > entity2.field2
     */
    public static Conditional gt(final Entity entity1, final String name, final Entity entity2, final String field2) {
        return new FieldComparison(entity1, name, ConditionalOperator.GT, entity2, field2);
    }

    /**
     * Genera un criterio de tipo '>' comparando dos atributos de la entidad por defecto.
     * 
     * @param name
     *            Nombre del primer atributo
     * @param field2
     *            Nombre del segundo atributo
     * @return Criterio de tipo name < field2
     */
    public static Conditional lt(final String name, final String field2) {
        return new FieldComparison(name, ConditionalOperator.LT, field2);
    }

/**
     * Genera un criterio de tipo '<' comparando dos atributos de la entidad indicada.
     *
     * @param entity Entidad a la que pertenecen los dos atributos
     * @param name Nombre del primer atributo
     * @param field2 Nombre del segundo atributo
     * @return Criterio de tipo entity.name < entity.field2
     */
    public static Conditional lt(final Entity entity, final String name, final String field2) {
        return new FieldComparison(entity, name, ConditionalOperator.LT, entity, field2);
    }

/**
     * Genera un criterio de tipo '<' comparando dos atributos de diferentes entidades.
     *
     * @param entity1 Entidad a la que pertenece el primer atributo
     * @param name Nombre del primer atributo
     * @param entity2 Entidad a la que pertenece el segundo atributo
     * @param field2 Nombre del segundo atributo
     * @return Criterio de tipo entity1.name < entity2.field2
     */
    public static Conditional lt(final Entity entity1, final String name, final Entity entity2, final String field2) {
        return new FieldComparison(entity1, name, ConditionalOperator.LT, entity2, field2);
    }

    /**
     * Genera un criterio de tipo '>=' comparando dos atributos de la entidad por defecto.
     * 
     * @param name
     *            Nombre del primer atributo
     * @param field2
     *            Nombre del segundo atributo
     * @return Criterio de tipo name >= field2
     */
    public static Conditional ge(final String name, final String field2) {
        return new FieldComparison(name, ConditionalOperator.GE, field2);
    }

    /**
     * Genera un criterio de tipo '>=' comparando dos atributos de la entidad indicada.
     * 
     * @param entity
     *            Entidad a la que pertenecen los dos atributos
     * @param name
     *            Nombre del primer atributo
     * @param field2
     *            Nombre del segundo atributo
     * @return Criterio de tipo entity.name >= entity.field2
     */
    public static Conditional ge(final Entity entity, final String name, final String field2) {
        return new FieldComparison(entity, name, ConditionalOperator.GE, entity, field2);
    }

    /**
     * Genera un criterio de tipo '>=' comparando dos atributos de diferentes entidades.
     * 
     * @param entity1
     *            Entidad a la que pertenece el primer atributo
     * @param name
     *            Nombre del primer atributo
     * @param entity2
     *            Entidad a la que pertenece el segundo atributo
     * @param field2
     *            Nombre del segundo atributo
     * @return Criterio de tipo entity1.name >= entity2.field2
     */
    public static Conditional ge(final Entity entity1, final String name, final Entity entity2, final String field2) {
        return new FieldComparison(entity1, name, ConditionalOperator.GE, entity2, field2);
    }

    /**
     * Genera un criterio de tipo '<=' comparando dos atributos de la entidad por defecto.
     * 
     * @param name
     *            Nombre del primer atributo
     * @param field2
     *            Nombre del segundo atributo
     * @return Criterio de tipo name <= field2
     */
    public static Conditional le(final String name, final String field2) {
        return new FieldComparison(name, ConditionalOperator.LE, field2);
    }

    /**
     * Genera un criterio de tipo '<=' comparando dos atributos de la entidad indicada.
     * 
     * @param entity
     *            Entidad a la que pertenecen los dos atributos
     * @param name
     *            Nombre del primer atributo
     * @param field2
     *            Nombre del segundo atributo
     * @return Criterio de tipo entity.name <= entity.field2
     */
    public static Conditional le(final Entity entity, final String name, final String field2) {
        return new FieldComparison(entity, name, ConditionalOperator.LE, entity, field2);
    }

    /**
     * Genera un criterio de tipo '<=' comparando dos atributos de diferentes entidades.
     * 
     * @param entity1
     *            Entidad a la que pertenece el primer atributo
     * @param name
     *            Nombre del primer atributo
     * @param entity2
     *            Entidad a la que pertenece el segundo atributo
     * @param field2
     *            Nombre del segundo atributo
     * @return Criterio de tipo entity1.name <= entity2.field2
     */
    public static Conditional le(final Entity entity1, final String name, final Entity entity2, final String field2) {
        return new FieldComparison(entity1, name, ConditionalOperator.LE, entity2, field2);
    }

    /**
     * Sobrescrito el método toString. Este método será invocado en el cliente para generar las URLs asociadas a los cirterios de búsqueda. Ejemplo :
     * atributo1-FEQ-atributo2
     */
    public String toString() {
        return getName() + QUERY_SEPARATOR + PREFIX_OPERATOR + getOperator() + QUERY_SEPARATOR + getField();
    }

    @Override
    public int hashCode() {
        if (entity2 == null)
            return Objects.hashCode(getOperator(), field);
        else
            return Objects.hashCode(getOperator(), field, entity2);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        FieldComparison f = (FieldComparison) obj;
        if ((entity2 == null && f.getEntity2() != null) || (entity2 != null && f.getEntity2() == null))
            return false;

        Conditional other = (Conditional) obj;
        return (super.equals(other) && field.equals(f.field));
    }
}
