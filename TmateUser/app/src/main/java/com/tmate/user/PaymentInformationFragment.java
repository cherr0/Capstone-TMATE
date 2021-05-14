package com.tmate.user;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.tmate.user.adapter.PaymentAdapter;
import com.tmate.user.net.DataService;

import java.io.IOException;
import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentInformationFragment extends Fragment {

    private ArrayList<Integer> imageList;
    private RadioGroup payment_radiogroup;
    private ConstraintLayout payment_simple_cl;
    private Button point_btn_all, point_btn_use;
    private EditText point_et;
    private TextView pay_po_result, pay_to_people, pay_total_h_ep_Fare, payment_informetion_h_s_place,
            payment_informetion_h_f_place, payment_informetion_m_point, point_rest, pay_h_allFare;

    private Integer point;

    Call<Integer> request;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_payment_information, container, false);

        this.initializeData();
        initWidget(v);
        Log.d("payment", "사용자 아이디 : " + getPreferenceString("m_id"));
        this.findData();

        //이미지 슬라이드관련
        ViewPager viewPager = v.findViewById(R.id.payment_pager);
        viewPager.setClipToPadding(false);
        float density = getResources().getDisplayMetrics().density;
        CircleIndicator indicator = v.findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        viewPager.setCurrentItem(2);
        viewPager.setAdapter(new PaymentAdapter(getContext(), imageList));


        //번들로 받아온 값
        payment_informetion_h_s_place.setText(getArguments().getString("h_s_place")); // 출발지
        payment_informetion_h_f_place.setText(getArguments().getString("h_f_place")); // 도착지
        pay_h_allFare.setText(getArguments().getString("h_ep_fare")+"원"); // 예상금액
        pay_to_people.setText(getArguments().getString("together")+"명"); // 동승인원


        //모두적용 버튼을 눌렀을때
//        point_btn_all.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });


        //간편결제를 눌렀을때 카드 선택뷰 보이기
        payment_simple_cl.setVisibility(View.GONE);
        payment_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.payment_simple) {
                    payment_simple_cl.setVisibility(View.VISIBLE);
                } else {
                    payment_simple_cl.setVisibility(View.GONE);
                }
            }
        });

        return v;
    }

    //위젯 선언
    public void initWidget(View v) {
        pay_po_result = v.findViewById(R.id.pay_po_result);
        point_et = v.findViewById(R.id.point_et);
        payment_simple_cl = v.findViewById(R.id.payment_simple_cl);
        payment_radiogroup = v.findViewById(R.id.payment_radiogroup);
        point_btn_all = v.findViewById(R.id.point_btn_use);
        point_btn_use = v.findViewById(R.id.point_btn_use);
        pay_po_result = v.findViewById(R.id.pay_po_result);
        pay_to_people = v.findViewById(R.id.pay_to_people);
        pay_total_h_ep_Fare = v.findViewById(R.id.pay_total_h_ep_Fare);
        payment_informetion_h_s_place = v.findViewById(R.id.payment_informetion_h_s_place);
        payment_informetion_h_f_place = v.findViewById(R.id.payment_informetion_h_f_place);
        payment_informetion_m_point = v.findViewById(R.id.payment_informetion_m_point);
        pay_h_allFare = v.findViewById(R.id.pay_h_allFare);
    }


    public void findData() {
        request = DataService.getInstance().memberAPI.getUnusedPoint(getPreferenceString("m_id"));
        request.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.code() == 200 && response.body() != null) {
                    point = response.body();
                    Log.d("PaymentFragment", "사용자 사용가능 포인트  " + point);

                } else {
                    try {
                        Log.d("SocialFragment", "에러 : " + response);
                        assert response.errorBody() != null;
                        Log.d("SocialFragment", "데이터 삽입 실패 : " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }
    //카드 이미지 리스트
    private void initializeData() {
        imageList = new ArrayList();
        imageList.add(R.drawable.ic_logo);
        imageList.add(R.drawable.ic_map);
        imageList.add(R.drawable.banner);
        imageList.add(R.drawable.logo);
    }

    public String getPreferenceString(String key) {
        SharedPreferences pref = getActivity().getSharedPreferences("loginUser", getActivity().MODE_PRIVATE);
        return pref.getString(key, "");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        request.cancel();
    }
}