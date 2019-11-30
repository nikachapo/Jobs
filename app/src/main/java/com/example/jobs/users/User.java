package com.example.jobs.users;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    protected final String COMPANY_USERS_TABLE_NAME = "Companies";
    protected final String COMPANY_USERS_VACANCIES_KEY_NAME = "Vacancies";
    protected final String PERSON_USERS_TABLE_NAME = "Users";
    protected DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

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

    public void writeNewUser(DatabaseReference mDatabase, CompanyUser companyUser) {

        mDatabase.child(COMPANY_USERS_TABLE_NAME).child(uID).setValue(companyUser);

        mDatabase.child("Emails").child(uID).setValue(companyUser.userEmail);

        mDatabase.child(COMPANY_USERS_TABLE_NAME).child(uID)
                .child(COMPANY_USERS_VACANCIES_KEY_NAME).setValue("");
    }

    public void writeNewUser(DatabaseReference mDatabase, PersonUser personUser) {

        mDatabase.child(PERSON_USERS_TABLE_NAME).child(uID).setValue(personUser);

        mDatabase.child("Emails").child(uID).setValue(personUser.userEmail);

    }


}