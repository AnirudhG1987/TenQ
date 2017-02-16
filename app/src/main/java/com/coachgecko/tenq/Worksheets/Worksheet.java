package com.coachgecko.tenq.Worksheets;

import lombok.Data;
import lombok.experimental.Builder;

/**
 * Created by Anirudh on 2/9/2017.
 */


@Data
public class Worksheet {

    private String key;
    private String description;
    private String name;
    private int noOfQuestions;
    private int noOfCorrectQuestions;
    private String score;

    public Worksheet() {
    }

    @Builder
    public Worksheet(String key, String name, String description,
                     String score, int noOfQuestions, int noOfCorrectQuestions) {
        this.key = key;
        this.name = name;
        this.description = description;
        this.noOfCorrectQuestions = noOfCorrectQuestions;
        this.noOfQuestions = noOfQuestions;
        this.score = score;
    }

}
