package com.tmate.driver.Fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.tmate.driver.R;
import com.tmate.driver.data.Phone;
import com.tmate.driver.databinding.FragmentCertificationBinding;
import com.tmate.driver.net.DataService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CertificationFragment extends Fragment implements Validator.ValidationListener{
    private FragmentCertificationBinding b;

    // 시간 타이머
    TextView time_counter;
    CountDownTimer countDownTimer;
    final int MILLISINFUTURE = 180 * 1000; //총 시간 (180초 = 5분)
    //    final  int MILLISINFUTURE = 5 * 1000;
    final int COUNT_DOWN_INTERVAL = 1000; //onTick 메소드를 호출할 간격 (1초)

    @Password
    TextView tv_certNum;

    @ConfirmPassword
    EditText et_confirmNum;

    Bundle bundle;
    Phone phone = new Phone();

    public Validator validator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentCertificationBinding.inflate(inflater, container, false);
        View view = b.getRoot();

        tv_certNum = view.findViewById(R.id.tv_certNum);
        et_confirmNum = view.findViewById(R.id.et_confirmNum);
        validator = new Validator(this);//필수
        validator.setValidationListener(this); //필수

        if (getArguments() != null) {
            bundle = getArguments();
            Log.d("번들 넘어오는 값",bundle.toString());
            Log.d("폰번호",getArguments().getString("m_id").substring(2,13));
            phone.setPhoneNumber(getArguments().getString("m_id").substring(2,13));
        }else {
            Log.d("JoinPageFragment.Bundle","번들 값을 받아오지 못했습니다.");
        }

        Authorization();

        countDownTimer(view);

        b.btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate(); //버튼 클릭 시 이벤트 발생 //필수
            }
        });



        return view;
    }

    @Override
    public void onValidationSucceeded() {
        countDownTimer.cancel();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        JoinPageFragment joinPageFragment = new JoinPageFragment();
        joinPageFragment.setArguments(bundle);
        transaction.replace(R.id.fm_main, joinPageFragment);
        transaction.addToBackStack(null);
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

    public void Authorization() {
        DataService.getInstance().common.sendSMS(phone).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        Toast.makeText(getActivity(), "인증번호를 발송하였습니다.", Toast.LENGTH_LONG).show();
                        b.tvCertNum.setText(response.body());
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
