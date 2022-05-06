package com.bepastem.jpadao;

import com.bepastem.models.VictimEmAlert;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VictimEmAlertJpa extends JpaRepository<VictimEmAlert, Long> {
    @Query("Select alert from VICTIMEMALERT alert where alert.emAlertId = ?1")
    VictimEmAlert getVictimEmAlertByEmAlertId(long userId);

    @Query("SELECT alert FROM VICTIMEMALERT alert WHERE alert.victimId_userId = :userId ORDER BY alert.time DESC")
    List<VictimEmAlert> getLatestVictimEmAlertByUserId(Pageable pageable, @Param("userId")long userId);

    /*@Query("Select alert from VICTIMEMALERT alert " +
            "where alert.time = (select max(alert1.time) from VICTIMEMALERT alert1 where alert1.victimId_userId = ?1)" )
    VictimEmAlert getLatestVictimEmAlertByUserId(long userId); */
}
