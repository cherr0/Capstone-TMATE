package com.tmate.driver.adapter;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.driver.Fragment.HistoryFragment;
import com.tmate.driver.R;
import com.tmate.driver.data.HistoryData;
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
    public void onBindViewHolder(@NonNull HistoryHolder holder, int position) {
        holder.onBind(items.get(position));


        holder.black_list_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] items = new String[]{"너무 시끄러워요", "시간을 안지켜요", "술을 마신거 같아요", "목적지변경을 강요해요", "불친절해요."};
                AlertDialog.Builder dialog = new AlertDialog.Builder(holder.black_list_submit.getContext());
                dialog.setTitle("선택");
                dialog.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(holder.black_list_submit.getContext(), "words : " + items[which], Toast.LENGTH_SHORT).show();
                    }
                }).setPositiveButton("보내기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.create();
                dialog.show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
    public void addItem(HistoryData data) {
        items.add(data);
    }
}
class HistoryHolder extends RecyclerView.ViewHolder {
    TextView hdate;
    TextView htogether;
    TextView re_amt;
    TextView history_username;
    TextView hstart;
    TextView hfinish;
    TextView htime;
    Button black_list_submit;
    void onBind(HistoryData data) {
        hdate.setText(data.getHdate());
        htogether.setText(data.getHtogether());
        re_amt.setText(data.getRe_amt());
        history_username.setText(data.getHistory_username());
        hstart.setText(data.getHstart());
        hfinish.setText(data.getHfinish());
        htime.setText(data.getHtime());
    }
    public HistoryHolder(@NonNull View itemView) {
        super(itemView);
        hdate = itemView.findViewById(R.id.hdate);
        htogether = itemView.findViewById(R.id.htogether);
        re_amt = itemView.findViewById(R.id.re_amt);
        history_username = itemView.findViewById(R.id.history_username);
        hstart = itemView.findViewById(R.id.hstart);
        hfinish = itemView.findViewById(R.id.hfinish);
        htime = itemView.findViewById(R.id.htime);
        black_list_submit = itemView.findViewById(R.id.black_list_submit);
    }
}