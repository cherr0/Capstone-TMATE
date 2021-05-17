package com.tmate.driver.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.tmate.driver.Fragment.Blacklist_managementFragment;
import com.tmate.driver.Fragment.HistoryFragment;
import com.tmate.driver.Fragment.MainViewFragment;
import com.tmate.driver.Fragment.NoticeFragment;
import com.tmate.driver.Fragment.ProfileFragment;
import com.tmate.driver.Fragment.StatisticsFragment;
import com.tmate.driver.R;
import com.tmate.driver.data.SidebarProfile;
import com.tmate.driver.databinding.ActivityDrawerBinding;
import com.tmate.driver.databinding.ActivityMainViewBinding;
import com.tmate.driver.net.DataService;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewActivity extends AppCompatActivity  implements View.OnClickListener{

    private ActivityMainViewBinding binding;
    private ActivityDrawerBinding drawerBinding;
    private View drawerView ;
    private TextView profile, history, black_list, notice, statistics, tv_home, state, car_name, car_num;
    private Button service;
    private CircleImageView profile_img;
    private long backBtnTime = 0;
    public static int navbarFlag  = R.id.tv_home;
    private Call<SidebarProfile> sidebarRequest;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainViewBinding.inflate(getLayoutInflater());
        drawerBinding = ActivityDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        drawerView = findViewById(R.id.drawer);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        MainViewFragment mainViewFragment = new MainViewFragment();
        transaction.replace(R.id.frame, mainViewFragment).commit();

        profile = findViewById(R.id.side_profile_name);
        profile_img = findViewById(R.id.circleImageView);
        state = findViewById(R.id.side_profile_state);
        history = findViewById(R.id.history);
        black_list = findViewById(R.id.black_list);
        notice = findViewById(R.id.notice);
        statistics = findViewById(R.id.statistics);
        service = findViewById(R.id.service);
        tv_home = findViewById(R.id.tv_home);
        car_name = findViewById(R.id.side_profile_car_name);
        car_num = findViewById(R.id.side_profile_car_num);

        profile_img.setOnClickListener(this);
        history.setOnClickListener(this);
        black_list.setOnClickListener(this);
        notice.setOnClickListener(this);
        statistics.setOnClickListener(this);
        service.setOnClickListener(this);
        tv_home.setOnClickListener(this);
        intent = getIntent();
        String d_id = intent.getStringExtra("d_id");
        // 사이드 바 프로필 작성
        sidebarRequest = DataService.getInstance().driver.searchSidebarProfile(d_id);
        Log.d("MainViewActivity", "d_id 값 : " + d_id);
        sidebarRequest.enqueue(new Callback<SidebarProfile>() {
            @Override
            public void onResponse(Call<SidebarProfile> call, Response<SidebarProfile> response) {
                Log.d("MainViewActivity", "바디 : " + response.body());
                Log.d("MainViewActivity", "코드 : " + response.code());
                if (response.code() == 200 && response.body() != null) {
                    SidebarProfile sidebarProfile = response.body();
                    if (response.body().getM_name() != null) {
                        if (sidebarProfile.getM_status() == 1) {
                            state.setText("대기");
                        } else if (sidebarProfile.getM_status() == 2) {
                            state.setText("운행중");
                        } else {
                            state.setText("휴식");
                        }
                        profile.setText(sidebarProfile.getM_name());
                        car_name.setText(sidebarProfile.getCar_model());
                        car_num.setText(sidebarProfile.getCar_no());
                    }
                } else {
                    Log.d("MainViewActivity", "연결 실패");
                }
            }

            @Override
            public void onFailure(Call<SidebarProfile> call, Throwable t) {

            }
        });



        binding.ivOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.drawerLayout.openDrawer(drawerView);
            }
        });

        ImageView iv_close = findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.drawerLayout.closeDrawers();
            }
        });

        binding.drawerLayout.setDrawerListener(listener);
        drawerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() { //사이드바 이벤트
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };


    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (v.getId()){
            case R.id.circleImageView : {
                ProfileFragment profileFragment = new ProfileFragment();
                transaction.replace(R.id.frame, profileFragment).commit();
                binding.drawerLayout.closeDrawers();
                navbarFlag = R.id.circleImageView;
                return;
            }
            case R.id.history : {
                HistoryFragment historyFragment = new HistoryFragment();
                transaction.replace(R.id.frame, historyFragment).commit();
                binding.drawerLayout.closeDrawers();
                navbarFlag = R.id.history;
                return;
            }
            case R.id.notice : {
                NoticeFragment noticeFragment = new NoticeFragment();
                transaction.replace(R.id.frame, noticeFragment).commit();
                binding.drawerLayout.closeDrawers();
                navbarFlag = R.id.notice;
                return;
            }
            case R.id.black_list : {
                Blacklist_managementFragment blacklistManagementFragment = new Blacklist_managementFragment();
                transaction.replace(R.id.frame, blacklistManagementFragment).commit();
                binding.drawerLayout.closeDrawers();
                navbarFlag = R.id.black_list;
                return;
            }
            case R.id.statistics : {
                StatisticsFragment statisticsFragment = new StatisticsFragment();
                transaction.replace(R.id.frame, statisticsFragment).commit();
                binding.drawerLayout.closeDrawers();
                navbarFlag = R.id.statistics;
                return;
            }
            case R.id.service : {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://pf.kakao.com/_tdxjxoK/chat"));
                startActivity(intent);
                return;
            }
            case R.id.tv_home : {
                MainViewFragment mainViewFragment = new MainViewFragment();
                transaction.replace(R.id.frame, mainViewFragment).commit();
                binding.drawerLayout.closeDrawers();
                return;
            }
            default: return;
        }
    }

    @Override
    public void onBackPressed() {
        switch (navbarFlag) {
            case R.id.circleImageView: case R.id.history: case R.id.black_list: case R.id.notice: case R.id.statistics:
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                MainViewFragment mainViewFragment = new MainViewFragment();
                transaction.replace(R.id.frame, mainViewFragment).commit();
                binding.drawerLayout.closeDrawers();
                navbarFlag = R.id.tv_home;
                break;
            case R.id.tv_home:
                long curTime = System.currentTimeMillis();
                long gapTime = curTime - backBtnTime;

                if (0 <= gapTime && 2000 >= gapTime) {
                    super.onBackPressed();
                } else {
                    backBtnTime = curTime;
                    Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
                }
            default:
        }
    }

    public String getPreferenceString(String key) {
        SharedPreferences pref = getSharedPreferences("loginDriver", MODE_PRIVATE);
        return pref.getString(key, "");
    }
}