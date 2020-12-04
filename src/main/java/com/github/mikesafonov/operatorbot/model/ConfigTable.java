package com.github.mikesafonov.operatorbot.model;

import javax.persistence.*;

@Entity
@Table(name = "config_table")
public class ConfigTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "config")
    private String config;
    @Column(name = "parameter")
    private String value;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
