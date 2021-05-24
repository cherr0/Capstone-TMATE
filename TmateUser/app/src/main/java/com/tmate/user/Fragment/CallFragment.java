package com.tmate.user.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.rd.PageIndicatorView;
import com.tmate.user.Activity.LoginActivity;
import com.tmate.user.Activity.MainViewActivity;
import com.tmate.user.Activity.MatchingMapActivity;
import com.tmate.user.R;
import com.tmate.user.adapter.CallAdvertisingAdapter;
import com.tmate.user.ui.driving.DrivingActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class CallFragment extends Fragment {
    ArrayList<String> list;
    private View view;
    private CardView Ll_together;
    private CardView Ll_solo, requset_list;
    private int together;
    private Dialog dialog;
    private Button btn_chat;
    private TextView point_charge;
    ViewFlipper call_banner_ViewFlipper, call_notice_ViewFlipper;
    ImageView viewFlipper_img_first, viewFlipper_img_second, viewFlipper_img_third, call_banner_img,
    call_logo;
    TextView call_notice_first, call_notice_second, call_notice_third;
    private CardView go_point;
    private CallAdvertisingAdapter caa;
    int currentPage = 0;
    ViewPager vp;
    Timer timer;
    final int NUM_PAGES = 5;  // 이미지의 총 갯수
    final long DELAY_MS = 3000;           // 오토 플립용 타이머 시작 후 해당 시간에 작동(초기 웨이팅 타임) ex) 앱 로딩 후 3초 뒤 플립됨.
    final long PERIOD_MS = 5000;          // 5초 주기로 작동


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_call, container, false);


        //로고 애니메이션
        call_logo = view.findViewById(R.id.call_logo);
        Animation myanim = AnimationUtils.loadAnimation(getContext(), R.anim.call_translate);
        call_logo.startAnimation(myanim);

        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_together);

        call_notice_ViewFlipper = view.findViewById(R.id.call_notice_ViewFlipper);
        call_notice_ViewFlipper.startFlipping();
        call_notice_ViewFlipper.setFlipInterval(5000);

        Ll_together = view.findViewById(R.id.together_call);
        Ll_together.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(); //동승 설정 알림창
            }
        });
        // 일반 호출
        Ll_solo = view.findViewById(R.id.normal_call);
        Ll_solo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                together =1;
                Intent intent = new Intent(getContext(), MatchingMapActivity.class);
                intent.putExtra("together", together);
                startActivity(intent);
                
            }
        });

        go_point = view.findViewById(R.id.go_point);
        go_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainViewActivity.navbarFlag = 4;
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                PointFragment pf = new PointFragment();
                transaction.replace(R.id.frameLayout, pf);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        PageIndicatorView pageIndicatorView = view.findViewById(R.id.vp_fm_call_indicator);
        pageIndicatorView.setCount(5);
        pageIndicatorView.setSelection(1);

        vp = view.findViewById(R.id.vp_fm_call);
        caa = new CallAdvertisingAdapter(getFragmentManager());
        vp.setAdapter(new CallAdvertisingAdapter(getChildFragmentManager()));

        point_charge = view.findViewById(R.id.point_charge);
        point_charge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainViewActivity.navbarFlag = 4;
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                PointChargeFragment pcf = new PointChargeFragment();
                transaction.replace(R.id.frameLayout, pcf);
                transaction.addToBackStack(null);
                transaction.commit();            }
        });
        call_notice_first = view.findViewById(R.id.call_notice_first);
        call_notice_first.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });


        return view;
    }
    @Override
    public void onResume() {
        super.onResume();

        // Adapter 세팅 후 타이머 실행
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                currentPage = vp.getCurrentItem();
                int nextPage = currentPage + 1;

                if (nextPage >= NUM_PAGES) {
                    nextPage = 0;
                }
                vp.setCurrentItem(nextPage, true);
                currentPage = nextPage;
            }
        };

        timer = new Timer(); // thread에 작업용 thread 추가
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

    }

    @Override
    public void onPause() {
        super.onPause();
        // 다른 액티비티나 프레그먼트 실행시 타이머 제거
        // 현재 페이지의 번호는 변수에 저장되어 있으니 취소해도 상관없음
        if(timer != null) {
            timer.cancel();
            timer = null;
        }
    }


    public void showDialog(){
        dialog.show();

        Button btn_no = dialog.findViewById(R.id.btn_double);
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                together =2;
                Intent intent = new Intent(getContext(), MatchingMapActivity.class);
                intent.putExtra("together", together);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        Button btn_yes = dialog.findViewById(R.id.btn_triple);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                together =3;
                Intent intent = new Intent(getContext(), MatchingMapActivity.class);
                intent.putExtra("together", together);
                startActivity(intent);
                dialog.dismiss();
            }
        });
    }
}