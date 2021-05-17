package com.tmate.driver.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.tmate.driver.activity.LoginActivity;
import com.tmate.driver.OnBackPressedListener;
import com.tmate.driver.activity.MainViewActivity;
import com.tmate.driver.databinding.FragmentCompletedBinding;
import com.tmate.driver.net.DataService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompletedFragment extends Fragment implements OnBackPressedListener {
    private FragmentCompletedBinding b;
    LoginActivity activity;
    long backKeyPressedTime;
    Bundle bundle;

    Call<Boolean> request;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentCompletedBinding.inflate(inflater, container, false);
        View view = b.getRoot();
        activity = (LoginActivity) getActivity();

        if (getArguments() != null) {
            bundle = getArguments();
            Log.d("번들 넘어오는 값",bundle.toString());
        }else {
            Log.d("AccRegFragment.Bundle","번들 값을 받아오지 못했습니다.");
        }

        request = DataService.getInstance().driver.approveSearch(getPreferenceString("d_id"));
        request.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.code() == 200) {
                    Log.d("레트로핏 값",response.body() + "");
                    if(response.body()) {
                        setPreference("d_approve", response.body().toString());
                        Log.d("CompletedFragment","승인 완료");
                        Toast.makeText(getContext(), "승인 완료 되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity().getApplication(), MainViewActivity.class);
                        startActivity(intent);
                    }else {
                        Log.d("CompletedFragment","승인 미완료");
                        Toast.makeText(getContext(), "아직 승인이 미완료 되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                t.printStackTrace();
            }
        });

        return  view;
    }


    @Override
    public void onBackPressed() {
        Toast toast = Toast.makeText(getContext(),"한번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT);
        if(System.currentTimeMillis() > backKeyPressedTime + 2000){
            backKeyPressedTime = System.currentTimeMillis();
            toast.show();
            return;
        }
        if(System.currentTimeMillis() <= backKeyPressedTime + 2000){
            getActivity().finish();
            toast.cancel();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.setOnBackPressedListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        request.cancel();
    }

    // 데이터 저장 함수
    public void setPreference(String key, String value){
        SharedPreferences pref = getActivity().getSharedPreferences("loginDriver", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getPreferenceString(String key) {
        SharedPreferences pref = getActivity().getSharedPreferences("loginDriver", getActivity().MODE_PRIVATE);
        return pref.getString(key, "");
    }
}
