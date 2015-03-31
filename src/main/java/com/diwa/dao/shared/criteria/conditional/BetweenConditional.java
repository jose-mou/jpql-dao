package com.diwa.dao.shared.criteria.conditional;

import com.diwa.dao.shared.entity.Entity;
import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * Modela los criterios de búsqueda between entre dos valores, esto equivale a la conjunción de los criterios >= y <= sobre el mismo atributo.
 * Equivale a la condición BETWEEN de SQL.
 * <p>
 * En este caso para la generación de los criterios Between se utilizará alguno de los constructores proporcionados.
 * <p>
 * A continuación se muestra un ejemplo de como se crea un criterio between:
 * <p>
 * <code>
 *        Criteria condicion = new BetweenConditional("atributo", valorInferior, valorSuperior);
 * </code>
 */
public class BetweenConditional extends Conditional {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Primer valor con el que vamos a comparar. Este atributo establece el límite inferior en la condición.
     */
    private Serializable value1;

    /**
     * Segundo valor con el que vamos a comparar. Este atributo establece el límite superior en la condición.
     */
    private Serializable value2;

    /**
     * Constructor vacío necesario para serialización en GWT.
     */
    public BetweenConditional() {
    }

    /**
     * Constructor que permite la creación de una condición de between entre dos valores.
     * 
     * @param name
     *            Nombre del atributo que vamos a comparar
     * @param value1
     *            Valor inferior. El atributo ha de ser superior a el valor indicado.
     * @param value2
     *            Valor superior. El atributo ha de ser inferior a el valor indicado.
     */
    public BetweenConditional(final String name, final Serializable value1, final Serializable value2) {
        super(name, ConditionalOperator.BETWEEN);
        this.value1 = value1;
        this.value2 = value2;
    }

    /**
     * Constructor que permite la creación de una condición de between entre dos valores. En este caso especificamos la entidad a la que pertenece el
     * atributo, en caso de que esa sea nula, se considera que el atributo pertenece a la entidad por defecto.
     * 
     * @param name
     *            Nombre del atributo que vamos a comparar
     * @param entity
     *            Entidad a la que pertenece el atributo que vamos a comparar, esta entidad ha de estar declarada en la búsqueda anteriormente.
     * @param value1
     *            Valor inferior. El atributo ha de ser superior a el valor indicado.
     * @param value2
     *            Valor superior. El atributo ha de ser inferior a el valor indicado.
     */
    public BetweenConditional(final String name, final Entity entity, final Serializable value1, final Serializable value2) {
        super(name, ConditionalOperator.BETWEEN, entity);
        this.value1 = value1;
        this.value2 = value2;
    }

    /**
     * Obtiene el límite superior de la condición.
     * 
     * @return Valor del límite superior establecido en la condición.
     */
    public Serializable getValue2() {
        return value2;
    }

    /**
     * Modifica el límite superior de la condición.
     * 
     * @param value2
     *            Valor del límite superior que va a ser establecido en la condición.
     */
    public void setValue2(final Serializable value2) {
        this.value2 = value2;
    }

    /**
     * Obtiene el límite inferior de la condición.
     * 
     * @return Valor del límite inferior establecido en la condición.
     */
    public Serializable getValue1() {
        return value1;
    }

    /**
     * Modifica el límite inferior de la condición.
     * 
     * @param value1
     *            Valor del límite inferior que va a ser establecido en la condición.
     */
    public void setValue1(final Serializable value1) {
        this.value1 = value1;
    }

    /**
     * Sobrescrito el método toString. Este método será invocado en el cliente para generar las URLs asociadas a los cirterios de búsqueda. Ejemplo :
     * atributo1-BETWEEN-v1,v2
     */
    public String toString() {
        return getName() + QUERY_SEPARATOR + getOperator() + QUERY_SEPARATOR + getValue1() + LIST_ELEMENT_SEPARATOR + getValue2();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getOperator(), value1, value2);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        BetweenConditional f = (BetweenConditional) obj;

        Conditional other = (Conditional) obj;
        return (super.equals(other) && value1.equals(f.value1) && value2.equals(f.value2));
    }

}
