package com.bepastem.models;

import com.bepastem.util.IdsGenerator;

import javax.persistence.*;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

@Entity(name = "SECURITYAGENCY")
public class SecurityAgency {
    @Id
    private long agencyId;
    @Column(unique = true, nullable = false, length = 64)
    private long agency_userId;
    @Column(unique = true, nullable = false, length = 64)
    private String unitName;
    @Column(unique = true, nullable = false, length = 64)
    private int unitCode;
    @Column(unique = true, nullable = false, length = 64)
    private int stateCode;
    @Column(nullable = false)
    private String city;    //town/village
    private String unitHead;
    @Column(unique = true, nullable = false, length = 64)
    private String email;
    @Column(unique = true, length = 64)
    private long phone;
    private String techSupportPerson;
    @Column(nullable = false, length = 64)
    private String category;
    @Column(unique = true, nullable = false, length = 64)
    private String addressId;
    @Column(unique = true, nullable = false, length = 64)
    private String subscriptionId;
    @Column(unique = true, nullable = false, length = 64)
    private String deviceImei;
    @Column(unique = true, nullable = false, length = 64)
    private String geolocationId;
    @Column(nullable = false, length = 64)
    private LocalDate createdOn;    // TODO: delete
    @Transient
    private final IdsGenerator idsGenerator = new IdsGenerator();

    public SecurityAgency() throws NoSuchAlgorithmException {
        this.agencyId = idsGenerator.generateLongId();
        this.createdOn = LocalDate.now();
    }

    public SecurityAgency(long agencyId, String unitName, int unitCode, int stateCode, String unitHead, String email, long phone,
                          String techSupportPerson, String category, String addressId, String subscriptionId, String deviceId,
                          String geolocationId, LocalDate createdOn, String city) {
        this.agencyId = agencyId;
        this.unitName = unitName;
        this.unitCode = unitCode;
        this.stateCode = stateCode;
        this.category = city;
        this.unitHead = unitHead;
        this.email = email;
        this.phone = phone;
        this.techSupportPerson = techSupportPerson;
        this.category = category;
        this.addressId = addressId;
        this.subscriptionId = subscriptionId;
        this.deviceImei = deviceId;
        this.geolocationId = geolocationId;
        this.createdOn = createdOn;
    }

    public long getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(long agencyId) {
        this.agencyId = agencyId;
    }

    public long getAgency_userId() {
        return agency_userId;
    }

    public void setAgency_userId(long agency_userId) {
        this.agency_userId = agency_userId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public int getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(int unitCode) {
        this.unitCode = unitCode;
    }

    public int getStateCode() {
        return stateCode;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    public String getUnitHead() {
        return unitHead;
    }

    public void setUnitHead(String unitHead) {
        this.unitHead = unitHead;
    }

    public String getEmail() {
        return email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public String getTechSupportPerson() {
        return techSupportPerson;
    }

    public void setTechSupportPerson(String techSupportPerson) {
        this.techSupportPerson = techSupportPerson;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getDeviceImei() {
        return deviceImei;
    }

    public void setDeviceImei(String deviceId) {
        this.deviceImei = deviceId;
    }

    public String getGeolocationId() {
        return geolocationId;
    }

    public void setGeolocationId(String geolocationId) {
        this.geolocationId = geolocationId;
    }

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public String toString() {
        return "SecurityAgency{" +
                "agencyId=" + agencyId +
                ", agency_userId=" + agency_userId +
                ", unitName='" + unitName + '\'' +
                ", unitCode=" + unitCode +
                ", stateCode=" + stateCode +
                ", city='" + city + '\'' +
                ", unitHead='" + unitHead + '\'' +
                ", email='" + email + '\'' +
                ", phone=" + phone +
                ", techSupportPerson='" + techSupportPerson + '\'' +
                ", category='" + category + '\'' +
                ", addressId='" + addressId + '\'' +
                ", subscriptionId='" + subscriptionId + '\'' +
                ", deviceImei='" + deviceImei + '\'' +
                ", geolocationId='" + geolocationId + '\'' +
                ", createdOn=" + createdOn +
                '}';
    }
}
