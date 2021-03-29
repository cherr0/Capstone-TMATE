package com.tmate.user.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.user.R;
import com.tmate.user.data.FriendData;

import java.util.ArrayList;

public class friendAdapter extends  RecyclerView.Adapter<friendHolder> {
    ArrayList<FriendData> items = new ArrayList<>();
    @NonNull
    @Override
    public friendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent, false);
        return new friendHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull friendHolder holder, int position) {
        holder.onBind(items.get(position));
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
    public void addItem(FriendData data) {
        items.add(data);
    }
}
class friendHolder extends RecyclerView.ViewHolder {
    ImageView iv_alert;
    ImageView iv_delete;
    TextView tv_name;
    TextView tv_phone;
    void onBind(FriendData data) {
        tv_name.setText(data.getTv_name());
        tv_phone.setText(data.getTv_phone());
    }
    public friendHolder(@NonNull View itemView) {
        super(itemView);
        iv_alert = itemView.findViewById(R.id.iv_alert);
        iv_delete = itemView.findViewById(R.id.iv_delete);
        tv_name = itemView.findViewById(R.id.tv_name);
        tv_phone = itemView.findViewById(R.id.tv_phone);
    }
}