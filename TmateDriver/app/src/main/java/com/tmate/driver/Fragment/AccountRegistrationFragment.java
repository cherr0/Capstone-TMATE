package com.tmate.driver.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.tmate.driver.R;
import com.tmate.driver.databinding.FragmentAccountRegistrationBinding;


public class AccountRegistrationFragment extends Fragment {
    private FragmentAccountRegistrationBinding b;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentAccountRegistrationBinding.inflate(inflater, container, false);
        View v = b.getRoot();

        b.bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogItems();
            }
        });
        b.callAdmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dAcnumLinear.setVisibility(View.GONE);
                b.bankList.setVisibility(View.GONE);
                b.bcAcnum.setText(b.dAcnum.getText().toString());
                b.bankCheck.setVisibility(View.VISIBLE);
                b.confirmMessage.setVisibility(View.VISIBLE);
            }
        });
        b.cardChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogItems();
            }
        });
        b.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                CompletedFragment cf = new CompletedFragment();
                transaction.replace(R.id.fm_main, cf);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });



        return v;
    }

    private void showAlertDialogItems() {
        String [] cards = new String[] {"국민은행","농협","신한은행","우리은행"};
        AlertDialog.Builder ad = new AlertDialog.Builder(getContext());

        ad.setTitle("은행사");
        ad.setItems(
                cards, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        b.bank.setVisibility(View.GONE);
                        b.dAcnumClick.setVisibility(View.GONE);
                        b.dAcnumLinear.setVisibility(View.VISIBLE);
                        b.bankNameSelect.setText(cards[which]);
                        b.bankCompany.setText(cards[which]);
                    }
                });
        ad.setNeutralButton("닫기",null).show();
    }



}
