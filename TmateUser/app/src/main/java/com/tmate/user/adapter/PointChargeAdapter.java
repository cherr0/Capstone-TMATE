package com.tmate.user.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tmate.user.Fragment.PointChargeFragment;
import com.tmate.user.R;

import java.util.ArrayList;

public class
PointChargeAdapter extends RecyclerView.Adapter<PointChargeAdapter.ViewHolder> {

    private ArrayList<String> itemList;
    private Context context;
    private View.OnClickListener onClickItem;
    private int prePosition = -1;


    public PointChargeAdapter(Context context, ArrayList<String> itemList) {
        this.context = context;
        this.itemList = itemList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // context 와 parent.getContext() 는 같다.
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_point_charge, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String item = itemList.get(position);
        holder.point.setText(item);
        // 이전 포지션과 현재 포지션 값이 같을 때
        if(prePosition == position) {
            holder.point.setTextColor(Color.parseColor("#0275d8"));
            holder.point.setBackgroundResource(R.drawable.select_box_bold);
            prePosition = position;
        }else {
            holder.point.setTextColor(Color.parseColor("#1f2023"));
            holder.point.setBackgroundResource(R.drawable.box_bold);

        }

        holder.point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prePosition = position;
                holder.point.setTextColor(Color.parseColor("#1f2023"));
                holder.point.setBackgroundResource(R.drawable.box_bold);

                Log.d("PointChargeAdapter","point값 : " + item);

                PointChargeFragment.chargePoint.setText(item);
                PointChargeFragment.allPoint.setText(item);

                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView point;
        public ViewHolder(View itemView) {
            super(itemView);
            point = itemView.findViewById(R.id.item_point_charge);
        }
    }
}
