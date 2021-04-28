package com.tmate.user.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.tmate.user.Fragment.MatchingFragment;
import com.tmate.user.Fragment.PointFragment;
import com.tmate.user.Fragment.ProfileFragment;
import com.tmate.user.R;

public class MatchingActivity extends AppCompatActivity {

     Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching);



        if (getIntent() != null) {
            Intent intent = getIntent();
            bundle = new Bundle();
            bundle.putString("slttd",intent.getStringExtra("slttd"));
            bundle.putString("slngtd",intent.getStringExtra("slngtd"));
            bundle.putString("flttd",intent.getStringExtra("flttd"));
            bundle.putString("flngtd",intent.getStringExtra("flngtd"));
            bundle.putInt("to_max", intent.getIntExtra("to_max",0));
            bundle.putString("h_s_place", intent.getStringExtra("h_s_place"));
            bundle.putString("h_f_place", intent.getStringExtra("h_f_place"));
            bundle.putString("h_ep_fare", intent.getStringExtra("h_ep_fare"));

        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        MatchingFragment mf = new MatchingFragment();
        mf.setArguments(bundle);
        transaction.replace(R.id.fm_matching_activity, mf).commit();

    }
}
//
//intent.putExtra("slttd", String.valueOf(tMapPointStart.getLatitude()));
//        intent.putExtra("slngtd", String.valueOf(tMapPointStart.getLongitude()));
//        intent.putExtra("flttd", String.valueOf(tMapPointEnd.getLatitude()));
//        intent.putExtra("flngtd", String.valueOf(tMapPointEnd.getLongitude()));
//        intent.putExtra("to_max", together);
//        intent.putExtra("h_s_place", h_s_place);
//        intent.putExtra("h_f_place", h_f_place);
//        intent.putExtra("h_ep_fare",totalFare);
//        intent.putExtra("h_ep_time",totalTime);
//        intent.putExtra("h_ep_distance",totalDistance);