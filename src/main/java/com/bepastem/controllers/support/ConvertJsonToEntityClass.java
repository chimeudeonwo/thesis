package com.bepastem.controllers.support;

import com.bepastem.common.JsonConversion;
import com.bepastem.models.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class ConvertJsonToEntityClass {

    public static final String VICTIM_USER_ID = "victimId_userId";
    public static final String DEVICES_FOUND = "devicesFound";
    public static final String TIME = "time";
    public static final String DATE = "localDate";
    public static final String IMEI = "user_IMEI";
    public static final String LAT = "latitude";
    public static final String LON = "longitude";
    public static final String CITY = "city";
    public static final String COUNTRY = "country";

    /**
     * @return Returns request body
     */
    public JsonObject getEntityFromRequestAsJson(HttpServletRequest request) throws IOException {
        try {
            return (JsonObject) JsonConversion.requestPayloadToJson(request, JsonObject.class);
        } catch (IOException e) {
            // TODO: use a logger to log the error
            System.out.println("Error from getEntityFromRequestAsJson(request): " + e.getMessage());
        }
        return new JsonObject();
    }

    /**
     * @param jsonObject is the json object containing user information extracted from request body
     * @return returns the extracted user details from the json request body
     */
    public Users getUserFromJsonObject(JsonObject jsonObject) {
        return (Users) JsonConversion.fromJsonToClassObject(jsonObject.get("user").getAsJsonObject(), Users.class);
    }

    /**
     * @param jsonObject is the json object containing user information extracted from request body
     * @return returns the extracted address details of the user from the json request body
     */
    public Address getAddressFromJsonObject(JsonObject jsonObject) {
        return (Address) JsonConversion.fromJsonToClassObject(jsonObject.get("address").getAsJsonObject(), Address.class);
    }

    /**
     * @param jsonObject is the json object containing user information extracted from request body
     * @return returns the extracted geolocation details of the user from the json request body
     */
    public Geolocation getGeolocationFromJsonObject(JsonObject jsonObject) {

        var newGeoRequest = jsonObject.get("geolocation").getAsJsonObject();

        var geoVictimEmAlert = new Geolocation();

        // set alert values
        geoVictimEmAlert.setCity(newGeoRequest.get(CITY).getAsString());
        geoVictimEmAlert.setCountry(newGeoRequest.get(COUNTRY).getAsString());
        geoVictimEmAlert.setUser_IMEI(newGeoRequest.get(IMEI).getAsString());
        geoVictimEmAlert.setLatitude(newGeoRequest.get(LAT).getAsString());
        geoVictimEmAlert.setLongitude(newGeoRequest.get(LON).getAsString());

        return geoVictimEmAlert;
    }

    /**
     * @param jsonObject is the json object containing user information extracted from request body
     * @return returns the extracted security agent details of the user from the json request body
     */
    public SecurityAgent getSecurityAgentFromJsonObject(JsonObject jsonObject) {
        return (SecurityAgent) JsonConversion.fromJsonToClassObject(jsonObject.get("securityAgent").getAsJsonObject(), SecurityAgent.class);
    }

    /**
     * @param jsonObject is the json object containing user information extracted from request body
     * @return returns the extracted security agency details of the user from the json request body
     */
    public SecurityAgency getSecurityAgencyFromJsonObject(JsonObject jsonObject) {
        return (SecurityAgency) JsonConversion.fromJsonToClassObject(jsonObject.get("securityAgency").getAsJsonObject(), SecurityAgency.class);
    }

    /**
     * @param jsonObject is the json object containing user information extracted from request body
     * @return returns the extracted security agent device details of the user from the json request body
     */
    public SecurityAgencyDevice getSecurityAgentDeviceFromJsonObject(JsonObject jsonObject) {
        return (SecurityAgencyDevice) JsonConversion.fromJsonToClassObject(jsonObject.get("securityAgentDevice").getAsJsonObject(), SecurityAgencyDevice.class);
    }

    /**
     * @param jsonObject is the json object containing user, victimEmAlert information extracted from request body
     * @return returns the extracted address details of the user from the json request body
     */
    public VictimEmAlert getVictimEmAlertFromJsonObject(JsonObject jsonObject) throws NoSuchAlgorithmException, ParseException {
        //request payload
        var newEmAlertRequest = jsonObject.get("victimEmAlert").getAsJsonObject();

        var victimEmAlert = new VictimEmAlert();

        // set alert values
        victimEmAlert.setVictimId_userId(newEmAlertRequest.get(VICTIM_USER_ID).getAsLong());
        victimEmAlert.setDevicesFound(newEmAlertRequest.get(DEVICES_FOUND).getAsString());

        long start = newEmAlertRequest.get(TIME).getAsLong();
        long end = System.currentTimeMillis();
        double duration =  ((((double) end - (double) start) / 1000) % 60); // in seconds
        victimEmAlert.setDuration(String.valueOf(duration));
        return victimEmAlert;
    }

}
