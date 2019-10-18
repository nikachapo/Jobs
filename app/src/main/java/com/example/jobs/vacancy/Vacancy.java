package com.example.jobs.vacancy;

import android.annotation.SuppressLint;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Vacancy {
    private static final String VACANCIES_TABLE_NAME = "Vacancies";
    public String postID;
    public String ownerID;
    public String vacancyHeader;
    public String vacancyBody;
    public String vacancyCity;
    public String currentTimeStamp = getCurrentTimeStamp();
    public String requiredAge;
    public String vacancySalary;
    public String vacancyCategory;
    public String requirements;
    public Vacancy(){
        //non argument constructor is required for Firebase
    }

    public Vacancy(String ownerID, String vacancyHeader,  String vacancyBody,String vacancyCity
            , String requiredAge, String vacancySalary, String vacancyCategory, String requirements) {

        this.ownerID = ownerID;
        this.vacancyHeader = vacancyHeader;
        this.vacancyCity = vacancyCity;
        this.vacancyBody = vacancyBody;
        this.requiredAge = requiredAge;
        this.vacancySalary = vacancySalary;
        this.vacancyCategory = vacancyCategory;
        this.requirements = requirements;
    }

    @SuppressLint("SimpleDateFormat")
    private String getCurrentTimeStamp() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public static void writePost(String ownerID,
                                 String vacancyHeader, String vacancyBody, String vacancyCity,
                                 String requiredAge, String vacancySalary, String vacancyCategory, String requirements){

        Vacancy vacancy = new Vacancy(ownerID,vacancyHeader,vacancyBody,vacancyCity,requiredAge,
                                   vacancySalary,vacancyCategory, requirements);
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference(VACANCIES_TABLE_NAME);
        mRef.setValue(vacancy);
    }
}
