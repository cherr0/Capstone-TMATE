package com.tmate.user.ui.driving;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tmate.user.R;
import com.tmate.user.databinding.FragmentDriverFinishingBinding;

public class DriverFinishingFragment extends Fragment {

    FragmentDriverFinishingBinding b;
    DrivingModel mViewModel;

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
                break;
            case 2: // 오류
                b.matchingFinishError.setVisibility(View.VISIBLE);
                break;
            case 3: // 취소
                b.matchingFinishCancel.setVisibility(View.VISIBLE);
                break;
        }

        b.matchingFinishHSPlace.setText(mViewModel.dispatch.getStart_place()); // 출발지
        b.matchingFinishHFPlace.setText(mViewModel.dispatch.getFinish_place()); // 도착지
        b.matchingFinishHSTime.setText(mViewModel.dispatch.getStart_time().toString()); // 출발 시간
        b.matchingFinishHETime.setText(mViewModel.dispatch.getEnd_time().toString()); // 도착 시간
        b.matchingFinishHEpDistance.setText(String.valueOf(mViewModel.dispatch.getDistance() / 1000)); // 이동 거리
        b.finishHAllFare.setText(mViewModel.dispatch.getAll_fare()); // 총합 택시 비용
        b.finishUsePoint.setText(mViewModel.dispatch.getUse_point()); // 사용 포인트
        b.togetherCount.setText(mViewModel.together); // 동승 인원
        b.totalAmount.setText(mViewModel.dispatch.getAmount()); // 최종 결제 금액

        b.matchingFinishSubmit.setOnClickListener(v -> { // 확인 완료 버튼
            NavController controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            controller.navigate(R.id.action_driverFinishingFragment_to_reviewFragment);
        });
    }

}