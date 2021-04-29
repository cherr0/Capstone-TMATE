package com.tmate.driver.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Checked;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.tmate.driver.R;
import com.tmate.driver.databinding.FragmentJoinpageBinding;

import java.util.List;

public class JoinPageFragment extends Fragment implements Validator.ValidationListener{
    private FragmentJoinpageBinding b;

    Bundle bundle;

    @Length(min = 2, max = 6, message = "최소3~최대5")
    @NotEmpty(message = "이름을 입력해주세요")
    EditText m_name;

    @NotEmpty(message = "휴대폰번호를 입력해 주세요")
    @Length(min = 11, max = 11, message = "올바르게 입력해 주세요")
    EditText et_phone;

    @NotEmpty(message = "생년월일를 입력해 주세요")
    @Length(min = 6, max = 6, message = "올바르게 입력해 주세요")
    EditText et_birth;

    @Checked
    RadioGroup gender;

    public Validator validator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentJoinpageBinding.inflate(inflater, container, false);
        View view = b.getRoot();

        if (getArguments() != null) {
            bundle = getArguments();
            Log.d("JoinPageFragment.Bundle", "번들 값 받아옴");
        }else {
            Log.d("JoinPageFragment.Bundle","번들 값을 받아오지 못했습니다.");
        }

        m_name = view.findViewById(R.id.m_name);
        et_birth = view.findViewById(R.id.et_birth);
        et_phone = view.findViewById(R.id.et_phone);
        gender = view.findViewById(R.id.gender);
        validator = new Validator(this);//필수
        validator.setValidationListener(this);//필수

        b.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate(); //버튼 클릭시 이벤트 발생 //필수
            }
        });
        return view;
    }

    //유효성 검사 통과하면 호출
    @Override
    public void onValidationSucceeded() {

        bundle.putString("m_birth",et_birth.getText().toString()); // 생년월일
        bundle.putString("m_name",m_name.getText().toString()); // 이름
        if (b.male.isChecked()) {
            bundle.putString("m_id","m1"+et_phone.getText().toString()+"0");
        }else if(b.female.isChecked()){
            bundle.putString("m_id","m2"+et_phone.getText().toString()+"0");
        }

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        CertificationFragment cf = new CertificationFragment();
        cf.setArguments(bundle);
        transaction.replace(R.id.fm_main, cf);
        transaction.addToBackStack(null).commit();
    }

    //유효성 검사 오류가 있을때 호출
    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getActivity());
            if (view instanceof EditText) {
                ((EditText)view).setError(message);
            } else {
                Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
            }
        }
    }
}