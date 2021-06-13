package com.tmate.user.ui.driving;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tmate.user.R;
import com.tmate.user.databinding.FragmentDriverFinishingBinding;
import com.tmate.user.net.DataService;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverFinishingFragment extends Fragment {

    FragmentDriverFinishingBinding b;
    DrivingModel mViewModel;

    // 이용횟수, 동승횟수 업데이트 하기 위함
    Call<Boolean> addCallCnt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentDriverFinishingBinding.inflate(getLayoutInflater());
        mViewModel = new ViewModelProvider(requireActivity()).get(DrivingModel.class);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        modelDataBinding();

    }

    // view 데이터 주입
    private void modelDataBinding() {
        switch(mViewModel.payFlag) {
            case 1: // 성공
                b.matchingFinishSuccess.setVisibility(View.VISIBLE);
                addCallCntRetrofit();
                break;
            case 2: // 오류
                b.matchingFinishError.setVisibility(View.VISIBLE);
                break;
            case 3: // 취소
                b.matchingFinishCancel.setVisibility(View.VISIBLE);
                break;
        }

        Log.d("DriverFinishingFragment","dispatch 넘어가는 데이터 : " + mViewModel.dispatch);

        b.matchingFinishHSPlace.setText(mViewModel.dispatch.getStart_place()); // 출발지
        b.matchingFinishHFPlace.setText(mViewModel.dispatch.getFinish_place()); // 도착지
        b.matchingFinishHSTime.setText(new SimpleDateFormat("HH:mm:ss").format(mViewModel.dispatch.getStart_time())); // 출발 시간
        b.matchingFinishHETime.setText(new SimpleDateFormat("HH:mm:ss").format(mViewModel.dispatch.getEnd_time())); // 도착 시간
        b.matchingFinishHEpDistance.setText(String.valueOf(mViewModel.dispatch.getEp_distance())); // 이동 거리
        b.finishHAllFare.setText(String.valueOf(mViewModel.dispatch.getAll_fare())); // 총합 택시 비용
        b.finishUsePoint.setText(String.valueOf(mViewModel.use_point)); // 사용 포인트
        b.togetherCount.setText(mViewModel.together); // 동승 인원
        int totalAmount = mViewModel.dispatch.getAll_fare() - mViewModel.dispatch.getUse_point();
        totalAmount = (totalAmount / Integer.parseInt(mViewModel.together)) - mViewModel.payCash - mViewModel.use_point;
        b.totalAmount.setText(String.valueOf(totalAmount)); // 최종 결제 금액

        b.matchingFinishSubmit.setOnClickListener(v -> { // 확인 완료 버튼
            NavController controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            controller.navigate(R.id.action_driverFinishingFragment_to_reviewFragment);
        });
    }

    public void addCallCntRetrofit() {

        addCallCnt = DataService.getInstance().matchAPI.modifyCallCnt(getPreferenceString("m_id"), Integer.parseInt(mViewModel.together));
        addCallCnt.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.code() == 200) {
                    Toast.makeText(getActivity(), "결제가 완료 되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    public String getPreferenceString(String key) {
        SharedPreferences pref = getActivity().getSharedPreferences("loginUser", getActivity().MODE_PRIVATE);
        return pref.getString(key, "");
    }

}