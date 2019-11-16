package com.example.jobs.users;

import com.google.firebase.database.DatabaseReference;

public class PersonUser extends User {


    public PersonUser() {
    }

    public PersonUser(String username, String userProfilePictureURL, String uID, String userEmail) {
        super(username, userProfilePictureURL, uID, userEmail);
    }


    public void writePersonUser(DatabaseReference mDatabase, String userEmail,
                                String name, String userProfilePictureURL,
                                String uID, String usersTableName){
        writeNewUser(mDatabase, userEmail, name, userProfilePictureURL, uID, usersTableName);
    }

}
