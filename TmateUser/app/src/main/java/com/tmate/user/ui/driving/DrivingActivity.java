package com.tmate.user.ui.driving;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.tmate.user.R;
import com.tmate.user.data.Dispatch;

public class DrivingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driving);
        DrivingModel model = new ViewModelProvider(this).get(DrivingModel.class);

        //동승유무값 가져오기 체크
        Intent intent = getIntent();
        model.together = intent.getExtras().getInt("together");
        model.dispatch = new Dispatch(); // 객체에 값을 담을 수 있도록 액티비티 생성 시 생성
        model.dispatch.setM_id(getPreferenceString("m_id")); // 시작 시 m_id 값 담기

    }

    public String getPreferenceString(String key) {
        SharedPreferences pref = getSharedPreferences("loginUser", MODE_PRIVATE);
        return pref.getString(key, "");
    }
}