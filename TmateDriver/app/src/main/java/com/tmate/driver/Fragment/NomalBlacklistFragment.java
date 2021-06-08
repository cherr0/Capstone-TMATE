package com.tmate.driver.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tmate.driver.databinding.FragmentNomalBlacklistBinding;

public class NomalBlacklistFragment extends Fragment {
    private FragmentNomalBlacklistBinding b;
    private View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentNomalBlacklistBinding.inflate(getLayoutInflater());
        v = b.getRoot();


        return v;
    }
}
