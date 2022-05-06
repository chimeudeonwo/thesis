package com.bepastem.models;

import com.bepastem.util.IdsGenerator;

import javax.persistence.*;
import java.security.NoSuchAlgorithmException;

@Entity(name="VICTIM")
public class Victim {
    @Id
    private long victimId;
    @Column(nullable = false, length = 64)
    private long victim_userId;
    @Column(nullable = false, length = 64)
    private String lastname;
    @Column(nullable = false, length = 64)
    private String firstname;
    @Column(unique = true, nullable = false, length = 64)
    private String userPhone;
    private String contactPersonEmail;
    @Column(unique = true, nullable = false,  length = 64)
    private String addressId;
    @Column(unique = true, nullable = false,  length = 64)
    private String subscriptionId;
    @Column(unique = true, nullable = false,  length = 64)
    private String imei;
    @Column(unique = true, nullable = false,  length = 64)
    private String geolocationId;
    @Transient
    private final IdsGenerator idsGenerator = new IdsGenerator();

    public Victim() throws NoSuchAlgorithmException {
        this.victimId = idsGenerator.generateLongId();
        this.addressId = idsGenerator.generateStringId();
        this.subscriptionId = idsGenerator.generateStringId();
        this.imei = idsGenerator.generateStringId();
        this.geolocationId = idsGenerator.generateStringId();
    }

    public Victim(String lastname, String firstname, String userPhone,
                  String contactPerson, String addressId, String subscriptionId, String userDeviceId, String geolocationId) throws NoSuchAlgorithmException {
        this.victimId = idsGenerator.generateLongId();
        this.lastname = lastname;
        this.firstname = firstname;
        this.userPhone = userPhone;
        this.contactPersonEmail = contactPerson;
        this.addressId = addressId;
        this.subscriptionId = subscriptionId;
        this.imei = userDeviceId;
        this.geolocationId = geolocationId;
    }

    public long getVictimId() {
        return victimId;
    }

    public void setVictimId(long victimId) {
        this.victimId = victimId;
    }

    public long getVictim_userId() {
        return victim_userId;
    }

    public void setVictim_userId(long victim_userId) {
        this.victim_userId = victim_userId;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getContactPersonEmail() {
        return contactPersonEmail;
    }

    public void setContactPersonEmail(String contactPerson) {
        this.contactPersonEmail = contactPerson;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getUserDeviceId() {
        return imei;
    }

    public void setUserDeviceId(String imei) {
        this.imei = imei;
    }

    public String getGeolocationId() {
        return geolocationId;
    }

    public void setGeolocationId(String geolocationId) {
        this.geolocationId = geolocationId;
    }

    @Override
    public String toString() {
        return "Victim{" +
                "victimId=" + victimId +
                ", victim_userId=" + victim_userId +
                ", lastname='" + lastname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", userPhone=" + userPhone +
                ", contactPerson='" + contactPersonEmail + '\'' +
                ", addressId='" + addressId + '\'' +
                ", subscriptionId='" + subscriptionId + '\'' +
                ", imei='" + imei + '\'' +
                ", geolocationId='" + geolocationId + '\'' +
                ", idsGenerator=" + idsGenerator +
                '}';
    }
}
