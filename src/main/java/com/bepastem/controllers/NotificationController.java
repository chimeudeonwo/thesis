package com.bepastem.controllers;

import com.bepastem.jpadao.VictimJpa;
import com.bepastem.services.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notification")
public class NotificationController {
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private VictimJpa victimJpa;

    /**When a new user (Victim) registers, we send email to the victim contact person email to inform the contact
     *  that the victim has used this contact as his/her emergency contact. */
    @PostMapping(value = "/victimcontactperson/consent/{victimId}")
    public String notifyVictimContactPersonConsent(@PathVariable long victimId){
        // get contact person where user == victim
        var victim = victimJpa.getById(victimId);

        String victimFullName = victim.getFirstname() + ", " + victim.getLastname(); //get victim fullname

        String subject = victimFullName + ", has given your email as the emergency contact";
        String messageBody = "You are being notified that " + victimFullName + ", has given your email contact "
                + "as the emergency contact. + \n \n Best regards, \n EmSys Mgt";

        emailSenderService.sendEmail(victim.getContactPersonEmail(), subject, messageBody);

        return "Notification sent successfully to victim's contact person email.";
    }

    @GetMapping(value = "/victimcontactperson/{victimId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String notifyVictimContactPerson(@PathVariable long victimId){
        System.out.println("/victimcontactperson/{victimId} called");
        // get contact person where user == victim
        var victim = victimJpa.getById(victimId);

        String victimFullName = victim.getFirstname() + ", " + victim.getLastname(); //get victim fullname

        String subject = "Your ward: " + victimFullName + ", MIGHT be in danger!";
        String messageBody = "You are being notified that " + victimFullName + ", might be in danger because we received" +
                " an emergency request for assistance. \nPlease keep calm, in case of confirmation of this emergency request, " +
                "the security agency responsible for responding to this emergency request will contact you." +
                "\n \n Best regards, \n EmSys Mgt";

        emailSenderService.sendEmail(victim.getContactPersonEmail(), subject, messageBody);

        return "Notification sent successfully to victim's contact person email.";
    }

}
