package com.tmate.user.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.user.R;
import com.tmate.user.data.Data;
import com.tmate.user.net.DataService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class historyAdapter extends RecyclerView.Adapter<HistoryHolder> {
    ArrayList<Data> items = new ArrayList<>();

    Context context;
    SharedPreferences pref;
    String m_id;

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
        holder.onBind(items.get(position));

        holder.itemView.setTag(position);

        holder.crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setTitle("이용기록");
                builder.setMessage("삭제하시겠습니까");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataService.getInstance().memberAPI.removeHistoryByM_id(holder.cv_merchant_uid.getText().toString(),m_id).enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                if(response.code() == 200) {
                                    Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                    items.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemChanged(position, items.size());
                                }
                            }

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });
                    }
                });
            builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    public void addItem(Data data) {
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
    ImageView crear;
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
        crear = itemView.findViewById(R.id.crear);
        cv_merchant_uid = itemView.findViewById(R.id.cv_merchant_uid);
    }

    void onBind(Data data) {
        start.setText(data.getStart());
        together.setText(data.getTogether());
        date.setText(data.getDate());
        finish.setText(data.getFinish());
        time.setText(data.getTime());
        drivername.setText(data.getDrivername());
        carinfo.setText(data.getCarinfo());
        cv_merchant_uid.setText(data.getMerchant_uid());
    }
}