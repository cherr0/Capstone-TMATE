package com.tmate.user.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.tmate.user.R;
import com.tmate.user.databinding.ActivityCallWaitingBinding;
import com.tmate.user.net.DataService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallWaitingActivity extends AppCompatActivity {

     // 이용코드는 필수
    String dp_id;

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

            b.cwMerchantUid.setText(intent.getStringExtra("dp_id"));
            dp_id = intent.getStringExtra("dp_id");
            b.waitHSPlace.setText(intent.getStringExtra("start_place"));
            b.cwHSLttd.setText(intent.getStringExtra("start_lat"));
            b.cwHSLngtd.setText(intent.getStringExtra("start_lng"));

            b.waitHFPlace.setText(intent.getStringExtra("finish_place"));
            b.cwHFLttd.setText(intent.getStringExtra("finish_lat"));
            b.cwHFLngtd.setText(intent.getStringExtra("finish_lng"));
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

        ImageView gif = findViewById(R.id.avi);
        Glide.with(this).asGif()
                .load(R.drawable.taxi_loading)
                .into(gif);

    }

    // 내부 쓰레드 클래스
    public class Matching implements Runnable {
        @Override
        public void run() {
            request = DataService.getInstance().matchAPI.getd_idDuringCall(dp_id);
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
                            intent.putExtra("dp_id",dp_id);
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
        deleteRequest = DataService.getInstance().matchAPI.removeNormalCall(dp_id);
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



