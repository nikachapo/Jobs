package com.example.jobs.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jobs.firebase.FireBaseDbHelper;
import com.example.jobs.fragments.BottomSheet;
import com.example.jobs.CompanyProfileActivity;
import com.example.jobs.R;
import com.example.jobs.vacancy.Vacancy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.varunest.sparkbutton.SparkButton;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;


public class VacancyAdapter extends RecyclerView.Adapter<VacancyAdapter.VacanciesViewHolder> {


    private ArrayList<Vacancy> vacancies;
    private Context context;
    private boolean userIsCompany;

    public VacancyAdapter(Context context, ArrayList<Vacancy> vacancies) {
        this.context = context;
        this.vacancies = vacancies;
    }

    @NonNull
    @Override
    public VacancyAdapter.VacanciesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_list_item, parent, false);
        TextView vacancyNameTextView = view.findViewById(R.id.list_item_vacancyName_textView);
        TextView vacancyBodyTextView = view.findViewById(R.id.list_item_vacancyBody_textView);
        TextView vacancyStartDateTextView = view.findViewById(R.id.list_item_start_date);
        TextView vacancyEndDateTextView = view.findViewById(R.id.list_item_end_date);
        ImageView vacancyOwnerLogoImageView = view.findViewById(R.id.list_item_profilePicture_imageView);
        TextView vacancyOwnerName = view.findViewById(R.id.list_item_company_textView);
        TextView vacancyCity = view.findViewById(R.id.list_item_city_textView);

        ConstraintLayout vacancyDetailsLayout = view.findViewById(R.id.vacancy_details_layout);
        SparkButton star = view.findViewById(R.id.list_item_spark_star_button);
        return new VacanciesViewHolder(view, vacancyNameTextView,
                vacancyBodyTextView, vacancyStartDateTextView,vacancyEndDateTextView,
                vacancyOwnerLogoImageView, vacancyDetailsLayout, star, vacancyOwnerName, vacancyCity);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final VacanciesViewHolder holder, int position) {
        final Vacancy vacancy = vacancies.get(position);


        holder.itemView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_animation));

        Picasso.with(context).load(vacancy.ownerProfileURL)
                .resize(50, 50)
                .into(holder.vacancyOwnerLogoImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap imageBitmap = ((BitmapDrawable) holder.vacancyOwnerLogoImageView.getDrawable()).getBitmap();
                        RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), imageBitmap);
                        imageDrawable.setCircular(true);
                        imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                        holder.vacancyOwnerLogoImageView.setImageDrawable(imageDrawable);
                    }

                    @Override
                    public void onError() {
                        holder.vacancyOwnerLogoImageView.setImageResource(R.drawable.ic_default_company_image);
                    }
                });

        holder.vacancyNameTextView.setText(vacancy.vacancyHeader);
        holder.vacancyBodyTextView.setText(vacancy.vacancyBody);
        holder.vacancyStartDateTextView.setText(vacancy.startDate);
        holder.vacancyEndDateTextView.setText(vacancy.endDate);
        holder.vacancyOwnerNameTextView.setText(vacancy.companyName);
        holder.vacancyCityTextView.setText(vacancy.vacancyCity);

        holder.vacancyOwnerLogoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), CompanyProfileActivity.class);
                intent.putExtra("imageurl", vacancy.ownerProfileURL);
                intent.putExtra("ownerID", vacancy.ownerID);
                intent.putExtra("companyName", vacancy.companyName);
                context.startActivity(intent);

            }
        });


        holder.vacancyDetailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheet bottomSheet = new BottomSheet(vacancy);
                bottomSheet.show(((AppCompatActivity) context).getSupportFragmentManager(), vacancy.vacancyHeader);


            }


        });

//#############################################################3

        FireBaseDbHelper.getCompanyUserReference(context)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            //if data in Companies key exists userIsCompany variable will become true
                            userIsCompany = true;
                        }
                        if (userIsCompany) {
                            FireBaseDbHelper.getCompanyUserReference(context).child("Favourites").child(vacancy.vacancyID)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                holder.starButton.setChecked(true);
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                        } else {
                            FireBaseDbHelper.getCurrentPersonUserReference(context).child("Favourites").child(vacancy.vacancyID)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                holder.starButton.setChecked(true);
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                        }

                        holder.starButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final DatabaseReference reference;

                                if (userIsCompany) {
                                    reference = FireBaseDbHelper.getCompanyUserReference(context)
                                            .child("Favourites").child(vacancy.vacancyID);
                                } else
                                    reference = FireBaseDbHelper.getCurrentPersonUserReference(context)
                                            .child("Favourites").child(vacancy.vacancyID);


                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            reference.setValue(null);
                                            holder.starButton.setChecked(false);

                                        } else {
                                            reference.setValue(vacancy);
                                            holder.starButton.setChecked(true);
                                            holder.starButton.playAnimation();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


//#############################################################3


    }

    @Override
    public int getItemCount() {
        return vacancies.size();
    }

    static class VacanciesViewHolder extends RecyclerView.ViewHolder {
        TextView vacancyNameTextView,
                vacancyBodyTextView,
                vacancyStartDateTextView,
                vacancyEndDateTextView,
                vacancyOwnerNameTextView,
                vacancyCityTextView;
        ImageView vacancyOwnerLogoImageView;
        ConstraintLayout vacancyDetailLayout;
        SparkButton starButton;

        VacanciesViewHolder(View itemView, TextView vacancyNameTextView,
                            TextView vacancyBodyTextView,
                            TextView vacancyStartDateTextView,
                            TextView vacancyEndDateTextView,
                            ImageView vacancyOwnerLogoImageView,
                            ConstraintLayout vacancyDetailLayout,
                            SparkButton starButton, TextView  vacancyOwnerNameTextView,
                            TextView vacancyCityTextView) {
            super(itemView);
            this.vacancyNameTextView = vacancyNameTextView;
            this.vacancyBodyTextView = vacancyBodyTextView;
            this.vacancyStartDateTextView = vacancyStartDateTextView;
            this.vacancyEndDateTextView = vacancyEndDateTextView;
            this.vacancyOwnerLogoImageView = vacancyOwnerLogoImageView;
            this.vacancyDetailLayout = vacancyDetailLayout;
            this.starButton = starButton;
            this.vacancyOwnerNameTextView = vacancyOwnerNameTextView;
            this.vacancyCityTextView = vacancyCityTextView;

        }
    }

}
