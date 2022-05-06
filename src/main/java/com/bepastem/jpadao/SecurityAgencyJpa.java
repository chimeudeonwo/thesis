package com.bepastem.jpadao;

import com.bepastem.models.SecurityAgency;
import com.bepastem.models.Victim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SecurityAgencyJpa extends JpaRepository<SecurityAgency, Long> {

    //@Query(value = "INSERT into Esp (id, name, date, time) VALUES (?1, ?2, ?3, ?4)", nativequery=true);
    // @Query(value = "insert into Esp (id, name) values (?1, ?2)")
    // public void insertDeviceIMEI(String deviceIMEI);

    @Modifying
    @Query(
            value =
                    "insert into SecurityAgencyDevice (name, age, email, status) values (:name, :age, :email, :status)",
            nativeQuery = true)
    void insertUser(@Param("name") String name, @Param("age") Integer age,
                    @Param("status") Integer status, @Param("email") String email);

    @Transactional
    @Modifying
    @Query("update SECURITYAGENTDEVICE device set device.deviceImei = ?1 where device.agentId = ?2")
    Victim updateSecurityAgencyDeviceImei(String imei, String agentId);

    @Query("select agency from SECURITYAGENCY agency where agency.email = ?1")
    SecurityAgency getAgencyByEmail(String email);

    @Query("select agency from SECURITYAGENCY agency where agency.unitName = ?1")
    SecurityAgency getAgencyByUnitName(String unitName);

    @Query("select agency from SECURITYAGENCY agency where agency.agency_userId = ?1")
    SecurityAgency getAgencyByAgencyId_userId(long userId);

    @Query("select agency from SECURITYAGENCY agency where agency.city = ?1")
    SecurityAgency[] getAgencyWithTown(String city);
}
