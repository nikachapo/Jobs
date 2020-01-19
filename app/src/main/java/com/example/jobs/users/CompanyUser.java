package com.example.jobs.users;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

import com.example.jobs.firebase.FireBaseDbHelper;
import com.example.jobs.adapters.VacancyAdapter;
import com.example.jobs.vacancy.Vacancy;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class CompanyUser extends User{
    private VacancyAdapter vacancyAdapter;

    public String description;


    public CompanyUser() {

    }

    @Override
    public Task<Void> writeNewUserTask(final Context context) {
        FireBaseDbHelper.getDatabaseReference().child(COMPANY_USERS_KEY_NAME).child(uID).setValue(this);
        FireBaseDbHelper.getDatabaseReference().child("Emails").child(uID).setValue(this.userEmail);

        return FireBaseDbHelper.getCompanyUserReference(context)
                .child(COMPANY_USERS_VACANCIES_KEY_NAME).setValue("");



    }

    public CompanyUser(String uID, String username,  String userProfilePictureURL,String userEmail,
                       String description) {

        super(username,userProfilePictureURL,uID,userEmail);
        this.description = description;

    }





    public ArrayList<Vacancy> getAllCompanyVacancies(final RecyclerView recyclerView,
                                                     String ownerID,
                                                     final ProgressBar bar,
                                                     final Context context,
                                                     final SwipeRefreshLayout swipeRefreshLayout){
        final ArrayList<Vacancy> vacancies = new ArrayList<>();
        FireBaseDbHelper.getDatabaseReference().child(COMPANY_USERS_KEY_NAME).child(ownerID)
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
                            if(swipeRefreshLayout.isRefreshing()) {
                                swipeRefreshLayout.setRefreshing(false);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        return vacancies;

    }
}