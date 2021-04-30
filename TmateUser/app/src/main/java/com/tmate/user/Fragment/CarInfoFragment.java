package com.tmate.user.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.tmate.user.Activity.CarInfoActivity;
import com.tmate.user.Activity.MatchingDetailActivity;
import com.tmate.user.R;
import com.tmate.user.data.JoinHistoryVO;
import com.tmate.user.databinding.ActivityMatchingMapBinding;
import com.tmate.user.databinding.FragmentCarInfoBinding;
import com.tmate.user.net.DataService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarInfoFragment extends Fragment {
    private FragmentCarInfoBinding b;
    private View view;

    // 레트로핏 연
    String h_status;
    String m_id;
    String merchant_uid;
    Call<JoinHistoryVO> request;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentCarInfoBinding.inflate(getLayoutInflater());
        view = b.getRoot();

        m_id = getActivity().getSharedPreferences("loginUser", Context.MODE_PRIVATE).getString("m_id", "");

        request = DataService.getInstance().matchAPI.getUsingHistory(m_id);

        request.enqueue(new Callback<JoinHistoryVO>() {
            @Override
            public void onResponse(Call<JoinHistoryVO> call, Response<JoinHistoryVO> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        JoinHistoryVO joinHistoryVO = response.body();

                        if (joinHistoryVO != null) {


                            merchant_uid = joinHistoryVO.getMerchant_uid();

                            switch (joinHistoryVO.getH_status()) {
                                case "0":
                                    h_status = "0";
                                    b.statusBadge.setText("매칭 중");
                                    break;
                                case "1":
                                    h_status = "1";
                                    b.statusBadge.setText("매칭 완료");
                                    break;
                                case "2":
                                    h_status = "2";
                                    b.statusBadge.setText("호출 중");
                                    break;
                                case "3":
                                    h_status = "3";
                                    b.statusBadge.setText("탑승 대기중");
                                    break;
                                case "4":
                                    h_status = "4";
                                    b.statusBadge.setText("탑승 중");
                                    break;
                            }

                            switch (joinHistoryVO.getMerchant_uid().substring(27)) {
                                case "0":
                                    b.htogether.setText("동승");
                                    break;

                                case "1":
                                    b.htogether.setText("일반");
                                    break;
                            }

                            b.hSPlace.setText(joinHistoryVO.getH_s_place());
                            b.hFPlace.setText(joinHistoryVO.getH_f_place());

                            // null 검
                            if (joinHistoryVO.getCar_model() != null && joinHistoryVO.getCar_no() != null) {
                                b.carModel.setText(joinHistoryVO.getCar_model());
                                b.carNo.setText(joinHistoryVO.getCar_no());
                            } else {
                                b.carModel.setText("택시 호출 후 생성");
                                b.carNo.setText("택시 호출 후 생성");
                            }
                        } else {
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            BoardingFragment mf = new BoardingFragment();
                            transaction.replace(R.id.fm_matching_activity, mf);
                            transaction.commit();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JoinHistoryVO> call, Throwable t) {
                t.printStackTrace();
            }
        });

        b.car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 세부 정보로 넘어가야 한다.
                if(h_status == "0" || h_status == "1" || h_status == "2") {


                    Intent intent = new Intent(getContext(), MatchingDetailActivity.class);

                    intent.putExtra("merchant_uid", merchant_uid);
                    intent.putExtra("m_id", m_id);
                    intent.putExtra("h_status", h_status);
                    startActivity(intent);
                }

                // 지도로 넘어간다. 실시간 위치 지도로 넘어간다.
                if(h_status == "3" || h_status == "4"){
                    Intent intent = new Intent(getContext(), CarInfoActivity.class);
                    intent.putExtra("merchant_uid", merchant_uid);
                    intent.putExtra("m_id", m_id);
                    intent.putExtra("h_status", h_status);
                    startActivity(intent);
                }
            }
        });

        return view;
    }
}
