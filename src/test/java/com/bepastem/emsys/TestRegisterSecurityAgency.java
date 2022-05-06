package com.bepastem.emsys;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@SpringBootTest
public class TestRegisterSecurityAgency {

    @Test
    public void testRegisterSecurityAgency() throws IOException {
        for(int i = 0; i < 10; i++) {
            var users = new JSONObject();
            users.put("username", "sec"+i);
            users.put("password", "pass"+i);
            users.put("email", "emailpat"+i);
            users.put("WHO", "SECURITYAGENCY");
            users.put("agreedToUserDataTerms", true);

            var agency = new JSONObject();
            agency.put("unitName", "hbf"+i);
            agency.put("unitCode", 40 + i - 1);
            agency.put("stateCode", 41 + i - 1);
            agency.put("unitHead", "Nils"+i);
            agency.put("city", "Hamburg");
            agency.put("email", "polizei"+i);
            agency.put("phone", (2564715 + i-1));
            agency.put("techSupportPerson", "anna"+i);
            agency.put("category", "private");

            var agent = new JSONObject();
            agent.put("username", "sec"+i);
            agent.put("password", "pass"+i);
            agent.put("fullName", "Ani Chike"+i);

            var securityAgentDeviceJson = new JSONObject();
            securityAgentDeviceJson.put("deviceName", "Android_3.1"+i);
            securityAgentDeviceJson.put("deviceImei", "3525ikk-tzgh55-ghsdj25-hhjj52"+i);

            var address = new JSONObject();
            address.put("streetAndHouseNr", "steil"+i);
            address.put("postcode", "22201"+i);
            address.put("lga", "hb"+i);
            address.put("state", "HB"+i);
            address.put("country", "DE"+i);

            var geolocation = new JSONObject();
            geolocation.put("longitude", (10.0504058+i));
            geolocation.put("latitude", (53.596297+i));
            geolocation.put("city", "Hamburg");
            geolocation.put("user_IMEI", "122:345:85:1:00");
            geolocation.put("country", "Germany");

            var json = new JSONObject();
            json.put("user", users);
            json.put("securityAgency", agency);
            json.put("securityAgent", agent);
            json.put("securityAgentDevice", securityAgentDeviceJson);
            json.put("geolocation", geolocation);
            json.put("address", address);

            URL newUrl = new URL("http://localhost:8080/api/v1/register/agency");
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
            }

        }
    }

}
