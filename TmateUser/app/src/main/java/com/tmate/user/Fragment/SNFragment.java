package com.tmate.user.Fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.tmate.user.R;
import com.tmate.user.data.PhoneDTO;
import com.tmate.user.net.DataService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SNFragment extends Fragment implements Validator.ValidationListener {
    private View view;
    private Button btn_profile;

    // 시간 타이머
    TextView time_counter;
    CountDownTimer countDownTimer;
    final int MILLISINFUTURE = 180 * 1000; //총 시간 (180초 = 5분)
//    final  int MILLISINFUTURE = 5 * 1000;
    final int COUNT_DOWN_INTERVAL = 1000; //onTick 메소드를 호출할 간격 (1초)

    @ConfirmPassword(message = "인증번호를 입력해주세요")
    EditText et_confirmNum; // 인증번호 입력

    @Password
    TextView tv_certNum;    // 인증번호 받는 란

    Bundle bundle;
    PhoneDTO phoneDTO = new PhoneDTO();
    DataService dataService = DataService.getInstance();
    Validator validator;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_s_n, container, false);

        btn_profile = view.findViewById(R.id.btn_profile);
        et_confirmNum = view.findViewById(R.id.et_confirmNum);
        tv_certNum = view.findViewById(R.id.tv_certNum);
        validator = new Validator(this);//필수
        validator.setValidationListener(this); //필수

        if (getArguments() != null) {
            bundle = getArguments();
            Log.d("폰번호",getArguments().getString("m_id").substring(2,13));
            Log.d("고유id값", getArguments().getString("m_imei"));
            phoneDTO.setPhoneNumber(getArguments().getString("m_id").substring(2,13));
        }


        Authorization();

        countDownTimer(view);

        time_counter.setOnClickListener(v -> {
            Authorization();
            countDownTimer(view);
        });

        btn_profile.setOnClickListener(v -> {
            validator.validate(); //버튼 클릭 시 이벤트 발생 //필수
        });
        return view;
    }

    // 카운트 다운
    public void countDownTimer(View view) {
        time_counter = (TextView) view.findViewById(R.id.time_counter);
        // 줄어드는 시간을 나타내는 TEXTVIEW

        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                long phoneAuthCount = millisUntilFinished / 1000;

                if ((phoneAuthCount - ((phoneAuthCount / 60) * 60)) >= 10) { //초가 10보다 크면 그냥 출력
                    time_counter.setText((phoneAuthCount / 60) + " : " + (phoneAuthCount - ((phoneAuthCount / 60) * 60)));
                } else { //초가 10보다 작으면 앞에 '0' 붙여서 같이 출력. ex) 02,03,04...
                    time_counter.setText((phoneAuthCount / 60) + " : 0" + (phoneAuthCount - ((phoneAuthCount / 60) * 60)));
                }
            }

            @Override
            public void onFinish() {
                Toast.makeText(getActivity(), "인증을 시간이 지났습니다.", Toast.LENGTH_SHORT).show();
                time_counter.setText("인증번호 재전송");
            }
        }.start();

    }

    @Override
    public void onValidationSucceeded() {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        Profile_Reg_Fragment profile_reg_fragment = new Profile_Reg_Fragment();
        profile_reg_fragment.setArguments(bundle);
        countDownTimer.cancel();
        transaction.replace(R.id.fm_main, profile_reg_fragment);
        transaction.commit();
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

    public void Authorization() {
        DataService.getInstance().commonAPI.sendSMS(phoneDTO).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        Toast.makeText(getActivity(), "인증번호를 발송하였습니다.", Toast.LENGTH_LONG).show();
                        tv_certNum.setText(response.body());
                        Log.d("인증번호 값", response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                Log.e("인증번호","발송 실패");
            }
        });
    }


}