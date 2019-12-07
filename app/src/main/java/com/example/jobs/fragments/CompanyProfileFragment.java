package com.example.jobs.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jobs.FirebaseDbHelper;
import com.example.jobs.R;
import com.example.jobs.users.CompanyUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CompanyProfileFragment extends Fragment {

    private Context context;

    public CompanyProfileFragment(Context context) {

        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.company_profile_fragment, container, false);

        final ImageView companyImage = view.findViewById(R.id.company_image_fragment);
        final TextView companyEmail = view.findViewById(R.id.company_email_fragment);
        final TextView companyName = view.findViewById(R.id.company_name_fragment);
        final TextView aboutCompany = view.findViewById(R.id.company_about_fragment);


        FirebaseDbHelper.getCurrentCompanyUserReference(getContext())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        CompanyUser companyUser = dataSnapshot.getValue(CompanyUser.class);

                        assert companyUser != null;
                        Picasso.with(context)
                                .load(companyUser.userProfilePictureURL)
                                .centerInside()
                                .fit()
                                .into(companyImage);
                        companyEmail.setText(companyUser.userEmail);
                        companyName.setText(companyUser.username);
                        aboutCompany.setText(companyUser.description);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().setTitle(R.string.app_name);
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle("Profile");
    }
}
