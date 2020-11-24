package com.github.mikesafonov.operatorbot.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import java.time.LocalDate;


@Entity
@Table(name = "additional_day_off")
public class AdditionalDayOff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "day_off")
    private LocalDate dayOff;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDayOff() {
        return dayOff;
    }

    public void setDayOff(LocalDate dayOff) {
        this.dayOff = dayOff;
    }
}
