package com.example.jobs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.jobs.users.CompanyUser;
import com.example.jobs.users.PersonUser;
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
    private static final String USERS_TABLE_NAME = "Users";
    private static final String COMPANIES_TABLE_NAME = "Companies";

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
        SignInButton signInButton = findViewById(R.id.sign_in_button);

        mAuth = FirebaseAuth.getInstance();

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
                    makeToast("აირჩიეთ ერთ-ერთი");
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
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                switch (checkedRadioButtonId) {
                    case R.id.radio_person:
                        //person radio is selected
                        DatabaseReference usersUidRef = mRef
                                .child(USERS_TABLE_NAME)
                                .child(Objects.requireNonNull(account.getId()));

                        usersUidRef.
                                addListenerForSingleValueEvent(
                                        addNewUserValueEventListener(USERS_TABLE_NAME,
                                                "შეიქმნა ახალი მომხმარებელი",account));
                        break;

                    case R.id.radio_company:
                        //Company radio is selected
                        DatabaseReference companiesUidRef = mRef
                                .child(COMPANIES_TABLE_NAME)
                                .child(Objects.requireNonNull(account.getId()));

                        companiesUidRef.addListenerForSingleValueEvent(
                                addNewUserValueEventListener(COMPANIES_TABLE_NAME,
                                "შეიქმნა ახალი კომპანია",account));
                        break;
                }
                startActivity(intent);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("MAin", "Google sign in failed", e);
                makeToast("შესვლის პრობლემა");

            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("authwith google", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Main", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Main", "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.",
                                    Snackbar.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

    private ValueEventListener addNewUserValueEventListener(final String tableName, final String toastMessage, final GoogleSignInAccount account){

        ValueEventListener companiesEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    //create new user
                    CompanyUser companyUser= new CompanyUser();
                    companyUser.writeCompanyUser(mRef, account.getEmail(),
                            account.getDisplayName(),
                            Objects.requireNonNull(account.getPhotoUrl()).toString(),
                            account.getId(), tableName);
                    makeToast(toastMessage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        return companiesEventListener;
    }

    private void makeToast(String text){
        Toast.makeText(LogInActivity.this, text, Toast.LENGTH_SHORT).show();
    }

}