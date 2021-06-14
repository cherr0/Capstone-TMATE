package com.tmate.user.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.user.R;
import com.tmate.user.data.CardData;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PaymentAdapter extends  RecyclerView.Adapter<PaymentAdapter.PaymentHolder> {
    private Context context;
    static ArrayList<CardData> items = new ArrayList<>();
    private SparseBooleanArray selectedItems = new SparseBooleanArray(0);
    private static int prePosition = -1;
    private String sid;
    private PaymentHolder paymentHolder;


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
        int posit = holder.getAdapterPosition();
        holder.onBind(items.get(posit));
        // 이전 포지션과 현재 포지션 값이 같을 때
        if(prePosition == posit) {
            holder.payment_card_check.setChecked(true);
            prePosition = posit;
        }else {
            holder.payment_card_check.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public void addItem(CardData data) {
        items.add(data);
    }
    public class PaymentHolder extends RecyclerView.ViewHolder {
        ImageView payment_card_image;
        TextView payment_pay_alias;
        TextView pay_company;
        TextView pay_sid;
        CheckBox payment_card_check;
        private int position;

        void onBind(CardData data) {
            switch (data.getPay_company()) {
                case "대구":
                    payment_card_image.setImageResource(R.drawable.dgb_card);
                    break;
                case "하나":
                    payment_card_image.setImageResource(R.drawable.hana_card);
                    break;
                case "기업":
                    payment_card_image.setImageResource(R.drawable.ibk_card);
                    break;
                case "카카오뱅크":
                    payment_card_image.setImageResource(R.drawable.kakao_card);
                    break;
                case "국민":
                    payment_card_image.setImageResource(R.drawable.kb_card);
                    break;
                case "농협":
                    payment_card_image.setImageResource(R.drawable.nhbank);
                    break;
                case "제일":
                    payment_card_image.setImageResource(R.drawable.sc_card);
                    break;
                case "신한":
                    payment_card_image.setImageResource(R.drawable.sh_card);
                    break;
                case "우리":
                    payment_card_image.setImageResource(R.drawable.woribk);
                    break;
            }
            payment_pay_alias.setText(data.getPay_alias());
            pay_company.setText(data.getPay_company());
            pay_sid.setText(data.getSid());

            payment_card_image.setOnClickListener(v ->{
                position =getAdapterPosition();
                prePosition = position;
                payment_card_check.setChecked(true);

                if (payment_card_check.isChecked()) {
                    setPreference("sid", data.getSid());
                    Log.d("PaymentAdapter", "sid : " + getPreferenceString("sid"));
                }
                notifyDataSetChanged();
            });

        }
        public PaymentHolder(@NonNull View itemView) {
            super(itemView);
            payment_card_image = itemView.findViewById(R.id.payment_card_image);
            payment_pay_alias = itemView.findViewById(R.id.payment_pay_alias);
            pay_company = itemView.findViewById(R.id.pay_company);
            payment_card_check = itemView.findViewById(R.id.payment_card_check);
            pay_sid = itemView.findViewById(R.id.pay_sid);

        }
        // perf 저장
        public void setPreference(String key, String value){
            SharedPreferences pref = itemView.getContext().getSharedPreferences("loginUser", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(key, value);
            editor.apply();
        }

        // perf 가져오기
        public String getPreferenceString(String key) {
            SharedPreferences pref = itemView.getContext().getSharedPreferences("loginUser", Context.MODE_PRIVATE);
            return pref.getString(key, "");
        }


    }


}
