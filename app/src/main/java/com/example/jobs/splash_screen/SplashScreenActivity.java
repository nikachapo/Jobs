package com.example.jobs.splash_screen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jobs.LogInActivity;
import com.example.jobs.R;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {
    private ImageView splashImage;
    private TextView splashText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        splashImage = findViewById(R.id.splash_image);
        splashText = findViewById(R.id.splash_text);


        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashScreenActivity.this, LogInActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, 1500);

    }

    @Override
    protected void onStart() {
        super.onStart();
        splashText.setAnimation(AnimationUtils
                .loadAnimation(getApplicationContext(), R.anim.user_image_slide_animation));

        splashImage.setAnimation(AnimationUtils
                .loadAnimation(getApplicationContext(), R.anim.vacancy_details_scale_animation));

    }
}
