package com.tmate.driver.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tmate.driver.R;

public class SpinnerCarColorAdapter extends ArrayAdapter<String> {
    int[] spinnerImages;
    Context context;

    public SpinnerCarColorAdapter(@NonNull Context context, int[] spinnerImages) {
        super(context, R.layout.spinner_car_color);
        this.spinnerImages = spinnerImages;
        this.context = context;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position,convertView,parent);
    }


    @Override
    public int getCount() {
        return spinnerImages.length;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder sub_menu_view_holder = new ViewHolder();

        if (convertView == null) {
            LayoutInflater mInflater  = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.spinner_car_color, parent, false);

            sub_menu_view_holder.spinner_car_color_img = (ImageView) convertView.findViewById(R.id.spinner_car_color_img);
            sub_menu_view_holder.spinner_car_color_layout = (LinearLayout)convertView.findViewById(R.id.spinner_car_color_layout);
            convertView.setTag(sub_menu_view_holder);
        } else {
            sub_menu_view_holder = (ViewHolder) convertView.getTag();
        }

        if (position == 0) {
            sub_menu_view_holder.spinner_car_color_layout.setVisibility(View.GONE);
        } else {
            sub_menu_view_holder.spinner_car_color_layout.setVisibility(View.VISIBLE);
        }

        sub_menu_view_holder.spinner_car_color_img.setImageResource(spinnerImages[position]);


        return convertView;
    }

    private static class ViewHolder{

        LinearLayout spinner_car_color_layout;

        ImageView spinner_car_color_img;

    }
}
