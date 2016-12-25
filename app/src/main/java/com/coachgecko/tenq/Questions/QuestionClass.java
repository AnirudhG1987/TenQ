package com.coachgecko.tenq.Questions;

/**
 * Created by Anirudh on 12/24/2016.
 */

public class QuestionClass {

    private String key;
    private String questionText;
    private String option1Text, option2Text, option3Text, option4Text;

    public QuestionClass(){}

    public QuestionClass( String questionText, String option1Text, String option2Text,
                          String option3Text, String option4Text){

        this.questionText = questionText;
        this.option1Text = option1Text;
        this.option2Text = option2Text;
        this.option3Text = option3Text;
        this.option4Text = option4Text;
    }

    public String getQuestionText(){
        return questionText;
    }
    public String getOption1Text(){
        return option1Text;
    }
    public String getOption2Text(){
        return option2Text;
    }
    public String getOption3Text(){
        return option3Text;
    }
    public String getOption4Text(){
        return option4Text;
    }
    public String getKey(){
        return key;
    }
    public void setKey(String key){
        this.key=key;
    }

}
