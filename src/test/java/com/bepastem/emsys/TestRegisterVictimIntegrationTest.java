package com.bepastem.emsys;

import com.bepastem.exceptions.UserNameExistException;
import io.jsonwebtoken.lang.Assert;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@SpringBootTest
class TestRegisterVictimIntegrationTest {

    TestRegisterVictimIntegrationTest() throws IOException {
    }

    @Test
    void testRegisterUsers() throws Exception {

        for (int i = 0; i < 10; i++) {
            var user = new JSONObject();
            user.put("username", "pat" + i);
            user.put("password", "pass" + i);
            user.put("email", "email" + i);
            user.put("WHO", "VICTIM");
            user.put("agreedToUserDataTerms", true);

            var victim = new JSONObject();
            victim.put("firstname", "firstname" + i);
            victim.put("lastname", "ude" + i);
            victim.put("contactPersonEmail", "patrickudeonwo@yahoo.com" + i);
            victim.put("userPhone", (255669854 + i - 1));
            victim.put("imei", "w541e-58245h65-d6d" + i);

            var address = new JSONObject();
            address.put("streetAndHouseNr", "Berliner Tor" + i);
            address.put("postcode", "22201" + i);
            address.put("lga", "bk" + i);
            address.put("state", "Hamburg" + i);
            address.put("country", "Germany" + i);

            var location = new JSONObject();
            location.put("user_IMEI", ("122.365.44" + i));
            location.put("longitude", ("1236544" + i));
            location.put("latitude", ("78592" + i));
            location.put("city", ("Hamburg" + i));
            location.put("country", "Germany");

            var json = new JSONObject();
            json.put("user", user);
            json.put("victim", victim);
            json.put("address", address);
            json.put("geolocation", location);

            URL newUrl = new URL("http://localhost:8080/api/v1/register/user");
            HttpURLConnection con = (HttpURLConnection) newUrl.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "*/*");
            con.setDoOutput(true);

            try (OutputStream os = con.getOutputStream()) {
                OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
                osw.write(json.toString());
                osw.flush();
                osw.close();
            }

            // read response
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                // return new String(response);
                System.out.println("response1: " + response);
                Assert.notNull(response);
            }

        }
    }

    @Test
    public void testRegisterUserNameAlreadyRegistered() throws Exception {
        try {

            for (int i = 0; i < 10; i++) {
                var user = new JSONObject();
                user.put("username", "pat" + i);
                user.put("password", "pass" + i);
                user.put("email", "email" + i);
                user.put("WHO", "VICTIM");
                user.put("agreedToUserDataTerms", true);

                var victim = new JSONObject();
                victim.put("firstname", "firstname" + i);
                victim.put("lastname", "ude" + i);
                victim.put("contactPersonEmail", "email@email.com" + i);
                victim.put("userPhone", ("255669854" + (i - 1)));
                victim.put("imei", "w541e-58245h65-d6d" + i);

                var address = new JSONObject();
                address.put("streetAndHouseNr", "steil" + i);
                address.put("postcode", "22201" + i);
                address.put("lga", "bk" + i);
                address.put("state", "HH" + i);
                address.put("country", "DE" + i);

                var location = new JSONObject();
                location.put("user_IMEI", ("122.365.44" + i));
                location.put("city", ("HH" + i));
                location.put("longitude", ("1236544" + i));
                location.put("latitude", ("78592" + i));

                var json = new JSONObject();
                json.put("user", user);
                json.put("victim", victim);
                json.put("address", address);
                json.put("geolocation", location);

                URL newUrl = new URL("http://localhost:8080/api/v1/register/user");
                HttpURLConnection con = (HttpURLConnection) newUrl.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json; utf-8");
                con.setRequestProperty("Accept", "*/*");
                con.setDoOutput(true);

                try (OutputStream os = con.getOutputStream()) {
                    OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
                    osw.write(json.toString());
                    osw.flush();
                    osw.close();
                }

                // read response
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    // return new String(response);
                    System.out.println("response1: " + response);
                    Assert.notNull(response);
                }

            }
            // fail("Username already exists");

        } catch (UserNameExistException e) {

        }

    }

}