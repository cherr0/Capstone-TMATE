package com.tmate.user.Fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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
import com.tmate.user.R;

import java.util.List;


public class BlankFragment extends Fragment implements Validator.ValidationListener {

    private View view;
    private Button btn_next;
    private final SNFragment snFragment = new SNFragment();
    @NotEmpty(message = "이름을 입력해주세요") // 필수입력
    @Length(min = 2,max = 5,message = "이름은 최소2~최대5로 입력 해주세요.") // 길이
    private EditText et_name;

    @NotEmpty(message = "전화번호를 입력해 주세요")
    @Length(min = 11, max = 11, message = "전화번호를 바르게 입력 해주세요.")
    private EditText et_phone;

    @NotEmpty(message = "생년월일을 입력 해주세요")
    @Length (min = 6,max = 6, message = "생년월일은 6자리로 입력 해주세요.\n ex)000426")
    private EditText et_birth;

    @NotEmpty
    @Length(min = 8, max = 20, message = "최소 8 ~ 최대 20")
    private EditText et_reg_password;

    @Checked(message = "성별을 선택해 주세요")
    private RadioGroup radioGroup2;
    private RadioButton radioButton6, radioButton7;

    public Validator validator;

    Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_blank, container, false);

        if (getArguments() != null) {
            bundle = getArguments();
        }

        et_name = view.findViewById(R.id.et_name);
        et_phone = view.findViewById(R.id.et_phone);
        et_birth = view.findViewById(R.id.et_birth);
        et_reg_password = view.findViewById(R.id.et_reg_password);
        radioButton6 = view.findViewById(R.id.radioButton6);
        radioButton7 = view.findViewById(R.id.radioButton7);
        radioGroup2 = view.findViewById(R.id.radioGroup2);

        validator = new Validator(this);
        validator.setValidationListener(this);

        btn_next = view.findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate(); // 버튼 클릭시 이벤트 발생 // 필수
            }
        });
        return view;
    }

    @Override //유효성 검사 통과하면 호출
    public void onValidationSucceeded() {
        bundle.putString("m_name", et_name.getText().toString());
        String birth_str = et_birth.getText().toString();
        birth_str.substring(0, 1);
        if (radioButton6.isChecked()) {
            if (birth_str == "0") {
                bundle.putString("m_id", "m3" + et_phone.getText().toString() + "0");
            } else {
                bundle.putString("m_id","m1"+et_phone.getText().toString()+"0");
            }
        }else if (radioButton7.isChecked()) {
            if (birth_str == "0") {
                bundle.putString("m_id", "m4" + et_phone.getText().toString() + "0");
            } else {
                bundle.putString("m_id","m2"+et_phone.getText().toString()+"0");
            }
        }
        bundle.putString("password", et_reg_password.getText().toString());
        bundle.putString("m_birth",et_birth.getText().toString());

        Log.i("BlankFragment","넘어가는 번들 값 : " + bundle);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        SNFragment snFragment = new SNFragment();
        snFragment.setArguments(bundle);
        transaction.replace(R.id.fm_main, snFragment).commit();
    }

    @Override //유효성 검사 오류가 있을때 호출
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