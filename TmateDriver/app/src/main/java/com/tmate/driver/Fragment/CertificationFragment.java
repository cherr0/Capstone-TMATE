package com.tmate.driver.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.tmate.driver.R;
import com.tmate.driver.databinding.FragmentCertificationBinding;


public class CertificationFragment extends Fragment {
    private FragmentCertificationBinding b;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentCertificationBinding.inflate(inflater, container, false);
        View view = b.getRoot();

        b.btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (b.etConfirmNum.getText().toString().length() == 0) {
                    Toast.makeText(getActivity(), "인증번호를 입력하세요", Toast.LENGTH_LONG).show();
                    return;
                } else {*/
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    CertificateEnrollmentFragment cef = new CertificateEnrollmentFragment();
                    transaction.replace(R.id.fm_main, cef);
                    transaction.addToBackStack(null);

                transaction.commit();
                }
            //}
        });



        return view;
    }

}
