package com.bepastem.controllers;

import com.bepastem.exceptions.UserNotFound;
import com.bepastem.jpadao.SecurityAgencyJpa;
import com.bepastem.jpadao.UsersJpaDao;
import com.bepastem.models.VictimEmResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@RestController
@RequestMapping("/api/v1/agency")
public class SecurityAgenciesController {

    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(3);
    @Autowired
    private final WebClient webClient;
    @Autowired
    private SecurityAgencyJpa securityAgencyJpa;

    public SecurityAgenciesController(WebClient webClientBuilder) {
        this.webClient = webClientBuilder;
    }

    /**SecDapp client calls this api every 1 minutes to check for new alert
     * @return returns the VictimAlertResponse[] associated with respective security agency using a webclient */
    @GetMapping(value = "/viewEmAlert/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public VictimEmResponse[] getVictimEmAlertsSentThisAgency(@PathVariable long userId){
        // use the userId from request and get the user (agency), then extract the agencyId from that
        var agency = securityAgencyJpa.getAgencyByAgencyId_userId(userId);

        if(agency == null) {
            throw new UserNotFound("No user with the given userId was found!");
        }

        return webClient
                .post()
                .uri("/victim/getEmAlertResponse/" + agency.getAgencyId())
                .retrieve()
                .bodyToMono(VictimEmResponse[].class)
                .block(REQUEST_TIMEOUT);
    }
}
