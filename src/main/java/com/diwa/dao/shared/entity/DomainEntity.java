package com.diwa.dao.shared.entity;

import com.google.common.base.Objects;

/**
 * Entidad del dominio. Esta clase representa a las entidades del dominio que representa los datos almacenados en
 * nuestra Base de Datos.
 * <p>
 * Éstas se utilizan en los criterios de búsqueda o a la hora de mostrar los datos.
 */
public class DomainEntity implements Entity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Alias utilizado para identificar la entidad. Este campo es obligatorio, pero si no es indicado éste será generado
     * de forma automática.
     */
    private String alias;

    /**
     * Clase del dominio a la que representa la entidad.
     */
    private Class< ? > clase;

    /**
     * Constructor vacío necesario para serialización en GWT.
     */
    public DomainEntity() {
    }

    /**
     * Genera una entidad del dominio a partir de la clase a la que representa. El objeto generado tendrá como alias una
     * cadena <code>o_</code> seguida del hashCode correspondiente a la clase.
     *
     * @param clase Clase del dominio a la que representa la entidad.
     */
    public DomainEntity(final Class< ? > clase) {
        this.clase = clase;
        // Evitamos dos posibles alias iguales
        alias = "o_" + Math.abs(clase.hashCode());
    }

    /**
     * Genera una entidad del dominio a partir de la clase a la que representa, asignándole el alias indicado.
     *
     * @param clase Clase del dominio a la que representa la entidad.
     * @param alias Alias utilizado para identificar la entidad
     */
    public DomainEntity(final Class< ? > clase, final String alias) {
        this.clase = clase;
        this.alias = alias;
    }

    /**
     * Alias utilizado para identificar la entidad. Este campo es obligatorio, pero si no es indicado éste será generado
     * de forma automática.
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Clase del dominio a la que representa la entidad.
     */
    public Class< ? > getClase() {
        return clase;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(clase);
    }
}
