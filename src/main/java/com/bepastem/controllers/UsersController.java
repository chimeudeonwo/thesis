package com.bepastem.controllers;

import com.bepastem.common.JsonConversion;
import com.bepastem.controllers.support.JwtSupport;
import com.bepastem.exceptions.UserNameExistException;
import com.bepastem.jpadao.AuthJpaDao;
import com.bepastem.jpadao.UsersJpaDao;
import com.bepastem.models.Users;
import com.bepastem.models.Victim;
import com.bepastem.models.VictimEmAlert;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UsersController {

    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthJpaDao auth;

    @Autowired
    private UsersJpaDao userJpaDao;

    @Autowired
    private JwtSupport support;

    public UsersController(PasswordEncoder passwordEncoder, AuthJpaDao auth, JwtSupport support) {
        this.passwordEncoder = passwordEncoder;
        this.auth = auth;
        this.support = support;
    }

    @GetMapping(value = "/home", produces = "application/json")
    // @CrossOrigin(origins = "http://localhost/8080")
    public String home() {
        return "welcome!, this is our home.";
    }

    /**returns ALL user information, NOTE: this should have admin level view*/
    @GetMapping(value = "/getUser", produces = "application/json")
    @ResponseBody
    public Users getUser(HttpServletRequest request) {
       return auth.getUserByUsername(support.getUsernameFromAuthorizationToken(request));
    }

    @GetMapping(value = "/getUserId/{userId}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Long> getUserId(HttpServletRequest request, @PathVariable long userId) {
       try{
           if(userJpaDao.getUserById(userId) != null){
               // return auth.getUserByUsername(support.getUsernameFromAuthorizationToken(request)).getUserId();
               return ResponseEntity
                       .status(HttpStatus.OK)
                       .body(auth.getUserByUsername(support.getUsernameFromAuthorizationToken(request)).getUserId());
           }
       } catch (UserNameExistException e){
           return ResponseEntity
                   .status(HttpStatus.BAD_REQUEST)
                   .body(0L);
       }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(0L);
    }

    @GetMapping(value = "/emAlertHistory/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<VictimEmAlert> getUserEmAlertHistory(@PathVariable long userId){
        return userJpaDao.getUserEmAlertHistory(userId);
    }

    @PostMapping(value = "/passwordUpdate", produces = "application/json")
    public void updateUserPassword(HttpServletRequest request) throws IOException {
        var user = auth.getUserByUsername(support.getUsernameFromAuthorizationToken(request));
        var requestBody = (JsonObject) JsonConversion.requestPayloadToJson(request, JsonObject.class);
        var newPasswordEncoded = passwordEncoder.encode(requestBody.get("password").getAsString());
        userJpaDao.updateUserPasswordByUsername(newPasswordEncoded, user.getUsername());
    }
}
