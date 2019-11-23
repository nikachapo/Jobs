package com.example.jobs.users;

import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.jobs.R;
import com.example.jobs.adapters.VacancyAdapter;
import com.example.jobs.vacancy.Vacancy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CompanyUser extends User {
    private static final String COMPANY_USERS_TABLE_NAME = "Companies";
    private static final String COMPANY_USERS_VACANCIES_KEY_NAME = "Vacancies";
    private DatabaseReference companiesRef = FirebaseDatabase.getInstance().getReference();
    private VacancyAdapter vacancyAdapter;
    public CompanyUser() {

    }

    public CompanyUser(String username, String userProfilePictureURL, String uID, String userEmail) {
        super(username, userProfilePictureURL, uID, userEmail);

    }

    public void writeCompanyUser(DatabaseReference mDatabase,
                                 String userEmail, String name,
                                 String userProfilePictureURL, String uID,
                                 String usersTableName){
        writeNewUser(mDatabase, userEmail, name, userProfilePictureURL, uID, usersTableName);
        mDatabase.child(COMPANY_USERS_TABLE_NAME).child(uID)
                .child(COMPANY_USERS_VACANCIES_KEY_NAME).setValue("");

    }

    public ArrayList<Vacancy> getAllCompanyVacancies(final RecyclerView recyclerView, String ownerID, final ProgressBar bar, final Context context){
        final ArrayList<Vacancy> vacancies = new ArrayList<>();
        companiesRef.child(COMPANY_USERS_TABLE_NAME).child(ownerID)
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