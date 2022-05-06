package com.bepastem.jpadao;

import com.bepastem.models.Users;
import com.bepastem.models.VictimEmAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UsersJpaDao extends JpaRepository<Users, Long> {
    @Query("Select user from USERS user where user.userId = ?1")
    Users getUserById(long userId);

    @Transactional
    @Modifying
    @Query("update USERS user set user.password = ?1 where user.username = ?2")
    void updateUserPasswordByUsername(String newPassword, String username);

    @Query("Select user from USERS user where user.username = ?1")
    Users getUserByUsername(String username);

    @Query("Select user from USERS user where user.email = ?1")
    Users getUserByEmail(String email);

    @Query("Select history from VICTIMEMALERT history where history.victimId_userId = ?1")
    List<VictimEmAlert> getUserEmAlertHistory(long userId);
}
