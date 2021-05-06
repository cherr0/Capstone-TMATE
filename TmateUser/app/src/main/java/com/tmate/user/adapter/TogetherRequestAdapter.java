package com.tmate.user.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.user.R;
import com.tmate.user.data.TogetherRequest;

import java.util.ArrayList;

public class TogetherRequestAdapter extends RecyclerView.Adapter<TogetherRequestHolder> {
    ArrayList<TogetherRequest> items = new ArrayList<>();
    
    @NonNull
    @Override
    public TogetherRequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.together_request_item, parent, false);
        return new TogetherRequestHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TogetherRequestHolder holder, int position) {
        holder.onBind(items.get(position));

        //거절버튼을 눌렀을때
        holder.btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(holder.btn_reject.getContext());
                builder.setTitle("거절");
                builder.setMessage("정말 거절 하시겠습니까?");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                items.remove(position);
                                notifyItemRemoved(position);
                                notifyItemChanged(position, items.size());
                            }
                        });
                builder.setNegativeButton("아니요",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.show();
            }
        });

        //  수락버튼을 눌렀을때
        holder.btn_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(holder.btn_reject.getContext());
                builder.setTitle("수락");
                builder.setMessage("정말 수락 하시겠습니까?");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                items.remove(position);
                                notifyItemRemoved(position);
                                notifyItemChanged(position, items.size());
                            }
                        });
                builder.setNegativeButton("아니요",
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



    public void addItem(TogetherRequest data) {
        items.add(data);
    }
}

class TogetherRequestHolder extends RecyclerView.ViewHolder {
    TextView m_id;
    TextView m_name;
    TextView m_birth;
    TextView distance;
    TextView m_t_use;
    TextView like;
    TextView dislike;
    TextView m_count;
    Button btn_agree;
    Button btn_reject;
    TextView tr_merchant_uid;

    public void onBind(TogetherRequest data) {
        m_id.setText(data.getM_id());
        m_name.setText(data.getM_name());
        m_birth.setText(data.getM_birth().toString());
        distance.setText(data.getDistance());
        m_t_use.setText(data.getM_t_use());
        like.setText(data.getLike());
        dislike.setText(data.getDislike());
        m_count.setText(data.getM_count());
        tr_merchant_uid.setText(data.getMerchant_uid());
    }

    public TogetherRequestHolder(@NonNull View itemView) {
        super(itemView);
        m_id = itemView.findViewById(R.id.request_m_id);
        m_name =itemView.findViewById(R.id.request_m_name);
        m_birth =itemView.findViewById(R.id.request_m_birth);
        distance =itemView.findViewById(R.id.request_distance);
        m_t_use =itemView.findViewById(R.id.request_m_t_use);
        like =itemView.findViewById(R.id.request_r_code_like);
        dislike =itemView.findViewById(R.id.request_r_code_dislike);
        btn_agree = itemView.findViewById(R.id.request_btn_agree);
        btn_reject = itemView.findViewById(R.id.request_btn_reject);
        m_count = itemView.findViewById(R.id.request_m_count);
        tr_merchant_uid = itemView.findViewById(R.id.tr_merchant_uid);
    }
}