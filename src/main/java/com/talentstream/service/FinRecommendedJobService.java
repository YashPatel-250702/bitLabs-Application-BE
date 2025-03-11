package com.talentstream.service;

import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.talentstream.dto.JobDTO;
import com.talentstream.dto.RecuriterSkillsDTO;
import com.talentstream.entity.Applicant;
import com.talentstream.entity.ApplicantProfile;
import com.talentstream.entity.ApplicantSkills;
import com.talentstream.entity.Job;
import com.talentstream.entity.RecuriterSkills;
import com.talentstream.repository.ApplicantProfileRepository;
import com.talentstream.repository.ApplyJobRepository;
import com.talentstream.repository.JobRepository;
import com.talentstream.exception.CustomException;
import com.talentstream.repository.RegisterRepository;
import com.talentstream.repository.SavedJobRepository;

@Service
public class FinRecommendedJobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ApplicantProfileRepository applicantRepository;

    @Autowired
    private RegisterRepository registerRepository;

    @Autowired
    private JobService jobService;

    @Autowired
    private RegisterRepository applicantRepository1;

    @Autowired
    private ApplyJobRepository applyJobRepository;

    @Autowired
    private SavedJobRepository savedJobRepository;

    private static final Logger logger = LoggerFactory.getLogger(FinRecommendedJobService.class);

    // Finds active jobs that match the skills of the applicant identified by
    // applicantId.
    public List<Job> findJobsMatchingApplicantSkills(long applicantId) {
        try {
            ApplicantProfile applicantProfile = applicantRepository.findByApplicantId(applicantId);
            Applicant applicant = registerRepository.findById(applicantId);

            if (applicantProfile == null) {
                return Collections.emptyList();
            }

            Set<ApplicantSkills> applicantSkills = applicantProfile.getSkillsRequired();
            Set<String> lowercaseApplicantSkillNames = applicantSkills.stream()
                    .map(skill -> skill.getSkillName().toLowerCase())
                    .collect(Collectors.toSet());

            List<Job> matchingJobs = jobRepository
                    .findBySkillsRequiredIgnoreCaseAndSkillNameIn(lowercaseApplicantSkillNames);

            matchingJobs = matchingJobs.stream()
                    .filter(job -> job.getStatus().equalsIgnoreCase("active"))
                    .collect(Collectors.toList());

            return matchingJobs;
        } catch (Exception e) {
            throw new CustomException("Error while finding recommended jobs", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Counts the number of recommended jobs for the applicant based on their skills
    // and promotion status.

    // Finds jobs that match the applicant's profile based on skills, preferred
    // locations, experience, and specialization.
    public long countRecommendedJobsForApplicant(long applicantId) {
        try {
            Optional<ApplicantProfile> optionalApplicant = applicantRepository.findByApplicantIdWithSkills(applicantId);
            if (optionalApplicant.isEmpty()) {
                return jobService.getJobsByPromoteState(applicantId, "yes").size();
            }

            ApplicantProfile applicant = optionalApplicant.get();
            Set<String> skillNames = applicant.getSkillsRequired().stream()
                    .map(skill -> skill.getSkillName().toLowerCase())
                    .collect(Collectors.toSet());

            Set<String> preferredLocations = applicant.getPreferredJobLocations();
            Integer experience = null;
            try {
                experience = Integer.parseInt(applicant.getExperience());
            } catch (NumberFormatException e) {
                System.out.println("Warning: Unable to parse experience as Integer");
            }

            String specialization = applicant.getSpecialization();

            // get Job IDs from Query
            List<Long> jobIds = jobRepository.findMatchingJobIds(skillNames, preferredLocations, experience,
                    specialization);

            if (jobIds.isEmpty())
                return 0;

            // fetch Applied & Saved Job IDs
            Set<Long> appliedJobIds = applyJobRepository.findJobIdsByApplicantId(applicantId);
            Set<Long> savedJobIds = savedJobRepository.findJobIdsByApplicantId(applicantId);

            // filter Applied/Saved Jobs
            long recommendedJobCount = jobIds.stream()
                    .filter(jobId -> !appliedJobIds.contains(jobId) && !savedJobIds.contains(jobId))
                    .count();

            return recommendedJobCount;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("Error while counting recommended jobs", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Finds active jobs that match the skills of the applicant identified by
    // applicantId.
    public List<JobDTO> recommendJobsForApplicant(long applicantId, int page, int size) {
        ApplicantProfile applicantProfile = applicantRepository.findByApplicantId(applicantId);

        if (applicantProfile == null) {
            throw new CustomException("Applicant profile not found", HttpStatus.NOT_FOUND);
        }

        Set<String> skillNames = applicantProfile.getSkillsRequired().stream()
                .map(skill -> skill.getSkillName().toLowerCase())
                .collect(Collectors.toSet());

        Set<String> preferredLocations = applicantProfile.getPreferredJobLocations();
        Integer experience = null;
        try {
            experience = Integer.parseInt(applicantProfile.getExperience());
        } catch (NumberFormatException e) {
            logger.warn("Warning: Unable to parse experience as Integer");
        }

        Page<Job> jobPage = jobRepository.findJobsMatchingApplicantProfile(
                applicantId,
                skillNames,
                preferredLocations,
                experience,
                applicantProfile.getSpecialization(),
                PageRequest.of(page, size));

        return jobPage.getContent().stream().parallel()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());

    }

    private JobDTO convertEntityToDTO(Job job) {
        JobDTO jobDTO = new JobDTO();
        jobDTO.setId(job.getId());
        jobDTO.setJobTitle(job.getJobTitle());
        jobDTO.setMinimumExperience(job.getMinimumExperience());
        jobDTO.setMaximumExperience(job.getMaximumExperience());
        jobDTO.setMinSalary(job.getMinSalary());
        jobDTO.setMaxSalary(job.getMaxSalary());
        jobDTO.setLocation(job.getLocation());
        jobDTO.setEmployeeType(job.getEmployeeType());
        jobDTO.setIndustryType(job.getIndustryType());
        jobDTO.setMinimumQualification(job.getMinimumQualification());
        jobDTO.setRecruiterId(job.getJobRecruiter().getRecruiterId());
        jobDTO.setCompanyname(job.getJobRecruiter().getCompanyname());
        jobDTO.setEmail(job.getJobRecruiter().getEmail());
        jobDTO.setMobilenumber(job.getJobRecruiter().getMobilenumber());
        jobDTO.setSpecialization(job.getSpecialization());
        jobDTO.setDescription(job.getDescription());
        jobDTO.setCreationDate(job.getCreationDate());
        jobDTO.setIsSaved(job.getIsSaved());

        Set<RecuriterSkillsDTO> skillsDTOList = job.getSkillsRequired().stream()
                .map(this::convertSkillsEntityToDTO)
                .collect(Collectors.toSet());
        jobDTO.setSkillsRequired(skillsDTOList);
        return jobDTO;
    }

    private RecuriterSkillsDTO convertSkillsEntityToDTO(RecuriterSkills skill) {
        RecuriterSkillsDTO skillDTO = new RecuriterSkillsDTO();
        skillDTO.setSkillName(skill.getSkillName());
        return skillDTO;
    }

    // Checks if a job is saved by a specific applicant based on job ID and
    // applicant ID.
    private boolean isJobSavedByApplicant(long jobId, long applicantId) {
        return savedJobRepository.existsByApplicantIdAndJobId(applicantId, jobId);
    }
}
