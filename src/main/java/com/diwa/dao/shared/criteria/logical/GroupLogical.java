package com.diwa.dao.shared.criteria.logical;

import com.diwa.dao.shared.criteria.Criteria;
import com.diwa.dao.shared.criteria.conditional.Conditional;
import com.google.common.base.Objects;

import java.util.Collection;
import java.util.List;

/**
 * Modela los criterios lógicos de disyunción(OR) y conjunción(AND) de criterios de búsqueda. Esto coincide con la operaciones AND y OR de SQL.
 * <p>
 * Nos proporciona un conjunto de métodos estáticos que nos facilitan la generación de criterios de disyunción/conjunción.
 * <p>
 * A continuación se muestra un ejemplo de como se crea un criterio a partir de los métodos estáticos:
 * <p>
 * <code>
 *        Criteria condicion = GroupLogical.and(ValueComparison.eq("at1", "valor1"), ValueComparison.lt("at2", value2));
 * </code>
 * <p>
 * NOTA: Por lo que se ha comprobado en la actualidad, en disyunciones/conjunciones bastante complejas, Hibernate no implementa correctamente la
 * especificación de JPA y genera una consulta errónea. Este fallo no está relacionado con el dao, sino con la implementación que Hibernate realiza de
 * la especificación de JPA.
 */
public class GroupLogical extends Logical {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Condiciones que son afectadas por el operador lógico.
     */
    private List<Criteria> contitionals;

    /**
     * Constructor vacío necesario para serialización en GWT.
     */
    public GroupLogical() {
    }

    /**
     * Constructor. Proporciona un criterio lógico a partir del conjunto de criterios que son afectados por el operador indicado
     * 
     * @param contitionals
     *            Condiciones que son afectadas por el operador lógico.
     * @param operator
     *            Operador que afecta a los criterios.
     */
    protected GroupLogical(final List<Criteria> contitionals, final LogicalOperator operator) {
        super(operator);
        setContitionals(contitionals);
    }

    /**
     * @return Condiciones que son afectadas por el operador lógico
     */
    public Collection<Criteria> getContitionals() {
        return contitionals;
    }

    /**
     * @param contitionals
     *            Condiciones que son afectadas por el operador lógico
     */
    public void setContitionals(final List<Criteria> contitionals) {
        this.contitionals = contitionals;
    }

    /**
     * Operación de AND. Corresponde al AND de SQL. Genera un GroupLogical con el operador AND (( {@link LogicalOperator#AND}))
     * 
     * @param criterias
     *            Criterios que son afectados por el operador
     * @return Criterio AND resultante
     */
    public static GroupLogical and(final List<Criteria> criterias) {
        return new GroupLogical(criterias, LogicalOperator.AND);
    }

    /**
     * Operación de OR. Corresponde al OR de SQL. Genera un GroupLogical con el operador OR (( {@link LogicalOperator#OR}))
     * 
     * @param criterias
     *            Criterios que son afectados por el operador
     * @return Criterio OR resultante
     */
    public static GroupLogical or(final List<Criteria> criterias) {
        return new GroupLogical(criterias, LogicalOperator.OR);
    }

    /**
     * Sobrescrito el método toString. Este método será invocado en el cliente para generar las URLs asociadas a los cirterios de búsqueda. Ejemplo :
     * Q1:OR:Q2:OR:Q3:OR:Q4
     */
    @Override
    public String toString() {
        String result = null;
        for (Criteria c : contitionals) {
            result = getOperator() + Criteria.QUERY_CRITERIA_SEPARATOR + c.toString();
        }
        return result.substring(getOperator().toString().length());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(contitionals, operator);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        GroupLogical f = (GroupLogical) obj;

        Conditional other = (Conditional) obj;
        return (super.equals(other) && contitionals.equals(f.contitionals) && operator.equals(f.operator));
    }
}
