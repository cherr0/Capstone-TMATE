package com.tmate.driver;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.driver.databinding.HistoryCardviewBinding;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<Holder> {
    ArrayList<HistoryData> items = new ArrayList<>();

    private HistoryCardviewBinding binding;

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_cardview, parent, false);
        return new Holder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.onBind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    void addItem(HistoryData data) {
        items.add(data);
    }



}

class Holder extends RecyclerView.ViewHolder {
    TextView hdate;
    TextView htogether;
    TextView re_amt;
    TextView history_username;
    TextView hstart;
    TextView hfinish;
    TextView htime;

    void onBind(HistoryData data) {
        hdate.setText(data.getHdate());
        htogether.setText(data.getHtogether());
        re_amt.setText(data.getRe_amt());
        history_username.setText(data.getHistory_username());
        hstart.setText(data.getHstart());
        hfinish.setText(data.getHfinish());
        htime.setText(data.getHtime());
    }

    public Holder(@NonNull View itemView) {
        super(itemView);
        hdate = itemView.findViewById(R.id.hdate);
        htogether = itemView.findViewById(R.id.htogether);
        re_amt = itemView.findViewById(R.id.re_amt);
        history_username = itemView.findViewById(R.id.history_username);
        hstart = itemView.findViewById(R.id.hstart);
        hfinish = itemView.findViewById(R.id.hfinish);
        htime = itemView.findViewById(R.id.htime);
    }
}