package com.talentstream.dto;

import javax.validation.constraints.NotBlank;

public class NotificationDto {
	
	@NotBlank(message = "Title is required")
	private String title;
	@NotBlank(message = "Body is required")
	private String body;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	

}
