package com.diwa.dao.shared.order;

import java.io.Serializable;

/**
 * Enumerado con las posibles direcciones que puede tener un criterio de orden. Los posibles valores son:
 * <p>
 * <ul>
 * <li>ASC => Los valores mayores se mostrarán al principio de los resultados y los últimos serán los menores.</li>
 * <li>DESC => Los valores menores se mostrarán al principio de los resultados y los últimos serán los mayores.</li>
 * </ul>
 */
public enum OrderDirection implements Serializable {
    /**
     * Operador de intersección.
     */
    ASC,
    /**
     * Operador de unión.
     */
    DESC;

    /**
     * Cadenas que representan a los tokens anteriormente definidos.
     */
    private static String[] operators = { "ASC", "DESC" };

    /**
     * @return La representación correspondiente al token.
     */
    public String getOperator() {
        return operators[this.ordinal()];
    }
}
