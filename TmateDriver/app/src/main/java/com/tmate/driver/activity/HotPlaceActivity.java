package com.tmate.driver.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.tmate.driver.R;
import com.tmate.driver.databinding.ActivityHotPlaceBinding;

public class HotPlaceActivity extends AppCompatActivity {
    private ActivityHotPlaceBinding b;
    private View v;
    private WebSettings wvset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityHotPlaceBinding.inflate(getLayoutInflater());
        v = b.getRoot();
        setContentView(v);

        b.wvHotplace.setWebViewClient(new WebViewClient()); //클릭 시 새창 안뜨게
        wvset = b.wvHotplace.getSettings(); //세부 세팅 등록
        wvset.setJavaScriptEnabled(true); //자바스크립트 허용
        b.wvHotplace.loadUrl("http://ec2-52-79-142-104.ap-northeast-2.compute.amazonaws.com:8080/tuser/place");
    }
}