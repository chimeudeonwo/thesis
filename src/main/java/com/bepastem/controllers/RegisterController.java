package com.bepastem.controllers;

import com.bepastem.common.JsonConversion;
import com.bepastem.controllers.support.ConvertJsonToEntityClass;
import com.bepastem.controllers.support.JwtSupport;
import com.bepastem.exceptions.EmailExistsException;
import com.bepastem.exceptions.UserNameExistException;
import com.bepastem.jpadao.*;
import com.bepastem.models.*;
import com.bepastem.security.Authorities;
import com.bepastem.services.ClosestSecurityAgencyService;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/register")
public class RegisterController {
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private JwtSupport jwtSupport;
    @Autowired
    private ConvertJsonToEntityClass convertJsonToEntityClass;
    @Autowired
    private AuthJpaDao authJpaDao;
    @Autowired
    private UsersJpaDao usersJpaDao;
    @Autowired
    private VictimJpa victimJpa;
    @Autowired
    private AddressJpa addressJpa;
    @Autowired
    private SubscriptionJpa subscriptionJpa;
    @Autowired
    private GeolocationJpa geolocationJpa;
    @Autowired
    private SecurityAgencyJpa securityAgencyJpa;
    @Autowired
    private SecurityAgentJpa securityAgentJpa;
    @Autowired
    private SecurityAgencyDeviceJpa securityAgencyDeviceJpa;
    @Autowired
    private ClosestSecurityAgencyService geolocationService;

    public RegisterController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a user for the system.
     * @return returns userId on success and 0 on error.
     */
    @PostMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity registerUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
         System.out.println("request: " + request);
        var jsonObject = convertJsonToEntityClass.getEntityFromRequestAsJson(request);
        System.out.println("jsonObject: " + jsonObject);
        var user = convertJsonToEntityClass.getUserFromJsonObject(jsonObject);
        var geolocation = convertJsonToEntityClass.getGeolocationFromJsonObject(jsonObject);
        var address = convertJsonToEntityClass.getAddressFromJsonObject(jsonObject);
        var subscription = new Subscription();

        try{
            authJpaDao.getUserByUsername(user.getUsername());
                //throw new UserNameExistException("An account with the given Username already exist!");
                //response.sendError(1, "An account with the given username already exist!");
                // System.out.println("Username already exits in DB");
                // response.sendError(HttpServletResponse.SC_BAD_REQUEST, "An account with the given username already exist!");
        } catch (UserNameExistException e){
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Username already exist: " + e.getMessage());
        }

        try{
            authJpaDao.getUserByEmail(user.getEmail());
                //throw new EmailExistsException("An account with the given Email already exist!");
                // response.sendError(1, "An account with the given email already exist!");

        } catch (EmailExistsException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Email already exist: " + e.getMessage());
        }

