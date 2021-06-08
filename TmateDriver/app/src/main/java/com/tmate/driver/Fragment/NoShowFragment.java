package com.tmate.driver.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tmate.driver.R;
import com.tmate.driver.databinding.FragmentNoShowBinding;

public class NoShowFragment extends Fragment {
    private FragmentNoShowBinding binding;
    boolean check = true;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentNoShowBinding.inflate(inflater);

        binding.noShowCheckOne.setOnClickListener(v -> {
            String noShow = "노쇼회원";
            String ride = "탑승중";
            if (check == true) {
                binding.noShowCheckOne.setText(noShow);
                binding.noShowCheckOne.setBackgroundResource(R.drawable.btn_stroke_on);
                check = false;
            } else {
                binding.noShowCheckOne.setText(ride);
                binding.noShowCheckOne.setBackgroundResource(R.drawable.btn_stroke_off);
                check = true;
            }
        });

        return binding.getRoot();
    }
}