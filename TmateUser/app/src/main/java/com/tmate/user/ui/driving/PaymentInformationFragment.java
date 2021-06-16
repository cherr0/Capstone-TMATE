package com.tmate.user.ui.driving;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.tmate.user.Activity.CallWaitingActivity;
import com.tmate.user.R;
import com.tmate.user.adapter.PaymentAdapter;
import com.tmate.user.adapter.SnapPagerScrollListener;
import com.tmate.user.data.CardData;
import com.tmate.user.data.Dispatch;
import com.tmate.user.data.SubscriptionRes;
import com.tmate.user.databinding.FragmentPaymentInformationBinding;
import com.tmate.user.net.DataService;
import com.tmate.user.net.KakaoService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentInformationFragment extends Fragment implements View.OnClickListener {

    private FragmentPaymentInformationBinding b;
    private DrivingModel mViewModel;
    private PaymentAdapter adapter;


    private ArrayList<CardData> cardArrayList;
    private Integer unusedPoint;
    private Integer point;
    private Integer price;
    private LinearLayoutManager linearLayoutManager;


    List<CardData> cardList;

    Call<Dispatch> getCheckingMasterOrUser;
    Call<Integer> pointRequest;
    Call<String> normalMatchingRequest;
    Call<List<CardData>> cardListRequest;
    Call<Boolean> modifyTogetherStatus;

    // 쓰레드
    boolean isRunning;
    Handler handler;
    Checking2 checking2;
    // 동승 매칭 이용코드
    String together_dp_id;
    // 동승 여부
    String together;
    
     // 방장
    String master;
    // 승객
    String user;

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
        getData(); // 카드 리스트
        findUnusedPoint(); // 사용자 미사용 포인트 검색
        selectedCard(); //간편결제를 눌렀을때 카드 선택뷰 보이기
        clickListenerApply(); // 클릭 리스너 연결

        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        b.payInfoRv.setLayoutManager(linearLayoutManager);
        cardArrayList = new ArrayList<>();
        adapter = new PaymentAdapter(cardArrayList);
        b.payInfoRv.setAdapter(adapter);

        // 뷰페이저 같이 쓰는거
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(b.payInfoRv);

        SnapPagerScrollListener listener = new SnapPagerScrollListener(
                snapHelper,
                SnapPagerScrollListener.ON_SCROLL,
                true,
                new SnapPagerScrollListener.OnChangeListener() {
                    @Override
                    public void onSnapped(int position) {

                        //position 받아서 이벤트 처리
                    }
                }
        );
        b.payInfoRv.addOnScrollListener(listener);


        if(mViewModel.together.equals("1")){
            b.paymentInformationHSPlace.setText(mViewModel.dispatch.getStart_place()); // 출발지
            b.paymentInformationHFPlace.setText(mViewModel.dispatch.getFinish_place()); // 도착지
            b.payHAllFare.setText(String.valueOf(price)); // 예상금액
            b.payToPeople.setText(mViewModel.together); // 동승인원
            b.payTotalAmount.setText(String.valueOf(price)); // 총합 지불 금액
        }
        else {
            together_dp_id = mViewModel.dispatch.getDp_id();
            checkingMasterOrUser(together_dp_id);
        }


        Log.d("PayInfoFragment", "사용자 아이디 : " + mViewModel.dispatch.getM_id());
        Log.d("PayInfoFragment", "사용자 미사용 포인트 : " + unusedPoint);
    }

    // 클릭 리스너 활성화3
    private void clickListenerApply() {
        b.paymentInformationFinish.setOnClickListener(this);
        b.pointBtnAll.setOnClickListener(this);
        b.pointBtnUse.setOnClickListener(this);
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
    private void getData() {
        CheckBox payment_card_check = getLayoutInflater().inflate(R.layout.payment_card, null).findViewById(R.id.payment_card_check);
        cardListRequest = DataService.getInstance().memberAPI.getUserCard(getPreferenceString("m_id"));
        cardListRequest.enqueue(new Callback<List<CardData>>() {
            @Override
            public void onResponse(Call<List<CardData>> call, Response<List<CardData>> response) {
                if(response.code() == 200 && response.body() != null) {
                    cardList = response.body();
                    Log.d("PayInfoFragment", "바디 : " + response.body());
                    Log.d("PayInfoFragment", "코드 : " + response.code());
                    for(CardData data : cardList) {
                        adapter.addItem(data);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<CardData>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    // 여기서 동승 구분 해야 된다.
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.payment_information_finish:   // 결제완료 버튼
                if(mViewModel.together.equals("1"))
                    normalMatching();
                else{
                    Dispatch dispatch = new Dispatch();
                    dispatch.setDp_id(together_dp_id);
                    dispatch.setDp_status("2");
                    // 상태 변화는 레트로 핏 메서드 넣기
                    modifyTogetherStatus = DataService.getInstance().matchAPI.modifyTogetherStatus(dispatch);
                    modifyTogetherStatus.enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if (response.code() == 200 && response.body() != null) {
                                NavController controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                                controller.navigate(R.id.action_paymentInformationFragment_to_callWaitingFragment);
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });

                }

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
                    b.paymentInformationMPoint.setText(String.valueOf(unusedPoint));
                    b.paymentInformationRestMPoint.setText(String.valueOf(unusedPoint));
                } else {
                    b.paymentInformationMPoint.setText("0");
                    b.paymentInformationRestMPoint.setText("0");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
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

                    // 만나서 결제 시 현금 결제 여부 추가
                    if(b.paymentMeet.isChecked()) {
                        mViewModel.use_cash = 1;
                    }

                    Log.d("PayInfoFragment", "넘어가는 dispatch 정보 : " + mViewModel.dispatch);
                    NavController controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                    controller.navigate(R.id.action_paymentInformationFragment_to_callWaitingFragment);

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
        if(price < unusedPoint) {
            Log.d("PayInfoFragment","포인트 적용 실패 price : " + price);
            Snackbar.make(v,"기본 요금을 넘는 포인트는 사용이 불가합니다.", Snackbar.LENGTH_SHORT).show();
            return;
        }else {
            b.payTotalAmount.setText(String.valueOf(price - unusedPoint));
        }


        b.paymentInformationRestMPoint.setText("0");
        b.payPoResult.setText(unusedPoint + "원");
        mViewModel.dispatch.setUse_point(unusedPoint); // 모두 사용이라 모든 포인트 사용포인트로 적용

        hideKeyBoard(); // 키보드 비활성화
    }

    private void usedPoint(View v) {
        String getUsePoint = b.pointEt.getText().toString();

        Log.d("PayInfoFragment", "적용하려는 포인트 : " + getUsePoint);

        if(getUsePoint.isEmpty()) { // 포인트 값 입력 확인
            Snackbar.make(v, "포인트 값을 입력해주세요", Snackbar.LENGTH_SHORT).show();
            return;
        }
        if(price < Integer.parseInt(getUsePoint)){
            Log.d("PayInfoFragment","포인트 적용 실패 price : " + price);
            Snackbar.make(v,"기본 요금을 넘는 포인트는 사용이 불가합니다.", Snackbar.LENGTH_SHORT).show();
            return;
        }
        point = Integer.parseInt(getUsePoint);

        if(point <= unusedPoint){ // 입력 포인트 초과되는지 확인
            b.paymentInformationRestMPoint.setText(String.valueOf(unusedPoint - point));
            b.payPoResult.setText(String.valueOf(point));
            b.payTotalAmount.setText(String.valueOf(price - point));
            b.pointEt.setText("");
            mViewModel.dispatch.setUse_point(point);  // 포인트 적용
            mViewModel.use_point = point;
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

    // perf 저장
    public void setPreference(String key, String value){
        SharedPreferences pref = getActivity().getSharedPreferences("loginUser", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    // 사용자와 방장을 가리기위해
    public void checkingMasterOrUser(String dp_id) {

        getCheckingMasterOrUser = DataService.getInstance().matchAPI.getCurrentDispatchBeforesuccess(dp_id);
        getCheckingMasterOrUser.enqueue(new Callback<Dispatch>() {
            @Override
            public void onResponse(Call<Dispatch> call, Response<Dispatch> response) {
                if (response.code() == 200 && response.body() != null) {
                    Dispatch di = response.body();
                    master = di.getM_id();
                    user = getPreferenceString("m_id");
                    together = di.getDp_id().substring(18);
                    price = di.getAll_fare();
                    mViewModel.together = together;
                    mViewModel.dispatch.setStart_place(di.getStart_place());
                    mViewModel.dispatch.setFinish_place(di.getFinish_place());

                    if (master.equals(user)) {
                        b.paymentInformationHSPlace.setText(di.getStart_place()); // 출발지
                        b.paymentInformationHFPlace.setText(di.getFinish_place()); // 도착지
                        b.payHAllFare.setText(String.valueOf(di.getAll_fare())); // 예상금액
                        b.payToPeople.setText(di.getDp_id().substring(18)); // 동승인원
                        b.payTotalAmount.setText(String.valueOf(di.getAll_fare())); // 총합 지불 금액
                        b.paymentInformationFinish.setVisibility(View.VISIBLE);
                    }

                    // 여기서 방장이 아니라면 쓰레드를 돌려야지
                    else{
                        b.paymentInformationHSPlace.setText(di.getStart_place()); // 출발지
                        b.paymentInformationHFPlace.setText(di.getFinish_place()); // 도착지
                        b.payHAllFare.setText(String.valueOf(di.getAll_fare())); // 예상금액
                        b.payToPeople.setText(di.getDp_id().substring(18)); // 동승인원
                        b.payTotalAmount.setText(String.valueOf(di.getAll_fare())); // 총합 지불 금액
                        b.paymentInformationFinish.setVisibility(View.VISIBLE);
                        b.paymentInformationFinish.setVisibility(View.GONE);
                        handler = new Handler();
                        checking2 = new Checking2();
                        isRunning = true;

                        Thread thread = new Thread(() -> {
                            try {
                                while (isRunning) {
                                    handler.post(checking2);
                                    Thread.sleep(2000);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });

                        isRunning = true;
                        thread.start();
                    }
                }
            }

            @Override
            public void onFailure(Call<Dispatch> call, Throwable t) {
                t.printStackTrace();
            }

        });

    }

    // 내부 쓰레드 클래스
    public class Checking2 implements Runnable {
        @Override
        public void run() {
            getCheckingMasterOrUser = DataService.getInstance().matchAPI.getCurrentDispatchBeforesuccess(together_dp_id);
            getCheckingMasterOrUser.enqueue(new Callback<Dispatch>() {
                @Override
                public void onResponse(Call<Dispatch> call, Response<Dispatch> response) {
                    if (response.code() == 200 && response.body() != null) {
                        Dispatch dispatch = response.body();
                        String dp_status = dispatch.getDp_status();

                        if (dp_status.equals("2")) {
                            isRunning = false;
                            NavController controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                            controller.navigate(R.id.action_paymentInformationFragment_to_callWaitingFragment);
                        }else {
                            isRunning = true;
                        }
                    }
                }

                @Override
                public void onFailure(Call<Dispatch> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        isRunning = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        isRunning = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        isRunning = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        isRunning = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        if(pointRequest != null) pointRequest.cancel();
        if (normalMatchingRequest != null) normalMatchingRequest.cancel();
    }
}