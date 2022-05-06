package com.bepastem.emsys;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class TestEmergencyAlert {

    private final long userId = 168670027;

    @Autowired
    private ServiceHelper serviceHelper;

    @Test
    public void testEmergencyAlertRequest() throws IOException {
        var response = serviceHelper.emergencyAlert();
        assert(response).contains("EmRequest of userId " + this.userId + " received. Duration = ");
    }

    @Test
    public void testEmergencyAlertResponse() throws IOException {
        var userIdStr = serviceHelper.emergencyAlertResponse();
        assert (userIdStr).equals("779196752");
    }

    @Test
    public void testVictimHome() throws IOException {
        System.out.println("body: " + serviceHelper.requestVictimHome().getBody());
        System.out.println("body: " + serviceHelper.requestVictimHome().getHeaders());
    }
}
