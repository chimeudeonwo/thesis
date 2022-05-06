package com.bepastem.controllers.support;

import com.bepastem.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class JwtSupport {

    @Autowired
    private JwtUtil jwtUtil;

    public String getUsernameFromAuthorizationToken(HttpServletRequest request){
        var authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return jwtUtil.extractUsername(authorizationHeader.substring(7));
        }
        return null;
    }
}
