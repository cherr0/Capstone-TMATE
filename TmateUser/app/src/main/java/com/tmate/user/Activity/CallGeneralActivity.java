package com.tmate.user.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button;

import com.tmate.user.R;

public class CallGeneralActivity extends AppCompatActivity {

    Button btn_fare;
    private ImageView btn_back_general;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_general);

        btn_fare = findViewById(R.id.btn_fare);

        btn_fare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CallGeneralActivity.this, PaymentSuccessActivity.class);
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