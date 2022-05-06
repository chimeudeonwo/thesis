package com.bepastem.models;

import com.bepastem.util.IdsGenerator;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Address {
    @Id
    private String addressId;
    private long addressId_userId;
    private String streetAndHouseNr;
    private int postcode;
    private String lga;
    private String state;
    private String country;
    @Transient
    private final IdsGenerator idsGenerator = new IdsGenerator();

    public Address() {
        this.addressId = idsGenerator.generateStringId();
    }

    public Address(String streetAndHouseNr, int postcode, String lga, String state, String country) {
        this.addressId = idsGenerator.generateStringId();
        this.streetAndHouseNr = streetAndHouseNr;
        this.postcode = postcode;
        this.lga = lga;
        this.state = state;
        this.country = country;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String id) {
        this.addressId = id;
    }

    public long getAddressId_userId() {
        return addressId_userId;
    }

    public void setAddressId_userId(long addressId_userId) {
        this.addressId_userId = addressId_userId;
    }

    public String getStreetAndHouseNr() {
        return streetAndHouseNr;
    }

    public void setStreetAndHouseNr(String streetAndHouseNr) {
        this.streetAndHouseNr = streetAndHouseNr;
    }

    public int getPostcode() {
        return postcode;
    }

    public void setPostcode(int postcode) {
        this.postcode = postcode;
    }

    public String getLga() {
        return lga;
    }

    public void setLga(String lga) {
        this.lga = lga;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Address{" +
                "addressId='" + addressId + '\'' +
                ", addressId_userId=" + addressId_userId +
                ", streetAndHouseNr='" + streetAndHouseNr + '\'' +
                ", postcode=" + postcode +
                ", lga='" + lga + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
