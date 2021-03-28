package com.tmate.user;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class MatchingDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_detail);


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        MatchingDetailFragment matchingDetailFragment = new MatchingDetailFragment();
        transaction.replace(R.id.fm_matching, matchingDetailFragment).commit();
    }
}