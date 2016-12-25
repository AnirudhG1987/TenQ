package com.coachgecko.tenq.Questions;

/**
 * Created by Anirudh on 12/24/2016.
 */

public class QuestionClass {

    private String key;
    private String subject, topic;
    private int difficulty;
    private String questionText, answerText;
    private String option1Text, option2Text, option3Text, option4Text;

    public QuestionClass(){}

    public QuestionClass(String subject, String topic, int difficulty, String questionText, String answerText, String option1Text, String option2Text,
                          String option3Text, String option4Text){

        this.subject = subject;
        this.topic = topic;
        this.difficulty = difficulty;
        this.questionText = questionText;
        this.answerText = answerText;
        this.option1Text = option1Text;
        this.option2Text = option2Text;
        this.option3Text = option3Text;
        this.option4Text = option4Text;
    }

    public String getSubject(){
        return subject;
    }
    public String getTopic(){
        return topic;
    }
    public int getDifficulty(){
        return difficulty;
    }

    public String getQuestionText(){
        return questionText;
    }
    public String getAnswerText(){
        return answerText;
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
