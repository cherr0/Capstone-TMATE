package com.tmate.driver;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class CheckActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        CheckFragment checkFragment = new CheckFragment();
        transaction.replace(R.id.frame_check, checkFragment).commit();
    }
}