package com.example.jobs.profile_configuration_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jobs.MainActivity;
import com.example.jobs.R;
import com.example.jobs.UserProfileActivity;
import com.example.jobs.users.PersonUser;
import com.example.jobs.users.SignedInUser;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class UserProfileConfigurationActivity extends AppCompatActivity {

    private EditText age, city, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_configuration);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Registration");

        TextView email = findViewById(R.id.person_email_conf);
        age = findViewById(R.id.person_age_conf);
        city = findViewById(R.id.person_city_conf);
        username = findViewById(R.id.person_username_conf);
        Button register = findViewById(R.id.person_register);


        Intent intent = getIntent();

        final String accountID = intent.getStringExtra("accountID");
        final String accountEmail = intent.getStringExtra("accountEmail");
        final String accountProfileURL = intent.getStringExtra("profileUrl");

        email.setText(accountEmail);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create new user
                PersonUser personUser = new PersonUser(username.getText().toString(),
                        accountProfileURL, accountID, accountEmail, age.getText().toString(),
                        city.getText().toString());
                personUser.writePersonUser();
                Toast.makeText(getApplicationContext(),"User added",Toast.LENGTH_LONG).show();

                startActivity(new Intent(UserProfileConfigurationActivity.this,
                        MainActivity.class));

                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        signOut();
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
        SignedInUser.signOut(this);

    }
}