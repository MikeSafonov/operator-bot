package com.github.mikesafonov.operatorbot.repository;

import com.github.mikesafonov.operatorbot.model.ChatStatus;
import com.github.mikesafonov.operatorbot.model.Role;
import com.github.mikesafonov.operatorbot.model.Status;
import com.github.mikesafonov.operatorbot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(nativeQuery = true, value = "SELECT * FROM users " +
            "WHERE id IN (SELECT mu.user_id FROM (SELECT um.user_id, MIN(um.maxDate)" +
            " FROM (SELECT user_id, max(times) AS maxDate "
            + "FROM timetable INNER JOIN users iu on iu.id = timetable.user_id "
            + "WHERE iu.status = 0 AND iu.role = 1 "
            + "GROUP BY user_id) as um "
            + "GROUP BY um.user_id "
            + "ORDER BY um.maxDate ASC "
            + "LIMIT 1) as mu)")
    Optional<User> findUserByUserStatusAndLastDutyDate();

    @Query(nativeQuery = true, value = "SELECT u.* FROM users u " +
            "NATURAL LEFT JOIN (SELECT user_id AS id FROM timetable) t " +
            "WHERE t.id IS NULL AND u.role = 1 AND u.status = 0 ORDER BY u.full_name ASC LIMIT 1")
    Optional<User> findDutyByRoleByStatusOrderByFullNameAsc();

    Optional<User> findByTelegramId(String telegramId);

    @Modifying
    @Query("UPDATE User u SET u.chatStatus = :chatStatus WHERE u.telegramId = :telegramId")
    void updateUserChatStatus(
            @Param("telegramId") String telegramId, @Param("chatStatus") ChatStatus chatStatus);

    @Modifying
    @Query("UPDATE User u SET u.role = :role WHERE u.telegramId = :telegramId")
    void changeUserRole(@Param("telegramId") String telegramId, @Param("role") Role role);

    List<User> findByRoleAndStatusOrderByFullNameAsc(Role role, Status status);
}
