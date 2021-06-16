package com.tmate.user.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.user.Fragment.ReviewFragment;
import com.tmate.user.R;
import com.tmate.user.data.Data;
import com.tmate.user.data.Dispatch;
import com.tmate.user.net.DataService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class historyAdapter extends RecyclerView.Adapter<HistoryHolder> {
    ArrayList<Dispatch> items = new ArrayList<>();

    Context context;
    SharedPreferences pref;
    String m_id;
    Dialog dialog;

    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);

        context = parent.getContext();
        pref = context.getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        m_id = pref.getString("m_id", "");

        return new HistoryHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull HistoryHolder holder, int position) {
        int posit = holder.getAdapterPosition();
        holder.onBind(items.get(posit));

        holder.itemView.setTag(posit);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public void addItem(Dispatch data) {
        items.add(data);
    }

    public void clear() {
        items.clear();
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
    TextView cv_merchant_uid;

    public HistoryHolder(@NonNull View itemView) {
        super(itemView);
        start = itemView.findViewById(R.id.hstart);
        together = itemView.findViewById(R.id.htogether);
        date = itemView.findViewById(R.id.hdate);
        finish = itemView.findViewById(R.id.hfinish);
        time = itemView.findViewById(R.id.htime);
        drivername = itemView.findViewById(R.id.hdrivername);
        carinfo = itemView.findViewById(R.id.hcarinfo);
        cv_merchant_uid = itemView.findViewById(R.id.cv_merchant_uid);
    }

    void onBind(Dispatch data) {
        start.setText(data.getStart_place());
        switch (data.getDp_id().substring(18)) {
            case "1":
                together.setText("일반");
                break;
            case "2":
                together.setText("동승");
        }
        String meet = new SimpleDateFormat("yy/MM/dd").format(data.getStart_time());
        date.setText(meet);
        finish.setText(data.getFinish_place());
        String startTime = new SimpleDateFormat("HH:mm").format(data.getStart_time());
        String finishTime;
        if (data.getEnd_time() != null) {
            finishTime = new SimpleDateFormat("HH:mm").format(data.getEnd_time());
            time.setText(startTime + " : " + finishTime);
        } else {
            time.setText(startTime);
        }


        drivername.setText(data.getM_name());
        carinfo.setText(data.getCar_no() + " | " + data.getCar_model());
        cv_merchant_uid.setText(data.getDp_id());
    }
}