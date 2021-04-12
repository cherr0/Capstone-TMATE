package com.tmate.driver.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.tmate.driver.R;
import com.tmate.driver.activity.HotPlaceActivity;
import com.tmate.driver.activity.TrafficInformationActivity;

public class StatisticsFragment extends Fragment {
    View view;
    CardView traffic_information, hot_place;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_statistics, container, false);


        traffic_information =  view.findViewById(R.id.traffic_information);
        hot_place = view.findViewById(R.id.hot_place);

        traffic_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TrafficInformationActivity.class);
                startActivity(intent);
            }
        });
        hot_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), HotPlaceActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}