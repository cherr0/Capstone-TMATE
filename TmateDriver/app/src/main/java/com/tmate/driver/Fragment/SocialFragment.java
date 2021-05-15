package com.tmate.driver.Fragment;

import android.content.Context;
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
import com.tmate.driver.R;
import com.tmate.driver.data.LoginVO;
import com.tmate.driver.data.Phone;
import com.tmate.driver.databinding.FragmentSocialBinding;
import com.tmate.driver.net.DataService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SocialFragment extends Fragment implements Validator.ValidationListener {
    private FragmentSocialBinding b;
    private Bundle bundle;
    private Phone phone;
    public Validator validator;
    private Context context;
    private Call<LoginVO> loginRequest;
    private SharedPreferences preferences;
    private Fragment completed;
    private LoginVO loginVO;
    private View view;

    @Length(min = 11, message = "이메일 또는 전화번호를 바르게 입력해주세요")
    @NotEmpty(message = "이메일 또는 전화번호를 입력해주세요")
    EditText et_userId;

    @Length(min = 8, max = 20, message = "비밀번호를 바르게 입력해주세요")
    @NotEmpty(message = "비밀번호를 입력해주세요")
    EditText et_password;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentSocialBinding.inflate(inflater, container, false);
        view = b.getRoot();
        context = container.getContext();

        loginVO = new LoginVO();

        et_userId = view.findViewById(R.id.et_userId);
        et_password = view.findViewById(R.id.et_password);

        validator = new Validator(this);//필수
        validator.setValidationListener(this);//필수

        if (getArguments() != null) {
            bundle = getArguments();
        } else {
            Log.d("SocialFragment.Bundle", "번들 값을 받아오지 못했습니다.");
        }

        if (!getPreferenceString("d_id").equals("")) {
            Log.d("SharedPreference", "d_id 값 있음 ");
        }

        b.btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 회원가입을 기존에 진행한 경우
                if (!getPreferenceString("d_id").equals("")) {
                    bundle.putString("d_id", getPreferenceString("d_id"));
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    CompletedFragment cf = new CompletedFragment();
                    cf.setArguments(bundle);
                    transaction.replace(R.id.fm_main, cf).commit();
                } else { // 회원가입 첫 시작인 경우
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    PhoneNumberFragment phoneNumberFragment = new PhoneNumberFragment();
                    phoneNumberFragment.setArguments(bundle);
                    transaction.replace(R.id.fm_main, phoneNumberFragment).commit();
                }
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

        b.btnLogin.setOnClickListener(v -> {
            Authorization();
        });
        return view;
    }

    public String getPreferenceString(String key) {
        SharedPreferences pref = getActivity().getSharedPreferences("loginDriver", getActivity().MODE_PRIVATE);
        return pref.getString(key, "");
    }

    // 데이터 저장 함수
    public void setPreference(String key, String value) {
        SharedPreferences pref = getActivity().getSharedPreferences("loginDriver", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.putString("d_id", loginVO.getM_id());
        editor.apply();
    }

    @Override
    public void onValidationSucceeded() {
        b.btnLogin.setEnabled(true);
        b.btnLogin.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4254BC")));
        b.btnLogin.setTextColor(Color.parseColor("#FFFFFF"));
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            b.btnLogin.setEnabled(false);
            b.btnLogin.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EFEFEF")));
            b.btnLogin.setTextColor(Color.parseColor("#000000"));
            String message = error.getCollatedErrorMessage(getActivity());
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void Authorization() {
        String id = b.etUserId.getText().toString();
        String password = b.etPassword.getText().toString();
        String auth = "d";
        Log.d("SocialFragment", "로그인 값 : " + id + ", " + password + ", " + auth);
        loginRequest = DataService.getInstance().common.loginCheck(id, password, auth);
        loginRequest.enqueue(new Callback<LoginVO>() {
            @Override
            public void onResponse(Call<LoginVO> call, Response<LoginVO> response) {
                Log.d("SocialFragment", "바디 : " + response.body());
                Log.d("SocialFragment", "코드 : " + response.code());
                if (response.code() == 200 && response.body() != null) {
                    LoginVO result = response.body();
                    Log.d("SocialFragment", "들어있는 값 :" + result.toString());

                    if (result.getM_id() != null) {
                        setPreference("d_id", bundle.getString("m_id"));
                        Log.d("SocialFragment", "로그인이 완료되었습니다");
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        CompletedFragment completedFragment = new CompletedFragment();
                        completedFragment.setArguments(bundle);
                        transaction.replace(R.id.fm_main, completedFragment).commit();
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