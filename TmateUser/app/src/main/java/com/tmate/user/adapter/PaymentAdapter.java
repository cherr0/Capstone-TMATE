package com.tmate.user.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.user.R;
import com.tmate.user.data.CardData;
import com.tmate.user.data.Notification;
import com.tmate.user.net.DataService;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PaymentAdapter extends  RecyclerView.Adapter<PaymentHolder> {
    private Context context;
    ArrayList<CardData> items = new ArrayList<>();


    public PaymentAdapter(ArrayList<CardData> cardArrayList) {

    }

    @NonNull
    @Override
    public PaymentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_card, parent, false);
        context = parent.getContext();

        return new PaymentHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull PaymentHolder holder, int position) {
        holder.onBind(items.get(position));
        holder.payment_card_image.setOnClickListener(v -> {
            Toast.makeText(context, "포지션" + position, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public void addItem(CardData data) {
        items.add(data);
    }



}
class PaymentHolder extends RecyclerView.ViewHolder {
    ImageView payment_card_image;
    TextView payment_pay_alias;
    TextView pay_company;
    void onBind(CardData data) {
        switch (data.getPay_company()) {
            case "DG":
                payment_card_image.setImageResource(R.drawable.dgb_card);
                break;
            case "HANA":
                payment_card_image.setImageResource(R.drawable.hana_card);
                break;
            case "IBK":
                payment_card_image.setImageResource(R.drawable.ibk_card);
                break;
            case "KAKAOBANK":
                payment_card_image.setImageResource(R.drawable.kakao_card);
                break;
            case "KB":
                payment_card_image.setImageResource(R.drawable.kb_card);
                break;
            case "NH":
                payment_card_image.setImageResource(R.drawable.nh_card);
                break;
            case "SC":
                payment_card_image.setImageResource(R.drawable.sc_card);
                break;
            case "SHINHAN":
                payment_card_image.setImageResource(R.drawable.sh_card);
                break;
            case "WOORI":
                payment_card_image.setImageResource(R.drawable.woori_card);
                break;
        }
        payment_pay_alias.setText(data.getPay_alias());
        pay_company.setText(data.getPay_company());
    }
    public PaymentHolder(@NonNull View itemView) {
        super(itemView);
        payment_card_image = itemView.findViewById(R.id.payment_card_image);
        payment_pay_alias = itemView.findViewById(R.id.payment_pay_alias);
        pay_company = itemView.findViewById(R.id.pay_company);

    }
}