package com.example.jobs.users;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

import com.example.jobs.FirebaseDbHelper;
import com.example.jobs.adapters.VacancyAdapter;
import com.example.jobs.vacancy.Vacancy;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CompanyUser extends User{
    private VacancyAdapter vacancyAdapter;

    public String description;

    public CompanyUser() {

    }

    @Override
    public Task<Void> writeNewUserCompleted(final Context context) {
        FirebaseDbHelper.getDatabaseReference().child(COMPANY_USERS_KEY_NAME).child(uID).setValue(this);

        FirebaseDbHelper.getDatabaseReference().child("Emails").child(uID).setValue(this.userEmail);
        return FirebaseDbHelper.getDatabaseReference().child(COMPANY_USERS_KEY_NAME).child(uID)
                .child(COMPANY_USERS_VACANCIES_KEY_NAME).setValue("");



    }

    public CompanyUser(String uID, String username,  String userProfilePictureURL,String userEmail,
                       String desctiption) {

        super(username,userProfilePictureURL,uID,userEmail);
        this.description = desctiption;

    }





    public ArrayList<Vacancy> getAllCompanyVacancies(final RecyclerView recyclerView,
                                                     String ownerID,
                                                     final ProgressBar bar,
                                                     final Context context){
        final ArrayList<Vacancy> vacancies = new ArrayList<>();
        FirebaseDbHelper.getDatabaseReference().child(COMPANY_USERS_KEY_NAME).child(ownerID)
                .child(COMPANY_USERS_VACANCIES_KEY_NAME)
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        if (vacancies.size() == 0) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Vacancy vacancy = postSnapshot.getValue(Vacancy.class);
                                assert vacancy != null;
                                vacancies.add(vacancy);
                            }

                            vacancyAdapter = new VacancyAdapter(context,vacancies);
                            recyclerView.setAdapter(vacancyAdapter);
                            bar.setVisibility(View.INVISIBLE);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        return vacancies;

    }
}