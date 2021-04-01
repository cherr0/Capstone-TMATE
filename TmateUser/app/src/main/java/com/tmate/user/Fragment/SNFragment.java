package com.tmate.user.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.tmate.user.R;
import com.tmate.user.data.PhoneDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SNFragment extends Fragment {
    private View view;
    private Button btn_profile;
    EditText et_confirmNum;
    Bundle bundle;
    PhoneDTO phoneDTO = new PhoneDTO();
    DataService dataService = new DataService();

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

        dataService.select.sendSMS(phoneDTO).enqueue(new Callback<Boolean>() {
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

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (et_confirmNum.getText().toString().length() == 0) {
                    Toast.makeText(getActivity(), "인증번호를 입력하세요", Toast.LENGTH_LONG).show();
                    return;
                }


                    phoneDTO.setConfirm(et_confirmNum.getText().toString());
                    dataService.select.confirm(phoneDTO).enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            if (response.isSuccessful()) {
                                if (response.code() == 200) {
                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    Profile_Reg_Fragment profile_reg_fragment = new Profile_Reg_Fragment();
                                    profile_reg_fragment.setArguments(bundle);
                                    transaction.replace(R.id.fm_main, profile_reg_fragment);
                                    transaction.commit();
                                }


                                if (response.body() == 0) {
                                    Toast.makeText(getActivity(), "인증번호가 틀렸습니다. 다시시도해 주세요", Toast.LENGTH_LONG);
                                    return;
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
}