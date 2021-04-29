package com.tmate.driver.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.tmate.driver.R;
import com.tmate.driver.data.Phone;
import com.tmate.driver.databinding.FragmentSocialBinding;


public class SocialFragment extends Fragment {
    private FragmentSocialBinding b;
    private Bundle bundle;
    private Phone phone;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentSocialBinding.inflate(inflater, container, false);
        View view = b.getRoot();

        if (getArguments() != null) {
            bundle = getArguments();
        }else {
            Log.d("SocialFragment.Bundle","번들 값을 받아오지 못했습니다.");
        }

        b.btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                JoinPageFragment blankFragment= new JoinPageFragment();
                blankFragment.setArguments(bundle);
                transaction.replace(R.id.fm_main, blankFragment).commit();
            }
        });

        return view;
    }
}
