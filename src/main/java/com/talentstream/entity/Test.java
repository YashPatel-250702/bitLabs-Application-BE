package com.talentstream.entity;

import java.util.List;
import javax.persistence.*;

@Entity
public class Test {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String testName;
    private String duration;
    private Integer numberOfQuestions;

    @ElementCollection
    private List<String> topicsCovered;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TestQuestions> questions;

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
    public List<TestQuestions> getQuestions() {
        return questions;
    }
    public void setQuestions(List<TestQuestions> questions) {
        this.questions = questions;
    }
}
