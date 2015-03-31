package com.diwa.dao.shared.criteria.logical;

import com.diwa.dao.shared.criteria.Criteria;

/**
 * Representa los diferentes criterios lógicos utilizados a la hora de filtrar en las consultas realizadas. Los criterios lógicos pueden ser los
 * siguientes: <li>
 * <ul>
 * Negación (NOT): Niega el criterio al que precede {@link NotLogical}
 * </ul>
 * <ul>
 * Unión (OR): Unión de criterios a la hora de realizar la búsqueda {@link GroupLogical}
 * </ul>
 * <ul>
 * Intersección (AND): Intersección de los criterios a la hora de realizar la búsqueda {@link GroupLogical}
 * </ul>
 * </li> Esta clase envuelve a los diferentes tipos de criterios lógicos y además permite la creación de los mismos mediante un conjunto de métodos
 * estáticos, que devuelven como resultado los correspondientes tipos de Logical. Desde los métodos estáticos se llama a los diferentes constructores
 * de la clase que extiende a ésta (Estos constructores sólo son accesibles de esta forma ya que son <code>protected</code>).
 * <p>
 * A continuación se muestra un ejemplo de como se crean los diferentes criterios a partir de los métodos estáticos:
 * <p>
 * <code>
 *        Criteria condicion = Conditional.eq("nombreAtributo", "valor");
 *        Criteria logico = Logical.not(condicion);
 * </code>
 */
public abstract class Logical implements Criteria {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Operador lógico.
     */
    protected LogicalOperator operator;

    /**
     * Constructor vacío necesario para serialización en GWT.
     */
    public Logical() {
    }

    /**
     * Contructor por defecto.
     */
    protected Logical(final LogicalOperator operator) {
        this.operator = operator;
    }

    /**
     * @return Operador lógico.
     */
    public final LogicalOperator getOperator() {
        return operator;
    }

    /**
     * @param operator
     *            Operador lógico.
     */
    public final void setOperator(final LogicalOperator operator) {
        this.operator = operator;
    }
}
