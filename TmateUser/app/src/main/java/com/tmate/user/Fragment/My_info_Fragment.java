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
import com.tmate.user.net.DataService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class My_info_Fragment extends Fragment implements View.OnClickListener{
    private View view;
    private TextView tv_preference;
    private TextView tv_alert;
    private TextView tv_friend;
    private TextView tv_history;
    private TextView tv_notice;
    private TextView tv_account;


    private TextView textView18;
    private TextView mi_m_name;




    private ConstraintLayout service;
    private TextView tv_card;

    //    DataService dataService = new DataService();
    DataService dataService = DataService.getInstance();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_info_, container, false);

        tv_account = view.findViewById(R.id.tv_account);


        mi_m_name = (TextView) view.findViewById(R.id.mi_m_name);
        textView18 = (TextView) view.findViewById(R.id.textView18);

        selectMemberInfo(getPreferenceString("m_id"));

        tv_account.setOnClickListener(new View.OnClickListener() {
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



        tv_preference = view.findViewById(R.id.tv_preference);
        tv_alert = view.findViewById(R.id.tv_alert);
        tv_friend = view.findViewById(R.id.tv_friend);
        tv_history = view.findViewById(R.id.tv_history);
        tv_notice = view.findViewById(R.id.tv_notice);
        service = view.findViewById(R.id.service);
        tv_card = view.findViewById(R.id.tv_card);


        tv_account.setOnClickListener(this);
        tv_preference.setOnClickListener(this);
        tv_alert.setOnClickListener(this);
        tv_friend.setOnClickListener(this);
        tv_history.setOnClickListener(this);
        tv_notice.setOnClickListener(this);
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
            case R.id.tv_account : { // 사용자 상세 프로필
                ProfileFragment profileFragment = new ProfileFragment();
                transaction.replace(R.id.frameLayout, profileFragment).commit();
                return ;
            }
            case R.id.tv_point : { // 포인트 관리
                PointFragment pointFragment = new PointFragment();
                transaction.replace(R.id.frameLayout, pointFragment).commit();
                return ;
            }
            case R.id.tv_preference : { // 선호 운행 옵션
                PreferenceFragment preferenceFragment = new PreferenceFragment();
                transaction.replace(R.id.frameLayout, preferenceFragment).commit();
                return ;
            }
            case R.id.tv_alert : { // 알림 설정
                NotificationFragment notificationFragment = new NotificationFragment();
                transaction.replace(R.id.frameLayout, notificationFragment).commit();
                return ;
            }
            case R.id.tv_friend :{ // 지인번호 등록
                FriendFragment friendFragment = new FriendFragment();
                transaction.replace(R.id.frameLayout, friendFragment).commit();
                return ;
            }
            case R.id.tv_history : { // 이용기록
                historyFragment historyFragment = new historyFragment();
                transaction.replace(R.id.frameLayout, historyFragment).commit();
                return ;
            }
            case R.id.tv_notice : { // 공지사항
                NoticeFragment noticeFragment = new NoticeFragment();
                transaction.replace(R.id.frameLayout, noticeFragment).commit();
                return ;
            }
            case R.id.service : { // 상담 연결
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://pf.kakao.com/_xlxnzUK/chat"));
                startActivity(intent);
                return;
            }
            case R.id.tv_card : { // 카드 관리
                card_management card_management = new card_management();
                transaction.replace(R.id.frameLayout, card_management).commit();
            }
            default: return;
        }
    }

    public void selectMemberInfo(String m_id) {

        dataService.memberAPI.selectProfile(m_id).enqueue(new Callback<Member>() {
            @Override
            public void onResponse(Call<Member> call, Response<Member> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        Member member = response.body();

//                        switch (member.getM_level()) {
//                            case "0":
//                                textView18.setText("일반");
//                                break;
//                            case "1":
//                                textView18.setText("우수");
//                                break;
//                            case "2":
//                                textView18.setText("최우수");
//                                break;
//                            case "3":
//                                textView18.setText("VIP");
//                                break;
//
//                        }
                        int normalCnt = member.getM_n_use();
                        int togetherCnt = member.getM_t_use();
                        int sumCnt = normalCnt + togetherCnt;
                        if (sumCnt < 5) {
                            textView18.setText("일반");
                        }
                        if (sumCnt >= 5 && sumCnt < 10) {
                            textView18.setText("우수");
                        }
                        if (sumCnt >= 10 && sumCnt < 20) {
                            textView18.setText("최우수");
                        }
                        if (sumCnt >= 20) {
                            textView18.setText("VIP");
                        }
                        mi_m_name.setText(member.getM_name());
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

