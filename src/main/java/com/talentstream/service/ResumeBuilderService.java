package com.talentstream.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.talentstream.dto.ResumeBuilderDto;
import com.talentstream.dto.ResumeCertificatesDto;
import com.talentstream.dto.ResumeEducationDto;
import com.talentstream.dto.ResumeExperienceDto;
import com.talentstream.dto.ResumeIntrestDto;
import com.talentstream.dto.ResumeLanguagesDto;
import com.talentstream.dto.ResumePersonalInfoDto;
import com.talentstream.dto.ResumeProjectDto;
import com.talentstream.dto.ResumeSkillsDto;
import com.talentstream.entity.Applicant;
import com.talentstream.entity.ResumeBuilder;
import com.talentstream.entity.ResumeCertificates;
import com.talentstream.entity.ResumeEducation;
import com.talentstream.entity.ResumeExperience;
import com.talentstream.entity.ResumeIntrest;
import com.talentstream.entity.ResumeLanguages;
import com.talentstream.entity.ResumePersonalInfo;
import com.talentstream.entity.ResumeProject;
import com.talentstream.entity.ResumeTechnicalSkills;
import com.talentstream.repository.ApplicantRepository;
import com.talentstream.repository.ResumeBuilderRepository;
import com.talentstream.repository.ResumeCertificatesRepository;
import com.talentstream.repository.ResumeEducationRepository;
import com.talentstream.repository.ResumeExperienceRepository;
import com.talentstream.repository.ResumeIntrestReposiotry;
import com.talentstream.repository.ResumeLanguagesRepository;
import com.talentstream.repository.ResumePersonalInfoRepository;
import com.talentstream.repository.ResumeProjectRepository;
import com.talentstream.repository.ResumeSkillsRepository;

@Service
public class ResumeBuilderService {

    

    @Autowired
    private ApplicantRepository applicantRepository;

    @Autowired
    private ResumeBuilderRepository resumeBuilderRepository;

    @Autowired
    private ResumePersonalInfoRepository resumePersonalInfoRepository;

    @Autowired
    private ResumeExperienceRepository resumeExperienceRepository;

    @Autowired
    private ResumeEducationRepository resumeEducationRepository;

    @Autowired
    private ResumeProjectRepository resumeProjectRepository;

    @Autowired
    private ResumeCertificatesRepository resumeCertificatesRepository;

    @Autowired
    private ResumeSkillsRepository resumeSkillsRepository;

    @Autowired
    private ResumeLanguagesRepository resumeLanguagesRepository;
    
