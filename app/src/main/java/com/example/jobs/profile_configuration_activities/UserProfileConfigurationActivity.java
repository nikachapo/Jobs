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
import com.example.jobs.users.PersonUser;
import com.example.jobs.FirebaseDbHelper;
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

public class UserProfileConfigurationActivity extends AppCompatActivity {

    private EditText age, city, username, about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_configuration);

        getSupportActionBar().setTitle("Registration");

        TextView email = findViewById(R.id.person_email_conf);
        age = findViewById(R.id.person_age_conf);
        city = findViewById(R.id.person_city_conf);
        username = findViewById(R.id.person_username_conf);
        about = findViewById(R.id.person_about_conf);
        Button register = findViewById(R.id.person_register);


        Intent intent = getIntent();

        final String accountID = intent.getStringExtra("accountID");
        final String accountEmail = intent.getStringExtra("accountEmail");
        final String accountProfileURL = intent.getStringExtra("profileUrl");

        email.setText(accountEmail);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                firebaseAuthWithGoogle(FirebaseDbHelper.getCurentAccount(getApplicationContext()));

                //if writeNewUserCompleted returns true move to MainActivity

                //creating new user
                PersonUser personUser = new PersonUser(username.getText().toString(),accountProfileURL,accountID,
                        accountEmail,age.getText().toString(),city.getText().toString(),
                        about.getText().toString());

                personUser.writeNewUserCompleted(getApplicationContext()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "User added", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(UserProfileConfigurationActivity.this,
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