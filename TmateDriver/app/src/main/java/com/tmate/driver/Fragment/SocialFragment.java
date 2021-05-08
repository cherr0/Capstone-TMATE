package com.tmate.driver.Fragment;

import android.content.SharedPreferences;
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

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.tmate.driver.R;
import com.tmate.driver.data.Phone;
import com.tmate.driver.databinding.FragmentSocialBinding;

import java.util.List;


public class SocialFragment extends Fragment implements Validator.ValidationListener{
    private FragmentSocialBinding b;
    private Bundle bundle;
    private Phone phone;
    public Validator validator;

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
        View view = b.getRoot();


        et_userId = view.findViewById(R.id.et_userId);
        et_password = view.findViewById(R.id.et_password);

        validator = new Validator(this);//필수
        validator.setValidationListener(this);//필수

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
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validator.validate(); // 텍스트 변경시 이벤트 발생생
           }
        });

        b.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

    @Override
    public void onValidationSucceeded() {
        b.btnLogin.setEnabled(true);
        b.btnLogin.setBackgroundResource(R.color.main);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for(ValidationError error : errors){
            View view = error.getView();
            b.btnLogin.setEnabled(false);
            String message = error.getCollatedErrorMessage(getActivity());
            if(view instanceof EditText){
                ((EditText)view).setError(message);
            }else{
                Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
            }
        }
    }
}