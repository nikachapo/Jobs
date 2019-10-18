package com.example.jobs;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.jobs.vacancy.Vacancy;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AddVacancyActivity extends AppCompatActivity {
    private DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
    GoogleSignInClient mGoogleSignInClient;

    private TextInputLayout vacancyNameLayout, vacancyBodyLayput, vacancyCityLayout;
    private EditText ageFromEditText;
    private EditText ageToEditText;
    private EditText salaryFromEditText;
    private EditText salaryToEditText;
    private TextView requirementsTextView;
    private EditText requirementsEditText;
    private Spinner vacancyCategorySpinner;
    private Button addVacancy, addRequirementsToTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vacancy);

        vacancyNameLayout = findViewById(R.id.vacancy_name_textInputEditText);
        vacancyBodyLayput = findViewById(R.id.vacancy_body_textInputEditText);
        vacancyCityLayout = findViewById(R.id.vacancy_city_textInputEditText);
        ageFromEditText = findViewById(R.id.age_from_edittext);
        ageToEditText = findViewById(R.id.age_to_edittext);
        salaryFromEditText= findViewById(R.id.salaty_from_edittext);
        salaryToEditText = findViewById(R.id.salaty_to_edittext);
        requirementsEditText = findViewById(R.id.vacancy_requirements_edittext);
        requirementsTextView = findViewById(R.id.vacancy_requirements_textview);
        vacancyCategorySpinner = findViewById(R.id.vacancy_category_spinner);
        addRequirementsToTextView = findViewById(R.id.add_requirements_to_textview_button);
        addVacancy = findViewById(R.id.add_vacancy);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        final GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);


        addRequirementsToTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requirementsTextView.setText(requirementsTextView.getText().toString()+
                        "◉"+requirementsEditText.getText().toString()+"\n");
                requirementsEditText.setText("");
            }
        });
        addVacancy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vacancy.writePost(account.getId(),
                        vacancyNameLayout.getEditText().getText().toString().trim(),
                        vacancyBodyLayput.getEditText().getText().toString().trim(),
                        vacancyCityLayout.getEditText().getText().toString().trim(),
                        ageFromEditText.getText().toString().trim()+"-"+
                                ageToEditText.getText().toString().trim(),
                        salaryFromEditText.getText().toString().trim()+"-"+
                                salaryToEditText.getText().toString().trim(),
                        vacancyCategorySpinner.getSelectedItem().toString(),
                        requirementsTextView.getText().toString()
                        );
            }
        });
    }
}
