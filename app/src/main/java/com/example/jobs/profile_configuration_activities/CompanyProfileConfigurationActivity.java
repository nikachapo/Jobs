package com.example.jobs.profile_configuration_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jobs.firebase.FireBaseDbHelper;
import com.example.jobs.MainActivity;
import com.example.jobs.R;
import com.example.jobs.users.CompanyUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class CompanyProfileConfigurationActivity extends AppCompatActivity {

    private TextInputLayout companyName, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profile_configuration);




        companyName = findViewById(R.id.company_name_conf);
        description = findViewById(R.id.company_history_conf);
        Button registerUser = findViewById(R.id.register_conf);
        TextView emailText = findViewById(R.id.email_conf);

        Intent intent = getIntent();

        boolean userIsRegistered = intent.getBooleanExtra("userIsRegistered",true);
        if(userIsRegistered){
        Objects.requireNonNull(getSupportActionBar()).setTitle("Edit Profile");
        }else {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Registration");
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        companyName.getEditText().setText(intent.getStringExtra("companyName"));
        description.getEditText().setText(intent.getStringExtra("companyAbout"));

        final String accountID = FireBaseDbHelper.getCurrentAccount(this).getId();
        final String accountEmail = FireBaseDbHelper.getCurrentAccount(this).getEmail();
        final String accountProfileURL = FireBaseDbHelper.getCurrentAccount(this).getPhotoUrl().toString();

        emailText.setText(accountEmail);

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                //Firebase authentication
//                firebaseAuthWithGoogle(FireBaseDbHelper.getCurrentAccount(getApplicationContext()));

                //create new user
                CompanyUser companyUser = new CompanyUser(accountID, companyName.getEditText().getText().toString()
                        , accountProfileURL, accountEmail, description.getEditText().getText().toString());

                companyUser.writeNewUserTask(getApplicationContext()).addOnCompleteListener(new OnCompleteListener<Void>() {
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

        DatabaseReference companiesUidRef = FireBaseDbHelper.getDatabaseReference()
                .child("Emails")
                .child(Objects
                        .requireNonNull(FireBaseDbHelper
                                .getCurrentAccount(getApplicationContext()).getId()));
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
        FireBaseDbHelper.signOut(this);
    }

//    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
//        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
//        final FirebaseAuth mAuth;
//        mAuth = FirebaseAuth.getInstance();
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            FirebaseUser user = mAuth.getCurrentUser();
//
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Toast.makeText(getApplicationContext(), "Sign in failed", Toast.LENGTH_LONG).show();
//                        }
//
//                    }
//                });
//    }


}
