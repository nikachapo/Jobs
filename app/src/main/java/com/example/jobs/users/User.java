package com.example.jobs.users;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
abstract class User {
    protected final String COMPANY_USERS_KEY_NAME = "Companies";
    protected final String COMPANY_USERS_VACANCIES_KEY_NAME = "Vacancies";
    protected final String PERSON_USERS_KEY_NAME = "Users";

    public String uID;
    public String username;
    public String userProfilePictureURL;
    public String userEmail;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String userProfilePictureURL, String uID, String userEmail) {
        this.username = username;
        this.userProfilePictureURL = userProfilePictureURL;
        this.uID = uID;
        this.userEmail = userEmail;

    }

    abstract Task<Void> writeNewUserCompleted(Context context);


}