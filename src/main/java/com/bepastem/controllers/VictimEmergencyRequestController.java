package com.bepastem.controllers;

import com.bepastem.controllers.support.ConvertJsonToEntityClass;
import com.bepastem.exceptions.CannotBeNullException;
import com.bepastem.exceptions.UserNotFound;
import com.bepastem.jpadao.*;
import com.bepastem.models.Geolocation;
import com.bepastem.models.SecurityAgency;
import com.bepastem.models.VictimEmResponse;
import com.bepastem.services.ClosestSecurityAgencyService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/api/v1/victim")
public class VictimEmergencyRequestController {
    @Autowired
    private GeolocationJpa geolocationJpa;
    @Autowired
    private ClosestSecurityAgencyService geolocationService;
    @Autowired
    private ConvertJsonToEntityClass convertJsonToEntityClass;
    @Autowired
    private VictimEmAlertResponseJpa victimEmAlertResponseJpa;
    @Autowired
    private VictimEmAlertJpa victimEmAlertJpa;
    @Autowired
    private VictimJpa victimJpa;
    @Autowired
    private UsersJpaDao usersJpaDao;
    @Autowired
    private SecurityAgencyJpa securityAgencyJpa;
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(3);
    @Autowired
    private final WebClient webClient;

    public VictimEmergencyRequestController(WebClient webClientBuilder) {
        this.webClient = webClientBuilder;
    }

