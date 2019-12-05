package com.example.jobs.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jobs.R;
import com.example.jobs.users.PersonUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class UserProfileFragment extends Fragment implements View.OnClickListener {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private String uID;
    private Context context;
    private ImageView userImage;
    private TextView userEmailText,
            userNameText,
            userCityText,
            userBirthText,
            aboutUserText;
    private EditText
            userNameEditText,
            userCityEditText,
            userBirthEditText,
            aboutUserEditText;
    private Button
            editUserNameButton,
            editUserCityButton,
            editUserBirthButton,
            editUserAboutButton,
            confirmUsernameButton,
            undoUsernameButton,
            confirmCityButton,
            undoCityButton,
            confirmBirthButton,
            undoBirthButton,
            confirmAboutButton,
            undoAboutButton;

    private PersonUser personUser;
    public UserProfileFragment(String uID, Context context) {
        this.uID = uID;
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        personUser = new PersonUser();
        View view = inflater.inflate(R.layout.user_profile_fragment, container, false);

        userImage = view.findViewById(R.id.user_image_fragment);
        userEmailText = view.findViewById(R.id.user_email_fragment);
        userNameText = view.findViewById(R.id.user_nametext_fragment);
        userCityText = view.findViewById(R.id.user_citytext_fragment);
        userBirthText = view.findViewById(R.id.user_birthtext_fragment);
        aboutUserText = view.findViewById(R.id.user_abouttext_fragment);

        userNameEditText = view.findViewById(R.id.user_name_fragment);
        userCityEditText = view.findViewById(R.id.user_city_fragment);
        userBirthEditText = view.findViewById(R.id.user_birth_fragment);
        aboutUserEditText = view.findViewById(R.id.user_about_fragment);


        editUserNameButton = view.findViewById(R.id.edit_user_name_fragment);
        confirmUsernameButton = view.findViewById(R.id.confirm_user_name_fragment);
        undoUsernameButton = view.findViewById(R.id.undo_user_name_fragment);

        editUserCityButton = view.findViewById(R.id.edit_user_city_fragment);
        confirmCityButton = view.findViewById(R.id.confirm_user_city_fragment);
        undoCityButton = view.findViewById(R.id.undo_user_city_fragment);

        editUserBirthButton = view.findViewById(R.id.edit_user_birth_fragment);
        confirmBirthButton = view.findViewById(R.id.confirm_user_birth_fragment);
        undoBirthButton = view.findViewById(R.id.undo_user_birth_fragment);

        editUserAboutButton = view.findViewById(R.id.edit_user_about_fragment);
        confirmAboutButton = view.findViewById(R.id.confirm_user_about_fragment);
        undoAboutButton = view.findViewById(R.id.undo_user_about_fragment);


        editUserNameButton.setOnClickListener(this);
        confirmUsernameButton.setOnClickListener(this);
        undoUsernameButton.setOnClickListener(this);

        editUserCityButton.setOnClickListener(this);
        confirmCityButton.setOnClickListener(this);
        undoCityButton.setOnClickListener(this);

        editUserBirthButton.setOnClickListener(this);
        confirmBirthButton.setOnClickListener(this);
        undoBirthButton.setOnClickListener(this);

        editUserAboutButton.setOnClickListener(this);
        confirmAboutButton.setOnClickListener(this);
        undoAboutButton.setOnClickListener(this);


        databaseReference.child("Users").child(uID)
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_user_name_fragment:
                setViewsForUserName();
                break;
            case R.id.confirm_user_name_fragment:
//                setUserInformation("username", userNameEditText.getText().toString());
                personUser.setUsername(uID,userNameEditText.getText().toString(),getContext());
                setViewsForUserName();
                break;
            case R.id.undo_user_name_fragment:
                setViewsForUserName();
                break;


            case R.id.edit_user_city_fragment:
                setViewsForUserCity();
                break;
            case R.id.confirm_user_city_fragment:
//                setUserInformation("city", userCityEditText.getText().toString());
                personUser.setCity(uID,userCityEditText.getText().toString(),getContext());
                setViewsForUserCity();
                break;
            case R.id.undo_user_city_fragment:
                setViewsForUserCity();
                break;


            case R.id.edit_user_birth_fragment:
                setViewsForUserBirth();
                break;
            case R.id.confirm_user_birth_fragment:
//                setUserInformation("birthDate", userBirthEditText.getText().toString());
                personUser.setBirthDate(uID,userBirthEditText.getText().toString(),getContext());
                setViewsForUserBirth();
                break;
            case R.id.undo_user_birth_fragment:
                setViewsForUserBirth();
                break;

            case R.id.edit_user_about_fragment:
                setViewsForUserAbout();
                break;
            case R.id.confirm_user_about_fragment:
//                setUserInformation("about", aboutUserEditText.getText().toString());
                personUser.setAbout(uID,aboutUserEditText.getText().toString(),getContext());
                setViewsForUserAbout();
                break;
            case R.id.undo_user_about_fragment:
                setViewsForUserAbout();
                break;


        }
    }

//    private void setUserInformation(final String key, final String value) {
//        databaseReference.child("Users").child(this.uID)
//                .child(key).setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                makeText("Success");
//            }
//        });
//    }

    private void makeText(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }


    private void setViewsAfterEditButtonTouch(Button edit, TextView text, EditText editText,
                                              Button confirm, Button undo) {
        if (edit.getVisibility() == View.VISIBLE) {
            edit.setVisibility(View.GONE);
            text.setVisibility(View.GONE);
            editText.setVisibility(View.VISIBLE);
            editText.setText(editText.getText());
            confirm.setVisibility(View.VISIBLE);
            undo.setVisibility(View.VISIBLE);
        } else {
            edit.setVisibility(View.VISIBLE);
            text.setVisibility(View.VISIBLE);
            editText.setVisibility(View.GONE);
            confirm.setVisibility(View.GONE);
            undo.setVisibility(View.GONE);
        }
    }


    private void setViewsForUserName() {
        setViewsAfterEditButtonTouch(editUserNameButton, userNameText, userNameEditText,
                confirmUsernameButton, undoUsernameButton);
    }

    private void setViewsForUserCity() {
        setViewsAfterEditButtonTouch(editUserCityButton, userCityText, userCityEditText,
                confirmCityButton, undoCityButton);
    }

    private void setViewsForUserBirth() {
        setViewsAfterEditButtonTouch(editUserBirthButton, userBirthText, userBirthEditText,
                confirmBirthButton, undoBirthButton);
    }

    private void setViewsForUserAbout() {
        setViewsAfterEditButtonTouch(editUserAboutButton, aboutUserText, aboutUserEditText,
                confirmAboutButton, undoAboutButton);
    }
}
