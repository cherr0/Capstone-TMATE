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

public class PointAdapter extends  RecyclerView.Adapter<PointHolder> {
    ArrayList<PointData> items = new ArrayList<>();
    @NonNull
    @Override
    public PointHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_point_item, parent, false);
        return new PointHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull PointHolder holder, int position) {
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
class PointHolder extends RecyclerView.ViewHolder {
    TextView poTime;
    TextView poResult;
    TextView mPoint;
    TextView poExact;
    TextView poCourse;

    void onBind(PointData data) {
        poTime.setText(data.getPoTime());
        poResult.setText(Integer.toString(data.getPoResult()));
        mPoint.setText(Integer.toString(data.getmPoint()));
        poExact.setText(data.getPoExact());
        poCourse.setText(data.getPoCourse());
    }
    public PointHolder(@NonNull View itemView) {
        super(itemView);

        poTime = itemView.findViewById(R.id.poTime);
        poResult = itemView.findViewById(R.id.poResult);
        mPoint = itemView.findViewById(R.id.mPoint);
        poExact = itemView.findViewById(R.id.poExact);
        poCourse = itemView.findViewById(R.id.poCourse);
    }
}
