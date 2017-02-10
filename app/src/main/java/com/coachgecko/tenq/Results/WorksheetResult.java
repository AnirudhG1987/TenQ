package com.coachgecko.tenq.Results;

import java.util.ArrayList;

import lombok.experimental.Builder;

/**
 * Created by Anirudh on 2/10/2017.
 */

public class WorksheetResult {

    private String key;
    private String worksheetID;
    private String studentID;
    private int noOfQuestions;
    private int noOfCorrectQuestions;
    private int noOfQuestionsAttempted;
    private ArrayList<String> manswersSelectedList;
    private ArrayList<Boolean> manswersResultList;

    public WorksheetResult() {
    }

    @Builder
    public WorksheetResult(String key, String worksheetID, String studentID, int noofQuestions,
                           int noOfQuestionsAttempted, int noofCorrectQuestions,
                           ArrayList<String> manswersSelectedList, ArrayList<Boolean> manswersResultList) {
        this.key = key;
        this.worksheetID = worksheetID;
        this.studentID = studentID;
        this.noOfQuestions = noofQuestions;
        this.noOfQuestions = noofQuestions;
        this.noOfQuestionsAttempted = noOfQuestionsAttempted;
        this.noOfCorrectQuestions = noofCorrectQuestions;
        this.manswersResultList = manswersResultList;
        this.manswersSelectedList = manswersSelectedList;
    }

}
