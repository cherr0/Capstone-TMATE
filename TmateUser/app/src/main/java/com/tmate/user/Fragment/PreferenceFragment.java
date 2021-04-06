package com.tmate.user.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.tmate.user.R;

public class PreferenceFragment extends Fragment {

    private View view;
    private ImageView btn_back_preference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_preference, container, false);

        btn_back_preference = view.findViewById(R.id.btn_back_preference);
        btn_back_preference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                MoreFragment more_fragment = new MoreFragment();
                transaction.replace(R.id.frameLayout, more_fragment).commit();
            }
        });

        return view;
    }
}
