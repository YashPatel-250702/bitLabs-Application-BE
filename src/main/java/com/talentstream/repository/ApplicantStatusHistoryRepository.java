package com.talentstream.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.talentstream.entity.ApplicantStatusHistory;


public interface ApplicantStatusHistoryRepository extends JpaRepository<ApplicantStatusHistory, Long> {
    List<ApplicantStatusHistory> findByApplyJob_ApplyjobidOrderByChangeDateDesc(long applyjobid);
    
    @Transactional
    @Modifying
    @Query("DELETE FROM ApplicantStatusHistory h WHERE h.applyJob.applyjobid = :applyJobId AND h.status IN :statusesToDelete")
    void deleteByApplyJobIdAndStatuses(Long applyJobId, List<String> statusesToDelete);
}


