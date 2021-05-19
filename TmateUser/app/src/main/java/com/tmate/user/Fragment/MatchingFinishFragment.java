package com.tmate.user.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.tmate.user.databinding.FragmentMatchingFinishBinding;

public class MatchingFinishFragment extends Fragment {
    private FragmentMatchingFinishBinding binding;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMatchingFinishBinding.inflate(getLayoutInflater());
        view = binding.getRoot();

        return view;
    }

}