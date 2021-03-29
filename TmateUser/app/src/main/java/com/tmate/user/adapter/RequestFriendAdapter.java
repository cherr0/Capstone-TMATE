package com.tmate.user.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.user.R;
import com.tmate.user.data.RequestFriendData;


import java.util.ArrayList;

public class RequestFriendAdapter extends  RecyclerView.Adapter<requestFriendHolder> {
    ArrayList<RequestFriendData> items = new ArrayList<>();

    @NonNull
    @Override
    public requestFriendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_request_item, parent, false);
        return new requestFriendHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull requestFriendHolder holder, int position) {
        holder.onBind(items.get(position));
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
    void onBind(RequestFriendData data) {
        tv_name2.setText(data.getTv_name2());
        tv_phone2.setText(data.getTv_phone2());
    }
    public requestFriendHolder(@NonNull View itemView) {
        super(itemView);
        btn_ok = itemView.findViewById(R.id.btn_ok);
        btn_no = itemView.findViewById(R.id.btn_no);
        tv_name2 = itemView.findViewById(R.id.tv_name2);
        tv_phone2 = itemView.findViewById(R.id.tv_phone2);
    }
}