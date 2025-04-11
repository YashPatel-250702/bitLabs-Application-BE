package com.talentstream.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.talentstream.dto.ApplicantNotificationDTO;
import com.talentstream.entity.Applicant;

@Repository
public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
	 @Query("SELECT new com.talentstream.dto.ApplicantNotificationDTO(a.id, a.email) FROM Applicant a")
	    List<ApplicantNotificationDTO> findAllApplicantIdAndEmails();

}
