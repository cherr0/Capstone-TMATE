package com.tmate.user.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.tmate.user.Fragment.MatchingFragment;
import com.tmate.user.Fragment.PointFragment;
import com.tmate.user.Fragment.ProfileFragment;
import com.tmate.user.R;

public class MatchingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        MatchingFragment mf = new MatchingFragment();
        transaction.replace(R.id.fm_matching_activity, mf).commit();

    }
}