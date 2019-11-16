package com.example.jobs;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.jobs.users.CompanyUser;
import com.example.jobs.vacancy.Vacancy;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spark.submitbutton.SubmitButton;

import java.util.Objects;


public class AddVacancyActivity extends AppCompatActivity {

    GoogleSignInClient mGoogleSignInClient;
    private DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
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


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        final GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);


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


        final CompanyUser[] companyUser = new CompanyUser[1];
        assert account != null;
        DatabaseReference usersUidRef = mRef
                .child("Companies")
                .child(Objects.requireNonNull(account.getId()));
        usersUidRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 companyUser[0] = dataSnapshot.getValue(CompanyUser.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        addVacancy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vacancy.writeVacancy(String.valueOf(
                       companyUser[0].userProfilePictureURL), account.getId(),
                        vacancyNameLayout.getEditText().getText().toString().trim(),
                        vacancyBodyLayput.getEditText().getText().toString().trim(),
                        vacancyCityLayout.getEditText().getText().toString().trim(),

                        ageFromEditText.getText().toString().trim() + "-" +
                                ageToEditText.getText().toString().trim(),

                        salaryFromEditText.getText().toString().trim() + "-" +
                                salaryToEditText.getText().toString().trim(),

                        vacancyCategorySpinner.getSelectedItem().toString(),
                        requirementsTextView.getText().toString(), getApplicationContext()
                        , v);
            }
        });
    }
}