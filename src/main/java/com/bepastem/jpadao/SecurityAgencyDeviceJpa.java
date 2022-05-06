package com.bepastem.jpadao;

import com.bepastem.models.SecurityAgencyDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SecurityAgencyDeviceJpa extends JpaRepository<SecurityAgencyDevice, Long> {

    @Query("select device from SECURITYAGENTDEVICE device where device.deviceImei = ?1")
    SecurityAgencyDevice getDeviceByImei(String deviceImei);
}
