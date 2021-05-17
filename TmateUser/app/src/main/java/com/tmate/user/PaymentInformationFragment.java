package com.tmate.user;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.tmate.user.adapter.PaymentAdapter;
import com.tmate.user.data.SubscriptionRes;
import com.tmate.user.databinding.FragmentPaymentInformationBinding;
import com.tmate.user.net.DataService;
import com.tmate.user.net.KakaoService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentInformationFragment extends Fragment implements View.OnClickListener {

    private FragmentPaymentInformationBinding b;

    private ArrayList<Integer> imageList;
    private Integer point;

    final String auth = "KakaoAK e24eec29f82748733f7a2be2de93c236";
    Call<Integer> pointRequest;
    Call<SubscriptionRes> subscriptionRequest;


    Bundle bundle;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentPaymentInformationBinding.inflate(getLayoutInflater());
        View v = b.getRoot();

        b.paymentInformationFinish.setOnClickListener(this);

        this.initializeData();
        Log.d("PayInfoFragment", "사용자 아이디 : " + getPreferenceString("m_id"));
        this.findData();

        if (getArguments() != null) {
            bundle = getArguments();
            Log.d("PayInfoFragment", "가져온 번들 값 : " + bundle);
            b.paymentInformationHSPlace.setText(bundle.getString("h_s_place")); // 출발지
            b.paymentInformationHFPlace.setText(bundle.getString("h_f_place")); // 도착지
            b.payHAllFare.setText(bundle.getString("h_ep_fare")+"원"); // 예상금액
            b.payToPeople.setText(bundle.getString("together")+"명"); // 동승인원
        }else {
            Log.d("PayInfoFragment","번들 값을 받아오지 못했습니다.");
        }

        //이미지 슬라이드관련
        ViewPager viewPager = v.findViewById(R.id.payment_pager);
        viewPager.setClipToPadding(false);
        float density = getResources().getDisplayMetrics().density;
        CircleIndicator indicator = v.findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        viewPager.setCurrentItem(2);
        viewPager.setAdapter(new PaymentAdapter(getContext(), imageList));

        //간편결제를 눌렀을때 카드 선택뷰 보이기
        selectedCard();



        return v;
    }



    public void findData() {
        pointRequest = DataService.getInstance().memberAPI.getUnusedPoint(getPreferenceString("m_id"));
        pointRequest.enqueue(new Callback<Integer>() {
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
                t.printStackTrace();
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
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.payment_information_finish:   // 결제완료 버튼
                Log.d("payInfoFragemnt", "결제 완료 눌림");
                kakaoSubscription("9000");
                return;

            case R.id.point_btn_all :               //모두적용 버튼

                return;
            case R.id.payment_information_point :   // 포인트 적용 버튼

                return;
        }
    }

    //간편결제를 눌렀을 때 카드 선택뷰 보이기
    private void selectedCard() {
        b.paymentSimpleCl.setVisibility(View.GONE);
        b.paymentRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.payment_simple) {
                    b.paymentSimpleCl.setVisibility(View.VISIBLE);
                } else {
                    b.paymentSimpleCl.setVisibility(View.GONE);
                }
            }
        });
    }

    private void kakaoSubscription(String amount) {
        Map<String,String> map = new HashMap<>();
        map.put("cid","TCSUBSCRIP");
        map.put("sid","S2895826273194274441");
        map.put("partner_order_id","d1010894658790");
        map.put("partner_user_id",getPreferenceString("m_id"));
        map.put("item_name","택시 운임");
        map.put("quantity","1");
        map.put("total_amount",amount);
        map.put("vat_amount","0");
        map.put("tax_free_amount","0");

        Log.d("payInfoFragemnt","map 전달 내용 : " + map);

        subscriptionRequest = KakaoService.getInstance().getApi().kakaoSubscription(auth,map);
        subscriptionRequest.enqueue(new Callback<SubscriptionRes>() {
            @Override
            public void onResponse(Call<SubscriptionRes> call, Response<SubscriptionRes> response) {
                if(response.code() == 200 && response.body() != null) {
                    SubscriptionRes result = response.body();
                    Log.d("payInfoFragemnt", "받아오는 값 :" + result);

                }else {
                    try {
                        Log.d("payInfoFragemnt", "에러 : " + response);
                        assert response.errorBody() != null;
                        Log.d("payInfoFragemnt", "데이터 삽입 실패 : " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SubscriptionRes> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(pointRequest != null) pointRequest.cancel();
        if(subscriptionRequest != null) subscriptionRequest.cancel();
    }
}