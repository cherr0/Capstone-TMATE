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
    private Dialog dialog2;
    private int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 100;
    private int result =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dialog2 != null && dialog2.isShowing()) {
            dialog2.dismiss();
        }
    }

}