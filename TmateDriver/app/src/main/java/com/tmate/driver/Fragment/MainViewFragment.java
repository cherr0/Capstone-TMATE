package com.tmate.driver.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.tmate.driver.R;
import com.tmate.driver.activity.WaitingActivity;
import com.tmate.driver.databinding.FragmentMainViewBinding;
import com.tmate.driver.net.DataService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewFragment  extends Fragment {
    private FragmentMainViewBinding b;

    // 레트로핏 관련
    Call<Boolean> request;
    private SharedPreferences pref;
    String d_id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentMainViewBinding.inflate(inflater, container, false);
        View view = b.getRoot();
        pref = getActivity().getSharedPreferences("loginDriver", Context.MODE_PRIVATE);
        d_id = pref.getString("d_id", "");

        b.carFind.setOnClickListener(v -> {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            CarFragment carFragment = new CarFragment();
            transaction.replace(R.id.frame, carFragment).commit();
        });

        b.dvSt.setOnClickListener(v -> {
            request = DataService.getInstance().call.modifyStatusByD_idAndFlag(d_id,1);
            request.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.code() == 200 && response.body() != null) {
                        Toast.makeText(getActivity(), "운행 대기중입니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(), WaitingActivity.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        });
        Glide.with(requireContext())
                .load(R.raw.taxi)
                .into(b.ivGif);
        return view;
    }
}