    @Autowired
    private ResumeIntrestReposiotry resumeIntrestReposiotry;

    
    //mehtod saving resume builder details
    public ResumeBuilder createResume(ResumeBuilderDto resumeBuilderDto, Long applicantId) {

        // Fetch applicant details
        Applicant applicant = applicantRepository.findById(applicantId)
                .orElseThrow(() -> new RuntimeException("Applicant not found with id: " + applicantId));

        Optional<ResumeBuilder> resumeBuilder2 = resumeBuilderRepository.findByApplicantId(applicantId);
        
        if(resumeBuilder2.isPresent()) {
        	throw new RuntimeException("Resume already exist for aplicant : "+applicantId);
        }
        
        ResumeBuilder resumeBuilder = new ResumeBuilder();
        

        resumeBuilder.setApplicant(applicant);

        // Saving personal info
        if (resumeBuilderDto.getResumePersonalInfo() != null) {
            ResumePersonalInfo resumePersonalInfo = convertToPersonalInfoEntity(
                    resumeBuilderDto.getResumePersonalInfo());
            resumePersonalInfoRepository.save(resumePersonalInfo);
            resumeBuilder.setResumePersonalInfo(resumePersonalInfo);
        }

        // Saving education details using saveAll()
        if (resumeBuilderDto.getResumeEducations() != null && !resumeBuilderDto.getResumeEducations().isEmpty()) {
            List<ResumeEducation> resumeEducations = convertToEducationEntities(resumeBuilderDto.getResumeEducations());
            resumeEducationRepository.saveAll(resumeEducations);
            resumeBuilder.setResumeEducations(resumeEducations);
        }

        // Saving experience details using saveAll()
        if (resumeBuilderDto.getResumeExperiences() != null && !resumeBuilderDto.getResumeExperiences().isEmpty()) {
            List<ResumeExperience> resumeExperiences = convertToExperienceEntities(
                    resumeBuilderDto.getResumeExperiences());
            resumeExperienceRepository.saveAll(resumeExperiences);
            resumeBuilder.setResumeExperiences(resumeExperiences);
        }

        // Saving certificates using saveAll()
        if (resumeBuilderDto.getResumeCertificates() != null && !resumeBuilderDto.getResumeCertificates().isEmpty()) {
            List<ResumeCertificates> resumeCertificates = convertToCertificatesEntities(
                    resumeBuilderDto.getResumeCertificates());
            resumeCertificatesRepository.saveAll(resumeCertificates);
            resumeBuilder.setResumeCertificates(resumeCertificates);
        }

        // Saving skills using saveAll()
        if (resumeBuilderDto.getResumeSkills() != null
                && resumeBuilderDto.getResumeSkills().getTechnicalSkills() != null) {
            List<ResumeTechnicalSkills> resumeSkills = convertToSkillsEntities(
                    resumeBuilderDto.getResumeSkills());
            resumeSkillsRepository.saveAll(resumeSkills);
            resumeBuilder.setResumeTechnicalSkills(resumeSkills);
        }

        // Saving projects using saveAll()
        if (resumeBuilderDto.getResumeProjects() != null && !resumeBuilderDto.getResumeProjects().isEmpty()) {
            List<ResumeProject> resumeProjects = convertToProjectEntities(resumeBuilderDto.getResumeProjects());
            resumeProjectRepository.saveAll(resumeProjects);
            resumeBuilder.setResumeProjects(resumeProjects);
        }

        // Saving languages using saveAll()
        if (resumeBuilderDto.getResumeLanguages() != null && !resumeBuilderDto.getResumeLanguages().isEmpty()) {
            List<ResumeLanguages> resumeLanguages = convertToLanguagesEntities(resumeBuilderDto.getResumeLanguages());
            resumeLanguagesRepository.saveAll(resumeLanguages);
            resumeBuilder.setResumeLanguages(resumeLanguages);
        }
        
        if(resumeBuilderDto.getResumeIntrest()!=null && resumeBuilderDto.getResumeIntrest().getIntrests() != null) {
			 List<ResumeIntrest> intrests = convertToIntrestEntities(resumeBuilderDto.getResumeIntrest());
			 resumeIntrestReposiotry.saveAll(intrests);
			 resumeBuilder.setResumeIntrests(intrests);
			 
		}

        // Save the final ResumeBuilder entity
         ResumeBuilder save = resumeBuilderRepository.save(resumeBuilder);
         applicant.setResumeId(String.valueOf(save.getResumeId()));
         applicantRepository.save(applicant);
		return save;
        
    }

