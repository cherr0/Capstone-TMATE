package com.tmate.user.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.user.R;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryHolder> {
    ArrayList<HistoryData> items = new ArrayList<>();



    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull HistoryHolder historyHolder, int position) {
        historyHolder.onBind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    void addItem(HistoryData historyData) {
        items.add(historyData);
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

    void onBind(HistoryData historyData) {
        start.setText(historyData.getStart());
        together.setText(historyData.getTogether());
        date.setText(historyData.getDate());
        finish.setText(historyData.getFinish());
        time.setText(historyData.getTime());
        drivername.setText(historyData.getDrivername());
        carinfo.setText(historyData.getCarinfo());
    }

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
}