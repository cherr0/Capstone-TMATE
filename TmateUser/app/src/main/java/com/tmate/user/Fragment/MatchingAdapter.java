package com.tmate.user.Fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.user.MatchingDetailActivity;
import com.tmate.user.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MatchingAdapter extends RecyclerView.Adapter<MatchingAdapter.CustomViewHolder> {

    private ArrayList<MatchingData> arrayList;

    public MatchingAdapter(ArrayList<MatchingData> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MatchingAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_matching,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MatchingAdapter.CustomViewHolder holder, int position) {
        holder.tv_matching_name.setText(arrayList.get(position).getTv_matching_name());
        holder.tv_personnel.setText(arrayList.get(position).getTv_personnel());
        holder.tv_start_place.setText(arrayList.get(position).getTv_start_place());
        holder.tv_end_place.setText(arrayList.get(position).getTv_end_place());

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
        return (null != arrayList ? arrayList.size() : 0);
    }

    public void addItem(MatchingData matchingData) {
        arrayList.add(matchingData);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected CircleImageView iv_profile;
        protected TextView tv_matching_name;
        protected TextView tv_personnel;
        protected TextView tv_start;
        protected TextView tv_start_place;
        protected TextView tv_end;
        protected TextView tv_end_place;


        public CustomViewHolder(View itemView) {
            super(itemView);

            this.iv_profile =(CircleImageView) itemView.findViewById(R.id.iv_profile);
            this.tv_matching_name = (TextView) itemView.findViewById(R.id.tv_matching_name);
            this.tv_personnel = (TextView) itemView.findViewById(R.id.tv_personnel);
            this.tv_start = (TextView) itemView.findViewById(R.id.tv_start);
            this.tv_start_place = (TextView) itemView.findViewById(R.id.tv_start_place);
            this.tv_end = (TextView) itemView.findViewById(R.id.tv_end);
            this.tv_end_place = (TextView) itemView.findViewById(R.id.tv_end_place);

        }
    }
}