 // Method to get resume and return ResumeBuilderDto
    public ResumeBuilderDto getResume(Long applicantId) {
        ResumeBuilder resumeBuilder = resumeBuilderRepository.findByApplicantId(applicantId)
                .orElseThrow(() -> new RuntimeException("Resume not found for Applicant id: " + applicantId));
        
       
        return convertToResumeBuilderDto(resumeBuilder);
    }

    
    //method to update resume builder details
    @Transactional
    public ResumeBuilder updateResume(Long applicantId, ResumeBuilderDto resumeDto) {
        
        ResumeBuilder existingResume = resumeBuilderRepository.findByApplicantId(applicantId)
                .orElseThrow(() -> new RuntimeException("Resume not found for applicant ID: " + applicantId));

        boolean isUpdated = false;

        
        if (resumeDto.getResumePersonalInfo() != null) {
            ResumePersonalInfo updatedPersonalInfo = convertToPersonalInfoEntity(resumeDto.getResumePersonalInfo());
            if (!updatedPersonalInfo.equals(existingResume.getResumePersonalInfo())) {
                existingResume.setResumePersonalInfo(updatedPersonalInfo);
                isUpdated = true;
            }
        }

       
        if (resumeDto.getResumeSkills() != null && resumeDto.getResumeSkills().getTechnicalSkills() != null) {
            List<ResumeTechnicalSkills> updatedSkills = convertToSkillsEntities(resumeDto.getResumeSkills());
            existingResume.getResumeTechnicalSkills().clear();
            existingResume.getResumeTechnicalSkills().addAll(updatedSkills);
            isUpdated = true;
        }

       
        if (resumeDto.getResumeEducations() != null && !resumeDto.getResumeEducations().isEmpty()) {
            List<ResumeEducation> updatedEducation = convertToEducationEntities(resumeDto.getResumeEducations());
            existingResume.getResumeEducations().clear();
            existingResume.getResumeEducations().addAll(updatedEducation);
            isUpdated = true;
        }

        
        if (resumeDto.getResumeExperiences() != null) {
            if (!resumeDto.getResumeExperiences().isEmpty()) {
                List<ResumeExperience> updatedExperiences = convertToExperienceEntities(resumeDto.getResumeExperiences());
                existingResume.getResumeExperiences().clear();
                existingResume.getResumeExperiences().addAll(updatedExperiences);
            } else {
                existingResume.getResumeExperiences().clear(); 
            }
            isUpdated = true;
        }

       
        if (resumeDto.getResumeProjects() != null && !resumeDto.getResumeProjects().isEmpty()) {
            List<ResumeProject> updatedProjects = convertToProjectEntities(resumeDto.getResumeProjects());
            existingResume.getResumeProjects().clear();
            existingResume.getResumeProjects().addAll(updatedProjects);
            isUpdated = true;
        }

      
        if (resumeDto.getResumeCertificates() != null) {
            if (!resumeDto.getResumeCertificates().isEmpty()) {
                List<ResumeCertificates> updatedCertificates = convertToCertificatesEntities(resumeDto.getResumeCertificates());
                existingResume.getResumeCertificates().clear();
                existingResume.getResumeCertificates().addAll(updatedCertificates);
            } else {
                existingResume.getResumeCertificates().clear(); 
            }
            isUpdated = true;
        }

      
        if (resumeDto.getResumeLanguages() != null && !resumeDto.getResumeLanguages().isEmpty()) {
            List<ResumeLanguages> updatedLanguages = convertToLanguagesEntities(resumeDto.getResumeLanguages());
            existingResume.getResumeLanguages().clear();
            existingResume.getResumeLanguages().addAll(updatedLanguages);
            isUpdated = true;
        }

        if (resumeDto.getResumeIntrest() != null) {
            if (resumeDto.getResumeIntrest().getIntrests() != null && !resumeDto.getResumeIntrest().getIntrests().isEmpty()) {
                List<ResumeIntrest> updatedInterests = convertToIntrestEntities(resumeDto.getResumeIntrest());
                existingResume.getResumeIntrests().clear();
                existingResume.getResumeIntrests().addAll(updatedInterests);
            } else {
                existingResume.getResumeIntrests().clear(); 
            }
            isUpdated = true;
        }

        // Save only if any updates were made
        return isUpdated ? resumeBuilderRepository.save(existingResume) : existingResume;
    }
    
    
    // Conversion method for personal info dto to entity
    private ResumePersonalInfo convertToPersonalInfoEntity(ResumePersonalInfoDto dto) {
        ResumePersonalInfo personalInfo = new ResumePersonalInfo();
        personalInfo.setFullName(dto.getFullName());
        personalInfo.setEmail(dto.getEmail());
        personalInfo.setPhoneNo(dto.getPhoneNo());
        personalInfo.setAddress(dto.getAddress());
        personalInfo.setLinkedin(dto.getLinkedin());
        personalInfo.setGithub(dto.getGithub());
        personalInfo.setWebsite(dto.getWebsite());
        personalInfo.setRole(dto.getRole());
        personalInfo.setSummary(dto.getSummary());
        return personalInfo;
    }

