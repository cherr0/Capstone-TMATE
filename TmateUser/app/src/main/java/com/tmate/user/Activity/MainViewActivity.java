package com.tmate.user.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tmate.user.Fragment.BoardingFragment;
import com.tmate.user.Fragment.CallFragment;
import com.tmate.user.Fragment.CarInfoFragment;
import com.tmate.user.Fragment.EventCloseFragment;
import com.tmate.user.Fragment.My_info_Fragment;
import com.tmate.user.R;
import com.tmate.user.net.DataService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainViewActivity extends AppCompatActivity {

    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private final CallFragment callFragment = new CallFragment();
    private final BoardingFragment boardingFragment = new BoardingFragment();
    private final CarInfoFragment carInfoFragment = new CarInfoFragment();
    private final EventCloseFragment eventFragment = new EventCloseFragment();
    private final My_info_Fragment my_info_fragment = new My_info_Fragment();
    private BottomNavigationView bottomNavigationView;
    public static int navbarFlag  = 0;
    private long backBtnTime = 0;

    // 사용자 등급 SharedPreference에 저장하기 위함
    Call<Integer> getUseCountRequest;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);

        Log.d("MainViewActivity", "m_id : " + getPreferenceString("m_id"));
        Log.d("MainViewActivity", "m_name : " + getPreferenceString("m_name"));

        getUseCount();

        fragmentManager.beginTransaction().replace(R.id.frameLayout, callFragment).commitAllowingStateLoss();

        bottomNavigationView = findViewById(R.id.bottomNavigationView_main_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                switch (menuItem.getItemId()) {
                    case R.id.call : {  // 메인 호출 화면
                        transaction.replace(R.id.frameLayout, callFragment).commitAllowingStateLoss();
                        navbarFlag = R.id.call;
                        return true;
                    }
                    case R.id.bo_info : {   // 탑승 정보 화면
                        transaction.replace(R.id.frameLayout, carInfoFragment).commitAllowingStateLoss();
                        navbarFlag = R.id.bo_info;
                        return true;
                    }
                    case R.id.event : {     // 이벤트 화면
                        transaction.replace(R.id.frameLayout, eventFragment).commitAllowingStateLoss();
                        navbarFlag = R.id.event;
                        return true;
                    }
                    case R.id.more : {      // 더보기 (기타 서비스 목록 화면)
                        transaction.replace(R.id.frameLayout, my_info_fragment).commitAllowingStateLoss();
                        navbarFlag = R.id.more;
                        return true;
                    }

                    default : return false;
                }
            }
        });
    }

    // 데이터 저장 함수
    public void setPreference(String key, String value){
        SharedPreferences pref = getSharedPreferences("loginUser", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    // 데이터 불러오기 함수
    public String getPreferenceString(String key){
        SharedPreferences pref = getSharedPreferences("loginUser", MODE_PRIVATE);
        return pref.getString(key, "");
    }

    @Override
    public void onBackPressed() {

        switch (navbarFlag) {

            case R.id.bo_info : case R.id.event : case R.id.more : case 4 :
                bottomNavigationView.setSelectedItemId(R.id.call);
                break;
            case 3:
                bottomNavigationView.setSelectedItemId(R.id.more);
                break;
            case R.id.call :
                long curTime = System.currentTimeMillis();
                long gapTime = curTime - backBtnTime;

                if (0 <= gapTime && 2000 >= gapTime){
                    super.onBackPressed();
                } else {
                    backBtnTime = curTime;
                    Toast.makeText(this, "한번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show();
                }
            default :

        }
    }

    public void getUseCount() {
        getUseCountRequest = DataService.getInstance().memberAPI.getUseCount(getPreferenceString("m_id"));
        getUseCountRequest.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.code() == 200 && response.body() != null) {
                    Integer cnt = response.body();
                    Log.d("넘어오는 회원횟수", String.valueOf(cnt));

                    if(cnt < 5)
                        setPreference("m_level","일반");
                    else if (cnt < 10)
                        setPreference("m_level","우수");
                    else if (cnt < 15)
                        setPreference("m_level","최우수");
                    else
                        setPreference("m_level","VIP");

                    Log.d("사용자 등급", getPreferenceString("m_level"));
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
