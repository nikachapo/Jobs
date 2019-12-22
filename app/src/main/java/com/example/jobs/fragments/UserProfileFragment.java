package com.example.jobs.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jobs.FirebaseDbHelper;
import com.example.jobs.R;
import com.example.jobs.profile_configuration_activities.UserProfileConfigurationActivity;
import com.example.jobs.users.PersonUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class UserProfileFragment extends Fragment {

    private Context context;
    private ImageView userImage;
    private TextView userEmailText,
            userNameText,
            userCityText,
            userBirthText,
            aboutUserText;


    public UserProfileFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_profile_fragment, container, false);


        userImage = view.findViewById(R.id.user_image_fragment);
        userEmailText = view.findViewById(R.id.user_email_fragment);

        userNameText = view.findViewById(R.id.user_nametext_fragment);
        userCityText = view.findViewById(R.id.user_citytext_fragment);
        userBirthText = view.findViewById(R.id.user_birthtext_fragment);
        aboutUserText = view.findViewById(R.id.user_abouttext_fragment);

        Button editButton = view.findViewById(R.id.user_edit_fragment);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startActivity(new Intent(getContext(), UserProfileConfigurationActivity.class));
            }
        });


        FirebaseDbHelper.getCurrentPersonUserReference(getContext())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        PersonUser personUser = dataSnapshot.getValue(PersonUser.class);
                        assert personUser != null;
                        Picasso.with(context)
                                .load(personUser.userProfilePictureURL)
                                .centerInside()
                                .fit()
                                .into(userImage);
                        userEmailText.setText(personUser.userEmail);
                        userNameText.setText(personUser.username);
                        userCityText.setText(personUser.city);
                        userBirthText.setText(personUser.birthDate);
                        aboutUserText.setText(personUser.about);
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