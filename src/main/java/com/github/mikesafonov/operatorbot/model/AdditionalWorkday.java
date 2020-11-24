package com.github.mikesafonov.operatorbot.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "additional_workday")
public class AdditionalWorkday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "workday")
    private LocalDate workday;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getWorkday() {
        return workday;
    }

    public void setWorkday(LocalDate workday) {
        this.workday = workday;
    }
}
