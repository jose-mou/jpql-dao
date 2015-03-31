package com.diwa.dao.shared.entity;

import java.io.Serializable;

/**
 * Enumerado correspondiente a los diferentes tipos de <code>Join</code> que existen. Los diferentes tipos de Join son
 * los siguientes:
 * <p>
 * <ul>
 * <li>LEFT_JOIN => Left join. Tanto los elementos que tienen correspondencia en la tabla del Join, como los que no
 * tienen correspondencia.</li>
 * <li>RIGHT_JOIN => Inner Join. Sólo aquellos que tienen correspondencia en la tabla del Join.</li>
 * <li>LEFT_FETCH_JOIN => Inner Fetch Join. Permite cargar los elementos del join.</li>
 * <li>RIGHT_JOIN => Inner Join. Permite cargar los elementos del Join.</li>
 * </ul>
 */
public enum JoinOperator implements Serializable {
    /**
     * Left join. Tanto los elementos que tienen correspondencia en la tabla del Join, como los que no tienen
     * correspondencia.
     */
    LEFT_JOIN,
    /**
     * Right Join. Sólo aquellos que tienen correspondencia en la tabla del Join.
     */
    RIGHT_JOIN,
    /**
     * Left Fetch Join. Carga tanto los que tienen correspondencia como los que no (nulos y no nulos).
     */
    LEFT_FETCH_JOIN,
    /**
     * Inner Join. Carga sólo aquellos que tienen correspondencia (no nulos).
     */
    RIGHT_FETCH_JOIN;

    /**
     * Cadenas que representan a los tokens anteriormente definidos.
     */
    private static String[] operators = {"LEFT JOIN", "INNER JOIN", "LEFT JOIN FETCH", "JOIN FETCH"};

    /**
     * @return La representación correspondiente al token.
     */
    public String getOperator() {
        return operators[this.ordinal()];
    }
}
