package com.bepastem.models;

import com.bepastem.util.IdsGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Entity(name="GEOLOCATION")
public class Geolocation {
    @Id
    private String geolocationId;
    @Column(nullable = false)
    private long geolocationId_userId;
    @Column(nullable = false)
    private String user_IMEI;
    @Column(nullable = false)
    private String city;    // not cityName
    private String country;
    private String latitude;
    private String longitude;
    @Column(nullable = false)
    private LocalDate localDate;
    private String time;
    @Transient
    private final IdsGenerator idsGenerator = new IdsGenerator();

    public Geolocation() {
        this.geolocationId = idsGenerator.generateStringId();
        this.localDate = LocalDate.now();
        this.time = String.valueOf(LocalTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)));
    }

    public Geolocation(long geolocationId_userId, String userI_IMEI, String city, String latitude,
                       String longitude, String country) {
        this.geolocationId = idsGenerator.generateStringId();
        this.geolocationId_userId = geolocationId_userId;
        this.user_IMEI = userI_IMEI;
        this.city = city;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.localDate = LocalDate.now();
        this.time = String.valueOf(LocalTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)));
    }

    public String getGeolocationId() {
        return geolocationId;
    }

    public long getGeolocationId_userId() {
        return geolocationId_userId;
    }

    public void setGeolocationId_userId(long geolocationId_userId) {
        this.geolocationId_userId = geolocationId_userId;
    }

    public String getUser_IMEI() {
        return user_IMEI;
    }

    public void setUser_IMEI(String userIpAddress) {
        this.user_IMEI = userIpAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String localTime) {
        this.time = localTime;
    }

    @Override
    public String toString() {
        return "Geolocation{" +
                "geolocationId='" + geolocationId + '\'' +
                ", geolocationId_userId=" + geolocationId_userId +
                ", user_IMEI='" + user_IMEI + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", localDate=" + localDate +
                ", time='" + time + '\'' +
                '}';
    }
}
