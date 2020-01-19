package com.example.jobs.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jobs.R;
import com.example.jobs.adapters.VacancyAdapter;
import com.example.jobs.firebase.FireBaseDbHelper;
import com.example.jobs.users.CompanyUser;
import com.example.jobs.vacancy.Vacancy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class VacancyListFragment extends Fragment {
    private ProgressBar progressBar;
    private DatabaseReference vacanciesRef = FireBaseDbHelper
            .getDatabaseReference().child("Vacancies");
    private RecyclerView.Adapter vacancyAdapter;
    private RecyclerView vacanciesListRecycler;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView favouritesTextView;
    private ImageView favouritesIcon;

    private String uID = null;
    private boolean gettingFavourites;
    private boolean userIsCompany;

    private Context mContext;

    public VacancyListFragment() {
        //empty constructor will run if User ID is not passed as argument
    }

    public VacancyListFragment(String uID) {
        this.uID = uID;
    }

    public VacancyListFragment(String uID, boolean gettingFavourites) {
        this.uID = uID;
        this.gettingFavourites = gettingFavourites;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.vacancy_list_fragment, container, false);


        //store data locally
        vacanciesRef.keepSynced(true);

        progressBar = view.findViewById(R.id.fragment_m_progressbar);
        vacanciesListRecycler = view.findViewById(R.id.fragment_vacancy_listview_with_refresh);
        favouritesTextView = view.findViewById(R.id.favourites_textView);
        favouritesIcon = view.findViewById(R.id.favourites_icon);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateRecyclerViewViewData(vacanciesListRecycler);
            }
        });



        //set RecyclerView quality
        vacanciesListRecycler.setHasFixedSize(true);
        vacanciesListRecycler.setItemViewCacheSize(20);
        vacanciesListRecycler.setDrawingCacheEnabled(true);
        vacanciesListRecycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        vacanciesListRecycler.setLayoutManager(layoutManager);

        vacancyAdapter = new VacancyAdapter(getContext(), getAllVacancies(vacanciesListRecycler));

        updateRecyclerViewViewData(vacanciesListRecycler);

        return view;


    }


    private void updateRecyclerViewViewData(final RecyclerView recyclerView) {
        ArrayList<Vacancy> vacancies;
        if (uID == null) {
            favouritesTextView.setText("All vacancies");
            favouritesIcon.setImageResource(R.drawable.vacancies_list_icon);
            vacancies = getAllVacancies(recyclerView);
        } else {
            if (gettingFavourites) {
                vacancies = getFavourites(recyclerView);
                favouritesTextView.setText("Favourites");
                favouritesIcon.setImageResource(R.drawable.ic_star_black_24dp);
            } else {
                favouritesTextView.setText("Vacancies");
                favouritesIcon.setImageResource(R.drawable.company_icon);
                CompanyUser companyUser = new CompanyUser();
                vacancies = companyUser.getAllCompanyVacancies(recyclerView, uID,
                        progressBar, mContext, swipeRefreshLayout);
            }
        }
        vacancyAdapter = new VacancyAdapter(Objects.requireNonNull(mContext), vacancies);
    }

    private ArrayList<Vacancy> getFavourites(final RecyclerView recyclerView) {
        final ArrayList<Vacancy> vacancies = new ArrayList<>();

        FireBaseDbHelper.getCompanyUserReference(getContext())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final DatabaseReference reference;

                        if (dataSnapshot.exists()) {
                            //if data in Companies key exists userIsCompany variable will become true
                            userIsCompany = true;
                        }
                        if (userIsCompany) {
                            reference = FireBaseDbHelper.getCompanyUserReference(mContext)
                                    .child("Favourites");
                        } else
                            reference = FireBaseDbHelper.getCurrentPersonUserReference(mContext)
                                    .child("Favourites");


                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (vacancies.size() == 0) {
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        Vacancy vacancy = postSnapshot.getValue(Vacancy.class);
                                        assert vacancy != null;
                                        vacancies.add(vacancy);
                                    }

                                    vacancyAdapter = new VacancyAdapter(Objects.requireNonNull(mContext), vacancies);
                                    recyclerView.setAdapter(vacancyAdapter);

                                    progressBar.setVisibility(View.GONE);

                                    if (swipeRefreshLayout.isRefreshing()) {
                                        swipeRefreshLayout.setRefreshing(false);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        return vacancies;
    }


    private ArrayList<Vacancy> getAllVacancies(final RecyclerView recyclerView) {
        final ArrayList<Vacancy> vacancies = new ArrayList<>();
        vacanciesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (vacancies.size() == 0) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Vacancy vacancy = postSnapshot.getValue(Vacancy.class);
                        assert vacancy != null;
                        vacancies.add(vacancy);
                    }

                    vacancyAdapter = new VacancyAdapter(Objects.requireNonNull(mContext), vacancies);
                    recyclerView.setAdapter(vacancyAdapter);

                    progressBar.setVisibility(View.GONE);

                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });
        return vacancies;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

}
