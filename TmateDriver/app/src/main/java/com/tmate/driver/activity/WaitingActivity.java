package com.tmate.driver.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.tmate.driver.R;

public class WaitingActivity extends AppCompatActivity {
    Button btn_drive_stop;
    private Dialog dialog;
    private Button matching_btn_refusal;
    private Button matching_btn_accept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        dialog = new Dialog(WaitingActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_matching);

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
                if (!WaitingActivity.this.isFinishing()) {
                    showDialog();
                }
            }
        }, 4000);
    }

    public void showDialog(){
        dialog.show();

        matching_btn_refusal = dialog.findViewById(R.id.matching_btn_refusal);
        matching_btn_refusal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        matching_btn_accept = dialog.findViewById(R.id.matching_btn_accept);
        matching_btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WaitingActivity.this, PaymentActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}