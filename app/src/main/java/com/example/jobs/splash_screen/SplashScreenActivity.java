package com.example.jobs.splash_screen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.jobs.LogInActivity;
import com.example.jobs.R;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private ImageView splashIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);



        splashIcon = findViewById(R.id.activity_splash_logo);
        /* New Handler to start the LoginActivity
         * and close this Splash-Screen after some seconds.*/

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the LoginActivity. */
                Intent mainIntent = new Intent(SplashScreenActivity.this, LogInActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, 1000);

    }

    @Override
    protected void onStart() {
        super.onStart();
        splashIcon.setAnimation(AnimationUtils
                .loadAnimation(getApplicationContext(), R.anim.slide_animation));

    }
}
