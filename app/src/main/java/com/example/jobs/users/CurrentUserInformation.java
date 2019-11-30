package com.example.jobs.users;

import android.content.Context;
import android.content.res.Resources;

import com.example.jobs.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class CurrentUserInformation {
    private static boolean userIsCompany;

    public static GoogleSignInClient getGoogleSignInClient(Context context) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(Resources.getSystem().getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        return GoogleSignIn.getClient(context, gso);
    }

    public static GoogleSignInAccount getGoogleSignInAccount(Context context) {
        return GoogleSignIn.getLastSignedInAccount(context);
    }

    public static boolean isUserIsCompany() {
        return userIsCompany;
    }

    public void setUserIsCompany(boolean userIsCompany) {
        this.userIsCompany = userIsCompany;
    }
}
