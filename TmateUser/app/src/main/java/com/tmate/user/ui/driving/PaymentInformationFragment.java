package com.tmate.user.ui.driving;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.tmate.user.Activity.CallWaitingActivity;
import com.tmate.user.R;
import com.tmate.user.adapter.PaymentAdapter;
import com.tmate.user.data.Dispatch;
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
    private DrivingModel mViewModel;

    private ArrayList<Integer> imageList;
    private Integer unusedPoint;
    private Integer point;
    private Integer price;


    Call<Integer> pointRequest;
    Call<SubscriptionRes> subscriptionRequest;
    Call<String> normalMatchingRequest;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentPaymentInformationBinding.inflate(getLayoutInflater());
        mViewModel = new ViewModelProvider(requireActivity()).get(DrivingModel.class);
        View v = b.getRoot();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        price = mViewModel.dispatch.getAll_fare(); // 시작 시
        cardImgList(); // 카드 이미지 리스트 가져오기
        findUnusedPoint(); // 사용자 미사용 포인트 검색
        selectedCard(); //간편결제를 눌렀을때 카드 선택뷰 보이기
        imgViewPager(view); //이미지 슬라이드 관련
        clickListenerApply(); // 클릭 리스너 연결

        b.paymentInformationHSPlace.setText(mViewModel.dispatch.getStart_place()); // 출발지
        b.paymentInformationHFPlace.setText(mViewModel.dispatch.getFinish_place()); // 도착지
        b.payHAllFare.setText(String.valueOf(price)); // 예상금액
        b.payToPeople.setText(String.valueOf(mViewModel.together)); // 동승인원
        b.payTotalAmount.setText(String.valueOf(price)); // 총합 지불 금액

        Log.d("PayInfoFragment", "사용자 아이디 : " + mViewModel.dispatch.getM_id());
        Log.d("PayInfoFragment", "사용자 미사용 포인트 : " + unusedPoint);
    }

    // 클릭 리스너 활성화
    private void clickListenerApply() {
        b.paymentInformationFinish.setOnClickListener(this);
        b.pointBtnAll.setOnClickListener(this);
        b.pointBtnUse.setOnClickListener(this);
    }

    // 이미지 슬라이드 관련
    private void imgViewPager(View v) {
        ViewPager viewPager = v.findViewById(R.id.payment_pager);
        viewPager.setClipToPadding(false);
        float density = getResources().getDisplayMetrics().density;
        CircleIndicator indicator = v.findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        viewPager.setCurrentItem(2);
        viewPager.setAdapter(new PaymentAdapter(getContext(), imageList));
    }


    /* ---------------------------
            카드 선택 관련
       --------------------------- */
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

    //카드 이미지 리스트
    private void cardImgList() {
        imageList = new ArrayList();
        imageList.add(R.drawable.ic_logo);
        imageList.add(R.drawable.ic_map);
        imageList.add(R.drawable.banner);
        imageList.add(R.drawable.logo);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.payment_information_finish:   // 결제완료 버튼
                normalMatching();
//                kakaoSubscription(b.payTotalAmount.getText().toString());
                return;

            case R.id.point_btn_all :  //모두적용 버튼
                allUsedPoint(v);
                return;

            case R.id.point_btn_use :   // 포인트 적용 버튼
                usedPoint(v);
                return;
        }
    }

    /* ------------------------
         레트로핏 사용 메서드
       ------------------------ */
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

        subscriptionRequest = KakaoService.getInstance().getApi().kakaoSubscription(DrivingModel.auth, map);
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

    // 결제 완료 시 일반 매칭
    // 넘어갈때 저장하고 CallWaiting 으로 넘어간다.
    private void normalMatching() {
        Log.d("payInfoFragemnt", "결제 완료 눌림");
        normalMatchingRequest = DataService.getInstance().matchAPI.registerNormalMatching(mViewModel.dispatch);
        normalMatchingRequest.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 200) {
                    String dp_id = response.body();
                    mViewModel.dispatch.setDp_id(dp_id);
                    Log.d("PayInfoFragment", "결제 완료 dp_id : " +  dp_id);

                    if(b.paymentMeet.isChecked()) {
                        mViewModel.use_cash = 1;

                    }

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    /* ------------------------
            가내수공업 메서드
       ------------------------ */
    private void allUsedPoint(View v) {
        Log.d("PayInfoFragment", "포인트 모두 적용");
        b.paymentInformationRestMPoint.setText("0");
        b.payPoResult.setText(unusedPoint + "원");
        mViewModel.usedPoint = unusedPoint; // 모두 사용이라 모든 포인트 사용포인트로 적용
        b.payTotalAmount.setText(String.valueOf(price - unusedPoint));
        hideKeyBoard(); // 키보드 비활성화
    }

    private void usedPoint(View v) {
        String getUsePoint = b.pointEt.getText().toString();
        Log.d("PayInfoFragment", "적용하려는 포인트 : " + getUsePoint);

        if(getUsePoint.isEmpty()) { // 포인트 값 입력 확인
            Snackbar.make(v, "포인트 값을 입력해주세요", Snackbar.LENGTH_SHORT).show();
            return;
        }
        point = Integer.parseInt(getUsePoint);

        if(point <= unusedPoint){ // 입력 포인트 초과되는지 확인
            b.paymentInformationRestMPoint.setText(String.valueOf(unusedPoint - point));
            b.payPoResult.setText(String.valueOf(point));
            b.payTotalAmount.setText(String.valueOf(price - point));
            b.pointEt.setText("");
            mViewModel.usedPoint = point; // 포인트 적용
            Log.d("PayInfoFragment", "포인트 적용 가격 : " + (price - point));
        }else {
            Snackbar.make(v, "보유 포인트 초과하여 사용은 불가합니다.", Snackbar.LENGTH_SHORT).show();
            b.pointEt.setText("");
        }

        hideKeyBoard(); // 키보드 비활성화
    }

    //키보드 숨기기
    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(b.pointEt.getWindowToken(), 0);
    }

    // perf 가져오기
    public String getPreferenceString(String key) {
        SharedPreferences pref = getActivity().getSharedPreferences("loginUser", getActivity().MODE_PRIVATE);
        return pref.getString(key, "");
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        if(pointRequest != null) pointRequest.cancel();
        if(subscriptionRequest != null) subscriptionRequest.cancel();
        if (normalMatchingRequest != null) normalMatchingRequest.cancel();
    }
}