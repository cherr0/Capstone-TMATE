package com.tmate.driver.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;
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
    private View drawerView ;
    private TextView profile, history, black_list, notice, statistics, tv_home, car_name, car_num;
    private Button service;
    private CircleImageView profile_img;
    private long backBtnTime = 0;
    public static int navbarFlag  = R.id.tv_home;
    private Intent intent;
    private Spinner state;

    private String d_id;

    private Call<SidebarProfile> sidebarRequest;
    private Call<Boolean> statusRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        drawerView = findViewById(R.id.drawer);
        d_id = getPreferenceString("d_id");
        Log.d("MainViewActivity", "d_id 값 : " + d_id);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        MainViewFragment mainViewFragment = new MainViewFragment();
        transaction.replace(R.id.frame, mainViewFragment).commit();

        profile = findViewById(R.id.side_profile_name);
        profile_img = findViewById(R.id.circleImageView);
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

        //Spinner객체 생성
        final Spinner spinner_field = (Spinner)findViewById(R.id.side_profile_state);
        //1번에서 생성한 field.xml의 item을 String 배열로 가져오기
        String[] str = getResources().getStringArray(R.array.state);
        //2번에서 생성한 spinner_item.xml과 str을 인자로 어댑터 생성.
        final ArrayAdapter<String> adapter= new ArrayAdapter<>(getApplicationContext(),R.layout.item_spinner,str);

        adapter.setDropDownViewResource(R.layout.simple_spinner_drop_down);
        spinner_field.setAdapter(adapter);

        //spinner 이벤트 리스너
        spinner_field.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int position = spinner_field.getSelectedItemPosition();
                statusRequest = DataService.getInstance().driver.setStatus(getPreferenceString("d_id"),position);
                statusRequest.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if(response.code() == 200 && response.body() != null) {
                            //선택된 항목
                            Snackbar.make(view,str[position] + " 중 입니다", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        t.printStackTrace();
                    }
                });

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // 사이드 바 프로필 작성
        sidebarRequest = DataService.getInstance().driver.searchSidebarProfile(d_id);
        sidebarRequest.enqueue(new Callback<SidebarProfile>() {
            @Override
            public void onResponse(Call<SidebarProfile> call, Response<SidebarProfile> response) {
                Log.d("MainViewActivity", "바디 : " + response.body());
                if (response.code() == 200 && response.body() != null) {
                    SidebarProfile data = response.body();
                    profile.setText(data.getM_name());
                    car_name.setText(data.getCar_model());
                    car_num.setText(data.getCar_no());
                } else {
                    Log.d("MainViewActivity", "연결 실패");
                }
            }

            @Override
            public void onFailure(Call<SidebarProfile> call, Throwable t) {
                t.printStackTrace();
            }
        });



        binding.ivOpen.setOnClickListener(v -> binding.drawerLayout.openDrawer(drawerView));

        ImageView iv_close = findViewById(R.id.iv_close);
        iv_close.setOnClickListener(v -> binding.drawerLayout.closeDrawers());

        binding.drawerLayout.setDrawerListener(listener);
        drawerView.setOnTouchListener((v, event) -> true);
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
            case R.id.circleImageView :
                ProfileFragment profileFragment = new ProfileFragment();
                transaction.replace(R.id.frame, profileFragment).commit();
                binding.drawerLayout.closeDrawers();
                navbarFlag = R.id.circleImageView;
                break;
            case R.id.history :
                HistoryFragment historyFragment = new HistoryFragment();
                transaction.replace(R.id.frame, historyFragment).commit();
                binding.drawerLayout.closeDrawers();
                navbarFlag = R.id.history;
                break;
            case R.id.notice :
                NoticeFragment noticeFragment = new NoticeFragment();
                transaction.replace(R.id.frame, noticeFragment).commit();
                binding.drawerLayout.closeDrawers();
                navbarFlag = R.id.notice;
                break;
            case R.id.black_list :
                Blacklist_managementFragment blacklistManagementFragment = new Blacklist_managementFragment();
                transaction.replace(R.id.frame, blacklistManagementFragment).commit();
                binding.drawerLayout.closeDrawers();
                navbarFlag = R.id.black_list;
                break;
            case R.id.statistics :
                StatisticsFragment statisticsFragment = new StatisticsFragment();
                transaction.replace(R.id.frame, statisticsFragment).commit();
                binding.drawerLayout.closeDrawers();
                navbarFlag = R.id.statistics;
                break;
            case R.id.service :
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://pf.kakao.com/_tdxjxoK/chat"));
                startActivity(intent);
                break;
            case R.id.tv_home :
                MainViewFragment mainViewFragment = new MainViewFragment();
                transaction.replace(R.id.frame, mainViewFragment).commit();
                binding.drawerLayout.closeDrawers();
                navbarFlag = R.id.home;
                break;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(sidebarRequest != null) sidebarRequest.cancel();
        if(statusRequest != null) statusRequest.cancel();
    }
}