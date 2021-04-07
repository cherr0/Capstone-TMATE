package com.tmate.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CallGeneralActivity extends AppCompatActivity {

    Button btn_fare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_general);

        btn_fare = findViewById(R.id.btn_fare);

        btn_fare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CallGeneralActivity.this,PaymentSuccessActivity.class);
                startActivity(intent);
            }
        });

    }
}