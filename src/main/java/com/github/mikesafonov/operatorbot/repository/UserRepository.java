package com.github.mikesafonov.operatorbot.repository;

import com.github.mikesafonov.operatorbot.model.Role;
import com.github.mikesafonov.operatorbot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

	@Query(nativeQuery = true, value = "SELECT * FROM users WHERE id IN (SELECT mu.user_id FROM (SELECT um.user_id, MIN(um.maxDate) FROM (SELECT user_id, max(times) AS maxDate "
			+ "FROM timetable INNER JOIN users iu on iu.id = timetable.user_id "
			+ "WHERE iu.status = 0 AND iu.role = 1 "
			+ "GROUP BY user_id) as um "
			+ "GROUP BY um.user_id "
			+ "ORDER BY um.maxDate ASC "
			+ "LIMIT 1) as mu)")
	Optional<User> findUserByUserStatusAndLastDutyDate();

	Optional<User> findFirstByRoleOrderByFullNameAsc(Role role);
	
	Optional<User> findByTelegramId(long telegramId);

	@Modifying
	@Query("UPDATE User u SET u.role = :role WHERE u.telegramId = :telegramId")
	void changeUserRole(@Param(value = "telegramId") long telegramId, @Param(value = "role") Role role);
}
