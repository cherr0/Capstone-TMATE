package com.tmate.user;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.CustomViewHolder> {

    private ArrayList<EventData> arrayList;

    public EventAdapter(ArrayList<EventData> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public EventAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_event_item, parent, false);
        EventAdapter.CustomViewHolder holder = new EventAdapter.CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.CustomViewHolder holder, int position) {
        holder.iv_event.setImageResource(arrayList.get(position).getIv_event());
        holder.tv_title.setText(arrayList.get(position).getTv_title());
        holder.tv_subTitle.setText(arrayList.get(position).getTv_subTitle());
        holder.tv_date.setText(arrayList.get(position).getTv_date());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EventDetailActivity.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public void addItem(EventData eventData) {
        arrayList.add(eventData);
    }

    public void addItem(EventAdapter eventAdapter) {
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected ImageView iv_event;
        protected TextView tv_title;
        protected TextView tv_date;
        protected TextView tv_subTitle;


        public CustomViewHolder(View itemView) {
            super(itemView);

            this.iv_event = (ImageView) itemView.findViewById(R.id.iv_event);
            this.tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            this.tv_subTitle = (TextView) itemView.findViewById(R.id.tv_subTitle);
            this.tv_date = (TextView) itemView.findViewById(R.id.tv_date);
        }
    }
}