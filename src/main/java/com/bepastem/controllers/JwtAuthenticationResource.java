package com.bepastem.controllers;

import com.bepastem.jpadao.UsersJpaDao;
import com.bepastem.jwtAuth.JwtAuthenticationRequest;
import com.bepastem.jwtAuth.JwtAuthenticationResponse;
import com.bepastem.security.JwtUtil;
import com.bepastem.security.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/v1/authenticate")
public class JwtAuthenticationResource {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UsersJpaDao usersJpaDao;

    @PostMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json; charset=UTF-8")
    // public ResponseEntity<?> createAuthenticationToken(HttpServletRequest httpServletRequest) throws Exception {
    public ResponseEntity<?> authenticateUser(@RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletRequest request) throws Exception {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        final String jwtGeneratedToken = jwtUtil.generateToken(userDetails);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new JwtAuthenticationResponse(jwtGeneratedToken,
                        usersJpaDao.getUserByUsername(userDetails.getUsername()).getUserId()));
    }

    @PostMapping(value = "/securityAgent", consumes = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<?> createAuthenticationToken(HttpServletRequest httpServletRequest) throws Exception {
    public ResponseEntity<?> authenticateSecurityAgent(@RequestBody JwtAuthenticationRequest authenticationRequest) throws Exception {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        final String jwtGeneratedToken = jwtUtil.generateToken(userDetails);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new JwtAuthenticationResponse(jwtGeneratedToken,
                usersJpaDao.getUserByUsername(userDetails.getUsername()).getUserId()));
    }
}
