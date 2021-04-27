package com.tmate.driver.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tmate.driver.R;

public class NoticeDetailActivity extends AppCompatActivity {

    private ImageView btn_back_noticeDetail;
    private TextView notice_detail_title, notice_date, notice_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);
        onBinding();

        btn_back_noticeDetail = findViewById(R.id.btn_back_noticeDetail);
        btn_back_noticeDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    void onBinding() {
        notice_detail_title = (TextView) findViewById(R.id.notice_detail_title);
        notice_detail_title.setText(getIntent().getStringExtra("title"));

        notice_date = (TextView) findViewById(R.id.notice_date);
        notice_date.setText(getIntent().getStringExtra("date"));

        notice_content = (TextView) findViewById(R.id.notice_content);
        notice_content.setText(getIntent().getStringExtra("content"));

    }
}