package com.tmate.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tmate.user.Fragment.BoardingFragment;
import com.tmate.user.Fragment.CallFragment;
import com.tmate.user.Fragment.EventFragment;
import com.tmate.user.Fragment.My_info_Fragment;


public class MainViewActivity extends AppCompatActivity {

    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private final CallFragment callFragment = new CallFragment();
    private final BoardingFragment boardingFragment = new BoardingFragment();
    private final EventFragment eventFragment = new EventFragment();
    private final My_info_Fragment my_info_fragment = new My_info_Fragment();
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);

        Intent intent = getIntent();


        setPreference("m_id", intent.getStringExtra("m_id"));

        Log.d("Get Preference : ", getPreferenceString("m_id"));

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, callFragment).commitAllowingStateLoss();

        bottomNavigationView = findViewById(R.id.bottomNavigationView_main_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                switch (menuItem.getItemId()) {
                    case R.id.call : {
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, callFragment).commitAllowingStateLoss();
                        return true;
                    }
                    case R.id.bo_info : {
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, boardingFragment).commitAllowingStateLoss();
                        return true;
                    }
                    case R.id.event : {
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, eventFragment).commitAllowingStateLoss();
                        return true;
                    }
                    case R.id.more : {
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, my_info_fragment).commitAllowingStateLoss();
                        return true;
                    }

                    default : return false;
                }
            }
        });
    }

    // 데이터 저장 함수
    public void setPreference(String key, String value){
        SharedPreferences pref = getSharedPreferences("loginUser", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    // 데이터 불러오기 함수
    public String getPreferenceString(String key){
        SharedPreferences pref = getSharedPreferences("loginUser", MODE_PRIVATE);
        return pref.getString(key, "");
    }
}
