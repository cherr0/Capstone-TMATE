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


public class SNFragment extends Fragment {
    private View view;
    private Button btn_profile;
    Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_s_n, container, false);

        btn_profile = view.findViewById(R.id.btn_profile);

        if (getArguments() != null) {
            bundle = new Bundle();
            bundle.putString("m_name", getArguments().getString("m_name"));
            bundle.putString("m_id", getArguments().getString("m_id"));
            bundle.putString("m_birth", getArguments().getString("m_birth"));
        }

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Profile_Reg_Fragment profile_reg_fragment = new Profile_Reg_Fragment();
                profile_reg_fragment.setArguments(bundle);
                transaction.replace(R.id.fm_main, profile_reg_fragment);
                transaction.commit();
            }
        });
        return view;
    }
}