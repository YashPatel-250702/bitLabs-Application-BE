package com.talentstream.controller;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.talentstream.dto.TestQuestionDTO;
import com.talentstream.service.TestQuestionService;

@RestController
@RequestMapping("/test")
public class TestQuestionController {

	
	private static final Logger LOGGER = LoggerFactory.getLogger(TestQuestionController.class);
	
	@Autowired
	private TestQuestionService testQuestionService;

	@PostMapping("/questions/{testName}")
	public ResponseEntity<String> addQuestionsToTest(@PathVariable String testName,
			@RequestBody List<TestQuestionDTO> questionDTOs) {
		try {
			String response = testQuestionService.addQuestionsToTest(testName, questionDTOs);
			return ResponseEntity.ok(response);
			
		} catch (RuntimeException e) {
			LOGGER.warn("Test Not found with name: {}", testName);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		catch (Exception e) {
			LOGGER.error("Error while creating test: {}", e.getMessage(), e);
			return ResponseEntity.internalServerError().body("Error while adding questions: " + e.getMessage());
		}
	}
}
