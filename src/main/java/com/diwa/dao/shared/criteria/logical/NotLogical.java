package com.diwa.dao.shared.criteria.logical;

import com.diwa.dao.shared.criteria.Criteria;
import com.diwa.dao.shared.criteria.conditional.Conditional;
import com.google.common.base.Objects;

/**
 * Criteria de tipo lógico que permite la negación de un <code>Criteria</code>. Representa a la negación de un criterio de búsqueda. Mediante este
 * criterio se permitirá la negación de los siguientes <code>Criteria</code>: <li>
 * <ul>
 * <code>BetweenConditional</code>: Se generará una sentencia del tipo NOT BETWEEN
 * </ul>
 * <ul>
 * <code>InConditional</code>: Se generará una sentencia del tipo NOT IN
 * </ul>
 * <ul>
 * <code>LikeConditional</code>: Se generará una sentencia del tipo NOT LIKE
 * </ul>
 * <ul>
 * <code>MemberConditional</code>: Se generará una sentencia del tipo NOT MEMBER OF
 * </ul>
 * <ul>
 * <code>GroupLogical</code>: Se generará una sentencia que negará el grupo formado por el criterio. Ejemplo: NOT (... AND ... AND ...))
 * </ul>
 * </li> En los siguientes casos no se podrá utilizar el componentes de esta clase para la negación: <li>
 * <ul>
 * <code>NullConditional</code>: La negación de este criterio deberá de ser del tipo IS NOT NULL, por ello se ha reemplazado el NotLogical por el
 * método {@see Conditional#isNotNull}
 * </ul>
 * <ul>
 * <code>EmptyConditional</code>: La negación de este criterio deberá de ser del tipo IS NOT EMPTY, por ello se ha reemplazado el NotLogical por el
 * método {@see Conditional#isNotEmpty}
 * </ul>
 * </li>
 * <p>
 * A continuación se muestra un ejemplo para el uso de este criterio lógico:
 * <p>
 * <code>
 * Criteria like = Conditional.like("nombre_atributo", "valor_atributo");
 * Criteria negación = Logical.not(like);
 * </code>
 */
public class NotLogical extends Logical {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Expresión que va a ser negada.
     */
    private Criteria expresion;

    /**
     * Constructor vacío necesario para serialización en GWT.
     */
    public NotLogical() {
    }

    /**
     * Constructor.
     * 
     * @param criteria
     *            Expresión que va a ser negada
     */
    public NotLogical(final Criteria criteria) {
        super(LogicalOperator.NOT);
        setExpresion(criteria);
    }

    /**
     * @return the expresión Expresión que va a ser negada
     */
    public Criteria getExpresion() {
        return expresion;
    }

    /**
     * @param expresion
     *            Expresión que va a ser negada
     */
    public void setExpresion(final Criteria expresion) {
        this.expresion = expresion;
    }

    /**
     * Sobrescrito el método toString. Este método será invocado en el cliente para generar las URLs asociadas a los cirterios de búsqueda. Ejemplo :
     * NOT:E1
     */
    @Override
    public String toString() {
        return getOperator() + Criteria.QUERY_CRITERIA_SEPARATOR + expresion;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(expresion, operator);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        NotLogical f = (NotLogical) obj;

        Conditional other = (Conditional) obj;
        return (super.equals(other) && expresion.equals(f.expresion) && operator.equals(f.operator));
    }

}
