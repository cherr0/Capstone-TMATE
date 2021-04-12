package com.tmate.user.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.user.R;
import com.tmate.user.data.Data;

import java.util.ArrayList;

public class historyAdapter extends RecyclerView.Adapter<HistoryHolder> {
    ArrayList<Data> items = new ArrayList<>();



    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new HistoryHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull HistoryHolder holder, int position) {
        holder.onBind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    public void addItem(Data data) {
        items.add(data);
    }



}

class HistoryHolder extends RecyclerView.ViewHolder {
    TextView together;
    TextView start;
    TextView finish;
    TextView date;
    TextView time;
    TextView drivername;
    TextView carinfo;

    public HistoryHolder(@NonNull View itemView) {
        super(itemView);
        start = itemView.findViewById(R.id.hstart);
        together = itemView.findViewById(R.id.htogether);
        date = itemView.findViewById(R.id.hdate);
        finish = itemView.findViewById(R.id.hfinish);
        time = itemView.findViewById(R.id.htime);
        drivername = itemView.findViewById(R.id.hdrivername);
        carinfo = itemView.findViewById(R.id.hcarinfo);
    }

    void onBind(Data data) {
        start.setText(data.getStart());
        together.setText(data.getTogether());
        date.setText(data.getDate());
        finish.setText(data.getFinish());
        time.setText(data.getTime());
        drivername.setText(data.getDrivername());
        carinfo.setText(data.getCarinfo());
    }
}