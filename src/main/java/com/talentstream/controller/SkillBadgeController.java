package com.talentstream.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.talentstream.dto.ApplicantSkillBadgeDTO;
import com.talentstream.dto.ApplicantSkillBadgeRequestDTO;
import com.talentstream.entity.ApplicantSkillBadge;
import com.talentstream.exception.CustomException;
import com.talentstream.service.SkillBadgeService;

@RestController
@RequestMapping("/skill-badges")
public class SkillBadgeController {

    @Autowired
    private SkillBadgeService skillBadgeService;
    
    @PostMapping("/save")
    public ResponseEntity<String> saveSkillBadge(@RequestBody ApplicantSkillBadgeRequestDTO request) {
    	ResponseEntity<String> result = skillBadgeService.saveApplicantSkillBadge(
            request.getApplicantId(),
            request.getSkillBadgeName(), // Assuming service method needs skillBadgeName
            request.getStatus()
        );
    	return result;
    }
    
    @GetMapping("/{id}/skill-badges")
    public ResponseEntity<?> getApplicantSkillBadges(@PathVariable Long id) {
    	try {
    		 return skillBadgeService.getApplicantSkillBadges(id);
    	}
    	catch (CustomException e) {
    		return ResponseEntity.status(e.getStatus()).body(e.getMessage());
		}
    	catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error while getting skill badges");
        }
    }
    
    
}
