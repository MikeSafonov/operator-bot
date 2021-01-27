package com.github.mikesafonov.operatorbot.repository;

import com.github.mikesafonov.operatorbot.model.InternalUser;
import com.github.mikesafonov.operatorbot.model.Timetable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface TimetableRepository extends JpaRepository<Timetable, Integer> {
	Optional<Timetable> findByTime(LocalDate time);

	@Modifying
	@Query("UPDATE Timetable u SET u.time = :date WHERE u.id = :id")
	void updateUserDate(@Param(value = "date") LocalDate date, @Param(value = "id") Integer id);

	@Query("SELECT t FROM Timetable t WHERE t.time <= current_date AND t.userId = :userId")
	Page<Timetable> findUsersDutyInFuture(@Param(value="userId") InternalUser user, Pageable pageable);
}
