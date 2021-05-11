package com.tmate.user.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.tmate.user.Fragment.SocialFragment;
import com.tmate.user.R;


public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d("로그아웃버튼시 값 체크 ", getPreferenceString("m_id"));

        Intent intent = getIntent();
        String m_imei = intent.getStringExtra("m_imei");
        Bundle bundle = new Bundle();
        bundle.putString("m_imei",m_imei);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        SocialFragment socialFragment = new SocialFragment();
        socialFragment.setArguments(bundle);
        transaction.replace(R.id.fm_main, socialFragment).commit();
    }

    // 데이터 저장 함수
    public void setPreference(String key, String value){
        SharedPreferences pref = getSharedPreferences("loginUser", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getPreferenceString(String key) {
        SharedPreferences pref = getSharedPreferences("loginUser", MODE_PRIVATE);
        return pref.getString(key, "");
    }
}