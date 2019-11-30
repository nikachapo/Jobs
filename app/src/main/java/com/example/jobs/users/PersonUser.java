package com.example.jobs.users;

public class PersonUser extends User {

    public String age;
    public String city;

    public PersonUser() {
    }

    public PersonUser(String username, String userProfilePictureURL, String uID, String userEmail, String age, String city) {
        super(username, userProfilePictureURL, uID, userEmail);
        this.age = age;
        this.city = city;
    }


    public void writePersonUser() {
        writeNewUser(databaseReference, this);
    }


}
