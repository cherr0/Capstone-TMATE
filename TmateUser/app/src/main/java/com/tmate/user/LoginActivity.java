package com.tmate.user;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.tmate.user.Fragment.SocialFragment;


public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        SocialFragment socialFragment = new SocialFragment();
        transaction.replace(R.id.fm_main, socialFragment).commit();
    }
}