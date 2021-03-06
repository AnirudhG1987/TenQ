package com.coachgecko.tenq.Results;

import com.coachgecko.tenq.Questions.Question;

import java.util.ArrayList;

import lombok.Data;
import lombok.experimental.Builder;

/**
 * Created by Anirudh on 2/10/2017.
 */
@Data
public class WorksheetResult {

    private String key;
    private String worksheetID;
    private String studentID;
    private int noOfQuestions;
    private int noOfCorrectQuestions;
    private int noOfQuestionsAttempted;
    private ArrayList<Question> questionsList;

    private long noOfStars;
    //private int score;

    public WorksheetResult() {
    }

    @Builder
    public WorksheetResult(String key, String worksheetID, String studentID, int noofQuestions,
                           int noOfQuestionsAttempted, int noofCorrectQuestions,
                           ArrayList<Question> questionsList, long noOfStars) {
        this.key = key;
        this.worksheetID = worksheetID;
        this.studentID = studentID;
        this.noOfQuestions = noofQuestions;
        this.noOfQuestions = noofQuestions;
        this.noOfQuestionsAttempted = noOfQuestionsAttempted;
        this.noOfCorrectQuestions = noofCorrectQuestions;
        this.questionsList = questionsList;
        this.noOfStars = noOfStars;
        //this.score = score;
    }

}
