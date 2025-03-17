package com.talentstream.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.talentstream.entity.Applicant;
import com.talentstream.entity.ApplicantTest;
import com.talentstream.repository.ApplicantRepository;
import com.talentstream.repository.ApplicantTestRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicantTestService {
 
	private LocalDateTime testDateTime;
	
	@Autowired
	private ApplicantRepository applicantRepository;
	
    @Autowired
    private ApplicantTestRepository applicantTestRepository;

//    public ApplicantTest saveTest(ApplicantTest test, Long applicantId) {
//        // Fetch the applicant
//        Applicant applicant = applicantRepository.findById(applicantId)
//                .orElseThrow(() -> new RuntimeException("Applicant not found"));
//        
//        // Set the applicant to the test
//        test.setApplicant(applicant);
//        
//        // Check if the test with the same name already exists for this applicant
//        Optional<ApplicantTest> existingTest = applicantTestRepository.findByApplicantIdAndTestName(applicantId, test.getTestName());
//
//        if (existingTest.isPresent()) {
//            // Update the existing test
//            ApplicantTest testToUpdate = existingTest.get();
//            testToUpdate.setTestScore(test.getTestScore());
//            testToUpdate.setTestStatus(test.getTestStatus());
//            testToUpdate.setTestDateTime(LocalDateTime.now());
//            // Save the updated test
//            return applicantTestRepository.save(testToUpdate);
//        } else {
//            // Save the new test
//            return applicantTestRepository.save(test);
//        }
//    }
    public ApplicantTest saveTest(ApplicantTest test, Long applicantId) {
        // Fetch the applicant
        Applicant applicant = applicantRepository.findById(applicantId)
                .orElseThrow(() -> new RuntimeException("Applicant not found"));
 
        // Check last failed test attempt within 7 days
        Optional<ApplicantTest> lastTest = applicantTestRepository.findByApplicantIdAndTestName(applicantId, test.getTestName());
 
        if (lastTest.isPresent()) {
            ApplicantTest previousTest = lastTest.get();
            LocalDateTime lastTestDate = previousTest.getTestDateTime();
            
            // If the last test was failed and within 7 days, restrict retry
            if (previousTest.getTestStatus().equals("F") && lastTestDate.isAfter(LocalDateTime.now().minusDays(7))) {
                throw new RuntimeException("You must wait 7 days before retaking the test.");
            }
 
            // Update existing test
            previousTest.setTestScore(test.getTestScore());
            previousTest.setTestStatus(test.getTestStatus());
            previousTest.setTestDateTime(LocalDateTime.now());
            return applicantTestRepository.save(previousTest);
        }
 
        // Set the applicant and save the new test
        test.setApplicant(applicant);
        return applicantTestRepository.save(test);
    }
    public List<ApplicantTest> getTestsByApplicantId(Long applicantId) {
        return applicantTestRepository.findByApplicantId(applicantId);
    }
    
    public Optional<ApplicantTest> getTestById(Long id) {
        return applicantTestRepository.findById(id);
    }
}
