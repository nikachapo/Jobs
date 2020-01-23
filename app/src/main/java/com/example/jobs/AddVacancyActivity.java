package com.example.jobs;


import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.jobs.firebase.FireBaseDbHelper;
import com.example.jobs.fragments.DatePickerFragment;
import com.example.jobs.vacancy.Vacancy;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;


public class AddVacancyActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener {

    private View rootLayout;

    private TextInputLayout vacancyNameLayout,
            vacancyBodyLayout,
            vacancyCityLayout;

    private EditText ageFromEditText,
            ageToEditText,
            salaryFromEditText,
            salaryToEditText,
            requirementsEditText;

    private TextView requirementsTextView,
            currentDateTextView,
            endDateTextView;

    private Spinner vacancyCategorySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.do_not_move, R.anim.do_not_move);
        setContentView(R.layout.activity_add_vacancy);
        setTitle("ვაკანსიის დამატება");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        vacancyNameLayout = findViewById(R.id.vacancy_name_textInputEditText);
        vacancyBodyLayout = findViewById(R.id.activity_add_vacancy_body_textInputEditText);
        vacancyCityLayout = findViewById(R.id.activity_add_vacancy_city_textInputEditText);
        ageFromEditText = findViewById(R.id.activity_add_age_from_editText);
        ageToEditText = findViewById(R.id.activity_add_age_to_editText);
        salaryFromEditText = findViewById(R.id.activity_add_salary_from_editText);
        salaryToEditText = findViewById(R.id.activity_add_salary_to_editText);
        requirementsEditText = findViewById(R.id.activity_add_vacancy_requirements_editText);
        requirementsTextView = findViewById(R.id.activity_add_vacancy_requirements_textview);
        currentDateTextView = findViewById(R.id.activity_add_start_date);
        endDateTextView = findViewById(R.id.activity_add_end_date);
        vacancyCategorySpinner = findViewById(R.id.activity_add_vacancy_category_spinner);
        Button addRequirementsToTextView = findViewById(R.id.activity_add_requirements_to_textview_button);


        setCurrentDate();

        endDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        rootLayout = findViewById(R.id.activity_add_root_layout);


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
        int cy = rootLayout.getHeight() - 50;

        float finalRadius = Math.max(rootLayout.getWidth(), rootLayout.getHeight());

        // create the animator for this view (the start radius is zero)
        Animator circularReveal = ViewAnimationUtils
                .createCircularReveal(rootLayout, cx, cy, 0, finalRadius);
        circularReveal.setDuration(1000);

        // make the view visible and start the animation
        rootLayout.setVisibility(View.VISIBLE);
        circularReveal.start();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        String currentDateString = DateFormat.getDateInstance().format(calendar.getTime());
        endDateTextView.setText(currentDateString);

    }

    private void setCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        String currentDateString = DateFormat.getDateInstance().format(calendar.getTime());
        currentDateTextView.setText(currentDateString);
    }






    private boolean validateVacancyName(){
        String vacancyNameInput = Objects.requireNonNull(vacancyNameLayout.getEditText()).getText().toString().trim();

        if(vacancyNameInput.isEmpty()){
            vacancyNameLayout.setError("Name is required");
            return  false;
        }else if(vacancyNameInput.length() > 20){
            vacancyNameLayout.setError("Name is too long");
            return  false;
        }
        else {
            vacancyNameLayout.setError(null);
            return true;
        }
    }

    private boolean validateVacancyBody(){
        String vacancyBodyInput = Objects.requireNonNull(vacancyBodyLayout.getEditText()).getText().toString().trim();

        if(vacancyBodyInput.isEmpty()){
            vacancyBodyLayout.setError("Description is required");
            return  false;
        }else if(vacancyBodyInput.length() > 300){
            vacancyBodyLayout.setError("Description is too long");
            return  false;
        }
        else {
            vacancyBodyLayout.setError(null);
            return true;
        }
    }

    private boolean validateVacancyCity(){
        String vacancyCityInput = Objects.requireNonNull(vacancyCityLayout.getEditText()).getText().toString().trim();

        if(vacancyCityInput.isEmpty()){
            vacancyCityLayout.setError("City is required");
            return  false;
        }else if(vacancyCityInput.length() > 15){
            vacancyCityLayout.setError("City is too long");
            return  false;
        }
        else {
            vacancyCityLayout.setError(null);
            return true;
        }
    }


    public void addVacancy(View view) {

        if(!validateVacancyName() | !validateVacancyBody() | !validateVacancyCity()){
            return;
        }

        GoogleSignInAccount account = FireBaseDbHelper.getCurrentAccount(getApplicationContext());

        Vacancy.writeVacancy(
                Objects.requireNonNull(account.getPhotoUrl()).toString(),
                account.getId(),
                Objects.requireNonNull(vacancyNameLayout.getEditText()).getText().toString().trim(),
                Objects.requireNonNull(vacancyBodyLayout.getEditText()).getText().toString().trim(),
                Objects.requireNonNull(vacancyCityLayout.getEditText()).getText().toString().trim(),

                ageFromEditText.getText().toString().trim() + "-" +
                        ageToEditText.getText().toString().trim(),

                salaryFromEditText.getText().toString().trim() + "-" +
                        salaryToEditText.getText().toString().trim() + "$",

                vacancyCategorySpinner.getSelectedItem().toString(),
                requirementsTextView.getText().toString(), account.getDisplayName(),
                currentDateTextView.getText().toString(),
                endDateTextView.getText().toString(), view);
    }
}