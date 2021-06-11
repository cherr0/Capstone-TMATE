package com.tmate.user.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.user.R;
import com.tmate.user.data.FriendData;
import com.tmate.user.data.Notification;
import com.tmate.user.net.DataService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class friendAdapter extends  RecyclerView.Adapter<friendHolder> {

    // 레트로핏
//    AdapterDataService dataService = new AdapterDataService();
    DataService dataService = DataService.getInstance();
    Call<Boolean> removeFriendRequest;

    // SharedPrefreprences
    private Context context;
    private static SharedPreferences pref;
    String m_id;

    // 알림 활성화를 위한 객체 생성
    Notification notification;

    ArrayList<FriendData> items = new ArrayList<>();
    @NonNull
    @Override
    public friendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent, false);
        context = parent.getContext();
        pref = context.getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        m_id = pref.getString("m_id", "");
        return new friendHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull friendHolder holder, int position) {
        holder.onBind(items.get(position));

        notification = new Notification();
        notification.setM_id(m_id);

        holder.iv_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(holder.iv_alert.getContext());
                builder.setTitle("알림 활성화");
                builder.setMessage("해당 지인 알림 활성화 하시겠습니까?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notification.setN_name(items.get(position).getTv_name());
                        notification.setN_phone(items.get(position).getTv_phone());
                        notification.setN_whether("1");
                        Log.d("활성화", notification.toString());
                            dataService.memberAPI.modifyStat(notification).enqueue(new Callback<Boolean>() {
                                @Override
                                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                    if (response.isSuccessful()) {
                                        if (response.code() == 200) {
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

                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });

        holder.iv_disalert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(holder.iv_delete.getContext());
                builder.setTitle("알림 비활성화");
                builder.setMessage("해당 지인 알림 비활성화 하시겠습니까?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notification.setN_name(items.get(position).getTv_name());
                        notification.setN_phone(items.get(position).getTv_phone());
                        notification.setN_whether("0");
                        Log.d("비활성화", notification.toString());
                        dataService.memberAPI.modifyStat(notification).enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                notifyItemRangeChanged(position, items.size());
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
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(holder.iv_delete.getContext());
                builder.setTitle("지인 삭제");
                builder.setMessage("해당 지인 알림 삭제 하시겠습니까?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //지인삭제
                        String n_name = items.get(position).getTv_name();
                        removeFriendRequest = DataService.getInstance().memberAPI.removeFriend(m_id, n_name);
                        removeFriendRequest.enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                if (response.code() == 200) {
                                    Toast.makeText(context, "지인이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
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

    }
    @Override
    public int getItemCount() {
        return items.size();
    }
    public void addItem(FriendData data) {
        items.add(data);
    }
    public void clear() {
        items.clear();
    }

}
class friendHolder extends RecyclerView.ViewHolder {
    ImageView iv_alert;
    ImageView iv_delete;
    ImageView iv_disalert;
    TextView tv_name;
    TextView tv_phone;
    TextView tv_flag;
    void onBind(FriendData data) {
        tv_flag.setText(data.getTv_flag());
        tv_name.setText(data.getTv_name());
        tv_phone.setText(data.getTv_phone());
    }
    public friendHolder(@NonNull View itemView) {
        super(itemView);
        iv_alert = itemView.findViewById(R.id.iv_alert);
        iv_delete = itemView.findViewById(R.id.iv_delete);
        tv_name = itemView.findViewById(R.id.tv_name);
        tv_phone = itemView.findViewById(R.id.tv_phone);
        tv_flag = itemView.findViewById(R.id.tv_flag);
        iv_disalert = itemView.findViewById(R.id.iv_disalert);
    }
}