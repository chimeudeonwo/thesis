package com.bepastem.jpadao;

import com.bepastem.models.Victim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VictimJpa extends JpaRepository<Victim, Long> {
    @Query("Select victim from VICTIM victim where victim.victim_userId = ?1")
    Victim getVictimByUserId(long userId);
}
