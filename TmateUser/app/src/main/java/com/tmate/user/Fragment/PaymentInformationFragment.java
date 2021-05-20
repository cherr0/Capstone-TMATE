package com.tmate.user.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.tmate.user.Activity.CallWaitingActivity;
import com.tmate.user.R;
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
    private Integer unusedPoint;
    private Integer point;
    private Integer price;

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
        b.pointBtnAll.setOnClickListener(this);
        b.pointBtnUse.setOnClickListener(this);

        cardImgList(); // 카드 이미지 리스트 가져오기
        findUnusedPoint(); // 사용자 미사용 포인트 검색
        Log.d("PayInfoFragment", "사용자 아이디 : " + getPreferenceString("m_id"));
        Log.d("PayInfoFragment", "사용자 미사용 포인트 : " + unusedPoint);


        if (getArguments() != null) {
            bundle = getArguments();
            Log.d("PayInfoFragment", "가져온 번들 값 : " + bundle);
            b.paymentInformationHSPlace.setText(bundle.getString("h_s_place")); // 출발지
            b.paymentInformationHFPlace.setText(bundle.getString("h_f_place")); // 도착지
            b.payHAllFare.setText(bundle.getString("h_ep_fare")+"원"); // 예상금액
            b.payToPeople.setText(bundle.getString("together")+"명"); // 동승인원
            b.payTotalAmount.setText(bundle.getString("h_ep_fare")); // 총 금액
            price = Integer.parseInt(bundle.getString("h_ep_fare")); // 총 금액 변수에 추가

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

    // 사용자 미사용 포인트 검색
    public void findUnusedPoint() {
        pointRequest = DataService.getInstance().memberAPI.getUnusedPoint(getPreferenceString("m_id"));
        pointRequest.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.code() == 200 && response.body() != null) {
                    unusedPoint = response.body();
                    Log.d("PaymentFragment", "사용자 사용가능 포인트  " + unusedPoint);
                    b.paymentInformationMPoint.setText(unusedPoint.toString());
                    b.paymentInformationRestMPoint.setText(unusedPoint.toString());
                } else {
                    try {
                        Log.d("PayInfoFragment", "에러 : " + response);
                        assert response.errorBody() != null;
                        Log.d("PayInfoFragment", "데이터 삽입 실패 : " + response.errorBody().string());
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
    private void cardImgList() {
        imageList = new ArrayList();
        imageList.add(R.drawable.ic_logo);
        imageList.add(R.drawable.ic_map);
        imageList.add(R.drawable.banner);
        imageList.add(R.drawable.logo);
    }

    //간편결제를 눌렀을 때 카드 선택뷰 보이기
    private void selectedCard() {
        b.paymentRadiogroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.payment_simple) {
                b.paymentSimpleCl.setVisibility(View.VISIBLE);
            } else {
                b.paymentSimpleCl.setVisibility(View.GONE);
            }
        });
    }

    // 카카오 정기결제 진행
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
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.payment_information_finish:   // 결제완료 버튼
                Log.d("payInfoFragemnt", "결제 완료 눌림");

                /*
                Intent intent = new Intent(getContext(), CallWaitingActivity.class);
                intent.putExtra("pay",b.payTotalHEpFare.getText().toString()); // 결제 금액
                intent.putExtra("use_point", b.payPoResult.getText().toString()); // 사용 포인트
                intent.putExtra("h_ep_fare", bundle.getString("h_ep_fare")); // 총금액
                intent.putExtra("h_s_place", bundle.getString("h_s_place")); // 출발지
                intent.putExtra("h_f_place", bundle.getString("h_f_place"));// 목적지
                intent.putExtra("slngtd",bundle.getString("slngtd")); // 출발지 경도
                intent.putExtra("slttd", bundle.getString("slttd")); // 출발지 위도
                intent.putExtra("flngtd", bundle.getString("flngtd")); // 도착지 경도
                intent.putExtra("flttd", bundle.getString("flttd")); // 도착지 위도
                intent.putExtra("together", bundle.getString("together")); // 동승인원

                Log.d("PayInfoFragment",intent.getExtras().toString());

                startActivity(intent);
                getActivity().finish();
                 */
                kakaoSubscription(b.payTotalAmount.getText().toString());
                return;

            case R.id.point_btn_all :  //모두적용 버튼
                Log.d("payInfoFragment", "포인트 모두 적용");
                b.paymentInformationRestMPoint.setText("0");
                b.payPoResult.setText(unusedPoint.toString() + "원");
                b.payTotalAmount.setText(String.valueOf(price - unusedPoint));
                hideKeyBoard(); // 키보드 비활성화
                return;

            case R.id.point_btn_use :   // 포인트 적용 버튼
                String getUsePoint = b.pointEt.getText().toString();
                Log.d("payInfoFragment", "적용하려는 포인트 : " + getUsePoint);

                if(getUsePoint.isEmpty()) { // 포인트 값 입력 확인
                    Toast.makeText(getActivity(), "포인트 값을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                point = Integer.parseInt(getUsePoint);

                if(point <= unusedPoint){ // 입력 포인트 초과되는지 확인
                    b.paymentInformationRestMPoint.setText(String.valueOf(unusedPoint - point));
                    b.payPoResult.setText(String.valueOf(point));
                    b.payTotalAmount.setText(String.valueOf(price - point));
                    b.pointEt.setText("");
                    Log.d("payInfoFragment", "적용 포인트 : " + (price - point));

                }else {
                    Toast.makeText(getActivity(), "보유 포인트 초과하여 사용은 불가합니다.", Toast.LENGTH_SHORT).show();
                    b.pointEt.setText("");
                }

                hideKeyBoard(); // 키보드 비활성화
                return;
        }
    }

    //키보드 숨기기
    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(b.pointEt.getWindowToken(), 0);
    }

    public String getPreferenceString(String key) {
        SharedPreferences pref = getActivity().getSharedPreferences("loginUser", getActivity().MODE_PRIVATE);
        return pref.getString(key, "");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(pointRequest != null) pointRequest.cancel();
        if(subscriptionRequest != null) subscriptionRequest.cancel();
    }
}