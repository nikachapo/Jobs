package com.example.jobs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jobs.adapters.VacancyAdapter;
import com.example.jobs.vacancy.Vacancy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private DatabaseReference vacanciesRef = FirebaseDatabase.getInstance().getReference();
    private VacancyAdapter vacancyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView vacanciesListView = findViewById(R.id.vacancy_listview);
        progressBar = findViewById(R.id.m_progressbar);


        updateListViewData(vacanciesListView);


        vacanciesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Vacancy vacancy = (Vacancy) adapterView.getAdapter().getItem(i);
                BottomSheet bottomSheet = new BottomSheet(vacancy.ownerID,vacancy.ownerProfileURL,
                        vacancy.vacancyCity, vacancy.vacancyCategory, vacancy.vacancyHeader, vacancy.vacancyBody,
                        vacancy.requiredAge, vacancy.requirements, vacancy.vacancySalary);
                bottomSheet.show(getSupportFragmentManager(), vacancy.vacancyHeader);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_vacancy:
                //Code to run when the settings item is clicked
                startActivity(new Intent(MainActivity.this, AddVacancyActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateListViewData(final ListView listView) {

        vacancyAdapter = new VacancyAdapter(getApplicationContext(), R.layout.custom_list_item, getAllVacancies(listView));
        listView.setAdapter(vacancyAdapter);


    }

    private ArrayList<Vacancy> getAllVacancies(final ListView listView) {
        final ArrayList<Vacancy> vacancies = new ArrayList<>();
        vacanciesRef.child("Vacancies").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (vacancies.size() == 0) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Vacancy vacancy = postSnapshot.getValue(Vacancy.class);
                        assert vacancy != null;
                        vacancies.add(vacancy);
                    }
                    vacancyAdapter = new VacancyAdapter(getApplicationContext(),
                            R.layout.custom_list_item, vacancies);
                    listView.setAdapter(vacancyAdapter);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });
        return vacancies;
    }
}