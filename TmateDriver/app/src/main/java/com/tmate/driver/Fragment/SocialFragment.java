package com.tmate.driver.Fragment;

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

import com.tmate.driver.R;
import com.tmate.driver.data.Phone;
import com.tmate.driver.databinding.FragmentSocialBinding;


public class SocialFragment extends Fragment {
    private FragmentSocialBinding b;
    private Bundle bundle;
    private Phone phone;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentSocialBinding.inflate(inflater, container, false);
        View view = b.getRoot();

        if (getArguments() != null) {
            bundle = getArguments();
        }else {
            Log.d("SocialFragment.Bundle","번들 값을 받아오지 못했습니다.");
        }

        if(!getPreferenceString("d_id").equals("")) {
            Log.d("SharedPreference","d_id 값 있음 ");
        }

        b.btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 회원가입을 기존에 진행한 경우
                if(!getPreferenceString("d_id").equals("")) {
                    bundle.putString("d_id",getPreferenceString("d_id"));
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    CompletedFragment cf = new CompletedFragment();
                    cf.setArguments(bundle);
                    transaction.replace(R.id.fm_main, cf).commit();
                }else { // 회원가입 첫 시작인 경우
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    PhoneNumberFragment phoneNumberFragment = new PhoneNumberFragment();
                    phoneNumberFragment.setArguments(bundle);
                    transaction.replace(R.id.fm_main, phoneNumberFragment).commit();
                }
            }
        });

        return view;
    }

    public String getPreferenceString(String key) {
        SharedPreferences pref = getActivity().getSharedPreferences("loginDriver", getActivity().MODE_PRIVATE);
        return pref.getString(key, "");
    }

    // 데이터 저장 함수
    public void setPreference(String key, String value){
        SharedPreferences pref = getActivity().getSharedPreferences("loginDriver", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }
}
