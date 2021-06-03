package com.tmate.user.Fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.user.Activity.MainViewActivity;
import com.tmate.user.R;
import com.tmate.user.adapter.ReviewAdapter;
import com.tmate.user.data.Attend;
import com.tmate.user.data.ReviewData;
import com.tmate.user.data.ReviewVO;
import com.tmate.user.databinding.FragmentReviewBinding;
import com.tmate.user.net.DataService;
import com.tmate.user.ui.driving.DrivingModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewFragment extends Fragment {
    private FragmentReviewBinding b;
    private ReviewAdapter adapter;
    private ArrayList<String> selectedItems = new ArrayList<>();
    private String reason;
    View view;

    ReviewVO reviewVO;
    DrivingModel mViewModel;

    Call<Boolean> reviewRequest;
    Call<List<Attend>> listRequest;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentReviewBinding.inflate(getLayoutInflater());
        mViewModel = new ViewModelProvider(requireActivity()).get(DrivingModel.class);
        view = b.getRoot();
        reviewVO = new ReviewVO();
        adapter = new ReviewAdapter(reviewVO);
        reviewVO.setDp_id(mViewModel.dispatch.getDp_id());
        reviewVO.setM_id(getPreferenceString("m_id"));

        getData();

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        b.reReTogether.setLayoutManager(manager); // LayoutManager 등록

        b.reReTogether.setAdapter(adapter);

        //평가완료 DB 저장 후 메인 뷰 이동
        b.reviewFinish.setOnClickListener(v -> {
            Log.d("ReviewFragment","보내지는 평가 리뷰 데이터 : " + reviewVO);
            reviewRequest = DataService.getInstance().matchAPI.updateReview(reviewVO);
            reviewRequest.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if(response.code() == 200 && response.body() != null) {
                        Intent intent = new Intent(getActivity(), MainViewActivity.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        });

        b.driverLike.setOnCheckedChangeListener((buttonView, isChecked) -> { // 드라이버 좋아
            b.driverDislike.setChecked(false); //싫어요 off
            b.driverLike.setChecked(true);
            reviewVO.setRe_driver("0");

            if(isChecked) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                final String[] result = getResources().getStringArray(R.array.driver_good_review);
                builder.setTitle("좋아요 한 사유를 선택해주세요");
                builder.setItems(R.array.driver_good_review, (dialog, pos) -> {
                    reason = result[pos];
                    reviewVO.setRe_driver_reason(reason);
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {
                b.driverLike.setChecked(false);
            }
        });
        b.driverDislike.setOnCheckedChangeListener((buttonView, isChecked) -> { // 드라이버 싫어
            b.driverLike.setChecked(false); //좋아요 off
            b.driverDislike.setChecked(true);
            reviewVO.setRe_driver("1");
            if(isChecked) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                final String[] result = getResources().getStringArray(R.array.driver_bad_review);
                builder.setTitle("싫어요 한 사유를 선택해주세요");
                builder.setItems(R.array.driver_bad_review, (dialog, pos) -> {
                    reason = result[pos];
                    reviewVO.setRe_driver_reason(reason);
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {
                b.driverDislike.setChecked(false);
            }
        });


        return view;
    }

    private void getData() {
        listRequest = DataService.getInstance().matchAPI.getPassengerList(mViewModel.dispatch.getDp_id());
        listRequest.enqueue(new Callback<List<Attend>>() {
            @Override
            public void onResponse(Call<List<Attend>> call, Response<List<Attend>> response) {
                if(response.code() == 200 && response.body() != null) {
                    for(Attend data : response.body()) {
                        String name = getPreferenceString("m_name");
                        if(!data.getM_name().equals(name)) {
                            adapter.addItem(data);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<List<Attend>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public String getPreferenceString(String key) {
        SharedPreferences pref = getActivity().getSharedPreferences("loginUser", getActivity().MODE_PRIVATE);
        return pref.getString(key, "");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(reviewRequest != null) reviewRequest.cancel();
        if(listRequest != null) listRequest.cancel();
    }
}