        var password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);

        //Set UserInfo Fields
        Role role = new Role(1, Authorities.USER.name());
        Set<Role> listOfRoles = new HashSet<>();
        listOfRoles.add(role);
        user.setRoles(listOfRoles);

        //create Victim or Agent or Agency the entities
        if(user.getWHO().equalsIgnoreCase("VICTIM")){
            var victim = (Victim) JsonConversion.fromJsonToClassObject(jsonObject.get("victim").getAsJsonObject(), Victim.class);

            //set securityAgency reference ids
            victim.setVictim_userId(user.getUserId());
            victim.setAddressId(address.getAddressId());
            victim.setGeolocationId(geolocation.getGeolocationId());
            victim.setSubscriptionId(subscription.getSubscriptionId());
            victim.setVictimId(victim.getVictimId());

            geolocation.setGeolocationId_userId(user.getUserId());

            address.setAddressId_userId(user.getUserId());
            subscription.setSubscriptionId_userId(user.getUserId());

            var saveAddress = addressJpa.save(address);
            var savedSubscription = subscriptionJpa.save(subscription);
            var savedGeolocation = geolocationJpa.save(geolocation);
            System.out.println("victim: " + victim);

            try{
                victimJpa.save(victim);
            } catch (DataAccessException e){
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body("Cannot save Victim, User Imei already exists: " + e.getMessage());
            }

            // return usersJpaDao.save(user).getUserId();
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(usersJpaDao.save(user).getUserId());
        }
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body("User is not VICTIM");
    }

    @PostMapping(value = "/agency", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public long registerSecAgency(HttpServletRequest httpServletRequest) throws IOException {
        var json = convertJsonToEntityClass.getEntityFromRequestAsJson(httpServletRequest);

        var user = convertJsonToEntityClass.getUserFromJsonObject(json);
        var securityAgency = convertJsonToEntityClass.getSecurityAgencyFromJsonObject(json);
        var securityAgent = convertJsonToEntityClass.getSecurityAgentFromJsonObject(json);
        var securityAgentDevice = convertJsonToEntityClass.getSecurityAgentDeviceFromJsonObject(json);
        var geolocation = convertJsonToEntityClass.getGeolocationFromJsonObject(json);
        var address = convertJsonToEntityClass.getAddressFromJsonObject(json);
        var subscription = new Subscription();

        if (securityAgencyJpa.getAgencyByEmail(securityAgency.getEmail()) != null || usersJpaDao.getUserByEmail(user.getEmail()) != null) {
            throw new EmailExistsException("An account with the given Email already exists!");
        }

        if (securityAgencyJpa.getAgencyByUnitName(securityAgency.getUnitName()) != null) {
            throw new UserNameExistException("An account with the given SecurityAgency unit name already exists!: " + securityAgency.getUnitName());
        }

        if (securityAgentJpa.getUserByUsername(securityAgent.getUsername()) != null) {
            throw new UserNameExistException("An account with the given Username already exists!: " + securityAgency.getUnitName());
        }

        if (securityAgencyDeviceJpa.getDeviceByImei(securityAgentDevice.getDeviceImei()) != null) {
            System.out.println("New device added to your agency");    //notify of new device and ask to confirm that its them //TODO: replace with securityAgency.getUnitName()
        }

        var password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);

        //Set UserInfo Fields
        Role role = new Role(1, Authorities.USER.name());
        Set<Role> listOfRoles = new HashSet<>();
        listOfRoles.add(role);
        listOfRoles.add(new Role(2, Authorities.SECURITY_AGENT.name()));
        listOfRoles.add(new Role(3, Authorities.SECURITY_AGENCY_UNIT_HEAD.name()));
        user.setRoles(listOfRoles);

        //create Victim or Agent or Agency the entities
        if(user.getWHO().equalsIgnoreCase("SECURITYAGENCY")){
            securityAgent.setAgent_userId(user.getUserId());

            //set securityAgency reference ids
            securityAgency.setAgency_userId(user.getUserId());
            securityAgency.setAddressId(address.getAddressId());
            securityAgency.setGeolocationId(geolocation.getGeolocationId());
            securityAgency.setSubscriptionId(subscription.getSubscriptionId());
            securityAgency.setDeviceImei(securityAgentDevice.getDeviceImei());

            // unit head i.e. the default security agent of this agency
            securityAgent.setUsername(user.getUsername());
            securityAgent.setPassword(password);
            securityAgent.setAgent_userId(user.getUserId());
            securityAgent.setFullName(securityAgent.getFullName());
            securityAgent.setAgent_unitName(securityAgency.getUnitName());

            geolocation.setGeolocationId_userId(user.getUserId());
            address.setAddressId_userId(user.getUserId());
            subscription.setSubscriptionId_userId(user.getUserId());

            addressJpa.save(address);
            subscriptionJpa.save(subscription);
            geolocationJpa.save(geolocation);
            securityAgentJpa.save(securityAgent);
            securityAgencyJpa.save(securityAgency);
            securityAgencyDeviceJpa.save(securityAgentDevice);
            return usersJpaDao.save(user).getUserId();
        }
        return 0;
    }

    /**Only the security unit heads can create agents i.e. will have access to this view level, must have role SECURITY_AGENCY_UNIT_HEAD */
    @PostMapping(value = "/agent", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public long registerSecurityAgent(HttpServletRequest request) throws IOException, NoSuchAlgorithmException {
        var jsonObject = (JsonObject) JsonConversion.requestPayloadToJson(request, JsonObject.class);
        var username =  jwtSupport.getUsernameFromAuthorizationToken(request);

        // only unit heads can have access to create security agents
        var unitHead = usersJpaDao.getUserByUsername(username);
        var unitHeadAgency = securityAgencyJpa.getAgencyByAgencyId_userId(unitHead.getUserId());
        var securityAgentToBeCreated = convertJsonToEntityClass.getSecurityAgentFromJsonObject(jsonObject);

        // create security agent as a user i.e. Users class
        var newUser = new Users();
        newUser.setUsername(securityAgentToBeCreated.getUsername());
        newUser.setPassword(passwordEncoder.encode(securityAgentToBeCreated.getPassword()));
        newUser.setEmail(unitHead.getEmail());
        newUser.setWHO("SECURITYAGENT");

        //set security agent references
        securityAgentToBeCreated.setAgent_agencyId(unitHeadAgency.getAgencyId());

        //Set UserInfo Fields
        Role role = new Role(1, Authorities.USER.name());
        Set<Role> listOfRoles = new HashSet<>();
        listOfRoles.add(role);
        listOfRoles.add(new Role(2, Authorities.SECURITY_AGENT.name()));
        newUser.setRoles(listOfRoles);

        //save the entities
        usersJpaDao.save(newUser);
        return securityAgentJpa.save(securityAgentToBeCreated).getAgentId();
    }

}
