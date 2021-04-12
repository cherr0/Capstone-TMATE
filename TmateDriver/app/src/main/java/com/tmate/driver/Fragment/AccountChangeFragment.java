package com.tmate.driver.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.tmate.driver.R;


public class AccountChangeFragment extends Fragment {
    View view;
    TextView bc_acnum, card_change, bank_name_select, bank_company;
    ImageView bank_list;
    Button call_admit, btn_next;
    EditText d_acnum;
    LinearLayout bank, d_acnum_linear, bank_check, confirm_message, d_acnum_click;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account_change, container, false);

        bank = view.findViewById(R.id.bank);
        call_admit = view.findViewById(R.id.call_admit);
        d_acnum_linear = view.findViewById(R.id.d_acnum_linear);
        bank_list = view.findViewById(R.id.bank_list);
        bc_acnum = view.findViewById(R.id.bc_acnum);
        d_acnum = view.findViewById(R.id.d_acnum);
        bank_check = view.findViewById(R.id.bank_check);
        confirm_message = view.findViewById(R.id.confirm_message);
        card_change = view.findViewById(R.id.card_change);
        btn_next = view.findViewById(R.id.btn_next);
        d_acnum_click = view.findViewById(R.id.d_acnum_click);
        bank_name_select = view.findViewById(R.id.bank_name_select);
        bank_company = view.findViewById(R.id.bank_company);

        bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogItems();
            }
        });
        call_admit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d_acnum_linear.setVisibility(View.GONE);
                bank_list.setVisibility(View.GONE);
                bc_acnum.setText(d_acnum.getText().toString());
                bank_check.setVisibility(View.VISIBLE);
                confirm_message.setVisibility(View.VISIBLE);
            }
        });
        card_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogItems();
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                ProfileFragment profileFragment = new ProfileFragment();
                transaction.replace(R.id.frame, profileFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });



        return view;
    }

    private void showAlertDialogItems() {
        String [] cards = new String[] {"국민은행","농협","신한은행","우리은행"};
        AlertDialog.Builder ad = new AlertDialog.Builder(getContext());

        ad.setTitle("은행사");
        ad.setItems(
                cards, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bank.setVisibility(View.GONE);
                        d_acnum_click.setVisibility(View.GONE);
                        d_acnum_linear.setVisibility(View.VISIBLE);
                        bank_name_select.setText(cards[which]);
                        bank_company.setText(cards[which]);
                    }
                });
        ad.setNeutralButton("닫기",null).show();
    }



}
