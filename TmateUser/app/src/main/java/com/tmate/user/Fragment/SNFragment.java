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

import com.tmate.user.R;
import com.tmate.user.data.PhoneDTO;
import com.tmate.user.net.DataService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SNFragment extends Fragment {
    private View view;
    private Button btn_profile;

    // 시간 타이머
    TextView time_counter;
    CountDownTimer countDownTimer;
    final int MILLISINFUTURE = 180 * 1000; //총 시간 (180초 = 5분)
//    final  int MILLISINFUTURE = 5 * 1000;
    final int COUNT_DOWN_INTERVAL = 1000; //onTick 메소드를 호출할 간격 (1초)

    EditText et_confirmNum;
    Bundle bundle;
    PhoneDTO phoneDTO = new PhoneDTO();
    DataService dataService = DataService.getInstance();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_s_n, container, false);

        btn_profile = view.findViewById(R.id.btn_profile);
        et_confirmNum = view.findViewById(R.id.et_confirmNum);

        if (getArguments() != null) {
            bundle = getArguments();
//            bundle.putString("m_name", getArguments().getString("m_name"));
//            bundle.putString("m_id", getArguments().getString("m_id"));
//            bundle.putString("m_birth", getArguments().getString("m_birth"));
            Log.d("폰번호",getArguments().getString("m_id").substring(2,13));
            Log.d("고유id값", getArguments().getString("m_imei"));
            phoneDTO.setPhoneNumber(getArguments().getString("m_id").substring(2,13));
        }


        Authorization();

        countDownTimer(view);

        time_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Authorization();
                countDownTimer(view);
            }
        });

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (et_confirmNum.getText().toString().length() == 0) {
                    Toast.makeText(getActivity(), "인증번호를 입력하세요", Toast.LENGTH_LONG).show();
                    return;
                }




                    phoneDTO.setConfirm(et_confirmNum.getText().toString());
                    dataService.memberAPI.confirm(phoneDTO).enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            if (response.isSuccessful()) {
                                if (response.code() == 200) {
                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    Profile_Reg_Fragment profile_reg_fragment = new Profile_Reg_Fragment();
                                    profile_reg_fragment.setArguments(bundle);
                                    countDownTimer.cancel();
                                    transaction.replace(R.id.fm_main, profile_reg_fragment);
                                    transaction.commit();
                                }


                                if (response.code() == 400) {

                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {

                        }
                    });
                }




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
                Log.d("Hazzang", phoneAuthCount + "");

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
        dataService.memberAPI.sendSMS(phoneDTO).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        Toast.makeText(getActivity(), "인증번호를 발송하였습니다.", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }


}