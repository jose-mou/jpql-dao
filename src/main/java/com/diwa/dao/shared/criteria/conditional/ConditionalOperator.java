package com.diwa.dao.shared.criteria.conditional;

import java.io.Serializable;

/**
 * Enumerado correspondiente a los diferentes tipos de <code>Conditional</code> que existen. Los diferentes tipos de
 * condiciones que podemos utilizar son los siguientes: >, >=, <, <=, =, BETWEEN, IN, LIKE, IS NULL, IS NOT NULL, EMPTY,
 * IS NOT EMPTY, MEMBER.
 */
public enum ConditionalOperator implements Serializable {
    /**
     * Operador igual.
     */
    EQ,
    /**
     * Operador no igual.
     */
    NE,
    /**
     * Operador menor o igual.
     */
    LE,
    /**
     * Operador mayor o igual.
     */
    GE,
    /**
     * Operador mayor.
     */
    GT,
    /**
     * Operador menor.
     */
    LT,
    /**
     * Operador between.
     */
    BETWEEN,
    /**
     * Operador en.
     */
    IN,
    /**
     * Operador like.
     */
    LIKE,
    /**
     * Operador es nulo.
     */
    IS_NULL,
    /**
     * Operador diferente de nulo.
     */
    IS_NOT_NULL,
    /**
     * Operador vació.
     */
    IS_EMPTY,
    /**
     * Operador no vacío.
     */
    IS_NOT_EMPTY,
    /**
     * Operador miembro.
     */
    MEMBER;
    /**
     * Cadenas que representan a los tokens anteriormente definidos.
     */
    private static String[] operators = {"=", "<>", "<=", ">=", ">", "<", "BETWEEN", "IN", "LIKE", "IS NULL",
        "IS NOT NULL", "IS EMPTY", "IS NOT EMPTY", "MEMBER OF"};

    /**
     * @return La representación correspondiente al token.
     */
    public String getOperator() {
        return operators[this.ordinal()];
    }
}
