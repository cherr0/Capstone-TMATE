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
import com.tmate.driver.databinding.FragmentCarAddBinding;

public class CarAddFragment extends Fragment {
    private FragmentCarAddBinding b;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentCarAddBinding.inflate(inflater, container, false);
        View v = b.getRoot();

        b.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                CarFragment cf = new CarFragment();
                transaction.replace(R.id.frame, cf);
                transaction.commit();
            }
        });

        return v;
    }
}
