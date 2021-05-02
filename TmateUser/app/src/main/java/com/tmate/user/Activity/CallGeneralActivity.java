package com.tmate.user.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.tmate.user.R;

public class CallGeneralActivity extends AppCompatActivity {

    Button btn_fare;
    private ImageView btn_back_general;
    Button btn_fare1;

    // 일반 호출 설정시 넘어온다.
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_general);

        if(getIntent() != null){
            intent = getIntent();

        }

        btn_fare = findViewById(R.id.btn_fare);
        btn_fare1 = findViewById(R.id.btn_fare2);

        btn_fare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CallGeneralActivity.this, PaymentSuccessActivity.class);
                startActivity(intent);
            }
        });

        btn_fare1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CallGeneralActivity.this, CallWaitingActivity.class);
                startActivity(intent);
            }
        });

        btn_back_general = findViewById(R.id.btn_back_general);
        btn_back_general.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}