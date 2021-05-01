package com.tmate.driver.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.tmate.driver.R;
import com.tmate.driver.data.Driver;
import com.tmate.driver.data.Member;
import com.tmate.driver.databinding.FragmentAccountRegistrationBinding;
import com.tmate.driver.net.DataService;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AccountRegistrationFragment extends Fragment implements Validator.ValidationListener {
    private FragmentAccountRegistrationBinding b;
    Bundle bundle;
    Validator validator;
    // 객체 담을 Map
    Map<String, String> map = new HashMap<>();

    Call<Boolean> request;

    @NotEmpty(message = "은행 선택 바랍니다.")
    TextView card_company;

    @NotEmpty(message = "계좌번호 입력 바랍니다.")
    EditText d_acnum;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentAccountRegistrationBinding.inflate(inflater, container, false);
        View v = b.getRoot();

        card_company = v.findViewById(R.id.card_company);
        d_acnum = v.findViewById(R.id.d_acnum);
        validator = new Validator(this);//필수
        validator.setValidationListener(this); //필수

        if (getArguments() != null) {
            bundle = getArguments();
            Log.d("번들 넘어오는 값",bundle.toString());
        }else {
            Log.d("AccRegFragment.Bundle","번들 값을 받아오지 못했습니다.");
        }

        b.bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogItems();
            }
        });

        b.cardChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogItems();
            }
        });

        b.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate(); //버튼 클릭 시 이벤트 발생 //필수
            }
        });
        return v;
    }

    private void showAlertDialogItems() {
        String [] cards = new String[] {"국민은행","농협","신한은행","우리은행","카카오뱅크"};
        AlertDialog.Builder ad = new AlertDialog.Builder(getContext());

        ad.setTitle("은행사");
        ad.setItems(
                cards, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        b.bank.setVisibility(View.GONE);
                        b.bankCheck.setVisibility(View.VISIBLE);
                        b.cardCompany.setText(cards[which]);
                    }
                });
        ad.setNeutralButton("닫기",null).show();
    }

    @Override
    public void onValidationSucceeded() {
        bundle.putString("bank_company", card_company.getText().toString());
        bundle.putString("d_acnum", d_acnum.getText().toString());

        // 사용자 객체 추가
        map.put("m_id", bundle.getString("m_id"));
        map.put("m_name", bundle.getString("m_name"));
        map.put("m_imei", bundle.getString("m_imei"));
        map.put("m_birth", bundle.getString("m_birth"));

        // 기사 객체 추가
        map.put("d_id", bundle.getString("m_id"));
        map.put("d_license_no", bundle.getString("d_license_no"));
        map.put("bank_company", bundle.getString("bank_company"));
        map.put("d_acnum", bundle.getString("d_acnum"));

        request = DataService.getInstance().driver.registerDriver(map);
        request.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.code() == 200) {
                    Log.i("AccountRegFragment","가입 신청 완료");
                    setPreference("d_id",bundle.getString("m_id"));
                    setPreference("m_name",bundle.getString("m_name"));
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    CompletedFragment cf = new CompletedFragment();
                    cf.setArguments(bundle);
                    transaction.replace(R.id.fm_main, cf);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(getContext(), "데이터 전송에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });


    }

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
}
