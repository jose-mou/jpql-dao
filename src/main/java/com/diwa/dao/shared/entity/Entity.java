package com.diwa.dao.shared.entity;

import java.io.Serializable;

/**
 * Interfaz que representa a todos los objetos que pueden hacer las funciones de entidad. Incluye tanto a las entidades
 * del dominio(<code>DomainEntity</code>), como algunos tipos de joins de SQL <code>JoinEntity</code>.
 * <p>
 * La implementación de esta interfaz conlleva implementar el método <code>getAlias()</code>. Toda entidad tiene
 * asignado un alias o se le genera uno de forma automática.
 * <p>
 * Los objetos de tipo <code>Entity</code> se caracterizan porque sus atributos podrán ser referenciados en los
 * criterios de búsqueda, orden o incluso a partir de ellos se podrán generar un join sobre un atributo de la entidad a
 * la que representan.
 */
public interface Entity extends Serializable{
    /**
     * Alias asignado a la entidad a la hora de crearla. Si este alias no fue indicado de forma manual a la hora de la
     * creación de la entidad, se asignará uno de forma automática basándonos en el hashCode del objeto.
     */
    String getAlias();
}
