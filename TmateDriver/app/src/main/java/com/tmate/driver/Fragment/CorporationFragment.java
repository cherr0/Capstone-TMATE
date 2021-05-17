package com.tmate.driver.Fragment;

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
import androidx.fragment.app.FragmentTransaction;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.tmate.driver.R;
import com.tmate.driver.databinding.FragmentCorporationBinding;

import java.util.List;


public class CorporationFragment extends Fragment implements Validator.ValidationListener{
    View view;
    Bundle bundle;
    private FragmentCorporationBinding b;
    @NotEmpty(message = "회사이름을 입력해주세요")
    EditText et_corp_name;

    @NotEmpty(message = "차량모델을 입력해주세요")
    EditText et_car_model;  // 차량모델

    @NotEmpty(message = "차량번호를 입력해주세요")
    EditText et_car_num;

    @NotEmpty(message = "분류를 입력해주세요")
    EditText et_car_kind;

    @NotEmpty(message = "색상을 입력해주세요")
    EditText et_car_color;

    public Validator validator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentCorporationBinding.inflate(inflater, container, false);
        View view = b.getRoot();

        et_corp_name = view.findViewById(R.id.et_corp_name);
        et_car_model = view.findViewById(R.id.et_car_model);
        et_car_num = view.findViewById(R.id.et_car_num);
        et_car_kind = view.findViewById(R.id.et_car_kind);
        et_car_color = view.findViewById(R.id.et_car_color);

        validator = new Validator(this);
        validator.setValidationListener(this);

        if (getArguments() != null) {
            bundle = getArguments();
            Log.d("번들 넘어오는 값",bundle.toString());
        }else {
            Log.d("CorpFragment.Bundle","번들 값을 받아오지 못했습니다.");
        }

        b.btnCorpSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String corp = b.spinnerCorp.getSelectedItem().toString();
                Log.d("corp", corp);
                b.btnProfile.setVisibility(View.VISIBLE);
                if (corp.equals("법인")) {
                    b.tvCorpName.setVisibility(View.VISIBLE);
                    b.etCorpName.setVisibility(View.VISIBLE);
                    b.tvCorpModel.setVisibility(View.VISIBLE);
                    b.etCorpModel.setVisibility(View.VISIBLE);
                    b.tvCorpNum.setVisibility(View.VISIBLE);
                    b.etCorpNum.setVisibility(View.VISIBLE);
                    b.tvCorpKind.setVisibility(View.VISIBLE);
                    b.etCorpKind.setVisibility(View.VISIBLE);
                    b.tvCorpColor.setVisibility(View.VISIBLE);
                    b.etCorpColor.setVisibility(View.VISIBLE);
                    b.tvCarModel.setVisibility(View.GONE);
                    b.etCarModel.setVisibility(View.GONE);
                    b.tvCarNum.setVisibility(View.GONE);
                    b.etCarNum.setVisibility(View.GONE);
                    b.tvCarKind.setVisibility(View.GONE);
                    b.etCarKind.setVisibility(View.GONE);
                    b.tvCarColor.setVisibility(View.GONE);
                    b.etCarColor.setVisibility(View.GONE);
                } else {
                    b.tvCarModel.setVisibility(View.VISIBLE);
                    b.etCarModel.setVisibility(View.VISIBLE);
                    b.tvCarNum.setVisibility(View.VISIBLE);
                    b.etCarNum.setVisibility(View.VISIBLE);
                    b.tvCarKind.setVisibility(View.VISIBLE);
                    b.etCarKind.setVisibility(View.VISIBLE);
                    b.tvCarColor.setVisibility(View.VISIBLE);
                    b.etCarColor.setVisibility(View.VISIBLE);
                    b.tvCorpName.setVisibility(View.GONE);
                    b.etCorpName.setVisibility(View.GONE);
                    b.tvCorpModel.setVisibility(View.GONE);
                    b.etCorpModel.setVisibility(View.GONE);
                    b.tvCorpNum.setVisibility(View.GONE);
                    b.etCorpNum.setVisibility(View.GONE);
                    b.tvCorpKind.setVisibility(View.GONE);
                    b.etCorpKind.setVisibility(View.GONE);
                    b.tvCorpColor.setVisibility(View.GONE);
                    b.etCorpColor.setVisibility(View.GONE);
                }
            }
        });

        b.btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate(); //버튼 클릭시 이벤트 발생 //필수
            }
        });

        return view;
    }
    @Override
    public void onValidationSucceeded() {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        CertificateEnrollmentFragment cef = new CertificateEnrollmentFragment();
        cef.setArguments(bundle);
        transaction.replace(R.id.fm_main, cef);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    //유효성 검사 오류가 있을때 호출
    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for(ValidationError error : errors){
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getActivity());
            if(view instanceof EditText){
                ((EditText)view).setError(message);
            }else{
                Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
            }
        }
    }
}