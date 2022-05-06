package com.bepastem.util;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class Utility {

    public Utility() {
    }

    public String[] getUserLoginDetails(String authorization) throws Exception {
        var userCredentials = new String[2];

        if(authorization == null){
            System.out.println("Authorization in Header in LoggedUserImpl is Null");
            throw new Exception("Authorization in Header is Null");
        }

        if(authorization.toLowerCase().startsWith("basic")) {
            // Authorization: Basic base64credentials
            String base64Credentials = authorization.substring("Basic".length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credDecoded, StandardCharsets.UTF_8);
            // credentials = username:password
            final String[] values = credentials.split(":", 2);

            //username = values[0];
            //password = values[1];
            userCredentials[0] = values[0];
            userCredentials[1] = values[1];
        }

        return userCredentials;
    }
}
