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
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        MatchingFragment mf = new MatchingFragment();
        mf.setArguments(bundle);
        transaction.replace(R.id.fm_matching_activity, mf).commit();

    }
}