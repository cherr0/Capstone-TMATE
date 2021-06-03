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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.user.R;
import com.tmate.user.data.Approval;
import com.tmate.user.data.TogetherRequest;
import com.tmate.user.net.DataService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TogetherRequestAdapter extends RecyclerView.Adapter<TogetherRequestHolder> {
    ArrayList<TogetherRequest> items = new ArrayList<>();
    Context context;
    SharedPreferences pref;
    String m_id;
    String m_id2;
    String dp_id;

    Call<Boolean> agreeApply;

    @NonNull
    @Override
    public TogetherRequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.together_request_item, parent, false);
        context = parent.getContext();
        pref = context.getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        m_id = pref.getString("m_id", "");

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
                                String id = holder.m_id.getText().toString();
                                String merchant_uid = holder.tr_merchant_uid.getText().toString();
                                DataService.getInstance().matchAPI.removeApproval(id, merchant_uid).enqueue(new Callback<Boolean>() {
                                    @Override
                                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                        if (response.code() == 200) {
                                            items.remove(position);
                                            notifyItemRemoved(position);
                                            notifyItemChanged(position, items.size());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Boolean> call, Throwable t) {
                                        t.printStackTrace();
                                    }
                                });

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

                                m_id2 = holder.m_id.getText().toString();
                                dp_id = holder.tr_merchant_uid.getText().toString();

                                agreeApply = DataService.getInstance().matchAPI.agreeApplyMatch(dp_id, m_id2);
                                agreeApply.enqueue(new Callback<Boolean>() {
                                    @Override
                                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                        if (response.code() == 200) {
                                            items.remove(position);
                                            notifyItemRemoved(position);
                                            notifyItemChanged(position, items.size());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Boolean> call, Throwable t) {
                                        t.printStackTrace();
                                    }
                                });


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
    TextView m_t_use;
    Button btn_agree;
    Button btn_reject;
    TextView tr_merchant_uid;

    public void onBind(TogetherRequest data) {
        m_id.setText(data.getM_id());
        m_name.setText(data.getM_name());
        m_birth.setText("20대");
        m_t_use.setText(data.getM_t_use() + data.getM_n_use()+"");
        tr_merchant_uid.setText(data.getDp_id());
    }

    public TogetherRequestHolder(@NonNull View itemView) {
        super(itemView);
        m_id = itemView.findViewById(R.id.request_m_id);
        m_name =itemView.findViewById(R.id.request_m_name);
        m_birth =itemView.findViewById(R.id.request_m_birth);
        m_t_use =itemView.findViewById(R.id.request_m_t_use);
        btn_agree = itemView.findViewById(R.id.request_btn_agree);
        btn_reject = itemView.findViewById(R.id.request_btn_reject);
        tr_merchant_uid = itemView.findViewById(R.id.tr_merchant_uid);
    }
}