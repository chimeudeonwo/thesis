package com.bepastem.jpadao;

import com.bepastem.exceptions.UserNameExistException;
import com.bepastem.models.Users;
import com.bepastem.models.Victim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthJpaDao extends JpaRepository<Users, Long> {

    @Query("Select victim from USERS victim where victim.username = ?1")
    Users getUserByUsername(String username) throws UserNameExistException;

    // @Query("Select user from USERS user where user.username = ?1 AND user.password = ?2")
    @Query("SELECT u FROM USERS u WHERE u.username = ?1 AND u.password = ?2")
    Victim getUserByUsernameAndPassword(String username, String password);

    @Query("Select victim from USERS victim where victim.email = ?1")
    Users getUserByEmail(String email);

    @Query("Select victim from USERS victim where victim.email = ?1")
    Victim getUserByEmailAndPassword(String email, String password);

}
