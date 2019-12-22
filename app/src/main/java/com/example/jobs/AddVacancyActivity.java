package com.example.jobs;


import android.animation.Animator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
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

    private View rootLayout;

    private TextInputLayout vacancyNameLayout,
            vacancyBodyLayout,
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
        overridePendingTransition(R.anim.do_not_move, R.anim.do_not_move);

        setContentView(R.layout.activity_add_vacancy);
        setTitle("Add Vacancy");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        vacancyNameLayout = findViewById(R.id.vacancy_name_textInputEditText);
        vacancyBodyLayout = findViewById(R.id.vacancy_body_textInputEditText);
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

        rootLayout = findViewById(R.id.root_layout);







        if (savedInstanceState == null) {
            rootLayout.setVisibility(View.INVISIBLE);

            ViewTreeObserver viewTreeObserver = rootLayout.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        circularRevealActivity();
                        rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }
        }














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
                        vacancyBodyLayout.getEditText().getText().toString().trim(),
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

    private void circularRevealActivity() {

        int cx = rootLayout.getWidth() / 2;
        int cy = rootLayout.getHeight()-50;

        float finalRadius = Math.max(rootLayout.getWidth(), rootLayout.getHeight());

        // create the animator for this view (the start radius is zero)
        Animator circularReveal = ViewAnimationUtils
                        .createCircularReveal(rootLayout, cx, cy, 0, finalRadius);
        circularReveal.setDuration(1000);

        // make the view visible and start the animation
        rootLayout.setVisibility(View.VISIBLE);
        circularReveal.start();
    }
}