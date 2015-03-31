package com.diwa.dao.shared.criteria.conditional;

import com.diwa.dao.shared.entity.Entity;
import com.google.common.base.Objects;

/**
 * Interface que han de implementar cualquier criterio de búsqueda que pueda ser sensible a maúsculas y minúsculas.
 */
public abstract class CaseSensitiveConditional extends Conditional {

    private static final long serialVersionUID = 1L;

    /**
     * Indica si será sensible a mayúsuculas/minúsuculas a la hora de realizar la comparación de strings. Este valor sólo se tendrá en cuenta cunado
     * comparamos cadenas y por defecto será sensible a mayúsuculas y minúsuculas.
     */
    private boolean caseSensitive = true;

    /**
     * Constructor vacío necesario para serialización en GWT.
     */
    public CaseSensitiveConditional() {
        super();
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
    protected CaseSensitiveConditional(final String name, final ConditionalOperator operator) {
        super(name, operator);
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
    protected CaseSensitiveConditional(final String name, final ConditionalOperator operator, final Entity entity) {
        super(name, operator, entity);
    }

    /**
     * Indica si la comparación será sensible a mayúsculas/minúsculas (caseSensitive) o no.
     * 
     * @return Si la comparación será sensible a mayúsculas/minúsculas.
     */
    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    /**
     * Establece si la comparación será sensible a mayúsculas/minúsculas.
     * 
     * @param caseSensitive
     *            Si la comparación será sensible a mayúsculas/minúsculas.
     */
    public void setCaseSensitive(final boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getOperator(), caseSensitive);
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
        return (super.equals(other) && caseSensitive == ((CaseSensitiveConditional) (Conditional) obj).caseSensitive);
    }

}
