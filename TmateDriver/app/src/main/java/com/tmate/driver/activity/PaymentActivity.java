package com.tmate.driver.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import com.skt.Tmap.TMapTapi;
import com.tmate.driver.R;
import com.tmate.driver.databinding.ActivityPaymentBinding;
import com.tmate.driver.services.driving_overlay;


public class PaymentActivity extends AppCompatActivity {
    private ActivityPaymentBinding binding;
    private Dialog dialog;
    private Dialog dialog2;
    private int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 100;
    private int result =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        result = intent.getExtras().getInt("서비스결과");
        if(result == 1) {
            dialog = new Dialog(PaymentActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_directions);
            mapShowdialog();
        }
        dialog2 = new Dialog(PaymentActivity.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.dialog_payment);
        binding.btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payshowDialog();
            }
        });
    }
    public void payshowDialog(){
        dialog2.show();
        Button btn_no = dialog2.findViewById(R.id.btn_no);
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });
        Button btn_yes = dialog2.findViewById(R.id.btn_yes);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentActivity.this, CheckActivity.class);
                startActivity(intent);
                dialog2.dismiss();
            }
        });
    }
    public void mapShowdialog() {
        dialog.show();
        Button bgt = dialog.findViewById(R.id.btn_go_tmap);
        bgt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
                TMapTapi tmaptapi = new TMapTapi(getApplication());
                boolean isTmapApp = tmaptapi.isTmapApplicationInstalled(); //앱 설치했는지 판단
                tmaptapi.invokeNavigate("", 128.5829737f, 35.8861837f,0,true);
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
            intent.putExtra("경로",2);
            startForegroundService(intent);
        } else {
            startService(new Intent(this, driving_overlay.class));
        }
        finish();
    }
}