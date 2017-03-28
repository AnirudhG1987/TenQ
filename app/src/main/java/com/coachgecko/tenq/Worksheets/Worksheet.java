package com.coachgecko.tenq.Worksheets;

import lombok.Data;
import lombok.experimental.Builder;

/**
 * Created by Anirudh on 2/9/2017.
 */

/*

Holds Meta data of Worksheet.

When pulled from Worksheets 


*/


@Data
public class Worksheet {

    private String key;
    private String description;
    private String name;
    //private int noOfQuestions;
    //private int noOfCorrectQuestions;
    private String score;
    private long noOfStars;

    public Worksheet() {
    }

    @Builder
    public Worksheet(String key, String name, String description,
                     long noOfStars, String score) {
        this.key = key;
        this.name = name;
        this.description = description;
        //this.noOfCorrectQuestions = noOfCorrectQuestions;
        //this.noOfQuestions = noOfQuestions;
        this.score = score;
        this.noOfStars = noOfStars;
    }

}
