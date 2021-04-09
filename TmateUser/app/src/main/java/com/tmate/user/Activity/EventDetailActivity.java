package com.tmate.user.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.tmate.user.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class EventDetailActivity extends AppCompatActivity {

    private ImageView btn_back_eventDetail;
    private TextView tv_ev_title;
    private TextView tv_ev_duration;
    private ImageView iv_ev_img;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        tv_ev_title = (TextView) findViewById(R.id.tv_ev_title);
        tv_ev_duration = (TextView) findViewById(R.id.tv_ev_duration);
        iv_ev_img = (ImageView) findViewById(R.id.iv_ev_img);

        Intent intent = getIntent();

        String imgURL = intent.getStringExtra("imgURL");
        tv_ev_title.setText(intent.getStringExtra("tv_ev_title"));
        tv_ev_duration.setText(intent.getStringExtra("tv_ev_duration"));
//        Glide.with(this).load("http://ec2-52-79-142-104.ap-northeast-2.compute.amazonaws.com:8080"+"/api/display?fileName="+imgURL).into(iv_ev_img);
//        iv_ev_img.setImageURI(Uri.parse("http://ec2-52-79-142-104.ap-northeast-2.compute.amazonaws.com:8080"+"/api/display?fileName="+imgURL));
        Thread mThread = new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL("http://10.0.2.2:9090"+"/api/display?fileName="+imgURL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        mThread.start();

        try {
            mThread.join();

            iv_ev_img.setImageBitmap(bitmap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 이전 화면
        btn_back_eventDetail = findViewById(R.id.btn_back_eventDetail);
        btn_back_eventDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}