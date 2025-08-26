package com.talentstream.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.talentstream.entity.Applicant;
import com.talentstream.entity.ApplicantProfile;
import com.talentstream.entity.Job;
import com.talentstream.exception.CustomException;
import com.talentstream.repository.ApplicantProfileRepository;
import com.talentstream.repository.ApplicantRepository;
import com.talentstream.repository.JobRepository;

@Service
public class SearchForaJobService {
	@Autowired
	private ApplicantRepository applicantRepository;

	@Autowired
	private JobRepository jobRepository;

	public Page<Job> searchJobsBySkillAndApplicant(Long applicantId, String skillName, Pageable pageable) {
    	 try {
             Optional<Applicant> applicantOptional = applicantRepository.findById(applicantId);
             if (applicantOptional.isPresent()) {
                 return jobRepository.findJobsBySkillName(skillName, pageable);
             }
                 throw new CustomException("Applicant not found",HttpStatus.INTERNAL_SERVER_ERROR);
             
         } catch (Exception e) {
        	 System.out.println(e.getMessage());
             throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
         
         }
    }
}
