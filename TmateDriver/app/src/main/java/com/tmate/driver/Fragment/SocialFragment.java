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
import com.tmate.driver.databinding.FragmentSocialBinding;


public class SocialFragment extends Fragment {
    private FragmentSocialBinding b;
    Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentSocialBinding.inflate(inflater, container, false);
        View view = b.getRoot();

        b.btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                JoinPageFragment blankFragment= new JoinPageFragment();
                transaction.replace(R.id.fm_main, blankFragment).commit();
            }
        });

        return view;
    }
}
