package com.talentstream.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.talentstream.entity.ResumeIntrest;

public class ResumeBuilderDto {



	@NotNull(message = "Personal info is required")
	@Valid
	private ResumePersonalInfoDto resumePersonalInfo;

	@NotNull(message = "Skills info is required")
	@Valid
	private ResumeSkillsDto resumeSkills;

	@Valid
	private List<ResumeExperienceDto> resumeExperiences;

	@NotEmpty(message = "At least one education entry is required")
	@Valid
	private List<ResumeEducationDto> resumeEducations;

	@NotEmpty(message = "At least one project is required")
	@Valid
	private List<ResumeProjectDto> resumeProjects;

	@Valid
	private List<ResumeCertificatesDto> resumeCertificates;

	@NotEmpty(message = "At least one language is required")
	@Valid
	private List<ResumeLanguagesDto> resumeLanguages;

	@Valid
	private ResumeIntrestDto resumeIntrest;

	
	public ResumeIntrestDto getResumeIntrest() {
		return resumeIntrest;
	}

	public void setResumeIntrest(ResumeIntrestDto resumeIntrest) {
		this.resumeIntrest = resumeIntrest;
	}

	

	public ResumePersonalInfoDto getResumePersonalInfo() {
		return resumePersonalInfo;
	}

	public void setResumePersonalInfo(ResumePersonalInfoDto resumePersonalInfo) {
		this.resumePersonalInfo = resumePersonalInfo;
	}

	public ResumeSkillsDto getResumeSkills() {
		return resumeSkills;
	}

	public void setResumeSkills(ResumeSkillsDto resumeSkills) {
		this.resumeSkills = resumeSkills;
	}

	public List<ResumeExperienceDto> getResumeExperiences() {
		return resumeExperiences;
	}

	public void setResumeExperiences(List<ResumeExperienceDto> resumeExperiences) {
		this.resumeExperiences = resumeExperiences;
	}

	public List<ResumeEducationDto> getResumeEducations() {
		return resumeEducations;
	}

	public void setResumeEducations(List<ResumeEducationDto> resumeEducations) {
		this.resumeEducations = resumeEducations;
	}

	public List<ResumeProjectDto> getResumeProjects() {
		return resumeProjects;
	}

	public void setResumeProjects(List<ResumeProjectDto> resumeProjects) {
		this.resumeProjects = resumeProjects;
	}

	public List<ResumeCertificatesDto> getResumeCertificates() {
		return resumeCertificates;
	}

	public void setResumeCertificates(List<ResumeCertificatesDto> resumeCertificates) {
		this.resumeCertificates = resumeCertificates;
	}

	public List<ResumeLanguagesDto> getResumeLanguages() {
		return resumeLanguages;
	}

	public void setResumeLanguages(List<ResumeLanguagesDto> resumeLanguages) {
		this.resumeLanguages = resumeLanguages;
	}

}
