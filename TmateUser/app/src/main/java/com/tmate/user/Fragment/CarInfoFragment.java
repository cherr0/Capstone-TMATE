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
    Call<Dispatch> request;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentCarInfoBinding.inflate(getLayoutInflater());
        view = b.getRoot();

        m_id = getActivity().getSharedPreferences("loginUser", Context.MODE_PRIVATE).getString("m_id", "");

        request = DataService.getInstance().matchAPI.getUsingHistory(m_id);

        request.enqueue(new Callback<Dispatch>() {
            @Override
            public void onResponse(Call<Dispatch> call, Response<Dispatch> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        Dispatch dispatch = response.body();

                        if (dispatch != null) {

                            b.constNoService.setVisibility(View.GONE);
                            b.carInfoContent.setVisibility(View.VISIBLE);


                            merchant_uid = dispatch.getDp_id();

                            switch (dispatch.getDp_status()) {
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

                            switch (dispatch.getDp_id().substring(18)) {
                                case "1":
                                    b.htogether.setText("일반");
                                    break;

                                default:
                                    b.htogether.setText("동승");
                                    break;
                            }

                            b.hSPlace.setText(dispatch.getStart_place());
                            b.hFPlace.setText(dispatch.getFinish_place());

                            // null 검
                            if (dispatch.getCar_model() != null && dispatch.getCar_no() != null) {
                                b.carModel.setText(dispatch.getCar_model());
                                b.carNo.setText(dispatch.getCar_no());
                            } else {
                                b.carModel.setText("택시 호출 후 생성");
                                b.carNo.setText("택시 호출 후 생성");
                            }
                        } else {
                            Log.d("안넘어 가는건가?", "왜 실행을 안할까?");
                            b.constNoService.setVisibility(View.VISIBLE);
                            b.carInfoContent.setVisibility(View.GONE);

//                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                            BoardingFragment mf = new BoardingFragment();
//                            transaction.replace(R.id.fm_matching_activity, mf);
//                            transaction.commit();

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Dispatch> call, Throwable t) {
                t.printStackTrace();
            }
        });

        b.car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 세부 정보로 넘어가야 한다.
                if(h_status.equals("0") || h_status.equals("1") || h_status.equals("2")) {


                    Intent intent = new Intent(getContext(), MatchingDetailActivity.class);

                    intent.putExtra("merchant_uid", merchant_uid);
                    intent.putExtra("m_id", m_id);
                    intent.putExtra("h_status", h_status);
                    startActivity(intent);
                }

                // 지도로 넘어간다. 실시간 위치 지도로 넘어간다.
                if(h_status.equals("3") || h_status.equals("4")){
                    Intent intent = new Intent(getContext(), CarInfoActivity.class);
                    intent.putExtra("dp_id", merchant_uid);
                    intent.putExtra("m_id", m_id);
                    intent.putExtra("h_status", h_status);
                    startActivity(intent);
                }
            }
        });

        return view;
    }
}
