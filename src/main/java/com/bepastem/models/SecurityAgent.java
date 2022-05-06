package com.bepastem.models;

import com.bepastem.util.IdsGenerator;

import javax.persistence.*;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Set;

@Entity(name = "SECURITYAGENT")
public class SecurityAgent {
    @Id
    private long agentId;
    @Column(nullable = false, length = 64)
    private long agent_agencyId;
    @Column(nullable = false, length = 64)
    private String fullName;
    @Column(unique = true, nullable = false, length = 64)
    private String username;
    @Column(nullable = false, length = 64)
    private String password;
    @Column(nullable = false, length = 64)
    private String agent_unitName; // all agent belongs to a unit
    @Column(nullable = false, length = 64)
    private long agent_userId;
    @Column(nullable = false, length = 64)
    private LocalDate createdOn;
    @Transient
    private final IdsGenerator idsGenerator = new IdsGenerator();

    public SecurityAgent() throws NoSuchAlgorithmException {
        this.agentId = idsGenerator.generateLongId();
        this.createdOn = LocalDate.now();
    }

    public SecurityAgent(long agencyId, long agentId, String username, String password, Set<Role> roles, LocalDate createdOn) {
        this.agent_agencyId = agencyId;
        this.agentId = agentId;
        this.createdOn = createdOn;
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

    public long getAgent_userId() {
        return agent_userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String name) {
        this.fullName = name;
    }

    public void setAgent_userId(long agent_userId) {
        this.agent_userId = agent_userId;
    }

    public String getAgent_unitName() {
        return agent_unitName;
    }

    public void setAgent_unitName(String agent_unitName) {
        this.agent_unitName = agent_unitName;
    }

    public void setAgent_agencyId(long agent_agencyId) {
        this.agent_agencyId = agent_agencyId;
    }

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "SecurityAgent{" +
                "agentId=" + agentId +
                ", agent_agencyId=" + agent_agencyId +
                ", name='" + fullName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", agent_unitName='" + agent_unitName + '\'' +
                ", agent_userId=" + agent_userId +
                ", updatedOn=" + createdOn +
                '}';
    }

}
