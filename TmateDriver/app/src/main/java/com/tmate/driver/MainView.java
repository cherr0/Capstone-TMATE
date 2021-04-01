package com.tmate.driver;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.tmate.driver.databinding.ActivityMainBinding;
import com.tmate.driver.databinding.MainViewBinding;

public class MainView extends AppCompatActivity {

    private MainViewBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MainViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}
