package com.tmate.user.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.tmate.user.R;
import com.tmate.user.data.History;
import com.tmate.user.databinding.FragmentMatchingDetailBinding;
import com.tmate.user.net.DataService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MatchingDetailFragment extends Fragment {
    private View view;
    private Button btn_match;
    private FragmentMatchingDetailBinding b;

    private Dialog dialog;
    private Bundle bundle;

    private String m_id;
    private String merchant_uid;

    DataService dataService = DataService.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b= FragmentMatchingDetailBinding.inflate(getLayoutInflater());
        view = b.getRoot();
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_together_info);

        if (getArguments() != null) {
            bundle = getArguments();
            merchant_uid = bundle.getString("merchant_uid");
            m_id = bundle.getString("m_id");
        }

        dataService.matchAPI.getMatchingDetail(merchant_uid).enqueue(new Callback<History>() {
            @Override
            public void onResponse(Call<History> call, Response<History> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        History history = response.body();
                        Log.d("잘받아오나요?", history.toString());

                        b.tvMName.setText(history.getM_name());

                        b.mfMerchantUid.setText(history.getMerchant_uid());
                        b.mfMid.setText(history.getM_id());

                        b.tvStartPlace.setText(history.getH_s_place());
                        b.mfHSLttd.setText(String.valueOf(history.getH_s_lttd()));
                        b.mfHSLngtd.setText(String.valueOf(history.getH_s_lngtd()));

                        b.tvEndPlace.setText(history.getH_f_place());
                        b.mfHFLttd.setText(String.valueOf(history.getH_f_lttd()));
                        b.mfHFLngtd.setText(String.valueOf(history.getH_f_lngtd()));

                    }
                }
            }

            @Override
            public void onFailure(Call<History> call, Throwable t) {
                t.printStackTrace();
            }
        });

        if(getPreferenceString("m_id") == bundle.getString("m_id")) {
            b.clBtnMember.setVisibility(View.VISIBLE);
            b.clBtnTogetherRequest.setVisibility(View.VISIBLE);

        } else {
            b.clBtnMember.setVisibility(View.INVISIBLE);
            b.clBtnTogetherRequest.setVisibility(View.INVISIBLE);
        }



        b.btnMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                MatchingMemberFragment matchingMemberFragment = new MatchingMemberFragment();
                transaction.replace(R.id.fm_matching, matchingMemberFragment);
                transaction.commit();
            }
        });

        b.btnMemberRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                TogetherRequestFragment togetherRequestFragment = new TogetherRequestFragment();
                transaction.replace(R.id.fm_matching, togetherRequestFragment);
                transaction.commit();
            }
        });

        b.btnMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                MatchingSeatFragment matchingSeatFragment = new MatchingSeatFragment();
                transaction.replace(R.id.fm_matching, matchingSeatFragment);
                transaction.commit();

            }
        });

        return view;
    }
    public void showDialog(){
        dialog.show();

        TextView exit = dialog.findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    // 데이터 불러오기 함수
    public String getPreferenceString(String key){
        SharedPreferences pref = getActivity().getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }


}