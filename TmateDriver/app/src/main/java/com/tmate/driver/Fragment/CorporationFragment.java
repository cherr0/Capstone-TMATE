package com.tmate.driver.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
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

    private String corpSpinner;
    private String corp;

    String car_color, car_kind;
    String color, kind;

    public Validator validator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentCorporationBinding.inflate(inflater, container, false);
        View view = b.getRoot();

        et_corp_name = view.findViewById(R.id.et_corp_name);
        et_car_model = view.findViewById(R.id.et_car_model);
        et_car_num = view.findViewById(R.id.et_car_num);

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
               corpSpinner = b.spinnerCorp.getSelectedItem().toString();
                Log.d("corp", corpSpinner);
                b.btnProfile.setVisibility(View.VISIBLE);
                if (corpSpinner.equals("법인")) {
                    b.corpConst.setVisibility(View.VISIBLE);
                    b.individualConst.setVisibility(View.GONE);
                    corp = "c";
                } else {
                    b.individualConst.setVisibility(View.VISIBLE);
                    b.corpConst.setVisibility(View.GONE);
                    corp = "i";
                }
            }
        });

        b.btnProfile.setOnClickListener(v -> {
            car_color = b.spinnerCarColor.getSelectedItem().toString();
            car_kind = b.spinnerCarKind.getSelectedItem().toString();
            car_color = b.spinnerCorpColor.getSelectedItem().toString();
            car_kind = b.spinnerCorpKind.getSelectedItem().toString();
            // 차량 분류
            switch (car_kind) {
                case "소형" :
                    kind = "0";
                    break;
                case "중형" :
                    kind = "1";
                    break;
                case "대형" :
                    kind = "2";
                    break;
            }
            // 차량 색상
            switch (car_color) {
                case "검은색" :
                    color = "black";
                    break;
                case "흰색" :
                    color = "white";
                    break;
                case "회색" :
                    color = "gray";
                    break;
                case "주황색" :
                    color = "orange";
                    break;
                case "노란색" :
                    color = "yellow";
                    break;
            }
            validator.validate(); //버튼 클릭시 이벤트 발생 //필수
        });

        return view;
    }
    @Override
    public void onValidationSucceeded() {

        bundle.putString("corp_code",corp);
        bundle.putString("car_kind",kind);
        bundle.putString("car_color",color);
        if (corp.equals("c")) {
            bundle.putString("corp_company", et_corp_name.getText().toString());
            bundle.putString("car_no", b.etCorpNum.getText().toString());
            bundle.putString("car_model", b.etCorpModel.getText().toString());
        } else {
            bundle.putString("car_no", et_car_num.getText().toString());
            bundle.putString("car_model", et_car_model.getText().toString());
        }
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