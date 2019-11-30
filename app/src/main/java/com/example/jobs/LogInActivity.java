package com.example.jobs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.jobs.profile_configuration_activities.CompanyProfileConfigurationActivity;
import com.example.jobs.profile_configuration_activities.UserProfileConfigurationActivity;
import com.example.jobs.users.CurrentUserInformation;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class LogInActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 0;
    private GoogleSignInClient mGoogleSignInClient;
    private DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth;
    private int checkedRadioButtonId;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);


        radioGroup = findViewById(R.id.radio_group);


        mAuth = FirebaseAuth.getInstance();
        SignInButton signInButton = findViewById(R.id.sign_in_button);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                if (checkedRadioButtonId == -1) {
                    //no item selected
                    makeToast("Choose one");
                } else {
                    signIn();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount lastSignedInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (lastSignedInAccount != null) {
            startActivity(new Intent(LogInActivity.this, MainActivity.class));
            finish();
        }

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                final GoogleSignInAccount account = task.getResult(ApiException.class);

                assert account != null;
                firebaseAuthWithGoogle(account);

                switch (checkedRadioButtonId) {
                    case R.id.radio_person:
                        //person radio is selected

                        DatabaseReference usersUidRef = mRef
                                .child("Emails")
                                .child(Objects.requireNonNull(account.getId()));

                        ValueEventListener usersEventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()) {
                                   Intent loginToRegister = new Intent(LogInActivity.this
                                           , UserProfileConfigurationActivity.class);
                                    loginToRegister.putExtra("accountID",account.getId());
                                    loginToRegister.putExtra("accountEmail",account.getEmail());
                                    loginToRegister.putExtra("profileUrl",account.getPhotoUrl().toString());

                                    //move to configure user profile Activity
                                    startActivity(loginToRegister);
                                } else {
                                    makeToast("Email already exists");
                                    //move to configure Main Activity
                                    startActivity(new Intent(LogInActivity.this
                                            , MainActivity.class));
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        };
                        usersUidRef.addListenerForSingleValueEvent(usersEventListener);
                        break;

                    case R.id.radio_company:
                        //Company radio is selected
                        DatabaseReference companiesUidRef = mRef
                                .child("Emails")
                                .child(Objects.requireNonNull(account.getId()));

                        ValueEventListener companiesEventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()) {
                                    //move to configure company profile Activity
                                    Intent loginToRegister = new Intent(LogInActivity.this
                                            , CompanyProfileConfigurationActivity.class);

                                    loginToRegister.putExtra("accountID",account.getId());
                                    loginToRegister.putExtra("accountEmail",account.getEmail());
                                    loginToRegister.putExtra("profileUrl",account.getPhotoUrl().toString());
                                    startActivity(loginToRegister);
                                } else {
                                    makeToast("Email already exists");
                                    //move to Main Activity
                                    startActivity(new Intent(LogInActivity.this
                                            , MainActivity.class));
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        };
                        companiesUidRef.addListenerForSingleValueEvent(companiesEventListener);

                        break;
                }

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("MAin", "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.",
                                    Snackbar.LENGTH_SHORT).show();

                        }

                    }
                });
    }


    private void makeToast(String text) {
        Toast.makeText(LogInActivity.this, text, Toast.LENGTH_SHORT).show();
    }


}