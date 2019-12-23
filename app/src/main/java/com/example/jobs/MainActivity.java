package com.example.jobs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jobs.fragments.CompanyProfileFragment;
import com.example.jobs.fragments.UserProfileFragment;
import com.example.jobs.fragments.VacancyListFragment;
import com.example.jobs.users.CompanyUser;
import com.example.jobs.users.PersonUser;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {
    private boolean userIsCompany = false;
    private DrawerLayout drawerLayout;
    private Fragment selectedFragment = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //      Check if current user is CompanyUser.
        //      If it is addVacancy button will become visible.
        checkUser();


        FloatingActionButton addVacancyFloatingButton = findViewById(R.id.floating_button);
        // customize Toolbar
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);
        //###############################################


        RelativeLayout toolbarRelativeLayout = findViewById(R.id.toolbar_linear_layout);
        toolbarRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
            }
        });



        addVacancyFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddVacancyActivity.class));
            }
        });


        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        //set drawer item values
        final TextView drawerEmail = headerView.findViewById(R.id.drawer_user_email);
        final ImageView drawerImage = headerView.findViewById(R.id.drawer_user_image);

        if(userIsCompany){
            FirebaseDbHelper.getCurrentCompanyUserReference(this)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            CompanyUser companyUser = dataSnapshot.getValue(CompanyUser.class);
                            assert companyUser != null;
                            drawerEmail.setText(companyUser.userEmail);
                            FirebaseDbHelper.loadImageWithPicasso(50,50,getApplicationContext(),
                                    companyUser.userProfilePictureURL,drawerImage,getResources());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }else {
            FirebaseDbHelper.getCurrentPersonUserReference(this)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            PersonUser personUser = dataSnapshot.getValue(PersonUser.class);
                            assert personUser != null;
                            drawerEmail.setText(personUser.userEmail);
                            FirebaseDbHelper.loadImageWithPicasso(50,50,getApplicationContext(),
                                    personUser.userProfilePictureURL,drawerImage,getResources());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
        //##########################

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_logout:
                        signOut();
                        break;
                    case R.id.nav_profile:
                        if (userIsCompany) {
                            selectedFragment = new CompanyProfileFragment(getApplicationContext());
                        } else {
                            selectedFragment = new UserProfileFragment(getApplicationContext());
                        }
                        break;

                }
                if (selectedFragment != null) {
                    onBackPressed();
                    getSupportFragmentManager().beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(R.id.fragment_frame, selectedFragment).commit();
                }
                return true;
            }
        });

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //replace Frame with custom Fragments
        if (savedInstanceState == null) {
            selectedFragment = new VacancyListFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_frame, selectedFragment).commit();
            //###############################################
        }

        //########################################################################
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }


    //check if current user is company
    private void checkUser() {
        FirebaseDbHelper.getCurrentCompanyUserReference(this)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            //if data in Companies key exists userIsCompany variable will become true
                            userIsCompany = true;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

    //signing out from profile
    private void signOut() {
        FirebaseDbHelper.signOut(this);
    }


    public void changeFragment(View view) {
        switch (view.getId()) {
            case R.id.home_button:
                selectedFragment = new VacancyListFragment();
                break;
            case R.id.favourites_button:
                break;
        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.fragment_frame, selectedFragment).commit();
        }
    }
}