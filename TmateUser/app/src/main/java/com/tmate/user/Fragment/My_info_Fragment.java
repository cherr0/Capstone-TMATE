package com.tmate.user.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PointF;
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

import com.tmate.user.FavoritesData;
import com.tmate.user.MainViewActivity;
import com.tmate.user.LoginActivity;
import com.tmate.user.R;


public class My_info_Fragment extends Fragment implements View.OnClickListener{
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
        tv_preference = view.findViewById(R.id.tv_preference);
        tv_alert = view.findViewById(R.id.tv_alert);
        tv_friend = view.findViewById(R.id.tv_friend);
        tv_history = view.findViewById(R.id.tv_history);
        tv_notice = view.findViewById(R.id.tv_notice);
        tv_bookmark = view.findViewById(R.id.tv_bookmark);
        service = view.findViewById(R.id.service);
        btn_logout = view.findViewById(R.id.btn_logout);
        tv_card = view.findViewById(R.id.tv_card);


        constraintLayout3.setOnClickListener(this);
        tv_point.setOnClickListener(this);
        tv_preference.setOnClickListener(this);
        tv_alert.setOnClickListener(this);
        tv_friend.setOnClickListener(this);
        tv_history.setOnClickListener(this);
        tv_notice.setOnClickListener(this);
        tv_bookmark.setOnClickListener(this);
        service.setOnClickListener(this);
        btn_logout.setOnClickListener(this);
        tv_card.setOnClickListener(this);

        return view;
    }

    public static void clearPerferences(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("m_id");
        editor.apply();
    }

    @Override
    public void onClick(View v) {


        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        MainViewActivity.navbarFlag = 3;
        switch (v.getId()) {
            case R.id.constraintLayout3 : {

                ProfileFragment profileFragment = new ProfileFragment();
                transaction.replace(R.id.frameLayout, profileFragment).commit();
                return ;
            }
            case R.id.tv_point : {
                PointFragment pointFragment = new PointFragment();
                transaction.replace(R.id.frameLayout, pointFragment).commit();
                return ;
            }
            case R.id.tv_preference : {
                PreferenceFragment preferenceFragment = new PreferenceFragment();
                transaction.replace(R.id.frameLayout, preferenceFragment).commit();
                return ;
            }
            case R.id.tv_alert : {
                NotificationFragment notificationFragment = new NotificationFragment();
                transaction.replace(R.id.frameLayout, notificationFragment).commit();
                return ;
            }
            case R.id.tv_friend :{
                FriendFragment friendFragment = new FriendFragment();
                transaction.replace(R.id.frameLayout, friendFragment).commit();
                return ;
            }
            case R.id.tv_history : {
                historyFragment historyFragment = new historyFragment();
                transaction.replace(R.id.frameLayout, historyFragment).commit();
                return ;
            }
            case R.id.tv_notice : {
                NoticeFragment noticeFragment = new NoticeFragment();
                transaction.replace(R.id.frameLayout, noticeFragment).commit();
                return ;
            }
            case R.id.tv_bookmark : {
                favoritesFragment favoritesFragment = new favoritesFragment();
                transaction.replace(R.id.frameLayout, favoritesFragment).commit();
                return ;
            }
            case R.id.service : {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://pf.kakao.com/_xlxnzUK/chat"));
                startActivity(intent);
                return;
            }
            case R.id.btn_logout : {
                clearPerferences(getActivity().getApplicationContext());
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
            case R.id.tv_card : {
                card_management card_management = new card_management();
                transaction.replace(R.id.frameLayout, card_management).commit();
            }
            default: return;
        }
    }
}

