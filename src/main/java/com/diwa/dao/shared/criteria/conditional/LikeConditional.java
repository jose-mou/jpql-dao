package com.diwa.dao.shared.criteria.conditional;

import com.diwa.dao.shared.criteria.Criteria;
import com.diwa.dao.shared.entity.Entity;
import com.google.common.base.Objects;

/**
 * Modela los criterios de búsqueda like, es decir, criterio de búsqueda de cadenas parecidas a la indicada. A la hora de buscar las cadenas parecidas
 * se pueden utilizar expresiones regulares con los caracteres '_' o '%'. Este criterio representa a la sentencia LIKE de de SQL.
 * <p>
 * Debido a que normalmente las aplicaciones suelen utilizar el like para cadenas que contienen una cadena determinada, a la hora de procesarla se
 * añade el carácter '%' al comienzo y al final de la cadena por la que buscamos. También se establece la posibilidad de no diferenciar entre
 * mayúsculas y minúsculas (caseSensitive = false), esto es modelado mediante el paso a mayúscula de cadena y la sentencia UPPER.
 * <p>
 * A continuación se muestra un ejemplo de como se crea un criterio between:
 * <p>
 * <code>
 *        Criteria condicion = new LikeConditional("atributo", "cadena");
 * </code>
 */
public class LikeConditional extends CaseSensitiveConditional {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Valor del atributo con el que vamos a comparar.
     */
    private String value;

    /**
     * Indica si se permitirán caulquier valor por la izquierda, es decir, si se añadirá el wildcar % a la izquierda de la expresión. Por defecto a
     * true.
     */
    private boolean leftWildcard = true;

    /**
     * Indica si se permitirán caulquier valor por la derecha, es decir, si se añadirá el wildcar % a la derecha de la expresión. Por defecto a true.
     */
    private boolean rightWildcard = true;

    /**
     * Constructor vacío necesario para serialización en GWT.
     */
    public LikeConditional() {
    }

    /**
     * Constructor. Genera un criterio LIKE no sensible a mayúsculas/minúsculas.
     * 
     * @param name
     *            Nombre del atributo que vamos a comparar
     * @param value
     *            Valor del atributo con el que vamos a comparar
     */
    public LikeConditional(final String name, final String value) {
        this(name, value, null);
    }

    /**
     * Constructor. Genera un criterio LIKE no sensible a mayúsculas/minúsculas.
     * 
     * @param name
     *            Nombre del atributo que vamos a comparar
     * @param value
     *            Valor del atributo con el que vamos a comparar
     * @param entity
     *            Entidad a la que pertenece el atributo
     */
    public LikeConditional(final String name, final String value, final Entity entity) {
        this(name, value, false, entity);
    }

    /**
     * Constructor. Genera un criterio LIKE.
     * 
     * @param name
     *            Nombre del atributo que vamos a comparar
     * @param value
     *            Valor del atributo con el que vamos a comparar
     * @param caseSensitive
     *            Indica si será sensible a mayúsculas/minúsculas
     */
    public LikeConditional(final String name, final String value, final boolean caseSensitive) {
        this(name, value, caseSensitive, null);
    }

    /**
     * Constructor. Genera un criterio LIKE.
     * 
     * @param name
     *            Nombre del atributo que vamos a comparar
     * @param value
     *            Valor del atributo con el que vamos a comparar
     * @param caseSensitive
     *            Indica si será sensible a mayúsculas/minúsculas
     * @param entity
     *            Entidad a la que pertenece el atributo
     */
    public LikeConditional(final String name, final String value, final boolean caseSensitive, final Entity entity) {
        super(name, ConditionalOperator.LIKE, entity);
        this.value = value;
        setCaseSensitive(caseSensitive);
    }

    /**
     * Proporciona el valor del atributo con el que vamos a comparar.
     * 
     * @return Valor del atributo con el que vamos a comparar
     */
    public String getValue() {
        return value;
    }

    /**
     * Establece el valor del atributo con el que vamos a comparar.
     * 
     * @param value
     *            Valor del atributo con el que vamos a comparar
     */
    public void setValue(final String value) {
        this.value = value;
    }

    /**
     * Sobrescrito el método toString. Este método será invocado en el cliente para generar las URLs asociadas a los cirterios de búsqueda. Ejemplo :
     * atributo-ILIKE-valor
     */
    public String toString() {
        if (isCaseSensitive()) {
            return getName() + Criteria.QUERY_SEPARATOR + getOperator() + getValue();
        } else {
            return getName() + Criteria.QUERY_SEPARATOR + "I" + getOperator() + Criteria.QUERY_SEPARATOR + getValue();
        }
    }

    /**
     * @return the leftWildcard
     */
    public boolean isLeftWildcard() {
        return leftWildcard;
    }

    /**
     * @param leftWildcard
     *            the leftWildcard to set
     */
    public void setLeftWildcard(boolean leftWildcard) {
        this.leftWildcard = leftWildcard;
    }

    /**
     * @return the rightWildcard
     */
    public boolean isRightWildcard() {
        return rightWildcard;
    }

    /**
     * @param rightWildcard
     *            the rightWildcard to set
     */
    public void setRightWildcard(boolean rightWildcard) {
        this.rightWildcard = rightWildcard;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getOperator(), value, leftWildcard, rightWildcard);
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
        LikeConditional lk = (LikeConditional) obj;
        return (super.equals(other) && value.equals(lk.value) && rightWildcard == lk.rightWildcard && leftWildcard == lk.leftWildcard);
    }

}
