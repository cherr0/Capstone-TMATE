package com.tmate.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class MatchingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        MatchingFragment mf= new MatchingFragment();
        transaction.replace(R.id.macing_frame, mf).commit();
    }
}