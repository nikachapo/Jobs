package com.example.jobs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

public class FirebaseDbHelper {


    public static void signOut(final Context context) {
        getCurrentClient(context).signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(context, "Signed out", Toast.LENGTH_LONG).show();
                context.startActivity(new Intent(context,LogInActivity.class));
                ((Activity) context).finish();

            }
        });
    }
    private static GoogleSignInOptions getGSO() {
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("259431725220-i7kkj3sgmburrdkrhe6v6ijglog12vmc.apps.googleusercontent.com")
                .requestEmail()
                .build();
    }


    public static DatabaseReference getDatabaseReference() {
        return FirebaseDatabase.getInstance().getReference();
    }


    public static DatabaseReference getCurrentPersonUserReference(Context context) {
        return getDatabaseReference().child("Users").child(Objects.requireNonNull(getCurentAccount(context).getId()));
    }

    public static DatabaseReference getCurrentCompanyUserReference(Context context) {
        return getDatabaseReference().child("Companies").child(Objects.requireNonNull(getCurentAccount(context).getId()));
    }

    public static GoogleSignInAccount getCurentAccount(Context context) {
        return GoogleSignIn.getLastSignedInAccount(context);
    }

    public static GoogleSignInClient getCurrentClient(Context context) {
        return GoogleSignIn.getClient(context, getGSO());
    }

    public static void loadImageWithPicasso(int width,int height,Context context,
                                            String userProfilePictureURL,
                                            final ImageView imageView, final Resources resources){
        Picasso.with(context).load(userProfilePictureURL)

                .resize(width, height)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap imageBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                        RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(resources, imageBitmap);
                        imageDrawable.setCircular(true);
                        imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                        imageView.setImageDrawable(imageDrawable);
                    }

                    @Override
                    public void onError() {
                        imageView.setImageResource(R.drawable.ic_account_black_24dp);
                    }
                });
    }

}


