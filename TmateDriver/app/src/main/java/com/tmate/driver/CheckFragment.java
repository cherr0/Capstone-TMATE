package com.tmate.driver;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class CheckFragment extends Fragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_check, container, false);

        Button btn_black_list = view.findViewById(R.id.btn_black_list);
        btn_black_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                BlackListSelectFragment blackListSelectFragment = new BlackListSelectFragment();
                transaction.replace(R.id.frame_check, blackListSelectFragment).commit();

            }
        });

        return view;
    }
}