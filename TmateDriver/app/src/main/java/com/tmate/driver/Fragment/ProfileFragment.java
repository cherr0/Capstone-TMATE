package com.tmate.driver.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.tmate.driver.R;

public class ProfileFragment extends Fragment {
    View view;
    ConstraintLayout d_acnum_change;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        d_acnum_change = view.findViewById(R.id.d_acnum_change);
        d_acnum_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                AccountChangeFragment accountChangeFragment = new AccountChangeFragment();
                transaction.replace(R.id.frame, accountChangeFragment).commit();
            }
        });


        return view;
    }
}