package com.tmate.user.adapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.tmate.user.R;
import com.tmate.user.data.CardData;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardAdapter extends  RecyclerView.Adapter<CardHolder>{

    // m_id 얻기 위함
    Context context;
    private static SharedPreferences pref;
    private String m_id;

    // 레트로핏 연동
    AdapterDataService adapterDataService = new AdapterDataService();


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
        holder.onBind(items.get(position));

        holder.card_rep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.card_rep.getText().equals("지정")) {
                    Toast.makeText(context.getApplicationContext(), "이미 지정된 카드입니다.", Toast.LENGTH_SHORT).show();
                }else{
                    final AlertDialog.Builder builder = new AlertDialog.Builder(holder.card_rep.getContext());
                    builder.setTitle("메인카드 지정");
                    builder.setMessage("대표카드로 지정하시겠습니까?");
                    builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapterDataService.crudAPI.modifyRep(holder.customer_uid.getText().toString(), m_id).enqueue(new Callback<Boolean>() {
                                @Override
                                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                    if (response.isSuccessful()) {
                                        if (response.code() == 200) {
                                            Toast.makeText(context.getApplicationContext(), "메인카드 지정하셨습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<Boolean> call, Throwable t) {

                                }
                            });
                        }
                    });
                    builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            }
        });

        holder.cardDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(holder.cardDelete.getContext());
                builder.setTitle("삭제");
                builder.setMessage("해당 항목을 삭제하시겠습니까?");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                adapterDataService.crudAPI.removeCard(holder.customer_uid.getText().toString()).enqueue(new Callback<Boolean>() {
                                    @Override
                                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                        if (response.isSuccessful()) {
                                            if (response.code() == 200) {
                                                items.remove(position);
                                                notifyItemRemoved(position);
                                                notifyItemRangeChanged(position, items.size());
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
                builder.setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.show();
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
}
class CardHolder extends RecyclerView.ViewHolder {
    ImageView bank_mark;
    TextView bank_name;
    TextView card_no;
    TextView card_rno;
    TextView customer_uid;
    Button cardDelete;
    TextView card_rep;

    void onBind(CardData data) {
        switch (data.getPay_company()){
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
        customer_uid.setText(data.getCustomer_uid());
        switch (data.getPay_rep()) {
            case "0":
                card_rep.setText("미지정");
                break;
            case "1":
                card_rep.setText("지정");
                break;
        }

    }
    public CardHolder(@NonNull View itemView) {
        super(itemView);
        bank_mark = (ImageView) itemView.findViewById(R.id.bank_mark);
        bank_name= (TextView) itemView.findViewById(R.id.bank_name);
        cardDelete = (Button) itemView.findViewById(R.id.deleteCard);
        card_no = (TextView) itemView.findViewById(R.id.card_no);
        card_rno = (TextView) itemView.findViewById(R.id.card_rno);
        customer_uid = (TextView) itemView.findViewById(R.id.customer_uid);
        card_rep = (TextView) itemView.findViewById(R.id.card_rep);
    }
}