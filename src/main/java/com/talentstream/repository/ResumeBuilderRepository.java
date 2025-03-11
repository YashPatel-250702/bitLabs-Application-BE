package com.talentstream.repository;

import com.talentstream.entity.ResumeBuilder;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeBuilderRepository extends JpaRepository<ResumeBuilder, Long> {

    @Query(value = "SELECT rb.* FROM resume_builder rb WHERE rb.applicant_id = :applicantId", nativeQuery = true)
    Optional<ResumeBuilder> findByApplicantId(Long applicantId);
      
}
