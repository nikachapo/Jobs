package com.example.jobs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jobs.users.SignedInUser;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class UserProfileActivity extends AppCompatActivity {
    private Button addVacancyAcivityButton;
    private static final String COMPANIES_TABLE_NAME = "Companies";
    //    private GoogleSignInClient mGoogleSignInClient;
    private DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        addVacancyAcivityButton = findViewById(R.id.add_vacancy_activity_button);
        TextView info = findViewById(R.id.user_information);
        ImageView userProfileImage = findViewById(R.id.user_profile_picture);
        Button signOutButton = findViewById(R.id.sign_out_button);

        addVacancyAcivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfileActivity.this, AddVacancyActivity.class));
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            String username = account.getDisplayName();
            String email = account.getEmail();
            String id = account.getId();
            Uri photoURL = account.getPhotoUrl();
            info.setText("Username:\n" + username + "\nEmail:\n" + email);
            Picasso.with(getApplicationContext())
                    .load(photoURL)
                    .centerInside()
                    .fit()
                    .into(userProfileImage);
        }


//      Check if current user is CompanyUser.
//      If it is addVacancy button will become visible.
        assert account != null;
        DatabaseReference companiesUidRef = mRef
                .child(COMPANIES_TABLE_NAME)
                .child(Objects.requireNonNull(account.getId()));

        companiesUidRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    addVacancyAcivityButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
                startActivity(new Intent(UserProfileActivity.this, LogInActivity.class));

            }
        });

    }


    private void signOut() {
        SignedInUser.signOut(this);
    }
}
