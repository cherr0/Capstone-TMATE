package com.tmate.user;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventAdapter extends  RecyclerView.Adapter<eventHolder> {

    ArrayList<EventData> items = new ArrayList<>();



    @NonNull
    @Override
    public eventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_event_item, parent, false);
        return new eventHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull eventHolder holder, int position) {
        holder.onBind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    public void addItem(EventData data) {
        items.add(data);
    }



}

class eventHolder extends RecyclerView.ViewHolder {
    ImageView iv_event;
    TextView tv_event;
    TextView tv_subTitle;
    TextView tv_date;

    void onBind(EventData data) {
        iv_event.setImageResource(data.getIv_event());
        tv_event.setText(data.getTv_title());
        tv_subTitle.setText(data.getTv_subTitle());
        tv_date.setText(data.getTv_date());
    }

    public eventHolder(@NonNull View itemView) {
        super(itemView);
        iv_event = itemView.findViewById(R.id.iv_event);
        tv_event = itemView.findViewById(R.id.tv_title);
        tv_subTitle = itemView.findViewById(R.id.tv_subTitle);
        tv_date = itemView.findViewById(R.id.tv_date);
    }
}
