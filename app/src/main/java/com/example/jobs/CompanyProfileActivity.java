package com.example.jobs;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.jobs.fragments.VacancyListFragment;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class CompanyProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profile);

        ImageView imageView = findViewById(R.id.img);

        Intent intent = getIntent();
        String imageURL = intent.getStringExtra("imageurl");
        String uID = intent.getStringExtra("ownerID");
        String companyName = intent.getStringExtra("companyName");

        setTitle(companyName);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);




        Picasso.with(this).load(imageURL).fit().centerCrop().into(imageView);

        Fragment vacancyListFragment = new VacancyListFragment(uID);
        getSupportFragmentManager().beginTransaction().replace(R.id.vacancy_listview_fragment_frame,vacancyListFragment).commit();


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
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
