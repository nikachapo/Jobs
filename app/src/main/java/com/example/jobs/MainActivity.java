package com.example.jobs;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jobs.firebase.FireBaseDbHelper;
import com.example.jobs.fragments.CompaniesListFragment;
import com.example.jobs.fragments.CompanyProfileFragment;
import com.example.jobs.fragments.UserProfileFragment;
import com.example.jobs.fragments.VacancyListFragment;
import com.example.jobs.users.CompanyUser;
import com.example.jobs.users.PersonUser;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.zip.Inflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {
    private boolean userIsCompany;
    private DrawerLayout drawerLayout;
    private Fragment selectedFragment = null;
    private TextView drawerEmail;
    private ImageView drawerImage;
    private FloatingActionButton addVacancyFloatingButton;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //      Check if current user is CompanyUser.
        //      If it is addVacancy button will become visible.
        checkUser();


        addVacancyFloatingButton = findViewById(R.id.activity_main_floating_button);

        // customize toolbar
        Toolbar toolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
        //###############################################


        drawerLayout = findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        NavigationView navigationView = findViewById(R.id.activity_main_navigationView);
        View headerView = navigationView.getHeaderView(0);
        //set drawer item values
        drawerEmail = headerView.findViewById(R.id.drawer_user_email);
        drawerImage = headerView.findViewById(R.id.drawer_user_image);
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
                            selectedFragment = new CompanyProfileFragment();
                        } else {
                            selectedFragment = new UserProfileFragment();
                        }
                        break;

                }
                if (selectedFragment != null) {
                    onBackPressed();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack("profile")
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(R.id.activity_main_fragment_frame, selectedFragment).commit();
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
                    .replace(R.id.activity_main_fragment_frame, selectedFragment).commit();
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
        FireBaseDbHelper.getCompanyUserReference(this)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            //if data in Companies key exists userIsCompany variable will become true
                            userIsCompany = true;
                        }

                        setNavigationDrawerInformation(userIsCompany);


                            addVacancyFloatingButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (userIsCompany) {
                                        startActivity(new Intent(MainActivity.this, AddVacancyActivity.class));
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Register as Company to add" +
                                                "Vacancy", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    //sets information of drawer
    private void setNavigationDrawerInformation(boolean userIsCompany) {
        if (userIsCompany) {
            FireBaseDbHelper.getCompanyUserReference(getApplicationContext())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            CompanyUser companyUser = dataSnapshot.getValue(CompanyUser.class);
                            assert companyUser != null;
                            drawerEmail.setText(companyUser.userEmail);
                            FireBaseDbHelper.loadImageWithPicasso(50, 50, getApplicationContext(),
                                    companyUser.userProfilePictureURL, drawerImage, getResources());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        } else {
            FireBaseDbHelper.getCurrentPersonUserReference(getApplicationContext())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            PersonUser personUser = dataSnapshot.getValue(PersonUser.class);
                            assert personUser != null;
                            drawerEmail.setText(personUser.userEmail);
                            FireBaseDbHelper.loadImageWithPicasso(50, 50, getApplicationContext(),
                                    personUser.userProfilePictureURL, drawerImage, getResources());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    }

    //signing out from profile
    private void signOut() {
        FireBaseDbHelper.signOut(this);
    }


    public void changeFragment(View view) {
        GoogleSignInAccount account = FireBaseDbHelper.getCurrentAccount(this);
        switch (view.getId()) {
            case R.id.activity_main_home_button:
                selectedFragment = new VacancyListFragment();
                break;
            case R.id.activity_main_favourites_button:
                selectedFragment = new VacancyListFragment(account.getId(), true);
                break;
            case R.id.activity_main_companies_button:
                selectedFragment = new CompaniesListFragment();
        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction().addToBackStack("fragment")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.activity_main_fragment_frame, selectedFragment).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.app_bar_search) {
            startActivity(new Intent(MainActivity.this, SearchActivity.class));
        }
        return true;
    }
}