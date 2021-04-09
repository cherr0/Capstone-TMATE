package com.tmate.driver.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.tmate.driver.R;
import com.tmate.driver.data.HistoryData;
import com.tmate.driver.databinding.HistoryCardviewBinding;
import java.util.ArrayList;



public class HistoryAdapter extends RecyclerView.Adapter<HistoryHolder> {
    ArrayList<HistoryData> items = new ArrayList<>();
    private HistoryCardviewBinding binding;
    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
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
    }
}