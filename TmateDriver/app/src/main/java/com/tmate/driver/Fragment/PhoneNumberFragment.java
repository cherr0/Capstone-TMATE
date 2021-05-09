package com.tmate.driver.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.tmate.driver.databinding.FragmentPhoneNumberBinding;

import java.util.List;


public class PhoneNumberFragment extends Fragment implements Validator.ValidationListener {
    private FragmentPhoneNumberBinding b;
    public Validator validator;
    Bundle bundle;

    @NotEmpty(message = "휴대폰번호를 입력해 주세요")
    @Length(min = 11, max = 11, message = "휴대폰번호를 올바르게 입력해 주세요")
    EditText et_phone;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentPhoneNumberBinding.inflate(inflater, container, false);
        View view = b.getRoot();

        et_phone = view.findViewById(R.id.et_phone);
        validator = new Validator(this);//필수
        validator.setValidationListener(this);//필수

        if (getArguments() != null) {
            bundle = getArguments();
            Log.d("PhNumFragment.Bundle", "번들 값 받아옴 bundle : " + bundle);
        } else {
            Log.d("PhNumFragment.Bundle", "번들 값을 받아오지 못했습니다.");
        }

        b.btnPhoneNumSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate(); //버튼 클릭시 이벤트 발생 //필수
            }
        });


        // X버튼 visibility 메소드
        b.etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            // editText 안 내용이 변경될 때마다 호출
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    b.btnClear.setVisibility(View.VISIBLE);
                } else {
                    b.btnClear.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        b.tvFirstScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                SocialFragment socialFragment = new SocialFragment();
                transaction.replace(R.id.fm_main, socialFragment).commit();
            }
        });

        clearText();

        return view;
    }

    @Override
    public void onValidationSucceeded() {
        String phoneNum = et_phone.getText().toString();
        Log.d("PhNumFragment", "phone Number : " + phoneNum);
        bundle.putString("phone", phoneNum);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        CertificationFragment cf = new CertificationFragment();
        cf.setArguments(bundle);

        transaction.replace(R.id.fm_main, cf);
        transaction.addToBackStack(null).commit();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getActivity());
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    //버튼 클릭 시 텍스트 내용을 없애는 메소드
    public void clearText() {
        b.btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.etPhone.setText("");
            }
        });
    }
}