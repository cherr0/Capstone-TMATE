package com.tmate.driver.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.tmate.driver.R;
import com.tmate.driver.data.AttendList;
import com.tmate.driver.data.Ban;
import com.tmate.driver.data.DriverHistory;
import com.tmate.driver.databinding.ActivityBlackListSelectBinding;
import com.tmate.driver.net.DataService;

import net.colindodd.toggleimagebutton.ToggleImageButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlackListSelectActivity extends AppCompatActivity {
    private final int blackList = 1;
    private String dp_id;
    private String ban_reason;
    private ActivityBlackListSelectBinding b;
    Call<Boolean> blackListRequest;
    Call<List<AttendList>> attendRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityBlackListSelectBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());


        Intent intent = getIntent();
        int blackList = intent.getExtras().getInt("blackList");
        dp_id = intent.getExtras().getString("dp_id");
        Log.d("BlackListActivity", "dp_id : " + dp_id);
        Log.d("blackList","뭐넘어오냐"+ blackList);
        attendList(); // 블랙리스트 좌석 별 승객 정보



        final List<String> list = new ArrayList<String>();
        b.btnSeat.setOnClickListener(v -> {
            blackListAdd();
        });
    }


    // 사유 다이얼로그
    private void reasonDialog() {
        final String[] items = new String[]{"너무 시끄러워요", "시간을 안지켜요", "술을 마신거 같아요", "목적지변경을 강요해요", "불친절해요."};
        AlertDialog.Builder dialog = new AlertDialog.Builder(BlackListSelectActivity.this);
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

    // 블랙리스트 등록
    private void blackListAdd() {
        Ban ban = new Ban();
        ban.setD_id(getPreferenceString("d_id"));
        ban.setBan_reason(ban_reason);
        ban.setM_id(getPreferenceString("m_id"));
        blackListRequest = DataService.getInstance().driver.addBlacklist(ban);
        blackListRequest.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("BlackListActivity", "바디 값 : " + response.body());
                    Intent requestIntent = new Intent(BlackListSelectActivity.this, MainViewActivity.class);
                    startActivity(requestIntent);
                }
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    // 좌석 별 승객 정보
    private void attendList() {
        attendRequest = DataService.getInstance().driver.historyAttendList(dp_id);
        attendRequest.enqueue(new Callback<List<AttendList>>() {
            @Override
            public void onResponse(Call<List<AttendList>> call, Response<List<AttendList>> response) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("BlackListActivity", "바디 값 : " + response.body());
                    for (AttendList data : response.body()) {
                        switch (data.getSeat()) {
                            case "0" :

                                b.seatOneMName.setText(data.getM_name());
                                b.seatOneMGender.setText(data.getGender());
                                b.seatOneMBirth.setText(data.getAge());
                                b.seatOneInfo.setVisibility(View.VISIBLE);
                                b.seatOne.setOnClickListener(v -> {
                                    b.seatOne.setChecked(true);
                                    b.seatTwo.setChecked(false);
                                    b.seatThree.setChecked(false);
                                    setPreference("m_id",data.getM_id());
                                    if (b.seatOne.isChecked())
                                    reasonDialog();
                                    b.seatOneReason.setText(ban_reason);
                                });
                                break;
                            case "1" :
                                b.seatTwoMName.setText(data.getM_name());
                                b.seatTwoMGender.setText(data.getGender());
                                b.seatTwoMBirth.setText(data.getAge());
                                b.seatTwoInfo.setVisibility(View.VISIBLE);
                                b.seatTwo.setOnClickListener(v -> {
                                    b.seatOne.setChecked(false);
                                    b.seatTwo.setChecked(true);
                                    b.seatThree.setChecked(false);
                                    setPreference("m_id",data.getM_id());
                                    if (b.seatTwo.isChecked())
                                    reasonDialog();
                                    b.seatTwoReason.setText(ban_reason);
                                });
                                break;
                            case "2" :
                                b.seatThreeMName.setText(data.getM_name());
                                b.seatThreeMGender.setText(data.getGender());
                                b.seatThreeMBirth.setText(data.getAge());
                                b.seatThreeInfo.setVisibility(View.VISIBLE);
                                b.seatThree.setOnClickListener(v -> {
                                    b.seatOne.setChecked(false);
                                    b.seatTwo.setChecked(false);
                                    b.seatThree.setChecked(true);
                                    setPreference("m_id",data.getM_id());
                                    if (b.seatThree.isChecked())
                                    reasonDialog();
                                    b.seatThreeReason.setText(ban_reason);
                                });
                                break;
                        }
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
        if (blackListRequest != null) attendRequest.cancel();
    }
}