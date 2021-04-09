package com.tmate.user.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.tmate.user.Activity.MainViewActivity;
import com.tmate.user.R;
import com.tmate.user.data.Member;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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

    private TextView textView18;
    private TextView textView17;


    // 테스트용 로그아웃 버튼

    private Button service;
    private TextView tv_card;

    DataService dataService = new DataService();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_info_, container, false);

        constraintLayout3 = view.findViewById(R.id.constraintLayout3);


        textView17 = (TextView) view.findViewById(R.id.textView17);
        textView18 = (TextView) view.findViewById(R.id.textView18);

        selectMemberInfo(getPreferenceString("m_id"));

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



        tv_point = view.findViewById(R.id.tv_point);
        tv_preference = view.findViewById(R.id.tv_preference);
        tv_alert = view.findViewById(R.id.tv_alert);
        tv_friend = view.findViewById(R.id.tv_friend);
        tv_history = view.findViewById(R.id.tv_history);
        tv_notice = view.findViewById(R.id.tv_notice);
        tv_bookmark = view.findViewById(R.id.tv_bookmark);
        service = view.findViewById(R.id.service);
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
            case R.id.tv_card : {
                card_management card_management = new card_management();
                transaction.replace(R.id.frameLayout, card_management).commit();
            }
            default: return;
        }
    }

    public void selectMemberInfo(String m_id) {

        dataService.profile.selectProfile(m_id).enqueue(new Callback<Member>() {
            @Override
            public void onResponse(Call<Member> call, Response<Member> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        Member member = response.body();

                        switch (member.getM_level()) {
                            case "0":
                                textView18.setText("일반");
                                break;
                            case "1":
                                textView18.setText("우수");
                                break;
                            case "2":
                                textView18.setText("최우수");
                                break;
                            case "3":
                                textView18.setText("VIP");
                                break;

                        }
                        textView17.setText(member.getM_name());
                    }
                }
            }

            @Override
            public void onFailure(Call<Member> call, Throwable t) {

            }
        });
    }

    public String getPreferenceString(String key) {
        SharedPreferences pref = getActivity().getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }
}

