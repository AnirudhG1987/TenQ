package com.coachgecko.tenq.Questions;

import java.util.ArrayList;

import lombok.Data;
import lombok.experimental.Builder;

/**
 * Created by Anirudh on 12/24/2016.
 */


@Data

public class Question {

    private String key;
    // private String subject, topic;
    //private int difficulty;
    private String question, answer;
    private ArrayList<String> options;

    public Question() {
    }

    @Builder
    public Question(String question, String answer, ArrayList<String> options) {


        this.question = question;
        this.answer = answer;
        this.options = options;
    }

}
