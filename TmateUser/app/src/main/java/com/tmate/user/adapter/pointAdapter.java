package com.tmate.user.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.user.R;
import com.tmate.user.data.PointData;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class pointAdapter extends  RecyclerView.Adapter<pointHolder> {
    ArrayList<PointData> items = new ArrayList<>();


    @NonNull
    @Override
    public pointHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_point_item, parent, false);
        return new pointHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull pointHolder holder, int position) {
        holder.onBind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public void addItem(PointData data) {
        items.add(data);
    }

}
class pointHolder extends RecyclerView.ViewHolder {
    
    TextView poTime;
    TextView poResult;
    TextView poExact;
    TextView poCourse;

    void onBind(PointData data) {
        poTime.setText(data.getPo_time().toString().substring(0,19));
        poResult.setText(data.getPo_result() + "");
//        mPoint.setText(data.get);
        poExact.setText(data.getPo_exact());
        poCourse.setText(data.getPo_course());
    }

    public pointHolder(@NonNull View itemView) {
        super(itemView);

        poTime = itemView.findViewById(R.id.poTime);
        poResult = itemView.findViewById(R.id.poResult);
        poExact = itemView.findViewById(R.id.poExact);
        poCourse = itemView.findViewById(R.id.poCourse);
    }
}
