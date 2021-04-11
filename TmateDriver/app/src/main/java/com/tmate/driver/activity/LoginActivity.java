package com.tmate.driver.activity;

import android.os.Bundle;

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

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        SocialFragment socialFragment = new SocialFragment();
        transaction.replace(R.id.fm_main, socialFragment).commit();
    }

    public void setOnBackPressedListener(OnBackPressedListener listener){
        this.listener = listener;
    }
    @Override public void onBackPressed() {
        if(listener!=null){
            listener.onBackPressed();
        }else{ super.onBackPressed();
        }
    }

}