package com.tmate.driver.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.skt.Tmap.TMapTapi;
import com.skt.Tmap.TMapView;
import com.tmate.driver.databinding.ActivityMatchingBinding;
import com.tmate.driver.services.driving_overlay;

public class MatchingActivity extends AppCompatActivity {
    private ActivityMatchingBinding binding;
    private int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMatchingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        TMapView tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey("l7xx4fac78a5b9bf445db00bb99ae2708cee");
        TMapTapi tmaptapi = new TMapTapi(this);

        binding.btnRefusal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
                boolean isTmapApp = tmaptapi.isTmapApplicationInstalled(); //앱 설치했는지 판단
                tmaptapi.invokeNavigate("", 128.5058153f, 35.8356814f,0,true);
                finish();
            }
        });
    }
    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {   // 마시멜로우 이상일 경우
            if (!Settings.canDrawOverlays(this)) {              // 다른앱 위에 그리기 체크
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, uri);
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            } else {
                startMain();
            }
        } else {
            startMain();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                finish();
            } else {
                startMain();
            }
        }
    }


    void startMain() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, driving_overlay.class));
        } else {
            startService(new Intent(this, driving_overlay.class));
        }
        finish();
    }
}