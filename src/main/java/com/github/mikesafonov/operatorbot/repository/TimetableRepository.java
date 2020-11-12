package com.github.mikesafonov.operatorbot.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.github.mikesafonov.operatorbot.model.Timetable;

public interface TimetableRepository extends JpaRepository<Timetable, Integer> {
	Optional<Timetable> findByTime(LocalDate time);

	@Modifying
	@Query("UPDATE Timetable u SET u.time = :date WHERE u.id = :id")
	void updateUserDate(@Param(value = "date") LocalDate date, @Param(value = "id") Integer id);
}
