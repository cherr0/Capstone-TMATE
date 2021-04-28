package com.tmate.driver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.tmate.driver.R;

public class CheckActivity extends AppCompatActivity {

    private Button driving_finish;
    ImageView iv_black_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);


        iv_black_list = findViewById(R.id.iv_black_list);
        iv_black_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckActivity.this, BlackListSelectActivity.class);
                intent.putExtra("blackList", 1);
                startActivity(intent);
                finish();
            }
        });
        driving_finish = findViewById(R.id.driving_finish);
        driving_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckActivity.this, WaitingActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}