package com.bepastem.jwtAuth;

import com.google.gson.Gson;
import org.json.JSONObject;

public class JwtAuthenticationResponse {
    private final String user_token;
    private final long user_id;

    public JwtAuthenticationResponse(String jwtToken, long userId) {
        this.user_token = jwtToken;
        this.user_id = userId;
    }

    public String getUser_token() {
        return user_token;
    }

    public long getUser_id() {
        return user_id;
    }

    @Override
    public String toString() {
        return "JwtAuthenticationResponse{" +
                "user_token='" + user_token + '\'' +
                ", user_id=" + user_id +
                '}';
    }

    public JSONObject toJson(String resp){
        Gson g = new Gson();
       return g.fromJson(resp, JSONObject.class);
    }
}
