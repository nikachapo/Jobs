package com.example.jobs;


import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jobs.vacancy.Vacancy;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

public class BottomSheet extends BottomSheetDialogFragment {
    private String profilePictureURL,
            city,
            vacancyCategory,
            vacancyName,
            vacancyBody,
            age,
            requirements,
            salary,
            ownerID,
            companyName;

    public BottomSheet(Vacancy vacancy) {
        this.ownerID = vacancy.ownerID;
        this.profilePictureURL = vacancy.ownerProfileURL;
        this.city = vacancy.vacancyCity;
        this.vacancyCategory = vacancy.vacancyCategory;
        this.vacancyName = vacancy.vacancyHeader;
        this.vacancyBody = vacancy.vacancyBody;
        this.age = vacancy.requiredAge;
        this.requirements = vacancy.requirements;
        this.salary = vacancy.vacancySalary;
        this.companyName = vacancy.companyName;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.bottom_sheet_layout, container, false);
        Button close = v.findViewById(R.id.close_bottom_sheet);
        final ImageView profilePicture = v.findViewById(R.id.sheet_profile_image);
        TextView cityTextView = v.findViewById(R.id.sheet_vacancy_city);
        TextView vacancyCategoryTextView = v.findViewById(R.id.sheet_vacancy_category);
        TextView vacancyNameTextView = v.findViewById(R.id.sheet_vacancy_name);
        TextView vacancyBodyTextView = v.findViewById(R.id.sheet_vacancy_body);
        TextView ageTextView = v.findViewById(R.id.sheet_vacancy_age);
        TextView requirementsTextView = v.findViewById(R.id.sheet_vacancy_requirements);
        TextView salaryTextView = v.findViewById(R.id.sheet_vacancy_salary);



        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });



        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bottomSheetToCompanyProfile = new Intent(getActivity(), CompanyProfileActivity.class);
                bottomSheetToCompanyProfile.putExtra("imageurl", profilePictureURL);
                bottomSheetToCompanyProfile.putExtra("ownerID", ownerID);
                bottomSheetToCompanyProfile.putExtra("companyName", companyName);

                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(
                        getActivity(), Pair.create(v.findViewById(R.id.sheet_profile_image), "imagetrans"));
                startActivity(bottomSheetToCompanyProfile, activityOptions.toBundle());
                dismiss();
            }
        });

        Picasso.with(getContext()).load(this.profilePictureURL)

                .resize(150, 150)
                .into(profilePicture, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap imageBitmap = ((BitmapDrawable) profilePicture.getDrawable()).getBitmap();
                        RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                        imageDrawable.setCircular(true);
                        imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                        profilePicture.setImageDrawable(imageDrawable);
                    }

                    @Override
                    public void onError() {
                        profilePicture.setImageResource(R.drawable.ic_default_company_image);
                    }
                });

        cityTextView.setText(this.city);
        vacancyCategoryTextView.setText(this.vacancyCategory);
        vacancyNameTextView.setText(this.vacancyName);
        vacancyBodyTextView.setText(this.vacancyBody);
        ageTextView.setText(this.age);
        requirementsTextView.setText(this.requirements);
        salaryTextView.setText(this.salary);

        return v;
    }


}

