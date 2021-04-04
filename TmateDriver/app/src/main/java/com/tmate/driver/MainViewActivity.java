package com.tmate.driver;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.tmate.driver.Fragment.NoticeFragment;
import com.tmate.driver.databinding.ActivityDrawerBinding;
import com.tmate.driver.databinding.ActivityMainViewBinding;

import org.w3c.dom.Text;

public class MainViewActivity extends AppCompatActivity  implements View.OnClickListener{

    private ActivityMainViewBinding binding;
    private ActivityDrawerBinding drawerBinding;
    private View drawerView ;
    private TextView profile, history, car, review, black_list, notice, statistics;


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

        profile = findViewById(R.id.profile);
        history = findViewById(R.id.history);
        car = findViewById(R.id.car);
        review = findViewById(R.id.review);
        black_list = findViewById(R.id.black_list);
        notice = findViewById(R.id.notice);
        statistics = findViewById(R.id.statistics);

        profile.setOnClickListener(this);
        history.setOnClickListener(this);
        car.setOnClickListener(this);
        review.setOnClickListener(this);
        black_list.setOnClickListener(this);
        notice.setOnClickListener(this);
        statistics.setOnClickListener(this);





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
            case R.id.profile : {
                ProfileFragment profileFragment = new ProfileFragment();
                transaction.replace(R.id.frame, profileFragment).commit();
                binding.drawerLayout.closeDrawers();
                return;
            }

            case R.id.notice : {
                NoticeFragment noticeFragment = new NoticeFragment();
                transaction.replace(R.id.frame, noticeFragment).commit();
                binding.drawerLayout.closeDrawers();
                return;
            }
            case R.id.statistics : {
                StatisticsFragment statisticsFragment = new StatisticsFragment();
                transaction.replace(R.id.frame, statisticsFragment).commit();
                binding.drawerLayout.closeDrawers();
                return;
            }
            default: return;
        }

    }
}