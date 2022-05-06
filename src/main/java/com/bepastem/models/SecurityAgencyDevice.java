package com.bepastem.models;

import com.bepastem.util.IdsGenerator;

import javax.persistence.*;
import java.security.NoSuchAlgorithmException;

@Entity(name = "SECURITYAGENTDEVICE")
public class SecurityAgencyDevice {
    @Id
    private long securityAgencyDeviceId;
    @Column(nullable = false, length = 64)
    private long agentId;
    @Column(nullable = false, length = 64)
    private long agent_agencyId;
    @Column(unique = true, nullable = false, length = 64)
    private String deviceImei;
    @Column(nullable = false, length = 64)
    private String deviceName;
    @Transient
    private IdsGenerator idsGenerator = new IdsGenerator();

    public SecurityAgencyDevice() throws NoSuchAlgorithmException {
        this.securityAgencyDeviceId = idsGenerator.generateLongId();
    }

    public long getSecurityAgencyDeviceId() {
        return securityAgencyDeviceId;
    }

    public void setSecurityAgencyDeviceId(long securityAgencyDeviceId) {
        this.securityAgencyDeviceId = securityAgencyDeviceId;
    }

    public long getAgentId() {
        return agentId;
    }

    public void setAgentId(long agentId) {
        this.agentId = agentId;
    }

    public long getAgent_agencyId() {
        return agent_agencyId;
    }

    public void setAgent_agencyId(long agent_agencyId) {
        this.agent_agencyId = agent_agencyId;
    }

    public String getDeviceImei() {
        return deviceImei;
    }

    public void setDeviceImei(String deviceImei) {
        this.deviceImei = deviceImei;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    @Override
    public String toString() {
        return "SecurityAgencyDevice{" +
                "securityAgencyDeviceId=" + securityAgencyDeviceId +
                ", agentId=" + agentId +
                ", agent_agencyId=" + agent_agencyId +
                ", deviceName='" + deviceName + '\'' +
                ", deviceImei='" + deviceImei + '\'' +
                '}';
    }
}
