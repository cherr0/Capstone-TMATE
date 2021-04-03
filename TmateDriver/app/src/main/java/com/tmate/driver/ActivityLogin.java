package com.tmate.driver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Toast;

import com.tmate.driver.Fragment.CompletedFragment;
import com.tmate.driver.Fragment.OnBackPressedListener;
import com.tmate.driver.Fragment.SocialFragment;
import com.tmate.driver.databinding.LoginActivityBinding;

public class ActivityLogin extends AppCompatActivity {
    // 마지막으로 뒤로 가기 버튼을 눌렀던 시간 저장
    private LoginActivityBinding binding;
    OnBackPressedListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = LoginActivityBinding.inflate(getLayoutInflater());
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