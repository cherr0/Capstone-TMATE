package com.tmate.driver.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.tmate.driver.R;
import com.tmate.driver.data.AttendList;
import com.tmate.driver.data.Ban;
import com.tmate.driver.databinding.ActivityBlackListSelectBinding;
import com.tmate.driver.databinding.ActivityNormalBlackListSelectBinding;
import com.tmate.driver.net.DataService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NormalBlackListSelectActivity extends AppCompatActivity {
    private final int blackList = 1;
    private ActivityNormalBlackListSelectBinding b;
    private String dp_id;
    private String ban_reason;
    Call<Boolean> blackListRequest;
    Call<List<AttendList>> attendRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityNormalBlackListSelectBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        Intent intent = getIntent();
        dp_id = intent.getExtras().getString("dp_id");

        attendList();

        b.btnNormalSeat.setOnClickListener(v -> {
            AddBlackList();
        });
    }
    private void reasonDialog() {
        final String[] items = new String[]{"너무 시끄러워요", "시간을 안지켜요", "술을 마신거 같아요", "목적지변경을 강요해요", "불친절해요."};
        AlertDialog.Builder dialog = new AlertDialog.Builder(NormalBlackListSelectActivity.this);
        dialog.setTitle("항목을 선택해주세요.");
        dialog.setSingleChoiceItems(
                items,
                0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ban_reason = items[which];
                    }
                });
        dialog.setPositiveButton("보내기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (blackList == 0)
                    finish();
            }
        });
        dialog.create();
        dialog.show();
    }


    //블랙리스트 등록
    private void AddBlackList() {
        Ban ban = new Ban();
        ban.setD_id(getPreferenceString("d_id"));
        ban.setBan_reason(ban_reason);
        ban.setM_id(getPreferenceString("m_id"));
        blackListRequest = DataService.getInstance().driver.addBlacklist(ban);
        blackListRequest.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.code() == 200 && response.body() != null) {
                    Intent addIntent = new Intent(NormalBlackListSelectActivity.this, MainViewActivity.class);
                    startActivity(addIntent);
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    // 승객 정보
    private void attendList() {
        attendRequest = DataService.getInstance().driver.historyAttendList(dp_id);
        attendRequest.enqueue(new Callback<List<AttendList>>() {
            @Override
            public void onResponse(Call<List<AttendList>> call, Response<List<AttendList>> response) {
                if (response.code() == 200 && response.body() != null) {
                    for (AttendList data : response.body()) {
                        b.normalMName.setText(data.getM_name());
                        b.normalMGender.setText(data.getGender());
                        b.normalMBirth.setText(data.getAge());
                        b.normalMName.setOnClickListener(v -> {
                            reasonDialog();
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<List<AttendList>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public String getPreferenceString(String key) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("loginDriver", getApplicationContext().MODE_PRIVATE);
        return pref.getString(key, "");
    }

    // 데이터 저장 함수
    public void setPreference(String key, String value) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("loginDriver", getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (attendRequest != null) attendRequest.cancel();
        if (blackListRequest != null) blackListRequest.cancel();
    }
}