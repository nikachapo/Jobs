package com.example.jobs;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.jobs.fragments.CompanyProfileFragment;
import com.example.jobs.fragments.UserProfileFragment;
import com.example.jobs.fragments.VacancyListFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {
    private boolean userIsCompany = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);



        Fragment vacancyListFragment = new VacancyListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame, vacancyListFragment).commit();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);



        final GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);


        //      Check if current user is CompanyUser.
        //      If it is addVacancy button will become visible.
        assert account != null;
        DatabaseReference companiesUidRef = FirebaseDatabase.getInstance().getReference()
                .child("Companies")
                .child(Objects.requireNonNull(account.getId()));
        companiesUidRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    userIsCompany = true;
                    invalidateOptionsMenu();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFragment = null;

                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        selectedFragment = new VacancyListFragment();
                        break;
                    case R.id.nav_profile:
                        if (userIsCompany) {
                            selectedFragment = new CompanyProfileFragment();
                        } else {
                            selectedFragment = new UserProfileFragment();
                        }
                        break;
                    case R.id.nav_menu:
                        startActivity(new Intent(MainActivity.this, UserProfileActivity.class));
                        break;
                }

                if(selectedFragment!=null) {
                    getSupportFragmentManager().beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(R.id.fragment_frame, selectedFragment).commit();
                }
                return true;
            }
        });

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
                //Code to run when the settings item is clicked
                startActivity(new Intent(MainActivity.this, AddVacancyActivity.class));

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}