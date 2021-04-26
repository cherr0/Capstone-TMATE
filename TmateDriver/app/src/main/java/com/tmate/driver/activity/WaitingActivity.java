package com.tmate.driver.activity;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.skt.Tmap.TMapTapi;
import com.skt.Tmap.TMapView;
import com.tmate.driver.R;
import com.tmate.driver.services.driving_overlay;

public class WaitingActivity extends AppCompatActivity {
    Button btn_drive_stop;
    private Dialog dialog;
    private Button matching_btn_refusal;
    private Button matching_btn_accept;
    private int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 100;
    private TMapView tMapView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey("l7xx4fac78a5b9bf445db00bb99ae2708cee");

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
                checkPermission();
                TMapTapi tmaptapi = new TMapTapi(getApplication());
                boolean isTmapApp = tmaptapi.isTmapApplicationInstalled(); //앱 설치했는지 판단
                tmaptapi.invokeNavigate("", 128.5058153f, 35.8356814f,0,true);
                dialog.dismiss();
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
            Intent intent = new Intent(this, driving_overlay.class);
            intent.putExtra("경로",3);
            startForegroundService(intent);
        } else {
            startService(new Intent(this, driving_overlay.class));
        }
        finish();
    }
}