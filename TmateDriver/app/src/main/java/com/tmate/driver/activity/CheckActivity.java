package com.tmate.driver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.tmate.driver.R;
import com.tmate.driver.data.Dispatch;
import com.tmate.driver.databinding.ActivityCheckBinding;
import com.tmate.driver.net.DataService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckActivity extends AppCompatActivity {

    ActivityCheckBinding b;

    String dp_id;

    Call<Dispatch> finishRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityCheckBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        if (getIntent() != null) {
            dp_id = getIntent().getStringExtra("dp_id");
            Log.d("CheckActivity","받은 dp_id 값 : " + dp_id);
        }

        finishRequest = DataService.getInstance().driver.getDriveFinish(dp_id);
        finishRequest.enqueue(new Callback<Dispatch>() {
            @Override
            public void onResponse(Call<Dispatch> call, Response<Dispatch> response) {
                if(response.code() == 200 && response.body() != null) {
                    Log.d("CheckActivity", "바디 값 : " + response.body());
                    Dispatch data = response.body();
                    b.tvStartPlace.setText(data.getStart_place());  // 출발지
                    b.tvEndPlace.setText(data.getFinish_place());   // 도착지
                    b.togetherValue.setText(data.getDp_id().substring(18)); // 동승 인원
                    b.distanceValue.setText(String.valueOf(data.getEp_distance()));    // 이동 거리
                    b.allFare.setText(String.valueOf(data.getAll_fare()) + "원");  // 총 요금
                }else {
                    try {
                        Log.d("CheckActivity", "에러 : " + response);
                        assert response.errorBody() != null;
                        Log.d("CheckActivity", "데이터 삽입 실패 : " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<Dispatch> call, Throwable t) {
                t.printStackTrace();
            }
        });

        b.ivBlackList.setOnClickListener(v -> {
            Intent intent = new Intent(CheckActivity.this, BlackListSelectActivity.class);
            intent.putExtra("blackList", 1);
            startActivity(intent);
            finish();
        });

        b.drivingFinish.setOnClickListener(v -> {
            Intent intent = new Intent(CheckActivity.this, WaitingActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(finishRequest != null) finishRequest.cancel();
    }
}