package com.diwa.dao.shared.criteria;

import java.io.Serializable;

/**
 * Interface que han de implementar cualquier criterio de búsqueda, tanto los criterios de condición como los criterios
 * lógicos.
 * <p>
 * Esta interface no obliga a implementar ningún método, sólo se utiliza como envoltorio o wrapper que permite
 * identificar los criterios que se utilizan para la búsqueda.
 */
public interface Criteria extends Serializable {

    /**
     * Caracter utilizado para separar los diferentes componentes dentro de una condición de la query '_'. Ej:
     * nombre-EQ-jose. Este parámetro se utilizará para el método toString de los criterias.
     */
    String QUERY_SEPARATOR = "-";

    /**
     * Caracter utilizado para separar los elementos de un listado ','. Ej: nombre-IN-jose,pedro,juan,andres. Este
     * parámetro se utilizará para el método toString de los criterias.
     */
    String LIST_ELEMENT_SEPARATOR = "-";

    /**
     * Caracter utilizado para separar las diferentes condiciones de la query ':'. Ej: q=Q1:Q2:Q3:OR:Q4
     */
    String QUERY_CRITERIA_SEPARATOR = ":";

    /**
     * Identificador utilizado como alias para la entidad por defecto en las queries creadas.
     */
    String DEFAULT_ENTITY_ALIAS = "e";
}
