package com.tmate.user.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.tmate.user.Activity.MainViewActivity;
import com.tmate.user.R;
import com.tmate.user.data.LoginVO;
import com.tmate.user.net.DataService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SocialFragment extends Fragment implements Validator.ValidationListener{
    private Validator validator;
    private View view;
    private Button btn_reg, btn_login;

    private Call<LoginVO> loginRequest;
    Bundle bundle;

    @Length(min = 11, message = "이메일 또는 전화번호를 바르게 입력해주세요")
    @NotEmpty(message = "이메일 또는 전화번호를 입력해주세요")
    EditText et_userId;

    @Length(min = 8, max = 20, message = "비밀번호를 바르게 입력해주세요")
    @NotEmpty(message = "비밀번호를 입력해주세요")
    EditText et_password;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_social, container, false);

        if (getArguments() != null) {
            bundle = getArguments();
        }

        validator = new Validator(this);//필수
        validator.setValidationListener(this);//필수
        et_password = view.findViewById(R.id.et_password);
        et_userId = view.findViewById(R.id.et_userId);
        btn_login = view.findViewById(R.id.btn_login);


        btn_reg = view.findViewById(R.id.btn_reg);
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                BlankFragment blankFragment= new BlankFragment();
                blankFragment.setArguments(bundle);
                transaction.replace(R.id.fm_main, blankFragment);
                transaction.commit();
            }
        });

        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validator.validate(); // 텍스트 변경시 이벤트 발생
            }
        });

        btn_login.setOnClickListener(v -> {
            Authorization();
        });
        return view;
    }

    @Override
    public void onValidationSucceeded() {
        btn_login.setEnabled(true);
        btn_login.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4254BC")));
        btn_login.setTextColor(Color.parseColor("#FFFFFF"));
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            btn_login.setEnabled(false);
            btn_login.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EFEFEF")));
            btn_login.setTextColor(Color.parseColor("#000000"));
            String message = error.getCollatedErrorMessage(getActivity());
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getPreferenceString(String key) {
        SharedPreferences pref = getActivity().getSharedPreferences("loginMember", getActivity().MODE_PRIVATE);
        return pref.getString(key, "");
    }

    // 데이터 저장 함수
    public void setPreference(String key, String value) {
        SharedPreferences pref = getActivity().getSharedPreferences("loginMember", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private void Authorization() {
        String id = et_userId.getText().toString();
        String password = et_password.getText().toString();
        String auth = "m";
        loginRequest = DataService.getInstance().commonAPI.loginCheck(id, password, auth);
        loginRequest.enqueue(new Callback<LoginVO>() {
            @Override
            public void onResponse(Call<LoginVO> call, Response<LoginVO> response) {
                Log.d("SocialFragment", "바디 : " + response.body());
                Log.d("SocialFragment", "코드 : " + response.code());
                if (response.code() == 200 && response.body() != null) {
                    LoginVO result = response.body();
                    Log.d("SocialFragment", "들어있는 값 :" + result.toString());

                    if (result.getM_id() != null) {
                        Log.d("SocialFragment", "로그인이 완료되었습니다");
                        setPreference("d_id", result.getM_id());
                        Log.d("SocialFragment", "d_id 값 : " + result.getM_id());
                        Intent intent = new Intent(getActivity(), MainViewActivity.class);
                        startActivity(intent);
                    } else {
                        Snackbar.make(view, "가입하지않은 계정이거나 잘못된 비밀번호입니다.", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(view, "가입하지않은 계정이거나 잘못된 비밀번호입니다.", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginVO> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (loginRequest != null)loginRequest.cancel();
    }
}