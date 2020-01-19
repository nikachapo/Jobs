package com.example.jobs;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jobs.firebase.FireBaseDbHelper;
import com.example.jobs.fragments.VacancyListFragment;
import com.example.jobs.users.CompanyUser;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class CompanyProfileActivity extends AppCompatActivity {

    private TextView aboutCompany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profile);


        final ImageView imageView = findViewById(R.id.img);
        aboutCompany = findViewById(R.id.activity_company_about_company_textView);


        Intent intent = getIntent();
        String imageURL = intent.getStringExtra("imageurl");
        String uID = intent.getStringExtra("ownerID");
        String companyName = intent.getStringExtra("companyName");

        //set company information
        setCompanyInformation(uID);

        Toolbar toolbar = findViewById(R.id.toolbar_company_profile);


        setSupportActionBar(toolbar);
        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_company_profile);
        collapsingToolbarLayout.setTitle(companyName);
        collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#E6D752"));

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        Picasso.with(this).load(imageURL).fit().centerCrop().into(imageView);

        Fragment vacancyListFragment = new VacancyListFragment(uID);
        getSupportFragmentManager().beginTransaction().replace(R.id.vacancy_listview_fragment_frame, vacancyListFragment).commit();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    private void setCompanyInformation(String uID) {
        FireBaseDbHelper.getCompanyUserReference(uID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CompanyUser companyUser = dataSnapshot.getValue(CompanyUser.class);
                assert companyUser != null;
                aboutCompany.setText(companyUser.description);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
