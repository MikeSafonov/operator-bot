package com.github.mikesafonov.operatorbot.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "config_table")
@Data
public class ConfigTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "config")
    private String config;
    @Column(name = "parameter")
    private String value;
}
