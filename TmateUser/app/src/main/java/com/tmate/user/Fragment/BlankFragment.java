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

import com.tmate.user.R;


public class BlankFragment extends Fragment {

    private View view;
    private Button btn_next;
    private final SNFragment snFragment = new SNFragment();
    private EditText et_name,et_phone,et_birth;
    private RadioButton radioButton6, radioButton7;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_blank, container, false);

        et_name = view.findViewById(R.id.et_name);
        et_phone = view.findViewById(R.id.et_phone);
        et_birth = view.findViewById(R.id.et_birth);
        radioButton6 = view.findViewById(R.id.radioButton6);
        radioButton7 = view.findViewById(R.id.radioButton7);

        btn_next = view.findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
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
                else{
                    Toast.makeText(getActivity(), "정보를 입력해주세요", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }
}