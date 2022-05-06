package com.bepastem.models;

import com.bepastem.util.IdsGenerator;

import javax.persistence.*;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity(name = "VICTIMEMRESPONSE")
public class VictimEmResponse {
    @Id
    private long emAlertResponseId;
    @Column(nullable = false)
    private long emAlertId_emAlertResponseId;
    @Column(nullable = false)
    private long agencyAlerted_agencyId;
    @Column(nullable = false)
    private LocalDate localDate;
    @Column(nullable = false)
    private String time;
    private String sentStatus;  // status = default, not sent which shall change to sent if successfully sent,
    private String rcvStatus;   // from client sends a response that it is received
    private String currentStatus;
    @Transient
    private final IdsGenerator idsGenerator = new IdsGenerator();

    public VictimEmResponse() throws NoSuchAlgorithmException {
        this.emAlertResponseId = idsGenerator.generateLongId();
        this.localDate = LocalDate.now();
        //this.time = LocalTime.now(ZoneId.of(String.valueOf(ZoneOffset.UTC)));
        this.currentStatus = "new";
    }

    public VictimEmResponse(long emAlertId_emAlertResponse, String sentStatus, String rcvStatus,
                            LocalDate localDate, LocalTime time, long agencyAlerted) throws NoSuchAlgorithmException {
        this.emAlertResponseId = idsGenerator.generateLongId();
        this.emAlertId_emAlertResponseId = emAlertId_emAlertResponse;
        this.sentStatus = sentStatus;
        this.rcvStatus = rcvStatus;
        this.localDate = LocalDate.now();
        this.currentStatus = "new";
        //this.time = LocalTime.now(ZoneId.of(String.valueOf(ZoneOffset.UTC)));
        this.agencyAlerted_agencyId = agencyAlerted;
    }

    public long getEmAlertResponseId() {
        return emAlertResponseId;
    }

    public long getEmAlertId_emAlertResponseId() {
        return emAlertId_emAlertResponseId;
    }

    public void setEmAlertId_emAlertResponseId(long emAlertId_emAlertResponse) {
        this.emAlertId_emAlertResponseId = emAlertId_emAlertResponse;
    }

    public String getSentStatus() {
        return sentStatus;
    }

    public void setSentStatus(String sentStatus) {
        this.sentStatus = sentStatus;
    }

    public String getRcvStatus() {
        return rcvStatus;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public void setRcvStatus(String rcvStatus) {
        this.rcvStatus = rcvStatus;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getAgencyAlerted_agencyId() {
        return agencyAlerted_agencyId;
    }

    public void setAgencyAlerted_agencyId(long agencyAlerted) {
        this.agencyAlerted_agencyId = agencyAlerted;
    }

    @Override
    public String toString() {
        return "VictimEmResponse{" +
                "emAlertResponseId=" + emAlertResponseId +
                ", emAlertId_emAlertResponse=" + emAlertId_emAlertResponseId +
                ", sentStatus='" + sentStatus + '\'' +
                ", rcvStatus='" + rcvStatus + '\'' +
                ", currentStatus='" + currentStatus + '\'' +
                ", localDate=" + localDate +
                ", time=" + time +
                ", agencyAlerted_agencyId=" + agencyAlerted_agencyId +
                '}';
    }
}
