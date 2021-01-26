package com.github.mikesafonov.operatorbot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.github.mikesafonov.operatorbot.model.InternalUser;

public interface InternalUserRepository extends JpaRepository<InternalUser, Integer> {

	@Query(nativeQuery = true, value = "SELECT * FROM internal_users WHERE id IN (SELECT mu.user_id FROM (SELECT um.user_id, MIN(um.maxDate) FROM (SELECT user_id, max(times) AS maxDate "
			+ "FROM timetable INNER JOIN internal_users iu on iu.id = timetable.user_id "
			+ "WHERE iu.status = 1 "
			+ "GROUP BY user_id) as um "
			+ "GROUP BY um.user_id "
			+ "ORDER BY um.maxDate ASC "
			+ "LIMIT 1) as mu)")
	Optional<InternalUser> findUserByUserStatusAndLastDutyDate();

	Optional<InternalUser> findFirstByOrderByFullNameAsc();
	
	Optional<InternalUser> findByTelegramId(long telegramId);
}
