package com.talentstream.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.persistence.JoinColumn;

@Entity
public class UserFcmTokens {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   

    @NotBlank(message = "Device Name Required")
    private String deviceName;

    @NotBlank(message = "FCM Token Required")
    private String fcmToken;
    
    private Boolean isTokenActive=true;
    
    
    private LocalDateTime createdAt;
    
    

    public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Boolean getIsTokenActive() {
		return isTokenActive;
	}

	public void setIsTokenActive(Boolean isTokenActive) {
		this.isTokenActive = isTokenActive;
	}

	@ManyToOne
    @JoinColumn(name = "applicant_id", nullable = false)
    private Applicant applicant; 

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }
}
