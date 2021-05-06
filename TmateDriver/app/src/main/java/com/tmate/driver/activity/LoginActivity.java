package com.tmate.driver.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.tmate.driver.Fragment.SocialFragment;
import com.tmate.driver.OnBackPressedListener;
import com.tmate.driver.R;
import com.tmate.driver.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    // 마지막으로 뒤로 가기 버튼을 눌렀던 시간 저장
    private ActivityLoginBinding binding;
    OnBackPressedListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String m_imei = intent.getStringExtra("m_imei");
        Bundle bundle = new Bundle();
        bundle.putString("m_imei",m_imei);
        Log.i("imei 값",m_imei);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        SocialFragment socialFragment = new SocialFragment();
        socialFragment.setArguments(bundle);
        transaction.replace(R.id.fm_main, socialFragment).commit();
    }

    public void setOnBackPressedListener(OnBackPressedListener listener){
        this.listener = listener;
    }

    @Override
    public void onBackPressed() {
        if(listener!=null){
            listener.onBackPressed();
        }else{
            super.onBackPressed();
        }
    }

    public String getPreferenceString(String key) {
        SharedPreferences pref = getSharedPreferences("loginDriver", MODE_PRIVATE);
        return pref.getString(key, "");
    }
}