package com.talentstream.dto;

public class ApplicantNotificationDTO {

	private Long id;
	private String email;
	
	
	
	public ApplicantNotificationDTO() {
		super();
	}


	public ApplicantNotificationDTO(Long id, String email) {
		super();
		this.id = id;
		this.email = email;
	}
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
