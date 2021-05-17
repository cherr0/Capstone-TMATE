package com.tmate.user.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tmate.user.R;
import com.tmate.user.net.DataService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallWaitingActivity extends AppCompatActivity {

     TextView cw_merchant_uid;

     TextView wait_h_s_place;
     TextView cw_h_s_lttd;
     TextView cw_h_s_lngtd;

     TextView wait_h_f_place;
     TextView cw_h_f_lttd;
     TextView cw_h_f_lngtd;

     TextView cw_match_message;

     // 이용코드는 필수
    String merchant_uid;

     // 레트로핏
     // 1. 기사코드 가져오는 요청 객체
     Call<String> request;
     // 2. 호출 취소 시 호출을 삭제하는 요청 객체
    Call<Boolean> deleteRequest;


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
        setContentView(R.layout.activity_call_waiting);

        initWidget();

        if (getIntent() != null) {
            intent = getIntent();
            // 이용 코드도 받아야 한다.
            cw_merchant_uid.setText(intent.getStringExtra("merchant_uid"));
            merchant_uid = intent.getStringExtra("merchant_uid");
            wait_h_s_place.setText(intent.getStringExtra("h_s_place"));
            cw_h_s_lttd.setText(intent.getStringExtra("h_s_lttd"));
            cw_h_s_lngtd.setText(intent.getStringExtra("h_s_lngtd"));

            wait_h_f_place.setText(intent.getStringExtra("h_f_place"));
            cw_h_f_lttd.setText(intent.getStringExtra("h_f_lttd"));
            cw_h_f_lngtd.setText(intent.getStringExtra("h_f_lngtd"));
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

    public void initWidget() {
        cw_merchant_uid = findViewById(R.id.cw_merchant_uid);
        cw_match_message = findViewById(R.id.cw_match_message);
        wait_h_s_place = findViewById(R.id.wait_h_s_place);
        cw_h_s_lttd = findViewById(R.id.cw_h_s_lttd);
        cw_h_s_lngtd = findViewById(R.id.cw_h_s_lngtd);
        wait_h_f_place = findViewById(R.id.wait_h_f_place);
        cw_h_f_lttd = findViewById(R.id.cw_h_f_lttd);
        cw_h_f_lngtd = findViewById(R.id.cw_h_s_lngtd);
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



