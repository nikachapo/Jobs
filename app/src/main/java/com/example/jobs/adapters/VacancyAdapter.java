package com.example.jobs.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.example.jobs.R;
import com.example.jobs.vacancy.Vacancy;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class VacancyAdapter extends ArrayAdapter<Vacancy> {
    private Context mContext;
    private int mResource;
    public VacancyAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Vacancy> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Vacancy vacancy = getItem(position);
        assert vacancy != null;
        String vacancyName = vacancy.vacancyHeader;
        String vacancyBody = vacancy.vacancyBody;
        String vacancyDate = vacancy.currentTimeStamp;
        String vacancyCity = vacancy.vacancyCity;
        final String companyLogo = vacancy.ownerProfileURL;

        //create view result
        final View result;

        //create item holder
        ListItemsViewHolder itemsViewHolder = new ListItemsViewHolder();

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource,parent,false);
            itemsViewHolder.vacancyNameTextView =  convertView.findViewById(R.id.company_name);
            itemsViewHolder.vacancyBodyTextView =  convertView.findViewById(R.id.vacancy_name);
            itemsViewHolder.vacancyDateRangeTextView =  convertView.findViewById(R.id.date_range);
            itemsViewHolder.vacancyOwnerLogoImageView =  convertView.findViewById(R.id.company_image);

            result = convertView;
            convertView.setTag(itemsViewHolder);

        }else {
            itemsViewHolder = (ListItemsViewHolder) convertView.getTag();
            result = convertView;
        }


        itemsViewHolder.vacancyNameTextView.setText(vacancyName);

        if(vacancyBody.length() > 40){
            itemsViewHolder.vacancyBodyTextView.setText(vacancyBody.substring(0,38)+"...");
        }else
            itemsViewHolder.vacancyBodyTextView.setText(vacancyBody);

        itemsViewHolder.vacancyDateRangeTextView.setText(vacancyDate);
        final View finalConvertView = convertView;
        final ListItemsViewHolder finalItemsViewHolder = itemsViewHolder;
        Picasso.with(getContext()).load(companyLogo)

                .resize(100, 100)
                .into(itemsViewHolder.vacancyOwnerLogoImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap imageBitmap = ((BitmapDrawable) finalItemsViewHolder.vacancyOwnerLogoImageView.getDrawable()).getBitmap();
                        RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(finalConvertView.getResources(), imageBitmap);
                        imageDrawable.setCircular(true);
                        imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                        finalItemsViewHolder.vacancyOwnerLogoImageView.setImageDrawable(imageDrawable);
                    }
                    @Override
                    public void onError() {
                        finalItemsViewHolder.vacancyOwnerLogoImageView.setImageResource(R.drawable.ic_default_company_image);
                    }
                });

        Animation animation = AnimationUtils.loadAnimation(mContext,R.anim.scale);
        result.startAnimation(animation);
        return convertView;
    }

    static class ListItemsViewHolder{
        TextView vacancyNameTextView ;
        TextView vacancyBodyTextView ;
        TextView vacancyDateRangeTextView;
        ImageView vacancyOwnerLogoImageView ;

    }

}
