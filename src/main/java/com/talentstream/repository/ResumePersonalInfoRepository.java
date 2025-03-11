package com.talentstream.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.talentstream.entity.ResumePersonalInfo;


public interface ResumePersonalInfoRepository extends JpaRepository<ResumePersonalInfo, Integer> {


//	Optional<PersonalInfo> findByApplicant(Applicant applicant);
//	 Optional<PersonalInfo> findByApplicant_Id(Long applicantId);
//	  boolean existsByApplicant_Id(Long applicantId);
//	  void deleteByApplicant_Id(Long applicantId);
	  
}
