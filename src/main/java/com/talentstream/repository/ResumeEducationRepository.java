package com.talentstream.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.talentstream.entity.ResumeEducation;

public interface ResumeEducationRepository extends JpaRepository<ResumeEducation, Integer> {
	
}
