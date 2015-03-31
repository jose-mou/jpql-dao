package com.diwa.dao.shared.entity;

import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * Representa a las sentencias SQL Join. Envuelve tanto a los FetchJoin como a los JoinEntity. De la misma forma que
 * envuelve a los diferentes tipos de Join, permite la creación de los mismos mediante un conjunto de métodos estáticos,
 * que devuelven como resultado los correspondientes tipos de Join. Desde los métodos estáticos se llama a los
 * diferentes constructores de la clase que extienden la clase (Estos constructores sólo son accesibles de esta forma ya
 * que son <code>protected</code>).
 * <p>
 * A continuación se muestra un ejemplo de como se crean los diferentes joins a partir de los métodos estáticos:
 * <p>
 * <code> FetchJoin join = Join.leftJoinFetch("nombreAtributo");
 *        EntityJoin entityJoin = Join.leftJoin("nombreAtributo", "alias");
 * </code>
 */
public abstract class Join implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Operador JOIN.
     */
    private JoinOperator operator;

    /**
     * Entidad sobre la que aplica el join. En el caso de que sea null se considera la entidad por defecto o base.
     */
    private Entity entity = null;

    /**
     * Nombre de la propiedad de la entidad sobre la que realizamos el Join.
     */
    private String name;

    /**
     * Constructor vacío necesario para serialización en GWT.
     */
    public Join() {
    }

    /**
     * Constructor que genera un join sobre la entidad base o por defecto. Este constructor sólo es llamado desde las
     * las entidades que extienden esta clase.
     * 
     * @param operator Operador correspondiente al tipo de Join que va a ser utilizado.
     * @param name Nombre de la propiedad sobre la que se realiza el join.
     */
    protected Join(final JoinOperator operator, final String name) {
        this.operator = operator;
        this.name = name;
    }

    /**
     * Constructor que genera un join sobre la entidad indicada. Este constructor sólo es llamado desde las las
     * entidades que extienden esta clase.
     * 
     * @param operator Operador correspondiente al tipo de Join que va a ser utilizado.
     * @param name Nombre de la propiedad sobre la que se realiza el join.
     * @param entity Entidad sobre la que se va a aplicar el join.
     */
    protected Join(final JoinOperator operator, final String name, final Entity entity) {
        this(operator, name);
        this.entity = entity;
    }

    /**
     * @return Operador JOIN ({@link JoinOperator}). El operador indica el tipo de join que se está realizando.
     */
    public JoinOperator getOperator() {
        return operator;
    }

    /**
     * @return Entidad sobre la que aplica el join. En el caso de que sea null se considera la entidad por defecto o
     *         base.
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * @return Nombre de la propiedad de la entidad sobre la que realizamos el Join.
     */
    public String getName() {
        return name;
    }
    
    @Override
    public int hashCode() {
        if (entity != null){
            return Objects.hashCode(getOperator(), name, entity);
        } else {
            return Objects.hashCode(getOperator(), name);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Join other = (Join) obj;
        return (super.equals(other) && getName().equals(other.getName()));
    }
}
