package com.bepastem.jpadao;

import com.bepastem.models.SecurityAgent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SecurityAgentJpa extends JpaRepository<SecurityAgent, Long> {

    @Query("Select agent from SECURITYAGENT agent where agent.agent_unitName = ?1")
    SecurityAgent getAgentByUnitName(String agent_unitName);

    @Query("Select agent from SECURITYAGENT agent where agent.agent_agencyId = ?1")
    SecurityAgent getSecurityAgentAgencyId(String username);

    @Query("Select agent from SECURITYAGENT agent where agent.username = ?1")
    SecurityAgent getUserByUsername(String username);
}
