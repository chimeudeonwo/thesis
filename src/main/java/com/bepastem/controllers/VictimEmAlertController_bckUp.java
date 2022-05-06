package com.bepastem.controllers;

import com.bepastem.controllers.support.ConvertJsonToEntityClass;
import com.bepastem.exceptions.UserNotFound;
import com.bepastem.exceptions.VictimEmergencyAlertIsNull;
import com.bepastem.jpadao.GeolocationJpa;
import com.bepastem.jpadao.UsersJpaDao;
import com.bepastem.jpadao.VictimEmAlertJpa;
import com.bepastem.models.VictimEmAlert;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

@RestController
@CrossOrigin(origins = "http://localhost:63342", maxAge = 3600)
@RequestMapping("/api/v1/victim")
public class VictimEmAlertController_bckUp {

    @Autowired
    private ConvertJsonToEntityClass convertJsonToEntityClass;
    @Autowired
    private VictimEmAlertJpa victimEmAlertJpa;
    @Autowired
    private UsersJpaDao usersJpaDao;
    @Autowired
    private GeolocationJpa geolocationJpa;

    @PostMapping("/emergencyAlert")
    public ResponseEntity<String> victimEmergencyAlert(HttpServletRequest request) throws IOException, NoSuchAlgorithmException, ParseException {
        var jsonFromRequest = convertJsonToEntityClass.getEntityFromRequestAsJson(request);
        // var victimEmAlert = convertJsonToEntityClass.getVictimEmAlertFromJsonElement(jsonFromRequest);
        var victimEmAlert = convertJsonToEntityClass.getVictimEmAlertFromJsonObject(jsonFromRequest);
        var victimGeolocation = convertJsonToEntityClass.getGeolocationFromJsonObject(jsonFromRequest);

        if(victimEmAlert == null){
            throw new VictimEmergencyAlertIsNull("Victim Emergency Alert is null"); // future --> may set sth to show a user might be in danger
        }

        // get victim
        var user = usersJpaDao.getUserById(victimEmAlert.getVictimId_userId());
        if(user == null) {
            throw new UserNotFound("No user with the received userId exists in DB");
        }
        victimGeolocation.setGeolocationId_userId(victimEmAlert.getVictimId_userId());
        geolocationJpa.save(victimGeolocation);
        var emAlert = victimEmAlertJpa.save(victimEmAlert);

        // redirect?
        var headers = this.setCustomHeader("/api/response/emAlertResponse", user.getUserId());
        return new ResponseEntity<String>(String.valueOf("EmRequest received at " + emAlert.getDuration()), headers, HttpStatus.CREATED);
    }

    @GetMapping(value = "/getEmAlert/{emAlertId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public VictimEmAlert getEmAlert(@PathVariable long emAlertId) {
        var alert = victimEmAlertJpa.getVictimEmAlertByEmAlertId(emAlertId);
        return alert;
    }

    private HttpHeaders setCustomHeader(String uriOrLocation, long userId) {
        URI location = URI.create(uriOrLocation);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(location);

        var latestGeolocation = geolocationJpa.getCurrentVictimLocation(userId);

        var currentLocation = new JSONObject();
        currentLocation.put("longitude", latestGeolocation.getLongitude());
        currentLocation.put("latitude", latestGeolocation.getLatitude());

        httpHeaders.add("CORS_Token", "hjfhfu-dhhhf-34kl-j34l");
        httpHeaders.add("currentVictimLocation", String.valueOf(currentLocation));
        return httpHeaders;
    }
}
