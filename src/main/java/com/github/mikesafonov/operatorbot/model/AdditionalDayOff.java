package com.github.mikesafonov.operatorbot.model;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import java.time.LocalDate;


@Entity
@Table(name = "additional_day_off")
@Data
public class AdditionalDayOff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "day_off")
    private LocalDate dayOff;
}
