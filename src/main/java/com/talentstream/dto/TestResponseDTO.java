package com.talentstream.dto;

import java.util.List;

public class TestResponseDTO {

	private Long id;
	private String testName;
	private String duration;
	private Integer numberOfQuestions;
	private List<String> topicsCovered;
	private List<TestQuestionResponseDTO> questions;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public Integer getNumberOfQuestions() {
		return numberOfQuestions;
	}

	public void setNumberOfQuestions(Integer numberOfQuestions) {
		this.numberOfQuestions = numberOfQuestions;
	}

	public List<String> getTopicsCovered() {
		return topicsCovered;
	}

	public void setTopicsCovered(List<String> topicsCovered) {
		this.topicsCovered = topicsCovered;
	}

	public List<TestQuestionResponseDTO> getQuestions() {
		return questions;
	}

	public void setQuestions(List<TestQuestionResponseDTO> questions) {
		this.questions = questions;
	}
}
