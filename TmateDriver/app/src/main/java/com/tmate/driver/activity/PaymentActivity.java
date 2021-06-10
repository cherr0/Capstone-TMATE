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
import android.widget.EditText;
import android.widget.TextView;

import com.skt.Tmap.TMapTapi;
import com.tmate.driver.R;
import com.tmate.driver.databinding.ActivityPaymentBinding;
import com.tmate.driver.net.DataService;
import com.tmate.driver.services.driving_overlay;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PaymentActivity extends AppCompatActivity {
    private ActivityPaymentBinding binding;
    private Dialog dialog2;

    private int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 100;
    private int result =0;

    // 네비에서 넘어오는 인텐트 정보
    // 1. 배차 정보
    String dp_id;

    // 레트로핏 관련
    Call<Boolean> modifyCallStatusRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (getIntent() != null) {
            dp_id = getIntent().getStringExtra("dp_id");
        }
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
        TextView textView = dialog2.findViewById(R.id.textView19);
        textView.setText(binding.editText.getText().toString()+"원\n입력하신 요금이\n맞습니까?");
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
                modifyCallStatusRequest = DataService.getInstance().call.modifyPaymentDpStatus(dp_id, Integer.valueOf(binding.editText.getText().toString()), "5");
                modifyCallStatusRequest.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.code() == 200) {
                            Intent intent = new Intent(PaymentActivity.this, CheckActivity.class);
                            startActivity(intent);
                            dialog2.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        t.printStackTrace();
                    }
                });

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