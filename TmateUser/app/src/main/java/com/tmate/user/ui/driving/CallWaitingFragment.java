package com.tmate.user.ui.driving;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.tmate.user.Activity.CallWaitingActivity;
import com.tmate.user.Activity.CarInfoActivity;
import com.tmate.user.Activity.MainViewActivity;
import com.tmate.user.R;
import com.tmate.user.databinding.FragmentCallWaitingBinding;
import com.tmate.user.net.DataService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallWaitingFragment extends Fragment {

    private DrivingModel mViewModel;
    FragmentCallWaitingBinding b;

    // 쓰레드 관련
    boolean isRunning;
    Matching matching;
    Handler handler;

    // 레트로핏
    // 1. 기사코드 가져오는 요청 객체
    Call<String> duringRequest;
    // 2. 호출 취소 시 호출을 삭제하는 요청 객체
    Call<Boolean> deleteRequest;

    // 호출 대기 시간 관련
    private long beforeTime = System.currentTimeMillis();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                deleteRequest = DataService.getInstance().matchAPI.removeNormalCall(mViewModel.dispatch.getDp_id());
                deleteRequest.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.code() == 200 && response.body() != null) {
                            Snackbar.make(getView(), "호출을 취소하셨습니다.", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(DrivingModel.class);
        b = FragmentCallWaitingBinding.inflate(getLayoutInflater());
        modelDataBinding();
        b.backCallWaiting.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MainViewActivity.class);
            startActivity(intent);
        });
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // 쓰레드 상태
        handler = new Handler();
        matching = new Matching();
        isRunning = true;

        Thread thread = new Thread(() -> {
            try {
                while (isRunning) {
                    long afterTime = System.currentTimeMillis();
                    long secTime = (afterTime - beforeTime)/1000;
                    System.out.println("시간차 " + secTime);
                    handler.post(matching);
                    Thread.sleep(3000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        isRunning=true;
        thread.start();
    }

    private void modelDataBinding() {
        b.waitHSPlace.setText(mViewModel.dispatch.getStart_place());
        b.waitHFPlace.setText(mViewModel.dispatch.getFinish_place());
    }

    // 내부 쓰레드 클래스
    public class Matching implements Runnable {
        @Override
        public void run() {
            duringRequest = DataService.getInstance().matchAPI.getd_idDuringCall(mViewModel.dispatch.getDp_id());
            duringRequest.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.code() == 200) {
                        String d_id = response.body();
                        if (d_id == null) {
                            isRunning = true;
                        }else{
                            isRunning = false;
                            mViewModel.dispatch.setD_id(d_id);
                            Log.d("CallWaitingFragment","매칭된 기사 d_id : " + d_id);
                            NavController controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                            controller.navigate(R.id.action_callWaitingFragment_to_driverWaitingFragment);
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        isRunning = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        isRunning = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        isRunning = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        isRunning = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        if(duringRequest != null) duringRequest.cancel();
        if(deleteRequest != null) deleteRequest.cancel();
    }
}