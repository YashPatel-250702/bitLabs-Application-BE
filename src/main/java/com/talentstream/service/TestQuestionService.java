package com.talentstream.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.talentstream.dto.TestQuestionDTO;
import com.talentstream.entity.Test;
import com.talentstream.entity.TestQuestions;
import com.talentstream.repository.TestQuestionRepository;
import com.talentstream.repository.TestRepository;

@Service
public class TestQuestionService {

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private TestQuestionRepository questionRepository;

    @Transactional
    public String addQuestionsToTest(String testName, List<TestQuestionDTO> questionDTOs) {
    
        Test test = testRepository.findByTestName(testName)
                .orElseThrow(() -> new RuntimeException("Test not found"));

        List<TestQuestions> questions = new ArrayList<>();

        for (TestQuestionDTO questionDTO : questionDTOs) {
            TestQuestions question = new TestQuestions();
            question.setTest(test); 
            question.setQuestion(questionDTO.getQuestion());
            question.setOptions(questionDTO.getOptions());
            question.setAnswer(questionDTO.getAnswer());
            question.setDifficulty(questionDTO.getDifficulty());

            questions.add(question);
        }

       
        questionRepository.saveAll(questions);
        Integer numberOfQuestions = test.getNumberOfQuestions();
        test.setNumberOfQuestions(numberOfQuestions+questions.size());
        testRepository.save(test);

        return questions.size() + " questions added successfully to Test Name: " + testName;
    }
}
