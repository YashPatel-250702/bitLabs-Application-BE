package com.talentstream.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.talentstream.entity.Applicant;
import com.talentstream.entity.UserFcmTokens;


public interface UserFcmTokensRepository extends JpaRepository<UserFcmTokens, Long> {
    
	 List<UserFcmTokens> findByApplicant_IdAndIsTokenActiveTrue(Long applicantId);
	 List<UserFcmTokens> findByApplicant_Id(Long applicantId);
}
