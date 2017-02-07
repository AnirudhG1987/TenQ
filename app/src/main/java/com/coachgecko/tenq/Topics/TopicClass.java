package com.coachgecko.tenq.Topics;

/**
 * Created by Anirudh on 2/5/2017.
 */

public class TopicClass {

    private String key;
    private String topicName;

    public TopicClass(){}

    public TopicClass(String topicName){
        this.topicName = topicName;
    }



    public String getTopicName(){return topicName;}
    public String getKey(){
        return key;
    }
    public void setKey(String key){
        this.key=key;
    }


}
