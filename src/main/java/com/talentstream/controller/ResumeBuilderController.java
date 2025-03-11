package com.talentstream.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.talentstream.dto.ResumeBuilderDto;
import com.talentstream.entity.ResumeBuilder;
import com.talentstream.service.ResumeBuilderService;

@RestController
@RequestMapping("/resume-builder")
public class ResumeBuilderController {

	private static final Logger logger = LoggerFactory.getLogger(ResumeBuilderController.class);

	@Autowired
	private ResumeBuilderService resumeBuilderService;

	@PostMapping("/saveresume/{applicantId}")
	public ResponseEntity<?> createResume(@Valid @RequestBody ResumeBuilderDto resumeDto, BindingResult bindingResult,
			@PathVariable Long applicantId) {

		try {
			if (bindingResult.hasErrors()) {
				Map<String, String> errors = new HashMap<>();
				bindingResult.getFieldErrors()
						.forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(errors);
			}

			ResumeBuilder resume = resumeBuilderService.createResume(resumeDto, applicantId);
			if (resume == null) {
				logger.error("Resume not saved");
				return ResponseEntity.badRequest().body("Resume not saved");
			}

			logger.info("Resume Saved Successfully");
			return ResponseEntity.ok("Resume Saved Successfully");
		} catch (RuntimeException e) {
			logger.error("Error while saving resume",e);
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			logger.error("Internal server error",e);
			return ResponseEntity.internalServerError().body("Internal server error");
		}
	}

	@GetMapping("getResume/{applicantId}")
	public ResponseEntity<?> getResumeWithEducation(@PathVariable Long applicantId) {
		try {
			ResumeBuilderDto resume = resumeBuilderService.getResume(applicantId);
			return ResponseEntity.ok(resume);
		} catch (RuntimeException e) {
			logger.error("Error while fetching resume",e);
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			logger.error("Internal server error",e);
			return ResponseEntity.internalServerError().body("Internal server error");
		}
	}
	@PutMapping("/updateResume/{applicantId}")
	public ResponseEntity<?> updateResume(@Valid @RequestBody ResumeBuilderDto resumeDto,
	                                      BindingResult bindingResult,
	                                      @PathVariable Long applicantId) {
	    try {
	        // Step 1: Validate input data
	        if (bindingResult.hasErrors()) {
	            Map<String, String> errors = new HashMap<>();
	            bindingResult.getFieldErrors()
	                    .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
	            return ResponseEntity.badRequest().body(errors);
	        }
 
	        // Step 2: Call service method to update resume
	        ResumeBuilder updatedResume = resumeBuilderService.updateResume(applicantId, resumeDto);
	        
	        if (updatedResume == null) {
	            return ResponseEntity.badRequest().body("Resume update failed or applicant not found");
	        }
 
	        // Step 3: Return success response
	        return ResponseEntity.ok("Resume Updated Successfully");
	        
	    } catch (RuntimeException e) {
	        return ResponseEntity.badRequest().body(e.getMessage());
	    } catch (Exception e) {
	        return ResponseEntity.internalServerError().body("Internal server error");
	    }
	}
 
}
