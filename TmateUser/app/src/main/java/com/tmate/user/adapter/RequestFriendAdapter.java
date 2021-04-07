package com.tmate.user.adapter;

import android.app.AlertDialog;
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
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.user.R;
import com.tmate.user.data.Notification;
import com.tmate.user.data.RequestFriendData;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.PendingIntent.getActivity;

public class RequestFriendAdapter extends  RecyclerView.Adapter<requestFriendHolder> {
    private Context context;
    ArrayList<RequestFriendData> items = new ArrayList<>();
    AdapterDataService dataService = new AdapterDataService();
    Notification notification = new Notification();
    String m_id;
    private static SharedPreferences pref ;



    @NonNull
    @Override
    public requestFriendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_request_item, parent, false);
        context = parent.getContext();
        pref = context.getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        m_id = pref.getString("m_id", "");
        return new requestFriendHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull requestFriendHolder holder, int position) {
        holder.onBind(items.get(position));

        holder.btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(holder.btn_ok.getContext());
                builder.setTitle("승인");
                builder.setMessage("지인을 승인하시겠습니까");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                     notification.setM_id(m_id);
                                     notification.setN_name(items.get(position).getTv_name2());
                                     notification.setN_phone(items.get(position).getTv_phone2());
                                     dataService.update.agreeAppro(items.get(position).getTv_id(),notification).enqueue(new Callback<Boolean>() {
                                         @Override
                                         public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                             if (response.isSuccessful()) {
                                                 if (response.code() == 200) {
                                                     items.remove(position);
                                                     notifyItemRemoved(position);
                                                     notifyItemRangeChanged(position,items.size());
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
                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });

        holder.btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(holder.btn_no.getContext());
                builder.setTitle("거절");
                builder.setMessage("해당 지인 요청을 거절하시겠습니까?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dataService.delete.removeApproval(items.get(position).getTv_id(), pref.getString("m_id", "")).enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                if (response.isSuccessful()) {
                                    if (response.code() == 200) {
                                        items.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position,items.size());
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
        });
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
    public void addItem(RequestFriendData data) {
        items.add(data);
    }



}
class requestFriendHolder extends RecyclerView.ViewHolder {
    Button btn_ok;
    Button btn_no;
    TextView tv_name2;
    TextView tv_phone2;
    TextView tv_id;
    void onBind(RequestFriendData data) {
        tv_name2.setText(data.getTv_name2());
        tv_phone2.setText(data.getTv_phone2());
        tv_id.setText(data.getTv_id());
    }
    public requestFriendHolder(@NonNull View itemView) {
        super(itemView);
        btn_ok = itemView.findViewById(R.id.btn_ok);
        btn_no = itemView.findViewById(R.id.btn_no);
        tv_name2 = itemView.findViewById(R.id.tv_name2);
        tv_phone2 = itemView.findViewById(R.id.tv_phone2);
        tv_id = itemView.findViewById(R.id.tv_id);
    }
}