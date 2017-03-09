package com.coachgecko.tenq.Authentication;

import lombok.Data;
import lombok.experimental.Builder;

/**
 * Created by anirudhgaddamanugu on 2/28/17.
 */
@Data
public class Student {


    private String fullName;
    private int age;
    private int grade;
    private String country;
    private String email;
    private String password;
    private int starsCollected;
    private int pointsEarned;

    public Student(){}

    @Builder
    public Student(String fullName, int age, String country, String email, String password,int grade, int starsCollected, int pointsEarned){
        this.age = age;
        this.fullName = fullName;
        this.country = country;
        this.email = email;
        this.grade = grade;
        this.password = password;
        this.starsCollected = starsCollected;
        this.pointsEarned = pointsEarned;
    }



}