    /**
     * Receives emAlert request and persists it. Calculates the top three closest security agencies to the user current location,
     * selects them and generates an emAlert response for the those agencies.
     * @return returns the time the alert request was received
     */
    @PostMapping(value = "/emAlert", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> respondToVictimAlertWithNearestAgenciesToVictimLocation(HttpServletRequest request) throws IOException, NoSuchAlgorithmException, ParseException {
        var jsonBody = convertJsonToEntityClass.getEntityFromRequestAsJson(request);
        var victimEmAlert = convertJsonToEntityClass.getVictimEmAlertFromJsonObject(jsonBody);
        var victimCurrentGeolocation = convertJsonToEntityClass.getGeolocationFromJsonObject(jsonBody);

        var user = usersJpaDao.getUserById(victimEmAlert.getVictimId_userId());
        if (user == null) {
            throw new UserNotFound("No user with the received userId exists in DB");
        }
        victimCurrentGeolocation.setGeolocationId_userId(victimEmAlert.getVictimId_userId());
        var geolocation = geolocationJpa.save(victimCurrentGeolocation);

        victimEmAlert.setVictimGeolocationId(geolocation.getGeolocationId());
        var savedAlert = victimEmAlertJpa.save(victimEmAlert); // to get the emAlertId for every save alert

        var closestSecAgencies = getNearestAgencies(victimCurrentGeolocation);

        // TODO: replace with logic to increase distance and search again if no nearest security station was found
        if (closestSecAgencies.size() < 1) {
            // set this information into the response table
            var response = new VictimEmResponse();
            response.setEmAlertId_emAlertResponseId(savedAlert.getEmAlertId());
            response.setSentStatus("Not Sent");
            response.setTime(victimEmAlert.getDuration()); // set response time to the duration taken to get the em alert and save to the db
            response.setAgencyAlerted_agencyId(0);
            victimEmAlertResponseJpa.save(response);
            throw new CannotBeNullException("list of nearest agencies returned null");
        }

        // get victim
        var victim = victimJpa.getVictimByUserId(user.getUserId());
        var victimLatestEmAlert = victimEmAlertJpa.getLatestVictimEmAlertByUserId(PageRequest.of(0, 1), victimEmAlert.getVictimId_userId());

        if (victimLatestEmAlert.size() < 1) {
            throw new CannotBeNullException("Latest alert cannot be null");
        }

        if (!(user.getWHO().equalsIgnoreCase("VICTIM"))) {
            throw new UserNotFound("No victim with the received userId exists in DB");
        }

        if(closestSecAgencies.size() >= 1){
            for (SecurityAgency sec : closestSecAgencies) {
                var response = new VictimEmResponse();
                response.setEmAlertId_emAlertResponseId(victimLatestEmAlert.get(0).getEmAlertId());
                response.setSentStatus("Sent");
                response.setTime(victimEmAlert.getDuration()); // set response time to the duration taken to get the em alert and save to the db
                response.setAgencyAlerted_agencyId(sec.getAgencyId());
                victimEmAlertResponseJpa.save(response);
            }
        }

        // web client send notification
        webClient
                .get()
                .uri("/notification/victimcontactperson/" + victim.getVictimId())
                .header(request.getHeader("Authorization"))
                .retrieve()
                .bodyToMono(String.class)
                .block(REQUEST_TIMEOUT);

        var headers = this.setCustomHeader(user.getUserId());
        return new ResponseEntity<String>(String.valueOf("EmRequest of userId " + user.getUserId() + " received. Duration = " + victimEmAlert.getDuration()), headers, HttpStatus.CREATED);
    }

    private List<SecurityAgency> getNearestAgencies(Geolocation victimLocation) {
        return geolocationService.topThreeClosestSecAgencies(victimLocation);
    }

    @PostMapping(path = "/testingAsync")
    public DeferredResult<String> value() throws ExecutionException, InterruptedException, TimeoutException {
        AsyncRestTemplate restTemplate = new AsyncRestTemplate();
        String baseUrl = "http://localhost:8080/api/v1/victim/emAlertResponse";

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        String value = "async testing";
        //restTemplate.po
        HttpEntity entity = new HttpEntity("parameters", requestHeaders);
        final DeferredResult<String> result = new DeferredResult<>();
        ListenableFuture<ResponseEntity<String>> futureEntity = restTemplate.getForEntity(baseUrl, String.class);

        futureEntity.addCallback(new ListenableFutureCallback<ResponseEntity<String>>() {
            @Override
            public void onSuccess(ResponseEntity<String> result) {
                System.out.println(result.getBody());
                // check if the callback is null
                if (result.getStatusCode().equals(200)) {
                    //save the acceptOrReject response from sec agency for the emAlert
                    System.out.println("res: " + result.getStatusCode());
                    System.out.println("res: " + result.getBody());
                }
                System.out.println("statusCode: " + result.getStatusCode());
                System.out.println("body: " + result.getBody());
                System.out.println("class: " + result.getClass());
            }

            @Override
            public void onFailure(Throwable ex) {
                result.setErrorResult(ex.getMessage());
            }
        });
        System.out.println("res1: " + result);
        return result;
    }

    /**
     * called internally using webclient.
     * Shows logged agency, the list of all emAlert forwarded to their agency and their respective status
     */
    @PostMapping(value = "/getEmAlertResponse/{agencyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public VictimEmResponse[] getEmAlertResponse(@PathVariable long agencyId, HttpServletRequest request) throws IOException {
        var json = convertJsonToEntityClass.getEntityFromRequestAsJson(request);

        if (agencyId < 1) {
            throw new CannotBeNullException("Path variable agencyId cannot be null!");
        }

        // Used try catch because json throw null pointer error if either of rcvStatus or emAlertResponseId is null
        try {
            if (json.get("rcvStatus").getAsString().equalsIgnoreCase("received") &&
                    (!json.get("emAlertResponseId").getAsString().equals(null))) {
                //get the exact agency of a specific victimResponse and update the agency received status
                var rcvStatus = json.get("rcvStatus").getAsString();
                var currentStatus = json.get("currentStatus").getAsString();
                var emResponseIdArray = json.get("emAlertResponseId").getAsString().split(","); // an array od all new emAlertResponseId
                //update each one
                int index = 0;
                while (index < emResponseIdArray.length) {
                    var specificResponse = victimEmAlertResponseJpa
                            .getEmAlertResponseByEmAlertResponseId(Long.parseLong(emResponseIdArray[index]));
                    if (specificResponse == null) {
                        throw new NotFoundException("No victimResponse with the given Id was found");
                    }
                    //update rcvStatus to received and status to in-progress
                    victimEmAlertResponseJpa.updateEmAlertResponse(rcvStatus, currentStatus, specificResponse.getEmAlertResponseId());
                    index++;
                }

                return victimEmAlertResponseJpa.getEmAlertResponseByEmAlertByAgencyId(agencyId);
            }
        } catch (Exception e) {
            // TODO: use a logger to log the error
            //e.printStackTrace();
            System.out.println("Error from getEmAlertResponse controller, cause: " + "This victimEmAlertResponse has not been clicked.");
        }
        return victimEmAlertResponseJpa.getEmAlertResponseByEmAlertByAgencyId(agencyId);
    }

    /**
     * gets all emAlert associated with the given agency that is not new
     */
    @PostMapping(value = "/notNewEmAlertResponse/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public VictimEmResponse[] getAllNotNewEmAlertResponse(@PathVariable long userId) {
        var user_agency = securityAgencyJpa.getAgencyByAgencyId_userId(userId);
        if (user_agency == null) {
            throw new UserNotFound("No User with the given id exists");
        }
        return victimEmAlertResponseJpa.getNotNewEmAlertResponseByAgencyId("new", user_agency.getAgencyId());
    }

    /**
     * Shows logged agency, the list of all emAlert forwarded to their agency and their respective status
     */
    @GetMapping(value = "/emAlertResponse/{agencyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public VictimEmResponse getAlertResponseById(@PathVariable long agencyId) {
        return webClient
                .get()
                .uri("/victim/getEmAlertResponse/" + agencyId)
                .retrieve()
                .bodyToMono(VictimEmResponse.class)
                .block(REQUEST_TIMEOUT);
    }

    private HttpHeaders setCustomHeader(long userId) {
        URI location = URI.create("/emAlert");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(location);

        httpHeaders.add("EmAlertResponse", "Received");
        httpHeaders.add("USER_ID", String.valueOf(userId));
        return httpHeaders;
    }
}
