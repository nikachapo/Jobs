package com.example.jobs.users;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

import com.example.jobs.R;
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

import java.util.Objects;

import androidx.annotation.NonNull;

public class SignedInUser {


    public static void signOut(final Context context) {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("259431725220-i7kkj3sgmburrdkrhe6v6ijglog12vmc.apps.googleusercontent.com")
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(context,gso);
        mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(context, "Signed out", Toast.LENGTH_LONG).show();
                ((Activity) context).finish();
            }
        });
    }



//    private static GoogleSignInOptions getGSO() {
//        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken("259431725220-i7kkj3sgmburrdkrhe6v6ijglog12vmc.apps.googleusercontent.com")
//                .requestEmail()
//                .build();
//    }


//        public static boolean currentUserIsCompany(final Context context) {
//        final boolean[] userIsCompany = {false};
//
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
////      Check if current user is CompanyUser.
////      If it is addVacancy button will become visible.
//        DatabaseReference companiesUidRef = FirebaseDatabase.getInstance().getReference()
//                .child("Companies")
//                .child(account.getId());
//
//        companiesUidRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    userIsCompany[0] = true;
//                    ((Activity) context).invalidateOptionsMenu();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//        return !userIsCompany[0];
//    }

}
