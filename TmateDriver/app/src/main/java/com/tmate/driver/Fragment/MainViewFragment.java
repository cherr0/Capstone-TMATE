package com.tmate.driver.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.tmate.driver.R;
import com.tmate.driver.activity.WaitingActivity;
import com.tmate.driver.databinding.FragmentMainViewBinding;

public class MainViewFragment  extends Fragment {
    private FragmentMainViewBinding b;
    private View view;
    TextView tv_car_no ;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentMainViewBinding.inflate(inflater, container, false);
        View view = b.getRoot();

        b.carFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                CarFragment carFragment = new CarFragment();
                transaction.replace(R.id.frame, carFragment).commit();
            }
        });

        b.dvSt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), WaitingActivity.class);
                startActivity(intent);
            }
        });

        tv_car_no = b.tvCarNo;
        return view;
    }
}