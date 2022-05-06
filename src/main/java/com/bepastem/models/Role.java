package com.bepastem.models;

import javax.persistence.*;

@Entity
public class Role{
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private int tableId;
    @Column(nullable = false)
    private int roleId;
    @Column(nullable = false)
    private String name;

    public Role() {
    }

    public Role(int roleId, String name) {
        this.roleId = roleId;
        this.name = name;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    @Override
    public String toString() {
        return "Role{" +
                "tableId=" + tableId +
                ", roleId=" + roleId +
                ", name='" + name + '\'' +
                '}';
    }
}
