package com.example.jobs.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.jobs.R;
import com.example.jobs.adapters.CompanyAdapter;
import com.example.jobs.firebase.FireBaseDbHelper;
import com.example.jobs.users.CompanyUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class CompaniesListFragment extends Fragment {

    private Context mContext;
    private RecyclerView.Adapter companyAdapter;
    private RecyclerView companiesRecycler;
    private ProgressBar companiesProgressBar;
    private SwipeRefreshLayout companiesSwipe;

    private DatabaseReference companiesRef = FireBaseDbHelper.getDatabaseReference()
            .child("Companies");
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_companies_list, container, false);

        companiesRecycler = view.findViewById(R.id.fragment_companies_recyclerView);

        companiesProgressBar = view.findViewById(R.id.fragment_companies_progressbar);
        companiesSwipe = view.findViewById(R.id.fragment_companies_swipe_refresh_layout);
        companiesSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateRecyclerViewViewData();
            }
        });

        //set RecyclerView quality
        companiesRecycler.setHasFixedSize(true);
        companiesRecycler.setItemViewCacheSize(20);
        companiesRecycler.setDrawingCacheEnabled(true);
        companiesRecycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        companiesRecycler.setLayoutManager(layoutManager);

        companyAdapter = new CompanyAdapter(getContext(), getAllCompanies());

        updateRecyclerViewViewData();


        return view;
    }


    private void updateRecyclerViewViewData() {
        companyAdapter = new CompanyAdapter(Objects.requireNonNull(mContext),getAllCompanies());
    }

    private ArrayList<CompanyUser> getAllCompanies() {
        Log.d("getAllcompanies","getting");
        final ArrayList<CompanyUser> companies = new ArrayList<>();
        companiesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (companies.size() == 0) {

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        CompanyUser companyUser = postSnapshot.getValue(CompanyUser.class);
                        companies.add(companyUser);
                        assert companyUser != null;
                        Log.d("for loop",companyUser.userEmail);

                    }

                    companyAdapter = new CompanyAdapter(Objects.requireNonNull(mContext), companies);
                    companiesRecycler.setAdapter(companyAdapter);
                    companiesProgressBar.setVisibility(View.GONE);

                    if (companiesSwipe.isRefreshing()) {
                        companiesSwipe.setRefreshing(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                companiesProgressBar.setVisibility(View.GONE);
            }
        });

        return companies;
    }
}
