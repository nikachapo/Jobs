package com.example.jobs.users;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
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

    protected void writeNewUser(DatabaseReference mDatabase,
                                    String userEmail,String name,
                                    String userProfilePictureURL, String uID,String usersTableName) {
        User user = new User(name,userProfilePictureURL,uID,userEmail );

        mDatabase.child(usersTableName).child(uID).setValue(user);
    }


}