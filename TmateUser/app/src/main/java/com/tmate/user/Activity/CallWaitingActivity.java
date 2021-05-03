package com.tmate.user.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tmate.user.R;

public class CallWaitingActivity extends AppCompatActivity {

     TextView cw_merchant_uid;

     TextView wait_h_s_place;
     TextView cw_h_s_lttd;
     TextView cw_h_s_lngtd;

     TextView wait_h_f_place;
     TextView cw_h_f_lttd;
     TextView cw_h_f_lngtd;



     String s_place;



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

            wait_h_s_place.setText(intent.getStringExtra("h_s_place"));
            cw_h_s_lttd.setText(intent.getStringExtra("h_s_lttd"));
            cw_h_s_lngtd.setText(intent.getStringExtra("h_s_lngtd"));

            wait_h_f_place.setText(intent.getStringExtra("h_f_place"));
            cw_h_f_lttd.setText(intent.getStringExtra("h_f_lttd"));
            cw_h_f_lngtd.setText(intent.getStringExtra("h_f_lngtd"));
        }

//        wait_h_s_place = findViewById(R.id.wait_h_s_place);
//        s_place = wait_h_s_place.getText().toString();
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


    public void initWidget() {
        cw_merchant_uid = findViewById(R.id.cw_merchant_uid);
        wait_h_s_place = findViewById(R.id.wait_h_s_place);
        cw_h_s_lttd = findViewById(R.id.cw_h_s_lttd);
        cw_h_s_lngtd = findViewById(R.id.cw_h_s_lngtd);
        wait_h_f_place = findViewById(R.id.wait_h_f_place);
        cw_h_f_lttd = findViewById(R.id.cw_h_f_lttd);
        cw_h_f_lngtd = findViewById(R.id.cw_h_s_lngtd);
    }
}