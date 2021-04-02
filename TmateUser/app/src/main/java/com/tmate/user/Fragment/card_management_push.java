package com.tmate.user.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.tmate.user.R;


public class card_management_push extends Fragment {

    Button cm_card;
    private View v;
    CheckBox c;
    LinearLayout corpDivision;
    LinearLayout bank;
    TextView bank_name_select;
    private ImageView btn_back_cardadd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.card_management_push, container, false);

        cm_card = v.findViewById(R.id.cm_card);
        corpDivision = v.findViewById(R.id.corp_division);
        c=v.findViewById(R.id.card_management_corp);


        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox)v).isChecked()) {
                    corpDivision.setVisibility(View.VISIBLE);
                } else {
                    corpDivision.setVisibility(View.GONE);
                }
            }
        });
        bank = v.findViewById(R.id.bank);
        bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogItems();
            }
        });



        cm_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(getContext());
                ad.setTitle("제목");
                ad.setMessage("카드 유효성 체크 안내");
                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        card_management cmd = new card_management();
                        transaction.replace(R.id.frameLayout, cmd).commit();
                    }
                });
                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();


    }
});

        btn_back_cardadd = v.findViewById(R.id.btn_back_cardadd);
        btn_back_cardadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                card_management card_management = new card_management();
                transaction.replace(R.id.frameLayout, card_management).commit();
            }
        });

        return v;
    }

    private void showAlertDialogItems() {
        String [] cards = new String[] {"국민은행","농협","신한은행","우리은행"};
        AlertDialog.Builder ad = new AlertDialog.Builder(getContext());

        bank_name_select = v.findViewById(R.id.bank_name_select);

        ad.setTitle("은행사");
        ad.setItems(
                cards, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bank_name_select.setText(cards[which]);
                    }
                });
        ad.setNeutralButton("닫기",null).show();
    }

}

