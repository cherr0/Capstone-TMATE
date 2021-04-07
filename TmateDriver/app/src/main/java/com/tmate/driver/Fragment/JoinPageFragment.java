package com.tmate.driver.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.tmate.driver.R;
import com.tmate.driver.databinding.FragmentJoinpageBinding;

public class JoinPageFragment extends Fragment{
    private FragmentJoinpageBinding b;
    Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentJoinpageBinding.inflate(inflater, container, false);
        View view = b.getRoot();


        b.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(b.mName.getText().toString().length() != 0 && b.etPhone.getText().toString().length() != 0 && b.etBirth.getText().toString().length() != 0) {

                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    CertificationFragment cf = new CertificationFragment();
                    transaction.replace(R.id.fm_main, cf);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else{
                    Toast.makeText(getActivity(), "정보를 입력해주세요", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }
}
