package com.example.jobs.users;

import com.example.jobs.vacancy.Vacancy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;

public class CompanyUser extends User {
    private static final String COMPANY_USERS_TABLE_NAME = "Companies";
    private static final String COMPANY_USERS_POSTS_AMOUNT_KEY_NAME = "postsAmount";
    public int postsAmount;
    public CompanyUser() {

    }

    public CompanyUser(String username, String userProfilePictureURL, String uID, String userEmail, int postsAmount) {
        super(username, userProfilePictureURL, uID, userEmail);
        this.postsAmount = postsAmount;
    }

    public void writeCompanyUser(DatabaseReference mDatabase,
                                 String userEmail, String name,
                                 String userProfilePictureURL, String uID,
                                 String usersTableName){
        writeNewUser(mDatabase, userEmail, name, userProfilePictureURL, uID, usersTableName);
        mDatabase.child(COMPANY_USERS_TABLE_NAME).child(uID)
                .child(COMPANY_USERS_POSTS_AMOUNT_KEY_NAME).setValue(postsAmount);

    }


    public int getPostsAmount(DatabaseReference mDatabase){
        final int[] postsAmount = new int[1];
        mDatabase.child(COMPANY_USERS_TABLE_NAME).child(this.uID)
                .child(COMPANY_USERS_POSTS_AMOUNT_KEY_NAME)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              postsAmount[0] =  dataSnapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return postsAmount[0];
    }

}
