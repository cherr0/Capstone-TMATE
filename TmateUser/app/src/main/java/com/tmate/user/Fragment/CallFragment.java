package com.tmate.user.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.rd.PageIndicatorView;
import com.tmate.user.Activity.LoginActivity;
import com.tmate.user.Activity.MainViewActivity;
import com.tmate.user.Activity.MatchingMapActivity;
import com.tmate.user.Activity.NoticeDetailActivity;
import com.tmate.user.R;
import com.tmate.user.adapter.CallAdvertisingAdapter;
import com.tmate.user.data.Notice;
import com.tmate.user.databinding.FragmentCallBinding;
import com.tmate.user.net.DataService;
import com.tmate.user.ui.driving.DrivingActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CallFragment extends Fragment implements View.OnClickListener {
    private View view;
    private String together;
    private Dialog dialog;

    FragmentCallBinding b;

    ViewFlipper call_notice_ViewFlipper;
    ImageView call_logo;
    private CallAdvertisingAdapter caa;
    int currentPage = 0;
    ViewPager vp;
    Timer timer;
    final int NUM_PAGES = 5;  // 이미지의 총 갯수
    final long DELAY_MS = 3000;           // 오토 플립용 타이머 시작 후 해당 시간에 작동(초기 웨이팅 타임) ex) 앱 로딩 후 3초 뒤 플립됨.
    final long PERIOD_MS = 5000;          // 5초 주기로 작동

    List<Notice> noticeList;
    Call<List<Notice>> noticeRequest;

    // 타임스탬프 String 변환용
    SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd", Locale.KOREA);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentCallBinding.inflate(getLayoutInflater());
        view = b.getRoot();
        clickListener();
        getMainLoticeList();

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

        PageIndicatorView pageIndicatorView = view.findViewById(R.id.vp_fm_call_indicator);
        pageIndicatorView.setCount(5);
        pageIndicatorView.setSelection(1);

        Button btn_double = dialog.findViewById(R.id.btn_double);
        btn_double.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(),DrivingActivity.class);
            intent.putExtra("together","2");
            startActivity(intent);
            dialog.dismiss();
        });
        Button btn_triple = dialog.findViewById(R.id.btn_triple);
        btn_triple.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(),DrivingActivity.class);
            intent.putExtra("together","3");
            startActivity(intent);
            dialog.dismiss();
        });

        vp = view.findViewById(R.id.vp_fm_call);
        vp.setAdapter(new CallAdvertisingAdapter(getChildFragmentManager()));
        caa = new CallAdvertisingAdapter(getFragmentManager());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Adapter 세팅 후 타이머 실행
        final Handler handler = new Handler();
        final Runnable Update = () -> {
            currentPage = vp.getCurrentItem();
            int nextPage = currentPage + 1;

            if (nextPage >= NUM_PAGES) {
                nextPage = 0;
            }
            vp.setCurrentItem(nextPage, true);
            currentPage = nextPage;
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

    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.normal_call:
                Intent intent = new Intent(getContext(),DrivingActivity.class);
                intent.putExtra("together", "1");
                startActivity(intent);
                break;
            case R.id.together_call:
                dialog.show();
                break;
            case R.id.call_notice_first:
                moveNoticeDetail(0);
                break;
            case R.id.call_notice_second:
                moveNoticeDetail(1);
                break;
            case R.id.call_notice_third:
                moveNoticeDetail(2);
                break;
        }

    }

    private void clickListener() {
        b.normalCall.setOnClickListener(this);
        b.togetherCall.setOnClickListener(this);
        b.callNoticeFirst.setOnClickListener(this);
        b.callNoticeSecond.setOnClickListener(this);
        b.callNoticeThird.setOnClickListener(this);
    }

    private void moveNoticeDetail(int position) {
        Intent intent = new Intent(requireActivity(), NoticeDetailActivity.class);
        Notice notice = noticeList.get(position);
        intent.putExtra("title",notice.getBd_title());
        intent.putExtra("date", sdf.format(notice.getBd_cre_date()));
        intent.putExtra("content", notice.getBd_contents());
        startActivity(intent);
    }

    private void getMainLoticeList() {
        noticeRequest = DataService.getInstance().memberAPI.getMainNoticeList();
        noticeRequest.enqueue(new Callback<List<Notice>>() {
            @Override
            public void onResponse(Call<List<Notice>> call, Response<List<Notice>> response) {
                if(response.code() == 200 && response.body() != null) {
                    noticeList = response.body();
                    Log.d("CallFragment", "받아오는 공지사항 : " + noticeList);
                    b.callNoticeFirst.setText(noticeList.get(0).getBd_title());
                    b.callNoticeSecond.setText(noticeList.get(1).getBd_title());
                    b.callNoticeThird.setText(noticeList.get(2).getBd_title());
                }
            }
            @Override
            public void onFailure(Call<List<Notice>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}