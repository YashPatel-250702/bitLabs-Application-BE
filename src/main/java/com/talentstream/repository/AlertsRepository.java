package com.talentstream.repository;

//import java.util.List;
//
//import org.springframework.data.jpa.repository.JpaRepository;
// 
//import com.talentstream.entity.Alerts;
// 
//public interface AlertsRepository extends JpaRepository<Alerts, Long>{
// 
//	List<Alerts> findByApplyJob_applyJobIdOrderByChangeDateDesc(long applyjobid);
//}

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.talentstream.dto.AlertsDTO;
import com.talentstream.entity.Alerts;
 
public interface AlertsRepository extends JpaRepository<Alerts, Long>{
 
	//List<Alerts> findByApplyJob_ApplyJobIdOrderByChangeDateDesc(long applyJobId);
 
	List<Alerts> findByApplicantIdOrderByChangeDateDesc(long applicantId);
	
	@Query("SELECT new com.talentstream.dto.AlertsDTO(" +
		       "a.alertsId, a.companyName, a.status, a.changeDate, a.jobTitle, a.seen, aj.applyjobid, j.id) " +
		       "FROM Alerts a " +
		       "JOIN a.applyJob aj " +
		       "JOIN aj.job j " +
		       "WHERE a.applicant.id = :applicantId AND a.status <> 'Visited'" +
		       "ORDER BY a.changeDate DESC")
		List<AlertsDTO> findAlertsByApplicantId(Long applicantId);
}