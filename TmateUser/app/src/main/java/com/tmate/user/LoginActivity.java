package com.tmate.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.tmate.user.Fragment.SocialFragment;


public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

         // sharedpreference 값 있을떄 바로 메인뷰로 간다.
        if (!(getPreferenceString("m_id").equals(""))) {
            Intent intent = new Intent(this, MainViewActivity.class);
            intent.putExtra("m_id", getPreferenceString("m_id"));
            startActivity(intent);
        }

        // sharedPreference 값 없을때 로그인 부터 시작한다.
        if(getPreferenceString("m_id").equals("")) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            SocialFragment socialFragment = new SocialFragment();
            transaction.replace(R.id.fm_main, socialFragment).commit();
        }
    }

    public String getPreferenceString(String key) {
        SharedPreferences pref = getSharedPreferences("loginUser", MODE_PRIVATE);
        return pref.getString(key, "");
    }
}