package com.tmate.user;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class EventDetailActivity extends AppCompatActivity {

    private ImageView btn_back_eventDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        btn_back_eventDetail = findViewById(R.id.btn_back_eventDetail);
        btn_back_eventDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}