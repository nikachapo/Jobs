package com.example.jobs.adapters;

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

import com.example.jobs.CompanyProfileActivity;
import com.example.jobs.R;
import com.example.jobs.users.CompanyUser;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.CompaniesViewHolder> {

    private Context context;
    private ArrayList<CompanyUser> companies;

    public CompanyAdapter(Context context, ArrayList<CompanyUser> companies){
        this.context = context;
        this.companies = companies;
    }

    @NonNull
    @Override
    public CompaniesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.company_list_item, parent, false);

        ImageView companyListImage = view.findViewById(R.id.company_list_item_profilePicture_imageView);
        TextView companyListName = view.findViewById(R.id.company_list_item_company_textView);
        CardView rootLayout = view.findViewById(R.id.company_list_root_layout);

        return new CompaniesViewHolder(view,companyListName,companyListImage,rootLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull final CompaniesViewHolder holder, int position) {
        holder.itemView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_animation));

        final CompanyUser company = companies.get(position);


        Picasso.with(context).load(company.userProfilePictureURL)
                .resize(50, 50)
                .into(holder.companyImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap imageBitmap = ((BitmapDrawable) holder.companyImageView.getDrawable()).getBitmap();
                        RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), imageBitmap);
                        imageDrawable.setCircular(true);
                        imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                        holder.companyImageView.setImageDrawable(imageDrawable);
                    }

                    @Override
                    public void onError() {
                        holder.companyImageView.setImageResource(R.drawable.ic_default_company_image);
                    }
                });

        holder.CompanyNameTextView.setText(company.username);

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CompanyProfileActivity.class);
                intent.putExtra("imageurl", company.userProfilePictureURL);
                intent.putExtra("ownerID", company.uID);
                intent.putExtra("companyName", company.username);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return companies.size();
    }

    static class CompaniesViewHolder extends RecyclerView.ViewHolder {
        TextView CompanyNameTextView;
        ImageView companyImageView;
        CardView rootLayout;

        CompaniesViewHolder(View itemView, TextView CompanyNameTextView,
                            ImageView companyImageView, CardView rootLayout) {
            super(itemView);
            this.CompanyNameTextView = CompanyNameTextView;
            this.companyImageView = companyImageView;
            this.rootLayout = rootLayout;


        }
    }

}
