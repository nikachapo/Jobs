package com.example.jobs.profile_configuration_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jobs.FirebaseDbHelper;
import com.example.jobs.MainActivity;
import com.example.jobs.R;
import com.example.jobs.users.CompanyUser;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class CompanyProfileConfigurationActivity extends AppCompatActivity {

    private EditText companyName, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profile_configuration);


        getSupportActionBar().setTitle("Profile configuration");

        companyName = findViewById(R.id.company_name_conf);
        description = findViewById(R.id.company_history_conf);
        Button registerUser = findViewById(R.id.register_conf);
        TextView emailText = findViewById(R.id.email_conf);
        Intent intent = getIntent();


        final String accountID = intent.getStringExtra("accountID");
        final String accountEmail = intent.getStringExtra("accountEmail");
        final String accountProfileURL = intent.getStringExtra("profileUrl");
        emailText.setText(accountEmail);

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Firebase authentication
                firebaseAuthWithGoogle(FirebaseDbHelper.getCurentAccount(getApplicationContext()));

                //create new user
                CompanyUser companyUser = new CompanyUser(accountID, companyName.getText().toString()
                        , accountProfileURL, accountEmail, description.getText().toString());

                companyUser.writeNewUserCompleted(getApplicationContext()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "User added", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(CompanyProfileConfigurationActivity.this,
                                MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Connection problem", Toast.LENGTH_LONG).show();
                    }
                });

            }


        });
    }


    @Override
    public void onBackPressed() {

        DatabaseReference companiesUidRef = FirebaseDbHelper.getDatabaseReference()
                .child("Emails")
                .child(Objects
                        .requireNonNull(FirebaseDbHelper
                                .getCurentAccount(getApplicationContext()).getId()));
        companiesUidRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
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
        FirebaseDbHelper.signOut(this);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        final FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Sign in failed", Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }


}
