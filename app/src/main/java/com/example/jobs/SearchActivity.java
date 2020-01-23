package com.example.jobs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;

import com.example.jobs.fragments.VacancyListFragment;

import java.util.Objects;

public class SearchActivity extends AppCompatActivity {

    private SearchView searchView;
    private Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        overridePendingTransition(R.anim.do_not_move, R.anim.do_not_move);

        searchView = findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("onQuery","start");
                fragment = new VacancyListFragment(true,query.toLowerCase().trim());
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_search_frameLayout, fragment).commit();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}
