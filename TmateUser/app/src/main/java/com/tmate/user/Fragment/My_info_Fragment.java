package com.tmate.user.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.tmate.user.MainViewActivity;
import com.tmate.user.LoginActivity;
import com.tmate.user.R;


public class My_info_Fragment extends Fragment {
    private ConstraintLayout constraintLayout3;

    private View view;
    private TextView tv_point;
    private TextView tv_preference;
    private TextView tv_alert;
    private TextView tv_friend;
    private TextView tv_history;
    private TextView tv_notice;
    private TextView tv_bookmark;

    // 테스트용 로그아웃 버튼
    private Button btn_logout;

    private Button service;

    private TextView tv_card;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_info_, container, false);

        constraintLayout3 = view.findViewById(R.id.constraintLayout3);
        constraintLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainViewActivity.navbarFlag = 3;
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                ProfileFragment profileFragment = new ProfileFragment();
                transaction.replace(R.id.frameLayout, profileFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        btn_logout = view.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearPerferences(getContext());
                SharedPreferences preferences = getActivity().getSharedPreferences("loginUser", Context.MODE_PRIVATE);
                String m_id = preferences.getString("m_id", null);
                Log.d("과연 이값은: ", m_id);

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        tv_point = view.findViewById(R.id.tv_point);
        tv_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainViewActivity.navbarFlag = 3;
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                PointFragment pointFragment = new PointFragment();
                transaction.replace(R.id.frameLayout, pointFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        tv_preference = view.findViewById(R.id.tv_preference);
        tv_preference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainViewActivity.navbarFlag = 3;
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                PreferenceFragment preferenceFragment = new PreferenceFragment();
                transaction.replace(R.id.frameLayout, preferenceFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        tv_alert = view.findViewById(R.id.tv_alert);
        tv_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainViewActivity.navbarFlag = 3;
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                NotificationFragment notificationFragment = new NotificationFragment();
                transaction.replace(R.id.frameLayout, notificationFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        tv_history = view.findViewById(R.id.tv_history);
        tv_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainViewActivity.navbarFlag = 3;
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                historyFragment hf = new historyFragment();
                transaction.replace(R.id.frameLayout, hf);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        tv_notice = view.findViewById(R.id.tv_notice);
        tv_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainViewActivity.navbarFlag = 3;
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                NoticeFragment nf = new NoticeFragment();
                transaction.replace(R.id.frameLayout, nf);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        tv_bookmark = view.findViewById(R.id.tv_bookmark);
        tv_bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainViewActivity.navbarFlag = 3;
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                favoritesFragment favoritesFragment = new favoritesFragment();
                transaction.replace(R.id.frameLayout, favoritesFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        tv_friend = view.findViewById(R.id.tv_friend);
        tv_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainViewActivity.navbarFlag = 3;
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                FriendFragment friendFragment = new FriendFragment();
                transaction.replace(R.id.frameLayout, friendFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        tv_card = view.findViewById(R.id.tv_card);
        tv_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainViewActivity.navbarFlag = 3;
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                card_management cm  = new card_management();
                transaction.replace(R.id.frameLayout, cm);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        service = view.findViewById(R.id.service);
        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    Toast.makeText(getContext(), "카카오톡 이동", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://pf.kakao.com/_xlxnzUK/chat"));
                    startActivity(intent);
                }
            }

        });
        return view;
    }

    public static void clearPerferences(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("m_id");
        editor.apply();
    }
}

