package com.example.jobs.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jobs.BottomSheet;
import com.example.jobs.CompanyProfileActivity;
import com.example.jobs.R;
import com.example.jobs.vacancy.Vacancy;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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
    private int lastPosition = -1;

    public VacancyAdapter(Context context, ArrayList<Vacancy> vacancies) {
        this.context = context;
        this.vacancies = vacancies;

    }

    @NonNull
    @Override
    public VacancyAdapter.VacanciesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_list_item, parent, false);
        TextView vacancyNameTextView = view.findViewById(R.id.company_name);
        TextView vacancyBodyTextView = view.findViewById(R.id.vacancy_name);
        TextView vacancyDateRangeTextView = view.findViewById(R.id.date_range);
        ImageView vacancyOwnerLogoImageView = view.findViewById(R.id.company_image);
        ConstraintLayout vacancyDetailsLayout = view.findViewById(R.id.vacancy_details_layout);

        return new VacanciesViewHolder(view, vacancyNameTextView,
                vacancyBodyTextView, vacancyDateRangeTextView, vacancyOwnerLogoImageView, vacancyDetailsLayout);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final VacanciesViewHolder holder, int position) {
        final Vacancy vacancy = vacancies.get(position);

        //Item Animation
        if (position > lastPosition) {
            lastPosition = position;
            holder.vacancyOwnerLogoImageView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.user_image_slide_animation));
            holder.vacancyDetailLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.vacancy_details_scale_animation));
        }


        Picasso.with(context).load(vacancy.ownerProfileURL)
                .resize(70, 70)
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

        if (vacancy.vacancyBody.length() > 40) {
            holder.vacancyBodyTextView.setText(vacancy.vacancyBody.substring(0, 30) + "...");
        } else
            holder.vacancyBodyTextView.setText(vacancy.vacancyBody);

        holder.vacancyDateRangeTextView.setText(vacancy.currentTimeStamp);

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
                BottomSheet bottomSheet = new BottomSheet(vacancy.ownerID, vacancy.ownerProfileURL,
                        vacancy.vacancyCity, vacancy.vacancyCategory, vacancy.vacancyHeader, vacancy.vacancyBody,
                        vacancy.requiredAge, vacancy.requirements, vacancy.vacancySalary, vacancy.companyName);
                bottomSheet.show(((AppCompatActivity) context).getSupportFragmentManager(), vacancy.vacancyHeader);

            }
        });


//        setAnimation(holder.itemView,position);
    }

    @Override
    public int getItemCount() {
        return vacancies.size();
    }

    static class VacanciesViewHolder extends RecyclerView.ViewHolder {
        TextView vacancyNameTextView;
        TextView vacancyBodyTextView;
        TextView vacancyDateRangeTextView;
        ImageView vacancyOwnerLogoImageView;
        ConstraintLayout vacancyDetailLayout;

        VacanciesViewHolder(View itemView, TextView vacancyNameTextView,
                            TextView vacancyBodyTextView,
                            TextView vacancyDateRangeTextView,
                            ImageView vacancyOwnerLogoImageView,
                            ConstraintLayout vacancyDetailLayout) {
            super(itemView);
            this.vacancyNameTextView = vacancyNameTextView;
            this.vacancyBodyTextView = vacancyBodyTextView;
            this.vacancyDateRangeTextView = vacancyDateRangeTextView;
            this.vacancyOwnerLogoImageView = vacancyOwnerLogoImageView;
            this.vacancyDetailLayout = vacancyDetailLayout;
        }
    }
//    private void setAnimation(ConstraintLayout vacancyDetailsLayout, int position)
//    {
//        // If the bound view wasn't previously displayed on screen, it's animated
//        if (position > lastPosition)
//        {
//            Animation animation = AnimationUtils.loadAnimation(context, R.anim.user_image_slide_animation);
//            vacancyDetailsLayout.startAnimation(animation);
//            lastPosition = position;
//        }
//    }
}
