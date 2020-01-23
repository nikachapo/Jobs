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

import com.example.jobs.firebase.FireBaseDbHelper;
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

    private Context mContext;
    private ImageView userImage;
    private TextView userEmailText,
            userNameText,
            userCityText,
            userBirthText,
            aboutUserText;


    public UserProfileFragment() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_profile_fragment, container, false);


        userImage = view.findViewById(R.id.user_profile_fragment_user_imageView);
        userEmailText = view.findViewById(R.id.user_profile_fragment_user_email_textView);

        userNameText = view.findViewById(R.id.user_profile_fragment_user_name_textView);
        userCityText = view.findViewById(R.id.user_profile_fragment_user_city_textView);
        userBirthText = view.findViewById(R.id.user_profile_fragment_user_birth_textView);
        aboutUserText = view.findViewById(R.id.user_profile_fragment_about_user_textView);



        FireBaseDbHelper.getCurrentPersonUserReference(getContext())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        PersonUser personUser = dataSnapshot.getValue(PersonUser.class);
                        assert personUser != null;
                        Picasso.with(mContext)
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
        Button editButton = view.findViewById(R.id.user_profile_fragment_edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), UserProfileConfigurationActivity.class);
                intent.putExtra("userName",userNameText.getText().toString());
                intent.putExtra("userCity",userCityText.getText().toString());
                intent.putExtra("userBirth",userBirthText.getText().toString());
                intent.putExtra("aboutUser",aboutUserText.getText().toString());

                startActivity(intent);

                       }
        });
        return view;


    }


}