package com.example.jobs.users;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;

public class PersonUser extends User {

    public String birthDate;
    public String city;
    public String about;

    public PersonUser() {
    }

    public PersonUser(String username, String userProfilePictureURL, String uID, String userEmail,
                      String birthDate, String city, String about) {
        super(username, userProfilePictureURL, uID, userEmail);
        this.birthDate = birthDate;
        this.city = city;
        this.about = about;
    }

    @Override
    public Task<Void> writeNewUserCompleted(final Context context) {

        DATABASE_REFERENCE.child(PERSON_USERS_TABLE_NAME).child(uID).setValue(this);
        return DATABASE_REFERENCE.child("Emails").child(uID).setValue(this.userEmail);

    }

    public void setBirthDate(String uID,String newBirthDate, Context context) {
        setUserInformation(uID,"birthDate", newBirthDate, "Birth date Changed Successfully", context);
    }

    public void setCity(String uID,String newCity, Context context) {
        setUserInformation(uID,"city", newCity, "City changed successfully", context);
    }

    public void setAbout(String uID,String newAbout, Context context) {
        setUserInformation(uID,"about", newAbout, "About user Changed Successfully", context);
    }

    public void setUsername(String uID,String newUsername, Context context) {
        setUserInformation(uID,"username", newUsername, "Username Changed Successfully", context);
    }


    private void setUserInformation(String uID,
                                    final String key,
                                    final String value,
                                    final String message,
                                    final Context context) {

        DATABASE_REFERENCE.child("Users").child(uID)
                .child(key).setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
