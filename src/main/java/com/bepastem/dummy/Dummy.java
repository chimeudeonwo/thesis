package com.bepastem.dummy;

import aj.org.objectweb.asm.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Dummy {

        private static final String DEFAULT_QUOTE = "Lorem ipsum dolor sit amet.";
        private final String baseUrl;

        public Dummy(String baseUrl) {
                this.baseUrl = baseUrl;
        }

        public String getAuthToken() {
                HttpClient client = HttpClient.newHttpClient();

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(baseUrl + "/api/authenticate"))
                        .header("Accept", "application/json")
                        .GET()
                        .build();

                try {
                        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

                        if (httpResponse.statusCode() != 200) {
                                return DEFAULT_QUOTE;
                        }

                        var responseBody = Arrays.toString(httpResponse.body().getBytes(StandardCharsets.UTF_8));
                        return responseBody;
                } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                        return DEFAULT_QUOTE;
                }
        }

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.valueOf("SAME_PROTOCOL"))
                .proxy(ProxySelector.of(new InetSocketAddress("127.0.0.1", 8080)))
                .authenticator(Authenticator.getDefault())
                .build();

        public void get(String uri) throws Exception {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(uri))
                        .build();

                HttpResponse<Path> response =
                        client.send(request, HttpResponse.BodyHandlers.ofFile(Paths.get("body.txt")));

                System.out.println("Response in file:" + response.body());
        }

}

