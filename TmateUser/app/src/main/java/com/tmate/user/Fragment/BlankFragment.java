package com.tmate.user.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
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
        radioButton6 = view.findViewById(R.id.radioButton6);
        radioButton7 = view.findViewById(R.id.radioButton7);

        validator = new Validator(this);
        validator.setValidationListener(this);

        btn_next = view.findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bundle.putString("m_name", et_name.getText().toString());
                if (radioButton6.isChecked()) {
                    bundle.putString("m_id","m1"+et_phone.getText().toString()+"0");
                }
                if (radioButton7.isChecked()) {
                    bundle.putString("m_id","m2"+et_phone.getText().toString()+"0");
                }

                bundle.putString("m_birth",et_birth.getText().toString());

                if(et_name.getText().toString().length() != 0 && et_phone.getText().toString().length() != 0 && et_birth.getText().toString().length() != 0) {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    SNFragment snFragment = new SNFragment();
                    snFragment.setArguments(bundle);
                    transaction.replace(R.id.fm_main, snFragment).commit();
                }

                validator.validate(); // 버튼 클릭시 이벤트 발생 // 필수
            }
        });
        return view;
    }

    //유효성 검사 통과하면 호출
    @Override
    public void onValidationSucceeded() {
        Toast.makeText(getActivity(), "성공", Toast.LENGTH_SHORT).show();
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