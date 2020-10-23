package com.github.mikesafonov.operatorbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.mikesafonov.operatorbot.model.Timetable;

public interface TimetableRepository extends JpaRepository<Timetable, Integer> {

}
