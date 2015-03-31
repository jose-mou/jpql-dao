package com.diwa.dao.shared.criteria;

import com.diwa.dao.shared.entity.Entity;
import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Criterio personalizado. Este creiterio no tiene ninguna estructura predefinida, de tal forma que a la hora de
 * componerlo se ha de tener en cuenta la estrucutra de los criterios de búqueda en una sentencia JPQL.
 * <p>
 * A la hora de definirlo se pueden indicar las propiedades a las que se hacen referencia en la query. Si la asociamos a
 * una entidad se resolverá la misma, en el caso de que no se asocie a una entidad querrá decir que es la propiedad hace
 * referencia a la entidad por defecto. Si una propiedad no es añadidad, pero es utilizada en la query, no será
 * sustituida.
 */
public class PersonalCriteria implements Criteria {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Criterio JPQL definido. Este criterio ha de ser coerente con la especificación JPQL.
     */
    private String query;

    /**
     * Indica si el criterio será procesado en los COUNT. Por defecto a true.
     */
    private boolean applyInCount = true;

    /**
     * Alias/Entidad a la que pertenece el atributo.
     */
    private Map<String, ReplaceProperty> properties = new HashMap<String, ReplaceProperty>();

    /**
     * Conjunto de parámetros que serán pasados a la query.
     */
    private Map<String, Serializable> parameters = new HashMap<String, Serializable>();

    /**
     * Constructor vacío necesario para serialización en GWT.
     */
    public PersonalCriteria() {
    }

    /**
     * Constructor a partir de la sentencia JPQL a la que corresponde el criterio de búsqueda.
     * 
     * @param query Sentencia JPQL que se corresponde con este criterio de busqueda.
     */
    public PersonalCriteria(final String query) {
        this.query = query;
    }

    /**
     * @return Criterio JPQL definido.
     */
    public String getQuery() {
        return query;
    }

    /**
     * Modifica el criterio JPQL definido.
     * 
     * @param query Criterio JPQL definido.
     */
    public void setQuery(final String query) {
        this.query = query;
    }

    /**
     * @return Map en el que la key son las propiedades definidas y los valores son las entidades sobre las que aplican
     *         las propiedades.
     */
    public Map<String, ReplaceProperty> getProperties() {
        return properties;
    }

    /**
     * Define una cadena que es utilizada en la query e indica la propiedad en de la entidad base con la que se mapea.
     * De esta forma todas las referencias a la propiedad indicada se remplazarán por la propiedad origen.
     * 
     * @param string Cadena definidad en el criterio creado.
     * @param targetProperty Propiedad de la entidad raiz.
     */
    public void addProperty(final String string, String targetProperty) {
        properties.put(string, new ReplaceProperty(targetProperty));
    }

    /**
     * Define la cadean que será reemplazada por la propiedad de la entidad indicada.
     * 
     * @param string Cadena definidad en el criterio creado y que será reemplazada.
     * @param targetProperty Propiedad de la entidad definida.
     * @param entity Entidad sobre la que definimos la propiedad.
     */
    public void addProperty(final String string, String targetProperty, Entity entity) {
        properties.put(string, new ReplaceProperty(targetProperty));
    }

    /**
     * @return Parámetros establecidos y los cuales serán sustituidos a la hora de generar la consulta.
     */
    public Map<String, Serializable> getParameters() {
        return parameters;
    }

    /**
     * Permite definir un parámetro que es utilizado en el criterio JPQL endicado antriormente. Para hacer referencia a
     * los parámetros definidos desde el criterio lo podemos realizar mediante el nombre del parametro precedido del
     * caracter ':'.
     * 
     * @param parameter Parámetro al que se hace referencia en el criterio JPQL.
     * @param value Valor asociado al parámetro.
     */
    public void addParameter(final String parameter, final Serializable value) {
        parameters.put(parameter, value);
    }

    /**
     * Indica si el crierio será aplicado en las operaciones COUNT.
     */
    public boolean isApplyInCount() {
        return applyInCount;
    }

    /**
     * Establece si el crierio será aplicado en la operación COUNT, por defecto su valor es true.
     */
    public void setApplyInCount(boolean applyInCount) {
        this.applyInCount = applyInCount;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(query, parameters, properties);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PersonalCriteria other = (PersonalCriteria) obj;
        return (super.equals(other) && query.equals(other.query) && Objects.equal(parameters, other.parameters) && Objects
                .equal(properties, other.properties));
    }

    /**
     * Representa una propiedad de una entidad del modelo original, la cual reemplazará a una cadena definida en el
     * personal criteria.
     */
    public static class ReplaceProperty {

        /**
         * Propiedad de la entidad de modelo a la que estamos referenciando.
         */
        private final String targetProperty;

        /**
         * Entidad de modelo a la que estamos referenciando. En el caso de que sea null indicará que la entidad es la
         * entidad raiz.
         */
        private final Entity referenceEntity;

        /**
         * Crea una propiedad que corresponde a la entidad por defecto.
         */
        public ReplaceProperty(String targetProperty) {
            this(targetProperty, null);
        }

        /**
         * Crea una propiedad que corresponde a la entidad indicada. En el caso de que referenceEntity sea null la
         * propiedad corresponderá a la entidad por defecto.
         */
        public ReplaceProperty(String targetProperty, Entity referenceEntity) {
            this.referenceEntity = referenceEntity;
            this.targetProperty = targetProperty;
        }

        /**
         * @return the targetProperty
         */
        public String getTargetProperty() {
            return targetProperty;
        }

        /**
         * @return the referenceEntity
         */
        public Entity getReferenceEntity() {
            return referenceEntity;
        }
    }
}
