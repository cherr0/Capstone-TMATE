package com.tmate.user;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoticeAdapter extends  RecyclerView.Adapter<NoticeHolder>{

    ArrayList<NoticeData> items = new ArrayList<>();



    @NonNull
    @Override
    public NoticeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_notice_item, parent, false);
        return new NoticeHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull NoticeHolder holder, int position) {
        holder.onBind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    public void addItem(NoticeData data) {
        items.add(data);
    }



}

class NoticeHolder extends RecyclerView.ViewHolder {
    TextView notice_title;
    TextView notice_date;

    void onBind(NoticeData data) {
        notice_title.setText(data.getNotice_title());
        notice_date.setText(data.getNotice_date());
    }

    public NoticeHolder(@NonNull View itemView) {
        super(itemView);
        notice_title = itemView.findViewById(R.id.notice_title);
        notice_date = itemView.findViewById(R.id.notice_date);
    }
}
