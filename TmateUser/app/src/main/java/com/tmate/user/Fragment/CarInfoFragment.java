package com.tmate.user.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.tmate.user.Activity.CarInfoActivity;
import com.tmate.user.Activity.MatchingDetailActivity;
import com.tmate.user.R;
import com.tmate.user.data.Dispatch;
import com.tmate.user.data.JoinHistoryVO;
import com.tmate.user.databinding.ActivityMatchingMapBinding;
import com.tmate.user.databinding.FragmentCarInfoBinding;
import com.tmate.user.net.DataService;
import com.tmate.user.ui.driving.DrivingActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarInfoFragment extends Fragment {
    private FragmentCarInfoBinding b;
    private View view;

    // 레트로핏 연동
    String dp_status;
    String at_status;
    String m_id;
    String dp_id;
    String together;
    int cur_people;
    int seat;
    Call<Dispatch> request;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentCarInfoBinding.inflate(getLayoutInflater());
        view = b.getRoot();
        b.noService.bringToFront();

        m_id = getActivity().getSharedPreferences("loginUser" , Context.MODE_PRIVATE).getString("m_id", "");

        request = DataService.getInstance().matchAPI.getUsingHistory(m_id);
        request.enqueue(new Callback<Dispatch>() {
            @Override
            public void onResponse(Call<Dispatch> call, Response<Dispatch> response) {
                if (response.code() == 200 && response.body() != null) {
                    Dispatch dispatch = response.body();
                    b.noService.setVisibility(View.GONE);
                    b.detailService.setVisibility(View.VISIBLE);
                    Log.d("CarInfoFragment", "dispatch : " + dispatch.toString());

                    dp_id = dispatch.getDp_id();
                    dp_status = dispatch.getDp_status();
                    at_status = dispatch.getAt_status();
                    seat = dispatch.getSeat();

                    switch (seat) {
                        case 1 :
                            b.ivDriverSeat.setImageResource(R.drawable.ic_sit_off_more);
                            break;
                        case 2:
                            b.ivSeatThree.setImageResource(R.drawable.ic_sit_off_more);
                            break;
                        case 3:
                            b.ivSeatOne.setImageResource(R.drawable.ic_sit_off_more);
                            break;
                    }

                    switch (dp_status) {
                        case "0":
                            b.statusBadge.setText("매칭 중");
                            break;
                        case "1":
                            b.statusBadge.setText("매칭 완료");
                            break;
                        case "2":
                            b.statusBadge.setText("호출 중");
                            break;
                        case "3":
                            b.statusBadge.setText("탑승 대기중");
                            break;
                        case "4":
                            b.statusBadge.setText("탑승 중");
                            break;
                    }

                    together = dispatch.getDp_id().substring(18);
                    switch (together) {
                        case "1":
                            b.htogether.setText("일반");
                            b.curPeople.setText("1명");
                            break;

                        default:
                            b.htogether.setText("동승");
                            b.curPeople.setText(dispatch.getCur_people() + "명");
                            break;
                    }

                    b.hSPlace.setText(dispatch.getStart_place());
                    b.hFPlace.setText(dispatch.getFinish_place());

                    // null 검출
                    if (dispatch.getCar_model() != null && dispatch.getCar_no() != null) {
                        b.carModel.setText(dispatch.getCar_model());
                        b.carNo.setText(dispatch.getCar_no());
                    } else {
                        b.carModel.setText("택시 호출 후");
                        b.carNo.setText("생성");
                    }
                }
            }

            @Override
            public void onFailure(Call<Dispatch> call, Throwable t) {
                t.printStackTrace();
            }
        });

        b.car.setOnClickListener(v -> {
            // 세부 정보로 넘어가야 한다.
            Intent intent = new Intent(getContext(), DrivingActivity.class);
            intent.putExtra("dp_id",dp_id);
            intent.putExtra("together", together);
            intent.putExtra("dp_status", dp_status);
            intent.putExtra("at_status", at_status);
            startActivity(intent);
        });

        return view;
    }
}
