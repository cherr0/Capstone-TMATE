package com.tmate.driver.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tmate.driver.databinding.ActivityMatchingBinding;

public class MatchingActivity extends AppCompatActivity {
    private ActivityMatchingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMatchingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnRefusal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MatchingActivity.this, PaymentActivity.class);
                startActivity(intent);
            }
        });
    }
}