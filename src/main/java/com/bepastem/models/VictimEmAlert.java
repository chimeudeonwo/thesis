package com.bepastem.models;

import com.bepastem.util.IdsGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

@Entity(name="VICTIMEMALERT")
public class VictimEmAlert {
    @Id
    private long emAlertId;
    @Column(nullable = false, length = 64)   // not unique, so that a user can have more than one EmAlert with same victimId
    private long victimId_userId;
    @Column(nullable = false, length = 64)// not unique, so that a user can have more than one EmAlert with same geolocationId
    private String victimGeolocationId;
    private String devicesFound;
    @Column(nullable = false, length = 64)
    private LocalDate localDate;
    @Column(nullable = false, length = 64)
    private String duration;    //TODO: change it to duration
    @Column(nullable = false)
    private String time;
    @Transient
    private final IdsGenerator idsGenerator = new IdsGenerator();

    public VictimEmAlert() throws NoSuchAlgorithmException {
        this.emAlertId = idsGenerator.generateLongId();
        this.localDate = LocalDate.now();
        this.time = String.valueOf(System.currentTimeMillis());
    }

    public VictimEmAlert(long emAlertId, long victimId, String geolocationId, String devicesFound) throws NoSuchAlgorithmException {
        this.emAlertId = idsGenerator.generateLongId();
        this.victimId_userId = victimId;
        this.victimGeolocationId = geolocationId;
        this.devicesFound = devicesFound;
        this.localDate = LocalDate.now();
        this.time = String.valueOf(System.currentTimeMillis());
    }

    public long getEmAlertId() {
        return emAlertId;
    }

    public void setEmAlertId(long emAlertId) {
        this.emAlertId = emAlertId;
    }

    public void setVictimId_userId(long userId) {
        this.victimId_userId = userId;
    }

    public void setDuration(String time) {
        this.duration = time;
    }

    public long getVictimId_userId() {
        return victimId_userId;
    }

    public String getVictimGeolocationId() {
        return victimGeolocationId;
    }

    public void setVictimGeolocationId(String geolocationId) {
        this.victimGeolocationId = geolocationId;
    }

    public String getDevicesFound() {
        return devicesFound;
    }

    public void setDevicesFound(String devicesFound) {
        this.devicesFound = devicesFound;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public String getDuration() {
        return duration;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "VictimEmAlert{" +
                "emAlertId=" + emAlertId +
                ", victimId_userId=" + victimId_userId +
                ", victimGeolocationId='" + victimGeolocationId + '\'' +
                ", devicesFound='" + devicesFound + '\'' +
                ", localDate=" + localDate +
                ", time='" + duration + '\'' +
                '}';
    }
}
