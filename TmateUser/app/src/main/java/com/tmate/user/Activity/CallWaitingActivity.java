package com.tmate.user.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tmate.user.R;

public class CallWaitingActivity extends AppCompatActivity {

    private TextView wait_h_s_place;
    private String s_place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_waiting);

        wait_h_s_place = findViewById(R.id.wait_h_s_place);
        s_place = wait_h_s_place.getText().toString();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(CallWaitingActivity.this, MainViewActivity.class);
                intent.putExtra("출발지", "경북대");
//                Log.d("CallWaitingActivity", s_place);
                startActivity(intent);
            }
        }, 4000);
    }
}