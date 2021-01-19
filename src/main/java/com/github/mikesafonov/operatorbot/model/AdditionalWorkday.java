package com.github.mikesafonov.operatorbot.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "additional_workday")
@Data
public class AdditionalWorkday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "workday")
    private LocalDate workday;
}
