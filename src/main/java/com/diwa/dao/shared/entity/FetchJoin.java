package com.diwa.dao.shared.entity;

import com.google.common.base.Objects;

/**
 * Representa a los Joins de tipo Fetch. La notación de estos joins son parecidos a los Joins de SQL, pero se les añade
 * al final el token FETCH.
 * <p>
 * A diferencia de los Joins de SQL, éstos no son entidades, es decir, no se pueden utilizar criterios sobre los mismos
 * para restringir las búsquedas, ordenar los resultados o realizar sobre estos otros joins. Los Joins de tipo FETCH
 * únicamente se utilizan para optimizar las consultas a BD.
 * <p>
 * Por ejemplo: Si tenemos una entidad 'Hijo' que tiene una relación ManyToOne a una entidad 'Padre', pero resulta que
 * durante el desarrollo de la aplicación sólo accedemos un par de veces desde la entidad 'Hijo' a la entidad padre,
 * pero sin embargo realizamos numerosas búsquedas sobre los hijos sin tener en cuenta los padres. En este caso utilizar
 * la estrategia de Fetch = EAGER (@see FetchType) nos penalizaría en la mayoría de consultas. Para solucionarlo podemos
 * indicarle a la consulta CRUD que realice un <code>FetchJoin</code> sobre el atributo que tiene la relación de
 * ManyToOne. Esto conllevaría que cuando acceda a BD, se traiga tanto el padre como el hijo en una sola consulta.
 * <p>
 * Ejemplo de uso:
 * <p>
 * <code>FetchJoin fetch = Join.leftJoinFetch("nombreAtributo");</code>
 * <p>
 * NOTA: Aunque la especificiación de JPA no permite los FetchJoin como entidades, es decir, asociados a un alias. Ya
 * que Hibernate y EclipseLink lo permiten lo vamos a soportar en el DAO, pero puiede que en otras implementaciones de
 * JPA no sea válido.
 * <p>
 * NOTA2: Los fetchjoins no se han de añadir a las entidades de la búsqueda sino que se han de añadir a los feches del
 * SearchInfo.
 */
public class FetchJoin extends Join implements Entity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Alias asociado al FechJoin. Este alias no pertenece a al especificación de JPA.
     */
    private String alias;

    /**
     * Constructor vacío necesario para serialización en GWT.
     */
    public FetchJoin() {
    }

    /**
     * Este constructor presupone que la entidad con la que se está realizándose el FetchJoin es la entidad base o por
     * defecto. Generará un FetchJoin del tipo indicado sobre el atributo indicado de la entidad base o por defecto.
     * 
     * @param operator Operador JOIN.
     * @param name Nombre de la propiedad sobre la que realizamos el Join.
     */
    protected FetchJoin(final JoinOperator operator, final String name) {
        super(operator, name);
    }

    /**
     * Genera un join sobre la entidad recibida. En caso de que la entidad recibida fuera null, se supondría que la
     * entidad a la que aplica el Join es la entidad base.
     * 
     * @param operator Operador correspondiente al tipo de Join.
     * @param name Nombre del atributo sobre el que realizamos el join.
     * @param entity Entidad sobre la que aplica el Join.
     */
    protected FetchJoin(final JoinOperator operator, final String name, final Entity entity) {
        super(operator, name, entity);
    }

    /**
     * Genera un LEFT JOIN ({@link JoinOperator#LEFT_FETCH_JOIN}) de tipo Fetch sobre la propiedad indicada de la
     * entidad base.
     * 
     * @param name Nombre de la propiedad sobre la que se realiza el join.
     * @return Objeto <code>FetchJoin</code> sobre la entidad base o por defecto, que corresponde a la propiedad
     *         indicada.
     */
    public static FetchJoin leftJoinFetch(final String name) {
        return new FetchJoin(JoinOperator.LEFT_FETCH_JOIN, name);
    }

    /**
     * Genera un RIGHT JOIN ({@link JoinOperator#RIGHT_FETCH_JOIN}) de tipo Fetch sobre la propiedad indicada de la
     * entidad base.
     * 
     * @param name Nombre de la propiedad sobre la que se realiza el join.
     * @return Objeto <code>FetchJoin</code> sobre la entidad base o por defecto, que corresponde a la propiedad
     *         indicada.
     */
    public static FetchJoin joinFetch(final String name) {
        return new FetchJoin(JoinOperator.RIGHT_FETCH_JOIN, name);
    }

    /**
     * Genera un LEFT JOIN ({@link JoinOperator#LEFT_JOIN}) de tipo Fetch sobre la propiedad indicada de la entidad
     * pasada com parámetro.
     * 
     * @param name Nombre de la propiedad sobre la que se realiza el join. Esta propiedad ha de pertenecer a la entidad
     *            pasada como parámetro
     * @param entity Entidad sobre la que se realiza el Join.
     * @return Objeto <code>FetchJoin</code> sobre la entidad pasada como parámetro, que corresponde a la propiedad
     *         indicada.
     */
    public static FetchJoin leftJoinFetch(final String name, final Entity entity) {
        return new FetchJoin(JoinOperator.LEFT_FETCH_JOIN, name, entity);
    }

    /**
     * Genera un RIGHT JOIN (@link JoinOperator#RIGHT_FETCH_JOIN) de tipo Fetch sobre la propiedad indicada de la
     * entidad pasada como parámetro.
     * 
     * @param name Nombre de la propiedad sobre la que se realiza el join. Esta propiedad ha de pertenecer a la entidad
     *            pasada como parámetro
     * @param entity Entidad sobre la que se realiza el Join.
     * @return Objeto <code>FetchJoin</code> sobre la entidad pasada como parámetro, que corresponde a la propiedad
     *         indicada.
     */
    public static FetchJoin joinFetch(final String name, final Entity entity) {
        return new FetchJoin(JoinOperator.RIGHT_FETCH_JOIN, name, entity);
    }

    /**
     * Devuelve el alias asociado al fechJoin. Esta funcionalidad no es soportada por JPA2 pero debido a que resuelve
     * bugs de hibernate se ha implementado. Ver lo problemas con distict y criterios de ordenación sobre entidades
     * relacionadas. Los fetchjoins no se han de añadir a las entidades de la búsqueda sino que se han de añadir a los
     * feches del SearchInfo.
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Establece el alias asociado al fechJoin. Esta funcionalidad no es soportada por JPA2 pero debido a que resuelve
     * bugs de hibernate se ha implementado. Los fetchjoins no se han de añadir a las entidades de la búsqueda sino que
     * se han de añadir a los feches del SearchInfo.
     */
    public void setAlias(String alias) {
        this.alias = alias;
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
        FetchJoin other = (FetchJoin) obj;
        return super.equals(obj) && alias == other .getAlias();
    }
}
