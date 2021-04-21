package com.tmate.driver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.tmate.driver.R;

public class CheckActivity extends AppCompatActivity {

    private Button btn_black_list_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        btn_black_list_submit = findViewById(R.id.btn_black_list_submit);
        btn_black_list_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckActivity.this, BlackListSelectActivity.class);
                intent.putExtra("blackList", 1);
                startActivity(intent);
            }
        });
    }
}