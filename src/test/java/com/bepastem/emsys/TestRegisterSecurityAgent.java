package com.bepastem.emsys;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@SpringBootTest
public class TestRegisterSecurityAgent {

    @Autowired
    private ServiceHelper serviceHelper;

    @Test
    public void registerSecurityAgent() throws IOException {

        String userCredentialJsonString = new JSONObject()
                .put("username", "patsec0")
                .put("password", "passsec0")
                //.put("password", new JSONObject().put("key1", "value1"))
                .toString();

        var outputStream = serviceHelper.writeStringToOutputStream(userCredentialJsonString);
        var readResponse = serviceHelper.readResponse();
        System.out.println("auth: " + readResponse);
        var authorization  = String.valueOf("Bearer :" + readResponse);
        // serviceHelper.disconnect();

        for(int i = 0; i < 10; i++) {
            var agent = new JSONObject();
            agent.put("username", "patsec"+i);
            agent.put("password", "passsec"+i);
            agent.put("name", "Joko udume"+i);

            var jsonData = new JSONObject();
            //json.put("username", "patsec0");
            //json.put("password", "passsec0");
            jsonData.put("securityAgent", agent);

            URL url = new URL("http://localhost:8080/api/register/agent");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Authorization", authorization);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; utf-8");
            httpURLConnection.setRequestProperty("Content-Length", "" + String.valueOf(jsonData).getBytes().length);
            httpURLConnection.setRequestProperty("Accept", "*/*");
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            /*myURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            myURLConnection.setRequestProperty("Content-Length", "" + postData.getBytes().length);
            myURLConnection.setRequestProperty("Content-Language", "en-US");
            myURLConnection.setUseCaches(false);*/

           System.out.println("header auth: " + httpURLConnection.getRequestProperty("Authorization"));


            try (OutputStream os = httpURLConnection.getOutputStream()) {
                OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
                osw.write(String.valueOf(jsonData));
                osw.flush();
                osw.close();
            }

            // read response
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                // return new String(response);
                System.out.println("response: " + response);

            }

        }
    }


}
