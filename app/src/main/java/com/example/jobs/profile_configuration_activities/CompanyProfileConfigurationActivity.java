package com.example.jobs.profile_configuration_activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.example.jobs.users.CompanyUser;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class CompanyProfileConfigurationActivity extends AppCompatActivity {

    private EditText companyName, companyHistory;
    private GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profile_configuration);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Registration");

        companyName = findViewById(R.id.company_name_conf);
        companyHistory = findViewById(R.id.company_history_conf);
        Button registerUser = findViewById(R.id.register_conf);
        TextView emailText = findViewById(R.id.email_conf);
        Intent intent= getIntent();




        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);




        final String accountID = intent.getStringExtra("accountID");
        final String accountEmail = intent.getStringExtra("accountEmail");
        final String accountProfileURL = intent.getStringExtra("profileUrl");
        emailText.setText(accountEmail);

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create new user
                CompanyUser companyUser = new CompanyUser(accountID,companyName.getText().toString()
                        ,accountProfileURL,accountEmail,companyHistory.getText().toString());
                companyUser.writeCompanyUser();
                Toast.makeText(getApplicationContext(),"Company added",Toast.LENGTH_LONG).show();

                startActivity(new Intent(CompanyProfileConfigurationActivity.this, MainActivity.class));
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
        mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(), "Signed out", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
