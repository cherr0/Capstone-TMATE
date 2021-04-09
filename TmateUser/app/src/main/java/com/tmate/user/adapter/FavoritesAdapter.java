package com.tmate.user.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.user.data.FavoritesData;
import com.tmate.user.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.CustomViewHolder> {

    private ArrayList<FavoritesData> arrayList;

    // 레트로핏 어뎁터 서비스
    AdapterComService dataService = new AdapterComService();

    // m_id 값 불러오기
    Context context;
    private static SharedPreferences pref;
    String m_id;

    public FavoritesAdapter(ArrayList<FavoritesData> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public FavoritesAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorites_item_list,parent,false);
        context = parent.getContext();
        pref = context.getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        m_id = pref.getString("m_id", "");
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesAdapter.CustomViewHolder holder, int position) {

        try {
            if(arrayList != null && arrayList.size() != 0) {
                holder.bm_name.setText(arrayList.get(position).getBm_name());
                holder.bm_date.setText(arrayList.get(position).getBm_date().substring(0, 10));
                holder.bm_id.setText(arrayList.get(position).getBm_id());
                holder.bm_lttd.setText(arrayList.get(position).getBm_lttd() + "");
                holder.bm_lngtd.setText(arrayList.get(position).getBm_lngtd() + "");
                holder.favorites_linear.setVisibility(View.VISIBLE);
            }
        } catch (NullPointerException e) {
            Log.e("아무것도 안옴", e.toString());
        }



        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String curName = holder.bm_name.getText().toString();
                Toast.makeText(v.getContext(), curName, Toast.LENGTH_SHORT).show();
            }
        });


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setTitle("즐겨찾기");
                builder.setMessage("삭제하시겠습니까");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dataService.deleteAPI.removeBookmark(holder.bm_id.getText().toString(),m_id).enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                if (response.isSuccessful()) {
                                    if (response.code() == 200) {
                                        remove(holder.getAdapterPosition());
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
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public void remove(int position) {
        try {
            arrayList.remove(position);
            notifyItemRemoved(position);
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }

    public void addItem(FavoritesData data) {
        arrayList.add(data);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected LinearLayout favorites_linear;
        protected TextView bm_name;
        protected TextView bm_date;
        protected TextView bm_id;
        protected TextView bm_lttd;
        protected TextView bm_lngtd;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.favorites_linear = (LinearLayout) itemView.findViewById(R.id.favorites_linear);
            this.bm_name = (TextView) itemView.findViewById(R.id.bm_name);
            this.bm_date = (TextView) itemView.findViewById(R.id.bm_date);
            this.bm_id = (TextView) itemView.findViewById(R.id.bm_id);
            this.bm_lttd = (TextView) itemView.findViewById(R.id.bm_lttd);
            this.bm_lngtd = (TextView) itemView.findViewById(R.id.bm_lngtd);


        }
    }
}
