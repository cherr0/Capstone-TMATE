package com.tmate.user.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.tmate.user.R;


public class My_info_Fragment extends Fragment {
    private ConstraintLayout constraintLayout3;

    private View view;
    private TextView tv_point;
    private TextView tv_preference;
    private TextView tv_alert;
    private TextView tv_history;
    private TextView tv_notice;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_info_, container, false);

        constraintLayout3 = view.findViewById(R.id.constraintLayout3);
        constraintLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                ProfileFragment profileFragment = new ProfileFragment();
                transaction.replace(R.id.frameLayout, profileFragment);
                transaction.commit();
            }
        });

        tv_point = view.findViewById(R.id.tv_point);
        tv_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                PointFragment pointFragment = new PointFragment();
                transaction.replace(R.id.frameLayout, pointFragment);
                transaction.commit();
            }
        });

        tv_preference = view.findViewById(R.id.tv_preference);
        tv_preference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                PreferenceFragment preferenceFragment = new PreferenceFragment();
                transaction.replace(R.id.frameLayout, preferenceFragment);
                transaction.commit();
            }
        });

        tv_alert = view.findViewById(R.id.tv_alert);
        tv_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                NotificationFragment notificationFragment = new NotificationFragment();
                transaction.replace(R.id.frameLayout, notificationFragment);
                transaction.commit();
            }
        });
        tv_history = view.findViewById(R.id.tv_history);
        tv_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                historyFragment hf = new historyFragment();
                transaction.replace(R.id.frameLayout, hf);
                transaction.commit();
            }
        });
        tv_notice = view.findViewById(R.id.tv_notice);
        tv_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                NoticeFragment nf = new NoticeFragment();
                transaction.replace(R.id.frameLayout, nf);
                transaction.commit();
            }
        });



        return view;
    }
}