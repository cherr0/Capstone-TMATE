package com.tmate.driver.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.tmate.driver.R;

public class WaitingActivity extends AppCompatActivity {
    Button btn_drive_stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        btn_drive_stop = findViewById(R.id.btn_drive_stop);
        btn_drive_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WaitingActivity.this, MatchingActivity.class);
                startActivity(intent);
            }
        }, 4000);
    }
}