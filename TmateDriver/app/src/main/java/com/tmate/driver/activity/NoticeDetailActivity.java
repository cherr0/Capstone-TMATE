package com.tmate.driver.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.tmate.driver.R;

public class NoticeDetailActivity extends AppCompatActivity {

    private ImageView btn_back_noticeDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);

        btn_back_noticeDetail = findViewById(R.id.btn_back_noticeDetail);
        btn_back_noticeDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}