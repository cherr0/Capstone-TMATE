package com.tmate.user.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.tmate.user.R;


public class SocialFragment extends Fragment {

    private View view;
    private Button btn_reg;
    Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_social, container, false);

        if (getArguments() != null) {
            bundle = getArguments();
        }

        btn_reg = view.findViewById(R.id.btn_reg);
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                BlankFragment blankFragment= new BlankFragment();
                blankFragment.setArguments(bundle);
                transaction.replace(R.id.fm_main, blankFragment);
                transaction.commit();
            }
        });
        return view;
    }
}