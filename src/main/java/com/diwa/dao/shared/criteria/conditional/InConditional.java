package com.diwa.dao.shared.criteria.conditional;

import com.diwa.dao.shared.criteria.Criteria;
import com.diwa.dao.shared.entity.Entity;
import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.List;

/**
 * Modela los criterios de búsqueda para obtener aquellos registros que tienen el atributo indicado dentro de un conjunto de valores establecidos.
 * Esta condición se satisfacerá para aquellos registros en los que el valor de atributo indicado corresponda a alguno de los valores indicados en la
 * lista pasada.
 * <p>
 * El criterio de búsqueda se corresponde con la anotación IN de SQL. Hay que tener en cuenta que el número máximo de valores permitidos en la
 * sentencia IN es de 999 elementos. Esta restricción viene dada por la BD.
 * <p>
 * A continuación se muestra un ejemplo de como se crea un criterio in:
 * <p>
 * <code>
 *        List valoresPosibles = new ArrayList();
 *        valoresPosibles.add("valor1");
 *        valoresPosibles.add("valor2");
 *        ...
 *        Criteria condicion = new InConditional("atributo", valoresPosibles);
 * </code>
 * <p>
 * Para la negación de este criterio ver NotLogical.
 */
public class InConditional extends CaseSensitiveConditional {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Colección de valores en los que se puede encontrar el atributo.
     */
    private List<? extends Serializable> values;

    /**
     * Constructor vacío necesario para serialización en GWT.
     */
    public InConditional() {
    }

    /**
     * Constructor.
     * 
     * @param name
     *            Nombre del atributo
     * @param values
     *            Colección de valores en los que se puede encontrar el atributo
     */
    public InConditional(final String name, final List<? extends Serializable> values) {
        super(name, ConditionalOperator.IN);
        this.values = values;
    }

    /**
     * Constructor.
     * 
     * @param name
     *            Nombre del atributo
     * @param values
     *            Colección de valores en los que se puede encontrar el atributo
     * @param entity
     *            Entidad a la que pertenece el atributo
     */
    public InConditional(final String name, final Entity entity, final List<? extends Serializable> values) {
        super(name, ConditionalOperator.IN, entity);
        this.values = values;
    }

    /**
     * Obtiene la colección de valores en los que se puede encontrar el atributo. Esta puede ser modificada a través de sus métodos.
     * 
     * @return Colección de valores en los que se puede encontrar el atributo
     */
    public List<? extends Serializable> getValues() {
        return values;
    }

    /**
     * Establece la colección de valores en los que se puede encontrar el atributo.
     * 
     * @param values
     *            Colección de valores en los que se puede encontrar el atributo
     */
    public void setValues(final List<? extends Serializable> values) {
        this.values = values;
    }

    /**
     * Sobrescrito el método toString. Este método será invocado en el cliente para generar las URLs asociadas a los cirterios de búsqueda. Ejemplo :
     * atributo-IN-valor1,valor2,valor3
     */
    public String toString() {
        String result = getName() + Criteria.QUERY_SEPARATOR + getOperator();
        for (Serializable v : values) {
            result = result + v.toString() + Criteria.LIST_ELEMENT_SEPARATOR;
        }
        return result.substring(0, result.length() - 1);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getOperator(), values);
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
        return (super.equals(other) && values.equals(((InConditional) obj).values));
    }

}
