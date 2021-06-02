package com.tmate.user.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.user.R;
import com.tmate.user.data.CardData;
import com.tmate.user.data.InactiveRes;
import com.tmate.user.net.DataService;
import com.tmate.user.net.KakaoService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardAdapter extends RecyclerView.Adapter<CardHolder> {

    // m_id 얻기 위함
    Context context;
    private static SharedPreferences pref;
    private String m_id;
    final String auth = "KakaoAK e24eec29f82748733f7a2be2de93c236";

    CardData card;

    // 레트로핏 연동
//    public Call<Boolean> selectCardRequest;
//    public Call<Boolean> deleteCardRequest;
    public Call<InactiveRes> kakaoInactiveRequest;
    DataService dataService = DataService.getInstance();

    ArrayList<CardData> items = new ArrayList<>();
    public int r = 0;

    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list, parent, false);
        context = parent.getContext();
        pref = context.getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        m_id = pref.getString("m_id", "");


        return new CardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, int position) {
        int posit = holder.getAdapterPosition();
        holder.onBind(items.get(posit));
        holder.active.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) { //on

                } else { //off

                }
            }
        });



    }

    private void inactiveCard(String sid, int position) {
        Map<String, String> map = new HashMap<>();
        map.put("cid", "TCSUBSCRIP");
        map.put("sid", sid);

        kakaoInactiveRequest = KakaoService.getInstance().getApi().kakaoInactive(auth, map);
        kakaoInactiveRequest.enqueue(new Callback<InactiveRes>() {
            @Override
            public void onResponse(Call<InactiveRes> call, Response<InactiveRes> response) {
                if (response.code() == 200 && response.body() != null) {
                    InactiveRes result = response.body();
                    Log.d("CardAdapter", "카카오페이 정기 결제 비활성화 완료 : " + result);
                } else {
                    try {
                        Log.d("CardAdapter", "에러 : " + response);
                        if (response.errorBody() != null)
                            Log.d("CardAdapter", "데이터 삽입 실패 : " +
                                    response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<InactiveRes> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(CardData data) {
        items.add(data);
    }

    public void clear() {
        items.clear();
    }
}

class CardHolder extends RecyclerView.ViewHolder {
    ImageView bank_mark;
    TextView bank_name;
    TextView card_no;
    TextView card_rno;
    TextView sid;
    TextView card_rep;

    //TextView card_rep;
    Switch active;

    void onBind(CardData data) {
        switch (data.getPay_company()) {
            case "국민":
                bank_mark.setImageResource(R.drawable.kb);
                break;
            case "농협":
                bank_mark.setImageResource(R.drawable.nh);
                break;
            case "신한":
                bank_mark.setImageResource(R.drawable.sinhan);
                break;
            case "우리":
                bank_mark.setImageResource(R.drawable.woori);
                break;
        }
        bank_name.setText(data.getPay_company());
        card_no.setText("**** **** **** ");
        card_rno.setText(data.getCredit_no());
        sid.setText(data.getSid());
        switch (data.getActive()) {
            case "0":
                active.setChecked(false);
                break;
            case "1":
                active.setChecked(true);
                break;
        }
        if(data.getPay_alias() != null) card_rep.setText(data.getPay_alias());

    }

    public CardHolder(@NonNull View itemView) {
        super(itemView);
        bank_mark = (ImageView) itemView.findViewById(R.id.bank_mark);
        bank_name = (TextView) itemView.findViewById(R.id.bank_name);
        card_no = (TextView) itemView.findViewById(R.id.card_no);
        card_rno = (TextView) itemView.findViewById(R.id.card_rno);
        sid = (TextView) itemView.findViewById(R.id.sid);
        card_rep = (TextView) itemView.findViewById(R.id.card_rep);
        active = (Switch) itemView.findViewById(R.id.active);
    }


}