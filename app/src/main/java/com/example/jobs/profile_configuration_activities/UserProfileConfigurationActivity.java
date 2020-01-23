package com.example.jobs.profile_configuration_activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jobs.MainActivity;
import com.example.jobs.R;
import com.example.jobs.fragments.DatePickerFragment;
import com.example.jobs.users.PersonUser;
import com.example.jobs.firebase.FireBaseDbHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class UserProfileConfigurationActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener {

    private TextView birthDate;
    private TextInputLayout city, username, about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_configuration);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Edit intro");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView email = findViewById(R.id.person_email_conf);
        birthDate = findViewById(R.id.person_age_conf);
        city = findViewById(R.id.person_city_conf);
        username = findViewById(R.id.person_username_conf);
        about = findViewById(R.id.person_about_conf);
        Button register = findViewById(R.id.person_register);


        Intent intent = getIntent();

        final boolean userIsRegistered = intent.getBooleanExtra("userIsRegistered", true);
        if (userIsRegistered) {
            Objects.requireNonNull(getSupportActionBar()).setTitle("შესწორება");
        } else {
            Objects.requireNonNull(getSupportActionBar()).setTitle("რეგისტრაცია");
        }

        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");

            }
        });

        Objects.requireNonNull(city.getEditText()).setText(intent.getStringExtra("userCity"));
        Objects.requireNonNull(username.getEditText()).setText(intent.getStringExtra("userName"));
        Objects.requireNonNull(about.getEditText()).setText(intent.getStringExtra("aboutUser"));


        final String accountID = FireBaseDbHelper.getCurrentAccount(this).getId();
        final String accountEmail = FireBaseDbHelper.getCurrentAccount(this).getEmail();
        final String accountProfileURL = Objects.requireNonNull(
                FireBaseDbHelper.getCurrentAccount(this)
                .getPhotoUrl()).toString();


        email.setText(accountEmail);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userIsRegistered) {

                    FireBaseDbHelper.getCurrentPersonUserReference(getApplicationContext())
                            .child("username").setValue(username.getEditText().getText().toString());
                    FireBaseDbHelper.getCurrentPersonUserReference(getApplicationContext())
                            .child("birthDate").setValue( birthDate.getText().toString());
                    FireBaseDbHelper.getCurrentPersonUserReference(getApplicationContext())
                            .child("city").setValue(city.getEditText().getText().toString());
                    FireBaseDbHelper.getCurrentPersonUserReference(getApplicationContext())
                            .child("about").setValue(about.getEditText().getText().toString());


                } else {
                    //creating new user
                    PersonUser personUser = new PersonUser(
                            username.getEditText().getText().toString(),
                            accountProfileURL,
                            accountID,
                            accountEmail,
                            birthDate.getText().toString(),
                            city.getEditText().getText().toString(),
                            about.getEditText().getText().toString());

                    personUser.writeNewUserTask(getApplicationContext())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getApplicationContext(),
                                            "გილოცავთ თქვენ დარეგისტრირდით",
                                            Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(
                                            UserProfileConfigurationActivity.this,
                                            MainActivity.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "კავშირის პრობლე,ა",
                                    Toast.LENGTH_LONG).show();
                        }
                    });


                }
            }
        });
    }

    @Override
    public void onBackPressed() {

        DatabaseReference companiesUidRef = FireBaseDbHelper.getDatabaseReference()
                .child("Emails")
                .child(Objects
                        .requireNonNull(FireBaseDbHelper
                                .getCurrentAccount(getApplicationContext()).getId()));
        companiesUidRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    signOut();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    private void signOut() {
        FireBaseDbHelper.signOut(this);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        String currentDateString = DateFormat.getDateInstance().format(calendar.getTime());
        birthDate.setText(currentDateString);

    }


}