package com.diwa.dao.shared.entity;

import com.google.common.base.Objects;

/**
 * Representa los Joins que representan a una entidad del dominio y por ello puede utilizarse en los criterios de
 * búsqueda. Esta entidad representa a la sentencia <code>JOIN</code> de SQL.
 * <p>
 * Los objetos de tipo JoinEntity se pueden utilizar para utilizar criterios de búsqueda sobre entidades con las que se
 * encuentra relacionado la entidad padre. Es una forma óptima de evitar los subselects (menos eficientes).
 */
public class JoinEntity extends Join implements Entity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Alias utilizado para identificar la entidad.
     */
    private String alias;

    /**
     * Constructor vacío necesario para serialización en GWT.
     */
    public JoinEntity() {
    }

    /**
     * Este constructor presupone que la entidad sobre la que se está realizándose el Join es la entidad base o por
     * defecto. Debido a que no se le indica el alias del join, se le asignará un alias formado a partir del nombre del
     * atributo sobre el que realizamos el Join y el hashCode del mismo.
     * 
     * @param name Nombre del atributo sobre el que realizamos el join.
     * @param operator Operador correspondiente al tipo de Join.
     */
    protected JoinEntity(final JoinOperator operator, final String name) {
        super(operator, name);
        alias = name + "_" + Math.abs(name.hashCode());
    }

    /**
     * Genera un join sobre la entidad recibida. En caso de que la entidad recibida fuera null, se supondría que la
     * entidad a la que aplica el Join es la entidad base. Debido a que no se le indica el alias del join, se le
     * asignará un alias formado a partir del nombre del atributo sobre el que realizamos el Join y el hashCode del
     * mismo.
     * 
     * @param operator Operador correspondiente al tipo de Join.
     * @param name Nombre del atributo sobre el que realizamos el join.
     * @param entity Entidad sobre la que aplica el Join.
     */
    protected JoinEntity(final JoinOperator operator, final String name, final Entity entity) {
        super(operator, name, entity);
        // Evitamos dos posibles alias iguales
        alias = name + "_" + Math.abs(name.hashCode());
    }

    /**
     * Este constructor presupone que la entidad sobre la que se está realizándose el Join es la entidad base o por
     * defecto. Al join generado se le asignará el alias indicado.
     * 
     * @param operator Operador correspondiente al tipo de Join.
     * @param name Nombre del atributo sobre el que realizamos el join.
     * @param alias Alias que se le asignará al Join generado.
     */
    protected JoinEntity(final JoinOperator operator, final String name, final String alias) {
        super(operator, name);
        this.alias = alias;
    }

    /**
     * Genera un join sobre la entidad recibida. En caso de que la entidad recibida fuera null, se supondría que la
     * entidad a la que aplica el Join es la entidad base. Se le asignará el alias indicado.
     * 
     * @param operator Operador correspondiente al tipo de Join.
     * @param name Nombre del atributo sobre el que realizamos el join.
     * @param entity Entidad sobre la que aplica el Join.
     * @param alias Alias que se le asignará al Join generado.
     */
    protected JoinEntity(final JoinOperator operator, final String name, final Entity entity, final String alias) {
        super(operator, name, entity);
        this.alias = alias;
    }

    /**
     * @see Entity.java
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Genera un RIGHT JOIN ({@link JoinOperator#RIGHT_JOIN}) sobre la entidad base. El join generado es una entidad,
     * por ello éste podrá ser utilizado en posteriores criterios de búsqueda o como entidad padres de otros joins. En
     * este caso el alias asignado a la entidad será autogenerado.
     * 
     * @param name Nombre de la propiedad sobre la que se realiza el join.
     * @return Objeto <code>JoinEntity</code> sobre la entidad base, que corresponde a la propiedad indicada como
     *         parámetro.
     */
    public static JoinEntity join(final String name) {
        return new JoinEntity(JoinOperator.RIGHT_JOIN, name);
    }

    /**
     * Genera un LEFT JOIN ({@link JoinOperator#LEFT_JOIN}) sobre la entidad base. El join generado es una entidad, por
     * ello éste podrá ser utilizado en posteriores criterios de búsqueda o como entidad padres de otros joins. En este
     * caso el alias asignado a la entidad será autogenerado.
     * 
     * @param name Nombre de la propiedad sobre la que se realiza el join.
     * @return Objeto <code>JoinEntity</code> sobre la entidad base, que corresponde a la propiedad indicada como
     *         parámetro.
     */
    public static JoinEntity leftJoin(final String name) {
        return new JoinEntity(JoinOperator.LEFT_JOIN, name);
    }

    /**
     * Genera un RIGHT JOIN ({@link JoinOperator#RIGHT_JOIN}) sobre la entidad base con el alias indicado. El join
     * generado es una entidad, por ello éste podrá ser utilizado en posteriores criterios de búsqueda o como entidad
     * padres de otros joins.
     * 
     * @param name Nombre de la propiedad sobre la que se realiza el join.
     * @param alias Alias que se le asignará al join y el cual se utilizará para acceder a sus atributos en los
     *            criterios.
     * @return Objeto <code>JoinEntity</code> sobre la entidad base, que corresponde a la propiedad indicada como
     *         parámetro.
     */
    public static JoinEntity join(final String name, final String alias) {
        return new JoinEntity(JoinOperator.RIGHT_JOIN, name, alias);
    }

    /**
     * Genera un LEFT JOIN ({@link JoinOperator#LEFT_JOIN}) sobre la entidad base con el alias indicado. El join
     * generado es una entidad, por ello éste podrá ser utilizado en posteriores criterios de búsqueda o como entidad
     * padres de otros joins.
     * 
     * @param name Nombre de la propiedad sobre la que se realiza el join.
     * @param alias Alias que se le asignará al join y el cual se utilizará para acceder a sus atributos en los
     *            criterios.
     * @return Objeto <code>JoinEntity</code> sobre la entidad base, que corresponde a la propiedad indicada como
     *         parámetro.
     */
    public static JoinEntity leftJoin(final String name, final String alias) {
        return new JoinEntity(JoinOperator.LEFT_JOIN, name, alias);
    }

    /**
     * Genera un RIGHT JOIN ({@link JoinOperator#RIGHT_JOIN}) sobre la entidad indicada. El join generado es una
     * entidad, por ello éste podrá ser utilizado en posteriores criterios de búsqueda o como entidad padres de otros
     * joins. En este caso el alias asignado a la entidad será autogenerado.
     * 
     * @param name Nombre de la propiedad sobre la que se realiza el join.
     * @param entity Entidad previamente definida y a la que corresponde el atributo sobre el que se realiza el Join.
     * @return Objeto <code>JoinEntity</code> sobre la entidad indicada, que corresponde a la propiedad indicada como
     *         parámetro.
     */
    public static JoinEntity join(final String name, final Entity entity) {
        return new JoinEntity(JoinOperator.RIGHT_JOIN, name, entity);
    }

    /**
     * Genera un LEFT JOIN ({@link JoinOperator#LEFT_JOIN}) sobre la entidad indicada. El join generado es una entidad,
     * por ello éste podrá ser utilizado en posteriores criterios de búsqueda o como entidad padres de otros joins. En
     * este caso el alias asignado a la entidad será autogenerado.
     * 
     * @param name Nombre de la propiedad sobre la que se realiza el join.
     * @param entity Entidad previamente definida y a la que corresponde el atributo sobre el que se realiza el Join.
     * @return Objeto <code>JoinEntity</code> sobre la entidad indicada, que corresponde a la propiedad indicada como
     *         parámetro.
     */
    public static JoinEntity leftJoin(final String name, final Entity entity) {
        return new JoinEntity(JoinOperator.LEFT_JOIN, name, entity);
    }

    /**
     * Genera un RIGHT JOIN ({@link JoinOperator#RIGHT_JOIN}) sobre la entidad indicada y con el alias indicado. El join
     * generado es una entidad, por ello éste podrá ser utilizado en posteriores criterios de búsqueda o como entidad
     * padres de otros joins.
     * 
     * @param name Nombre de la propiedad sobre la que se realiza el join.
     * @param entity Entidad previamente definida y a la que corresponde el atributo sobre el que se realiza el Join.
     * @param alias Alias que se le asignará al join y el cual se utilizará para acceder a sus atributos en los
     *            criterios.
     * @return Objeto <code>JoinEntity</code> sobre la entidad indicada, que corresponde a la propiedad indicada como
     *         parámetro.
     */
    public static JoinEntity join(final String name, final Entity entity, final String alias) {
        return new JoinEntity(JoinOperator.RIGHT_JOIN, name, entity, alias);
    }

    /**
     * Genera un LEFT JOIN ({@link JoinOperator#LEFT_JOIN}) sobre la entidad indicada y con el alias indicado. El join
     * generado es una entidad, por ello éste podrá ser utilizado en posteriores criterios de búsqueda o como entidad
     * padres de otros joins.
     * 
     * @param name Nombre de la propiedad sobre la que se realiza el join.
     * @param entity Entidad previamente definida y a la que corresponde el atributo sobre el que se realiza el Join.
     * @param alias Alias que se le asignará al join y el cual se utilizará para acceder a sus atributos en los
     *            criterios.
     * @return Objeto <code>JoinEntity</code> sobre la entidad indicada, que corresponde a la propiedad indicada como
     *         parámetro.
     */
    public static JoinEntity leftJoin(final String name, final Entity entity, final String alias) {
        return new JoinEntity(JoinOperator.LEFT_JOIN, name, entity, alias);
    }

    @Override
    public int hashCode() {
        if (alias != null){
            return Objects.hashCode(super.hashCode(), alias);
        } else {
            return Objects.hashCode(super.hashCode());
        }
    }

    @Override
    public boolean equals(Object obj) {
        JoinEntity other = (JoinEntity) obj;
        return super.equals(obj) && alias == other .getAlias();
    }
}
