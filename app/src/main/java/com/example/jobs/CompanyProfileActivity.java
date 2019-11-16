package com.example.jobs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.jobs.users.CompanyUser;
import com.example.jobs.vacancy.Vacancy;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class CompanyProfileActivity extends AppCompatActivity {
    private ImageView imageView;
    private ListView vacancyListView;
    private ProgressBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profile);

        imageView = findViewById(R.id.img);
        vacancyListView = findViewById(R.id.company_vacancy_listview);
        bar = findViewById(R.id.progress_bar);
        Intent intent = getIntent();

        String imageURL = intent.getStringExtra("imageurl");
        String uID = intent.getStringExtra("ownerID");

        vacancyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Vacancy vacancy = (Vacancy) adapterView.getAdapter().getItem(i);
                BottomSheet bottomSheet = new BottomSheet(vacancy.ownerID,vacancy.ownerProfileURL,
                        vacancy.vacancyCity, vacancy.vacancyCategory, vacancy.vacancyHeader, vacancy.vacancyBody,
                        vacancy.requiredAge, vacancy.requirements, vacancy.vacancySalary);
                bottomSheet.show(getSupportFragmentManager(), vacancy.vacancyHeader);

            }
        });

        Picasso.with(this).load(imageURL).fit().centerCrop().into(imageView);

        CompanyUser companyUser = new CompanyUser();
        companyUser.getAllCompanyVacancies(vacancyListView,uID,bar,this);




    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
