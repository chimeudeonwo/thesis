package com.bepastem.jpadao;

import com.bepastem.models.VictimEmResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface VictimEmAlertResponseJpa extends JpaRepository<VictimEmResponse, Long> {

    @Query("select response from VICTIMEMRESPONSE response where response.agencyAlerted_agencyId = ?1 ")
    VictimEmResponse[] getEmAlertResponseByEmAlertByAgencyId(long agencyId);

    @Query("select response from VICTIMEMRESPONSE response where response.currentStatus <> ?1 and response.agencyAlerted_agencyId = ?2")
    //@Query(value = "select * from VictimEmResponse response where response.currentStatus =?1 AND response.agencyAlerted_agencyId =?2", nativeQuery = true)
    VictimEmResponse[] getNotNewEmAlertResponseByAgencyId(String currentStatus, long agencyId);
    //List<VictimEmResponse> getNotNewEmAlertResponseByAgencyId(String currentStatus, long agencyId);

    @Query("select response from VICTIMEMRESPONSE response where response.emAlertResponseId = ?1 ")
    VictimEmResponse getEmAlertResponseByEmAlertResponseId(long agencyId);

    @Modifying
    @Transactional
    @Query("update VICTIMEMRESPONSE response set response.rcvStatus = ?1, response.currentStatus =?2 " +
            "where response.emAlertResponseId = ?3")
    void updateEmAlertResponse(String rcvStatus, String currentStatus, long emAlertResponseId);
}
