package com.example.jobs.vacancy;

import android.annotation.SuppressLint;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;

public class Vacancy implements Serializable {
    private static final String VACANCIES_KEY_NAME = "Vacancies";
    private static final String COMPANIES_KEY_NAME = "Companies";
    public String
            ownerProfileURL,
            vacancyID,
            ownerID,
            vacancyHeader,
            vacancyBody,
            vacancyCity,
            requiredAge,
            vacancySalary,
            vacancyCategory,
            requirements,
            companyName,
            startDate,
            endDate;


    public Vacancy() {
        //non argument constructor is required for Firebase
    }

    public Vacancy(String vacancyID, String ownerProfileURL,
                   String ownerID, String vacancyHeader,
                   String vacancyBody, String vacancyCity,
                   String requiredAge, String vacancySalary,
                   String vacancyCategory, String requirements,
                   String companyName, String startDate, String endDate) {

        this.vacancyID = vacancyID;
        this.ownerProfileURL = ownerProfileURL;
        this.ownerID = ownerID;
        this.vacancyHeader = vacancyHeader;
        this.vacancyCity = vacancyCity;
        this.vacancyBody = vacancyBody;
        this.requiredAge = requiredAge;
        this.vacancySalary = vacancySalary;
        this.vacancyCategory = vacancyCategory;
        this.requirements = requirements;
        this.companyName = companyName;
        this.startDate = startDate;
        this.endDate = endDate;
    }


    public static void writeVacancy(String ownerProfileURL, String ownerID,
                                    String vacancyHeader, String vacancyBody, String vacancyCity,
                                    String requiredAge, String vacancySalary, String vacancyCategory,
                                    String requirements, String companyName,String startDate,
                                    String endDate,final View view) {

        DatabaseReference vacancyRef = FirebaseDatabase.getInstance().getReference(VACANCIES_KEY_NAME);
        DatabaseReference companiesRef = FirebaseDatabase.getInstance().getReference(COMPANIES_KEY_NAME);

        String vacancyID = vacancyRef.push().getKey();
        assert vacancyID != null;

        Vacancy vacancy = new Vacancy(
                vacancyID, ownerProfileURL, ownerID, vacancyHeader,
                vacancyBody, vacancyCity, requiredAge,
                vacancySalary, vacancyCategory, requirements, companyName,
                startDate,endDate);

        vacancyRef.child(vacancyID).setValue(vacancy);
        companiesRef.child(ownerID).child("Vacancies").child(vacancyID).setValue(vacancy)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Snackbar.make(view, "ვაკანსია დამატებულია", Snackbar.LENGTH_LONG).show();
                    }
                });

    }
}