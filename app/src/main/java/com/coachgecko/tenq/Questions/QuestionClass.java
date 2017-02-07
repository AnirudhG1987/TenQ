package com.coachgecko.tenq.Questions;

import java.util.ArrayList;

/**
 * Created by Anirudh on 12/24/2016.
 */

public class QuestionClass {

    private String key;
    private String subject, topic;
    private int difficulty;
    private String questionText, answerText;
    private ArrayList<String> options;

    public QuestionClass(){}

    public QuestionClass(String subject, String topic, int difficulty, String questionText, String answerText){

        this.subject = subject;
        this.topic = topic;
        this.difficulty = difficulty;
        this.questionText = questionText;
        this.answerText = answerText;
    }

    public QuestionClass(String subject, String topic, int difficulty,
                         String questionText, String answerText, ArrayList<String> options){

        this.subject = subject;
        this.topic = topic;
        this.difficulty = difficulty;
        this.questionText = questionText;
        this.answerText = answerText;
        this.options = options;

    }

    public void setOptions(ArrayList<String> options){ this.options = options; }

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
    public ArrayList<String> getOptions() {return options; }
    public String getKey(){
        return key;
    }

    public void setKey(String key){
        this.key=key;
    }

}
