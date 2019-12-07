package com.example.jobs;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.jobs.vacancy.Vacancy;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.textfield.TextInputLayout;
import com.spark.submitbutton.SubmitButton;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;


public class AddVacancyActivity extends AppCompatActivity {

    private TextInputLayout vacancyNameLayout,
            vacancyBodyLayput,
            vacancyCityLayout;

    private EditText ageFromEditText,
            ageToEditText,
            salaryFromEditText,
            salaryToEditText,
            requirementsEditText;

    private TextView requirementsTextView;

    private Spinner vacancyCategorySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vacancy);
        setTitle("Add Vacancy");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        vacancyNameLayout = findViewById(R.id.vacancy_name_textInputEditText);
        vacancyBodyLayput = findViewById(R.id.vacancy_body_textInputEditText);
        vacancyCityLayout = findViewById(R.id.vacancy_city_textInputEditText);
        ageFromEditText = findViewById(R.id.age_from_edittext);
        ageToEditText = findViewById(R.id.age_to_edittext);
        salaryFromEditText = findViewById(R.id.salaty_from_edittext);
        salaryToEditText = findViewById(R.id.salaty_to_edittext);
        requirementsEditText = findViewById(R.id.vacancy_requirements_edittext);
        requirementsTextView = findViewById(R.id.vacancy_requirements_textview);
        vacancyCategorySpinner = findViewById(R.id.vacancy_category_spinner);
        Button addRequirementsToTextView = findViewById(R.id.add_requirements_to_textview_button);
        SubmitButton addVacancy = findViewById(R.id.add_vacancy);



        addRequirementsToTextView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                requirementsTextView.setText(requirementsTextView.getText().toString() +
                        "◉" + requirementsEditText.getText().toString() + "\n");
                requirementsEditText.setText("");
                ///
            }
        });


        addVacancy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInAccount account = FirebaseDbHelper.getCurentAccount(getApplicationContext());

                Vacancy.writeVacancy(
                        account.getPhotoUrl().toString(),
                        account.getId(),
                        vacancyNameLayout.getEditText().getText().toString().trim(),
                        vacancyBodyLayput.getEditText().getText().toString().trim(),
                        vacancyCityLayout.getEditText().getText().toString().trim(),

                        ageFromEditText.getText().toString().trim() + "-" +
                                ageToEditText.getText().toString().trim(),

                        salaryFromEditText.getText().toString().trim() + "-" +
                                salaryToEditText.getText().toString().trim(),

                        vacancyCategorySpinner.getSelectedItem().toString(),
                        requirementsTextView.getText().toString(), account.getDisplayName(), v);
            }
        });
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

}