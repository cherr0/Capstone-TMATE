package com.tmate.user.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.tmate.user.R;
import com.tmate.user.data.CardData;
import com.tmate.user.net.DataService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class card_management_push extends Fragment {

    Button cm_card;
    private View v;
    CheckBox c;
    LinearLayout corpDivision;
    LinearLayout bank;

    TextView bank_name_select;
    EditText cardnum1, cardnum2, cardnum3, cardnum4 , use_date_num1, use_date_num2, card_password, card_cvc;

    // 레트로핏 카드 추가
    DataService dataService = DataService.getInstance();

    // SharedPreferences
    Context context;
    private static SharedPreferences pref;
    private  String m_id;

    private ImageView btn_back_cardadd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.card_management_push, container, false);

        context = container.getContext();
        pref = context.getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        m_id = pref.getString("m_id", "");

        cm_card = v.findViewById(R.id.cm_card);
        corpDivision = v.findViewById(R.id.corp_division);

        cardnum1 = (EditText) v.findViewById(R.id.cardnum1);
        cardnum2 = (EditText) v.findViewById(R.id.cardnum2);
        cardnum3 = (EditText) v.findViewById(R.id.cardnum3);
        cardnum4 = (EditText) v.findViewById(R.id.cardnum4);

        use_date_num1 = (EditText) v.findViewById(R.id.use_date_num1);
        use_date_num2 = (EditText) v.findViewById(R.id.use_date_num2);

        card_password = (EditText) v.findViewById(R.id.card_password);

        card_cvc = (EditText) v.findViewById(R.id.card_cvc);

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


                        CardData cardData = new CardData();
                        cardData.setCredit_cvc(card_cvc.getText().toString());
                        cardData.setCredit_no(cardnum1.getText().toString()+cardnum2.getText().toString()+cardnum3.getText().toString()+cardnum4.getText().toString());
                        cardData.setCredit_pw(card_password.getText().toString());
                        cardData.setPay_company(bank_name_select.getText().toString());
                        cardData.setCredit_vali(use_date_num1.getText().toString() + "/" + use_date_num2.getText().toString());
                        cardData.setM_id(m_id);

                        dataService.memberAPI.registerCard(cardData).enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                if (response.isSuccessful()) {
                                    if (response.code() == 200) {

                                        Log.d("등록한 카드정보 ", cardData.toString());

                                        dialog.dismiss();
                                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                        card_management cmd = new card_management();
                                        transaction.replace(R.id.frameLayout, cmd).commit();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });


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
        String [] cards = new String[] {"국민","농협","신한","우리"};
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

