package com.diwa.dao.shared.criteria.logical;

import java.io.Serializable;

/**
 * Enumerado correspondiente a los diferentes tipos de <code>Logical</code> que existen. Los diferentes tipos lógicos
 * que podemos utilizar son los siguientes: AND, OR y NOT
 */
public enum LogicalOperator implements Serializable {
    /**
     * Operador que representa la intersección de dos criterios.
     */
    AND,
    /**
     * Operador que representa la unión de dos criterios.
     */
    OR,
    /**
     * Operador que representa la negación de otro criterio.
     */
    NOT;

    /**
     * Cadenas que representan a los tokens anteriormente definidos.
     */
    private static String[] operators = {"AND", "OR", "NOT"};

    /**
     * @return La representación correspondiente al token.
     */
    public String getOperator() {
        return operators[this.ordinal()];
    }
}
