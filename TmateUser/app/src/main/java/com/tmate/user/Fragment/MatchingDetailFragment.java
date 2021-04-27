package com.tmate.user.Fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.tmate.user.R;
import com.tmate.user.databinding.FragmentMatchingDetailBinding;


public class MatchingDetailFragment extends Fragment {
    private View view;
    private Button btn_match;
    private FragmentMatchingDetailBinding b;
    private Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b= FragmentMatchingDetailBinding.inflate(getLayoutInflater());
        view = b.getRoot();
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_together_info);

        b.btnMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                MatchingMemberFragment matchingMemberFragment = new MatchingMemberFragment();
                transaction.replace(R.id.fm_matching, matchingMemberFragment);
                transaction.commit();
            }
        });

        b.btnMemberRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                TogetherRequestFragment togetherRequestFragment = new TogetherRequestFragment();
                transaction.replace(R.id.fm_matching, togetherRequestFragment);
                transaction.commit();
            }
        });

        b.btnMatch.setOnClickListener(new View.OnClickListener() {
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
    public void showDialog(){
        dialog.show();

        TextView exit = dialog.findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

}