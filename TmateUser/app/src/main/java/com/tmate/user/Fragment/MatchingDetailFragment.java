package com.tmate.user.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tmate.user.R;


public class MatchingDetailFragment extends Fragment {
    private View view;
    private Button btn_match;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_matching_detail, container, false);

        btn_match = view.findViewById(R.id.btn_match);
        btn_match.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                MatchingSeatFragment matchingSeatFragment = new MatchingSeatFragment();
                transaction.replace(R.id.fm_matching, matchingSeatFragment);
                transaction.commit();
            }
        });
        return view;
    }

}