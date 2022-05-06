package com.bepastem.emsys;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@SpringBootTest
class TestGetAuthenticated {

    private URL url = new URL("http://localhost:8080/api/authenticate");
    private HttpURLConnection con = (HttpURLConnection)this.url.openConnection();

    TestGetAuthenticated() throws IOException {
    }

    @Test
    void contextLoads() throws IOException {
    }

    @Test
    public void getAuthToken() {
        HttpClient client = HttpClient.newHttpClient();

        var baseUrl = "http://localhost:8080";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/v1/authenticate/user"))
                .header("Accept", "*/*")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"username\":\"pat1\",\"password\":\"pass\"} "))
                .build();

        try {
            HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (httpResponse.statusCode() != 200) {
                System.out.println("resp body _not 200: " + httpResponse);
                System.out.println("DEFAULT_QUOTE_not 200");
            }
            System.out.println("resp body: " + httpResponse.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("DEFAULT_QUOTE_catch");
        }
    }

    @Test
    public void getAuthenticated() throws IOException, JSONException {
        URL newUrl = new URL("http://localhost:8080/api/v1/authenticate/user");

        HttpURLConnection con = (HttpURLConnection)newUrl.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "*/*");
        con.setDoOutput(true);

        String userCredentialJsonString = new JSONObject()
                .put("username", "pat1")
                .put("password", "pass1")
                .toString();

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = userCredentialJsonString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // read response
        try(BufferedReader br = new BufferedReader(
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
