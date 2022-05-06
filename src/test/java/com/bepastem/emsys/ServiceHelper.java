package com.bepastem.emsys;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

@Component
public class ServiceHelper {

    private URL url;
    private HttpURLConnection con;

    public ServiceHelper() throws IOException {
        this.url = new URL("http://localhost:8080/api/v1/authenticate/user");
        this.con = (HttpURLConnection) this.url.openConnection();
    }

    public ServiceHelper(String endpoint) throws IOException {
        this.url = new URL("http://localhost:8080/api/v1/" + endpoint);
        this.con = (HttpURLConnection) this.url.openConnection();
    }

    public String emergencyAlert() throws IOException {
        var constr = new ServiceHelper("victim/emAlert");
        this.url = constr.url;
        this.con = constr.con;

        var victimEmAlert = new JSONObject();
        victimEmAlert.put("victimId_userId", 168670027);
        victimEmAlert.put("geolocationId", "pass");
        victimEmAlert.put("time", System.currentTimeMillis());
        victimEmAlert.put("devicesFound", "122.987.27839");

        var geolocation = new JSONObject();
        geolocation.put("geolocationId_userId", 168670027);
        geolocation.put("latitude", "53.596297");
        geolocation.put("longitude", "10.0504058");
        geolocation.put("user_IMEI", "449040073366407");
        geolocation.put("city", "Hamburg");
        geolocation.put("country", "Germany");

        var json = new JSONObject();
        json.put("victimEmAlert", victimEmAlert);
        json.put("geolocation", geolocation);

        var writtenJson = this.writeStringToOutputStream(String.valueOf(json));
        var response = this.readResponse();
        return response;
    }

    public String emergencyAlertResponse() throws IOException {
        var constr = new ServiceHelper("victim/emAlertResponse/779196752");
        this.url = constr.url;
        this.con = constr.con;
        var victimEmAlert = new JSONObject();
        victimEmAlert.put("victimId_userId", 170497036);
        victimEmAlert.put("devicesFound", "122.987.2739"); // use map Map<String, String> to map device imei and location

        var geolocation = new JSONObject();
        geolocation.put("latitude", "25.3445");
        geolocation.put("longitude", "234.1145");
        geolocation.put("user_IMEI", "122.365.441");
        geolocation.put("city", "HB");
        geolocation.put("country", "DE");

        var json = new JSONObject();
        json.put("victimEmAlert", victimEmAlert);
        json.put("geolocation", geolocation);

        var writtenJson = this.writeStringToOutputStream(String.valueOf(json));
        var response = this.readResponse();
        System.out.println("EmAlertServiceHelper: " + response);
        return response;
    }

    public HttpEntity<?> requestVictimHome() {
        System.out.println("entered");
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE));
        HttpEntity<String> entity = new HttpEntity<String>("helloWorld", headers);
        URI location = template.postForLocation("http://localhost:8080/api/v1/victim/home", entity);
        template.postForEntity("http://localhost:8080/api/v1/victim/home", entity, MediaType.APPLICATION_JSON.getClass().getComponentType());
        template.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                if (response.getHeaders().get("onError").equals("Error Occurred")) {
                    return true;
                }
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                new DefaultResponseErrorHandler();
            }
        });


       /* HttpEntity<String> entity = template.getForEntity("https://example.com", String.class);
        String body = entity.getBody();
        MediaType contentType = entity.getHeaders().getContentType();*/
        return entity;
    }

    public HttpEntity<?> getUserEmAlertHistories() {
        final long userId = 65537; //TODO: change
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE));
        HttpEntity<String> entity = new HttpEntity<String>("helloWorld", headers);
        URI location = template.postForLocation("http://localhost:8080/api/v1/user/emAlertHistory/"+userId, entity);
        template.postForEntity("http://localhost:8080/api/v1/victim/home", entity, MediaType.APPLICATION_JSON.getClass().getComponentType());
        template.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                if (response.getHeaders().get("onError").equals("Error Occurred")) {
                    return true;
                }
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                new DefaultResponseErrorHandler();
            }
        });


       /* HttpEntity<String> entity = template.getForEntity("https://example.com", String.class);
        String body = entity.getBody();
        MediaType contentType = entity.getHeaders().getContentType();*/
        return entity;
    }

    //@RequestMapping("/handle")
    public HttpEntity<Object> handle() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("error", new UsernameNotFoundException("").getMessage());


        HttpEntity<JSONObject> responseBody = new HttpEntity<>(new JSONObject(), responseHeaders);
        return new HttpEntity<Object>(new JSONObject(), responseHeaders);
    }

    public OutputStream writeStringToOutputStream(String userCredentialJsonString) throws IOException {
        // URL newUrl = new URL("http://localhost:8080/api/authenticate/user");

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "*/*");
        con.setDoOutput(true);

        OutputStream os = con.getOutputStream();
        try {
            byte[] input = userCredentialJsonString.getBytes("utf-8");
            os.write(input, 0, input.length);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return os;
    }

    public String readResponse() throws IOException {
        //StringBuilder authToken = null;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(this.getInputStream(), "utf-8"))) {
            StringBuilder token = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                token.append(responseLine.trim());
            }
            System.out.println("readResponse: " + token);
            this.disConnect();
            return new String(token);
            // authToken = token;

        }
    }

    public InputStream getInputStream() throws IOException {
        return this.con.getInputStream();
    }

    public void disConnect() {
        this.con.disconnect();
    }
}
