package com.tmate.user.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tmate.user.Activity.CarInfoActivity;
import com.tmate.user.databinding.ActivityMatchingMapBinding;
import com.tmate.user.databinding.FragmentCarInfoBinding;

public class CarInfoFragment extends Fragment {
    private FragmentCarInfoBinding b;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentCarInfoBinding.inflate(getLayoutInflater());
        view = b.getRoot();

        b.car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),CarInfoActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
