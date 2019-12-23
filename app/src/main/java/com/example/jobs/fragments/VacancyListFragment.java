package com.example.jobs.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.jobs.LogInActivity;
import com.example.jobs.R;
import com.example.jobs.adapters.VacancyAdapter;
import com.example.jobs.splash_screen.SplashScreenActivity;
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
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

public class VacancyListFragment extends Fragment {
    private ProgressBar progressBar;
    private DatabaseReference vacanciesRef = FirebaseDatabase.getInstance().getReference();
    private String uID = null;
    private RecyclerView.Adapter vacancyAdapter;
    private RecyclerView vacanciesListRecycler;

    private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;

    public VacancyListFragment() {
        //empty constructor will run if User ID is not passed as argument
    }

    public VacancyListFragment(String uID) {
        this.uID = uID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.vacancy_list_fragment, container, false);

        progressBar = view.findViewById(R.id.fragment_m_progressbar);


        mWaveSwipeRefreshLayout = view.findViewById(R.id.main_swipe);
        if (uID == null) {
            vacanciesListRecycler = view.findViewById(R.id.fragment_vacancy_listview_with_refresh);
            mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    // Do work to refresh the list here.
                    updateListViewData(vacanciesListRecycler);

                    new Handler().postDelayed(new Runnable(){
                        @Override
                        public void run() {
                            // Call setRefreshing(false) when the list has been refreshed.
                            mWaveSwipeRefreshLayout.setRefreshing(false);
                        }
                    }, 700);

                }
            });

        } else {
            mWaveSwipeRefreshLayout.setVisibility(View.GONE);
            vacanciesListRecycler = view.findViewById(R.id.fragment_vacancy_listview);
        }





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