    // conversion method for education dto to entity
    private List<ResumeEducation> convertToEducationEntities(List<ResumeEducationDto> dtos) {
        return dtos.stream().map(dto -> {
            ResumeEducation resumeEducation = new ResumeEducation();
            resumeEducation.setCollege(dto.getCollege());
            resumeEducation.setStandard(dto.getStandard());
            resumeEducation.setStartYear(dto.getStartYear());
            resumeEducation.setEndYear(dto.getEndYear());
            resumeEducation.setCgpa(dto.getCgpa());
            return resumeEducation;
        }).collect(Collectors.toList());
    }

    // conversion method for experience dto to entity
    private List<ResumeExperience> convertToExperienceEntities(List<ResumeExperienceDto> dtos) {
        return dtos.stream().map(dto -> {
            ResumeExperience resumeExperience = new ResumeExperience();
            resumeExperience.setCompany(dto.getCompany());
            resumeExperience.setJobTitle(dto.getJobTitle());
            resumeExperience.setStartDate(dto.getStartDate());
            resumeExperience.setEndDate(dto.getEndDate());
            resumeExperience.setDescription(dto.getDescription());
            return resumeExperience;
        }).collect(Collectors.toList());
    }

    // conversion method for certificates dto to entity
    private List<ResumeCertificates> convertToCertificatesEntities(List<ResumeCertificatesDto> dtos) {
        return dtos.stream().map(dto -> {
            ResumeCertificates resumeCertificates = new ResumeCertificates();
            resumeCertificates.setTitle(dto.getTitle());
            resumeCertificates.setIssuedBy(dto.getIssuedBy());
            resumeCertificates.setYear(dto.getYear());
            return resumeCertificates;
        }).collect(Collectors.toList());
    }

    // conversion method for skills dto to entity
    private List<ResumeTechnicalSkills> convertToSkillsEntities(ResumeSkillsDto dto) {
        return dto.getTechnicalSkills().stream().map(skill -> {
            ResumeTechnicalSkills resumeTechnicalSkill = new ResumeTechnicalSkills();
            resumeTechnicalSkill.setTechnicalSkills(skill);
            return resumeTechnicalSkill;
        }).collect(Collectors.toList());
    }

    // conversion method for projects dto to entity
    private List<ResumeProject> convertToProjectEntities(List<ResumeProjectDto> projectDtos) {
        return projectDtos.stream().map(dto -> {
            ResumeProject project = new ResumeProject();
            project.setTitle(dto.getTitle());
            project.setDescription(dto.getDescription());
            project.setStartDate(dto.getStartDate());
            project.setEndDate(dto.getEndDate());
            project.setLink(dto.getLink());
            return project;
        }).collect(Collectors.toList());
    }

    // conversion method for languages dto to entity
    private List<ResumeLanguages> convertToLanguagesEntities(List<ResumeLanguagesDto> dtos) {
        return dtos.stream().map(dto -> {
            ResumeLanguages resumeLanguages = new ResumeLanguages();
            resumeLanguages.setLanguageName(dto.getLanguageName());
            return resumeLanguages;
        }).collect(Collectors.toList());
    }
    
    private List<ResumeIntrest> convertToIntrestEntities(ResumeIntrestDto dto) {
        return dto.getIntrests().stream().map(intrest -> {
            ResumeIntrest resumeIntrest=new ResumeIntrest();
            resumeIntrest.setIntrest(intrest);
            return resumeIntrest;
            
        }).collect(Collectors.toList());
        
    }
    
 // Convert ResumeBuilder entity to ResumeBuilderDto 
    private ResumeBuilderDto convertToResumeBuilderDto(ResumeBuilder entity) {
        ResumeBuilderDto dto = new ResumeBuilderDto();
        
        // Convert each entity to DTO
        dto.setResumePersonalInfo(convertToPersonalInfoDto(entity.getResumePersonalInfo()));
        dto.setResumeSkills(convertToSkillsDto(entity.getResumeTechnicalSkills()));
        dto.setResumeExperiences(convertToExperienceDtos(entity.getResumeExperiences()));
        dto.setResumeEducations(convertToEducationDtos(entity.getResumeEducations()));
        dto.setResumeProjects(convertToProjectDtos(entity.getResumeProjects()));
        dto.setResumeCertificates(convertToCertificatesDtos(entity.getResumeCertificates()));
        dto.setResumeLanguages(convertToLanguagesDtos(entity.getResumeLanguages()));
        dto.setResumeIntrest(convertToIntrestDto(entity.getResumeIntrests()));
        
        return dto;
    }
    
    
 // Conversion method for personal info entity to DTO
    private ResumePersonalInfoDto convertToPersonalInfoDto(ResumePersonalInfo entity) {
        ResumePersonalInfoDto dto = new ResumePersonalInfoDto();
        dto.setFullName(entity.getFullName());
        dto.setEmail(entity.getEmail());
        dto.setPhoneNo(entity.getPhoneNo());
        dto.setAddress(entity.getAddress());
        dto.setLinkedin(entity.getLinkedin());
        dto.setGithub(entity.getGithub());
        dto.setWebsite(entity.getWebsite());
        dto.setRole(entity.getRole());
        dto.setSummary(entity.getSummary());
        return dto;
    }

