package com.tmate.driver;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.CustomViewHolder> {

    private ArrayList<NoticeData> arrayList;

    public NoticeAdapter(ArrayList<NoticeData> arrayList){this.arrayList = arrayList;}

    @NonNull
    @Override
    public NoticeAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_notice_item, parent, false);
        NoticeAdapter.CustomViewHolder holder = new NoticeAdapter.CustomViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public void addItem(NoticeData noticeData) {arrayList.add(noticeData);}

    public void addItem(NoticeAdapter noticeAdapter){

    }

    @Override
    public void onBindViewHolder(@NonNull NoticeAdapter.CustomViewHolder holder, int position) {
        holder.notice_title.setText(arrayList.get(position).getNotice_title());
        holder.notice_date.setText(arrayList.get(position).getNotice_date());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NoticeDetailActivity.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{

        protected TextView notice_title;
        protected TextView notice_date;

        public CustomViewHolder(View itemView) {
            super(itemView);

            this.notice_title = (TextView) itemView.findViewById(R.id.notice_title);
            this.notice_date = (TextView) itemView.findViewById(R.id.notice_date);
        }
    }
}

