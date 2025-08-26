package com.talentstream.dto;

import java.time.LocalDateTime;

public class AlertsDTO {
	
	private Long alertsId;
	private String companyName;
	private String status;
	private LocalDateTime changeDate;
	private String jobTitle;
	private boolean seen;
	private Long applyjobid;
	private Long jobId;
	
	public AlertsDTO(Long alertsId, String companyName, String status, LocalDateTime changeDate, String jobTitle,
			boolean seen, Long applyjobid, Long jobId) {
		super();
		this.alertsId = alertsId;
		this.companyName = companyName;
		this.status = status;
		this.changeDate = changeDate;
		this.jobTitle = jobTitle;
		this.seen = seen;
		this.applyjobid = applyjobid;
		this.jobId = jobId;
	}

	public Long getAlertsId() {
		return alertsId;
	}

	public void setAlertsId(Long alertsId) {
		this.alertsId = alertsId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(LocalDateTime changeDate) {
		this.changeDate = changeDate;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public boolean isSeen() {
		return seen;
	}

	public void setSeen(boolean seen) {
		this.seen = seen;
	}

	public Long getApplyjobid() {
		return applyjobid;
	}

	public void setApplyjobid(Long applyjobid) {
		this.applyjobid = applyjobid;
	}

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}
	
	
	
	

}
