package com.tmate.user.ui.driving;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.tmate.user.R;
import com.tmate.user.data.Dispatch;

import java.nio.file.Path;

public class DrivingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driving);
        DrivingModel model = new ViewModelProvider(this).get(DrivingModel.class);

        //동승유무값 가져오기 체크
        Intent intent = getIntent();
        String together = intent.getStringExtra("together");
        String dp_status = intent.getStringExtra("dp_status");
        String at_status = intent.getStringExtra("at_status");
        String dp_id = intent.getStringExtra("dp_id");
        Log.d("DrivingActivity", "동승 인원 : " + together);
        if(dp_id != null) Log.d("DrivingActivity","배차 코드 : " + dp_id);
        if(at_status != null) Log.d("DrivingActivity", "배차 상태 : " + dp_status);
        if(dp_status != null) Log.d("DrivingActivity", "참여 상태 : " + at_status);

        model.dispatch = new Dispatch(); // 객체에 값을 담을 수 있도록 액티비티 생성 시 생성
        model.dispatch.setDp_status(dp_status);
        model.dispatch.setAt_status(at_status);
        model.dispatch.setDp_id(dp_id);
        model.dispatch.setM_id(getPreferenceString("m_id")); // 시작 시 m_id 값 담기

        NavController controller = Navigation.findNavController(this, R.id.nav_host_fragment);
        if(at_status.equals("1")) { // 참여 상태 1(참가) 인 경우 배차 상태대로 이동
            switch(dp_status) {
                case "0":
                    controller.navigate(R.id.action_global_matchingDetailFragment);
                    break;
                case "2":
                    controller.navigate(R.id.action_global_callWaitingFragment);
                    break;
                case "3":
                    controller.navigate(R.id.action_global_driverWaitingFragment);
                    break;
                case "4" :
                    controller.navigate(R.id.action_global_driverMovingFragment);
                    break;
            }
        }else { // 참여 상태 0,1,2 인 경우 이동
            controller.navigate(R.id.action_global_matchingFragment);
        }

    }

    public String getPreferenceString(String key) {
        SharedPreferences pref = getSharedPreferences("loginUser", MODE_PRIVATE);
        return pref.getString(key, "");
    }
}