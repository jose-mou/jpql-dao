package com.diwa.dao.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by josemo on 5/1/15.
 */
@Entity(name = "entityUser")
@Table(name = "entity_user")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(name = "user_name", nullable = false, unique = true)
    private String name;

    public Long getId () {
        return id;
    }

    public void setId (Long id) {
        this.id = id;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }
}
