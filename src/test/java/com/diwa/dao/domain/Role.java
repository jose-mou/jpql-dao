package com.diwa.dao.domain;

import javax.persistence.*;

@Entity
public class Role {

    private long roleId;

    private String name;

    @Column(nullable = false, unique = true)
    public String getName () {
        return name;
    }

    @Id
    @GeneratedValue
    public long getRoleId () {
        return roleId;
    }

    public void setName (String name) {
        this.name = name;
    }
}
