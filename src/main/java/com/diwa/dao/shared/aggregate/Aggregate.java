package com.diwa.dao.shared.aggregate;

import java.io.Serializable;

/**
 * Funciones SQL que se aplican sobre los atributos de la BD. La ejecución de estas funciones permiten generar
 * resultados estadísticos sobre un atributo de la BD. Las diferentes funciones contempladas son:
 * <ul>
 * <li>AVG => Genera la media de los valores.</li>
 * <li>MAX => Devuelve el valor máximo.</li>
 * <li>MIN => Devuelve el valor mínimo.</li>
 * <li>SUM => Suma todo los valores del atributo indicado.</li>
 * </ul>
 * También se encuentra el agregado COUNT, pero debido a que este es utilizado con mayor frecuencia, se ha separado de
 * éstos.
 */
public enum Aggregate implements Serializable {
    /**
     * Operador de media.
     */
    AVG,
    /**
     * Operador de máximo.
     */
    MAX,
    /**
     * Operador de mínimo.
     */
    MIN,
    /**
     * Operador de suma.
     */
    SUM;

    /**
     * Cadenas que representan a los tokens anteriormente definidos.
     */
    private static String[] operators = { "AVG", "MAX", "MIN", "SUM" };

    /**
     * @return La representación correspondiente al token.
     */
    public String getOperator() {
        return operators[this.ordinal()];
    }
}
