package com.mrurespect.employeeapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ISEQ$$_83141")
    @SequenceGenerator(name = "ISEQ$$_83141", sequenceName = "ISEQ$$_83141", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Role{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}