    // Conversion method for education to DTO
    private List<ResumeEducationDto> convertToEducationDtos(List<ResumeEducation> entities) {
        return entities.stream().map(entity -> {
            ResumeEducationDto dto = new ResumeEducationDto();
            dto.setCollege(entity.getCollege());
            dto.setStandard(entity.getStandard());
            dto.setStartYear(entity.getStartYear());
            dto.setEndYear(entity.getEndYear());
            dto.setCgpa(entity.getCgpa());
            return dto;
        }).collect(Collectors.toList());
    }

    // Conversion method for experience entity to DTO
    private List<ResumeExperienceDto> convertToExperienceDtos(List<ResumeExperience> entities) {
        return entities.stream().map(entity -> {
            ResumeExperienceDto dto = new ResumeExperienceDto();
            dto.setCompany(entity.getCompany());
            dto.setJobTitle(entity.getJobTitle());
            dto.setStartDate(entity.getStartDate());
            dto.setEndDate(entity.getEndDate());
            dto.setDescription(entity.getDescription());
            return dto;
        }).collect(Collectors.toList());
    }

    // Conversion method for certificates entity to DTO
    private List<ResumeCertificatesDto> convertToCertificatesDtos(List<ResumeCertificates> entities) {
        return entities.stream().map(entity -> {
            ResumeCertificatesDto dto = new ResumeCertificatesDto();
            dto.setTitle(entity.getTitle());
            dto.setIssuedBy(entity.getIssuedBy());
            dto.setYear(entity.getYear());
            return dto;
        }).collect(Collectors.toList());
    }

    // Conversion method for skills entity to DTO
    private ResumeSkillsDto convertToSkillsDto(List<ResumeTechnicalSkills> entities) {
        ResumeSkillsDto dto = new ResumeSkillsDto();
        dto.setTechnicalSkills(entities.stream()
                .map(ResumeTechnicalSkills::getTechnicalSkills)
                .collect(Collectors.toList()));
        return dto;
    }

    // Conversion method for projects entity to DTO
    private List<ResumeProjectDto> convertToProjectDtos(List<ResumeProject> entities) {
        return entities.stream().map(entity -> {
            ResumeProjectDto dto = new ResumeProjectDto();
            dto.setTitle(entity.getTitle());
            dto.setDescription(entity.getDescription());
            dto.setStartDate(entity.getStartDate());
            dto.setEndDate(entity.getEndDate());
            dto.setLink(entity.getLink());
            return dto;
        }).collect(Collectors.toList());
    }

    // Conversion method for languages entity to DTO
    private List<ResumeLanguagesDto> convertToLanguagesDtos(List<ResumeLanguages> entities) {
        return entities.stream().map(entity -> {
            ResumeLanguagesDto dto = new ResumeLanguagesDto();
            dto.setLanguageName(entity.getLanguageName());
            return dto;
        }).collect(Collectors.toList());
    }

    // Conversion method for interests entity to DTO
    private ResumeIntrestDto convertToIntrestDto(List<ResumeIntrest> entities) {
        ResumeIntrestDto dto = new ResumeIntrestDto();
        dto.setInterests(entities.stream()
                .map(ResumeIntrest::getIntrest)
                .collect(Collectors.toList()));
        return dto;
    }

    
}
