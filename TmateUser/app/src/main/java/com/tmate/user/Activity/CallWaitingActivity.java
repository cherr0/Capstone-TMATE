package com.tmate.user.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tmate.user.R;
import com.tmate.user.databinding.ActivityCallWaitingBinding;
import com.tmate.user.net.DataService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallWaitingActivity extends AppCompatActivity {

     // 이용코드는 필수
    String merchant_uid;

     // 레트로핏
     // 1. 기사코드 가져오는 요청 객체
     Call<String> request;
     // 2. 호출 취소 시 호출을 삭제하는 요청 객체
     Call<Boolean> deleteRequest;

     // 뷰바인딩
    ActivityCallWaitingBinding b;

     // 쓰레드 관련
     boolean isRunning;
     Matching matching;
     Handler handler;

    // 호출 대기 시간 관련
    long beforeTime = System.currentTimeMillis();


    // CallGeneralActivity에서 넘어오는 인텐트를 받기 위함
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityCallWaitingBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        if (getIntent() != null) {
            intent = getIntent();
            // 이용 코드도 받아야 한다.
            /*
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
             */
            b.cwMerchantUid.setText(intent.getStringExtra("merchant_uid"));
            merchant_uid = intent.getStringExtra("merchant_uid");
            b.waitHSPlace.setText(intent.getStringExtra("h_s_place"));
            b.cwHSLttd.setText(intent.getStringExtra("h_s_lttd"));
            b.cwHSLngtd.setText(intent.getStringExtra("h_s_lngtd"));

            b.waitHFPlace.setText(intent.getStringExtra("h_f_place"));
            b.cwHFLttd.setText(intent.getStringExtra("h_f_lttd"));
            b.cwHFLngtd.setText(intent.getStringExtra("h_f_lngtd"));
        }

        // 쓰레드 상태
        handler = new Handler();
        matching = new Matching();
        isRunning = true;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isRunning) {
                        long afterTime = System.currentTimeMillis();
                        long secTime = (afterTime - beforeTime)/1000;
                        System.out.println("시간차 " + secTime);
                        handler.post(matching);
                        Thread.sleep(3000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        isRunning=true;
        thread.start();

    }

    // 내부 쓰레드 클래스
    public class Matching implements Runnable {
        @Override
        public void run() {
            request = DataService.getInstance().matchAPI.getd_idDuringCall(merchant_uid);
            request.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.code() == 200) {
                        String d_id = response.body();

                        if (d_id == null) {
                            isRunning = true;
                        }

                        else{
                            isRunning = false;
                            Intent intent = new Intent(CallWaitingActivity.this, CarInfoActivity.class);
                            intent.putExtra("merchant_uid",merchant_uid);
                            intent.putExtra("d_id", d_id);
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        isRunning = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isRunning = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isRunning = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }

    // 뒤로 가기 버튼 눌릴때
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        deleteRequest = DataService.getInstance().matchAPI.removeNormalCall(merchant_uid);
        deleteRequest.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.code() == 200 && response.body() != null) {
                    Toast.makeText(CallWaitingActivity.this, "호출을 취소하셨습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}



