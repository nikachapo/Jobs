package com.example.jobs.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.jobs.R;
import com.example.jobs.adapters.VacancyAdapter;
import com.example.jobs.users.CompanyUser;
import com.example.jobs.vacancy.Vacancy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class VacancyListFragment extends Fragment {
    private ProgressBar progressBar;
    private DatabaseReference vacanciesRef = FirebaseDatabase.getInstance().getReference();
    private String uID = null;
    private RecyclerView.Adapter vacancyAdapter;


    public VacancyListFragment() {
        //empty constructor is needed
    }

    public VacancyListFragment(String uID) {
        this.uID = uID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.vacancy_list_fragment, container, false);
        progressBar = view.findViewById(R.id.fragment_m_progressbar);

        RecyclerView vacanciesListRecycler = view.findViewById(R.id.fragment_vacancy_listview);
        vacanciesListRecycler.setHasFixedSize(true);
        vacanciesListRecycler.setItemViewCacheSize(20);
        vacanciesListRecycler.setDrawingCacheEnabled(true);
        vacanciesListRecycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        vacanciesListRecycler.setLayoutManager(layoutManager);

        vacancyAdapter = new VacancyAdapter(getContext(), getAllVacancies(vacanciesListRecycler));

        updateListViewData(vacanciesListRecycler);

        return view;


    }


    private void updateListViewData(final RecyclerView recyclerView) {
        ArrayList<Vacancy> vacancies;
        if (uID == null) {
            vacancies = getAllVacancies(recyclerView);
        } else {
            CompanyUser companyUser = new CompanyUser();
            vacancies = companyUser.getAllCompanyVacancies(recyclerView, uID, progressBar, getContext());
        }
        vacancyAdapter = new VacancyAdapter(Objects.requireNonNull(getContext()), vacancies);
        recyclerView.setAdapter(vacancyAdapter);
    }

    private ArrayList<Vacancy> getAllVacancies(final RecyclerView listView) {
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

                    vacancyAdapter = new VacancyAdapter(Objects.requireNonNull(getContext()), vacancies);
                    listView.setAdapter(vacancyAdapter);

                    progressBar.setVisibility(View.GONE);

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
