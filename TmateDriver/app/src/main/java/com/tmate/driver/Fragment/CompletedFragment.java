package com.tmate.driver.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tmate.driver.activity.LoginActivity;
import com.tmate.driver.OnBackPressedListener;
import com.tmate.driver.databinding.FragmentCompletedBinding;


public class CompletedFragment extends Fragment implements OnBackPressedListener {
    private FragmentCompletedBinding b;
    LoginActivity activity;
    long backKeyPressedTime;
    Toast toast;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentCompletedBinding.inflate(inflater, container, false);
        View v = b.getRoot();
        activity = (LoginActivity) getActivity();
        toast = Toast.makeText(getContext(),"한번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT);

        return  v;
    }


    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() > backKeyPressedTime + 2000){
            backKeyPressedTime = System.currentTimeMillis();
            toast.show();
            return;
        }
        if(System.currentTimeMillis() <= backKeyPressedTime + 2000){
            getActivity().finish();
            toast.cancel();
        }
    }
    @Override public void onResume() {
        super.onResume();
        activity.setOnBackPressedListener(this);
    }

}
