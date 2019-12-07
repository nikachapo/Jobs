package com.example.jobs;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.jobs.fragments.CompanyProfileFragment;
import com.example.jobs.fragments.UserProfileFragment;
import com.example.jobs.fragments.VacancyListFragment;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {
    private boolean userIsCompany = false;
    private DrawerLayout drawerLayout;
    private Fragment selectedFragment = null;
    private GoogleSignInAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // customize Toolbar
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);
        //###############################################

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        NavigationView navigationView = findViewById(R.id.nav_view);

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
                            selectedFragment = new UserProfileFragment(account.getId(), getApplicationContext());
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
        if(savedInstanceState == null) {
            selectedFragment = new VacancyListFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_frame, selectedFragment).commit();
            //###############################################
        }
        //get current user Google account
        account = FirebaseDbHelper.getCurentAccount(this);

        //      Check if current user is CompanyUser.
        //      If it is addVacancy button will become visible.
        checkUser();

        //set BottomNavigationItem Listeners
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.bottom_nav_home:
                        selectedFragment = new VacancyListFragment();
                        break;
                    case R.id.nav_profile:
                        if (userIsCompany) {
                            selectedFragment = new CompanyProfileFragment(getApplicationContext());
                        } else {
                            selectedFragment = new UserProfileFragment(account.getId(), getApplicationContext());
                        }
                        break;


                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(R.id.fragment_frame, selectedFragment).commit();
                }
                return true;
            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        MenuItem addVacancy = menu.findItem(R.id.action_add_vacancy);
        if (!userIsCompany) {
            addVacancy.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_vacancy:
                //Code to run when the Settings item is clicked
                startActivity(new Intent(MainActivity.this, AddVacancyActivity.class));
                break;
            case R.id.action_bar_search:
                //Code to run when the Search item is clicked
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                break;
        }
        return true;
    }

    private void checkUser() {
        FirebaseDbHelper.getCurrentCompanyUserReference(this)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            //if data in Companies key exists userIsCompany variable will become true
                            userIsCompany = true;
                            invalidateOptionsMenu();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

    private void signOut(){
        FirebaseDbHelper.signOut(this);
    }
}