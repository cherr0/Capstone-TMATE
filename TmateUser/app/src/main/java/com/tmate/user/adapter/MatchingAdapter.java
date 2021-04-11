package com.tmate.user.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.user.Activity.MatchingDetailActivity;
import com.tmate.user.R;
import com.tmate.user.data.MatchingData;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MatchingAdapter extends RecyclerView.Adapter<MatchingHolder> {

    private ArrayList<MatchingData> items = new ArrayList<>();

    public MatchingAdapter(ArrayList<MatchingData> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public MatchingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_matching,parent,false);
        MatchingHolder holder = new MatchingHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MatchingHolder holder, int position) {

        try {
            holder.onBind(items.get(position));
        } catch (Exception e) {
            Log.e("아무것도 안옴", e.toString());
        }

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MatchingDetailActivity.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != items ? items.size() : 0);
    }

    public void addItem(MatchingData matchingData) {
        items.add(matchingData);
    }
}

class MatchingHolder extends RecyclerView.ViewHolder {

    CircleImageView iv_profile;
    TextView tv_matching_name;
    TextView tv_personnel;
    TextView tv_start;
    TextView tv_start_place;
    TextView tv_end;
    TextView tv_end_place;

    public MatchingHolder(View itemView) {
        super(itemView);

        this.iv_profile =(CircleImageView) itemView.findViewById(R.id.iv_profile);
        this.tv_matching_name = (TextView) itemView.findViewById(R.id.tv_matching_name);
        this.tv_personnel = (TextView) itemView.findViewById(R.id.tv_personnel);
        this.tv_start = (TextView) itemView.findViewById(R.id.tv_start);
        this.tv_start_place = (TextView) itemView.findViewById(R.id.tv_start_place);
        this.tv_end = (TextView) itemView.findViewById(R.id.tv_end);
        this.tv_end_place = (TextView) itemView.findViewById(R.id.tv_end_place);
    }

    void onBind(MatchingData data) {
        tv_matching_name.setText(data.getTv_matching_name());
        tv_personnel.setText(data.getTv_personnel());
        tv_start_place.setText(data.getTv_start_place());
        tv_end_place.setText(data.getTv_end_place());
    }
}