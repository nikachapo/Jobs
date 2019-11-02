package com.example.jobs;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Picasso;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BottomSheet extends BottomSheetDialogFragment {
    private String profilePictureURL;
    private String city;
    private String vacancyCategory;
    private String vacancyName;
    private String vacancyBody;
    private String age;
    private String requirements;
    private String salary;

    public BottomSheet(String profilePictureURL, String city, String vacancyCategory,
                       String vacancyName, String vacancyBody, String age,
                       String requirements, String salary) {
        this.profilePictureURL = profilePictureURL;
        this.city = city;
        this.vacancyCategory = vacancyCategory;
        this.vacancyName = vacancyName;
        this.vacancyBody = vacancyBody;
        this.age = age;
        this.requirements = requirements;
        this.salary = salary;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_layout, container, false);
        Button close = v.findViewById(R.id.close_bottom_sheet);
        ImageView profilePicture = v.findViewById(R.id.sheet_profile_image);
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

        Picasso.with(getContext())
                .load(this.profilePictureURL)
                .centerInside()
                .fit()
                .into(profilePicture);

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

