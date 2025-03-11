package com.talentstream.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.talentstream.entity.ResumeExperience;


public interface ResumeExperienceRepository extends JpaRepository<ResumeExperience, Integer> {

//	  List<Experience> findByApplicant_Id(Long id);
//	  Optional<Experience> findByIdAndApplicant_Id(Integer experienceId, Long applicantId);
//	  boolean existsByApplicant_Id(Long applicantId);
//	  void deleteByApplicant_Id(Long applicantId);
}
