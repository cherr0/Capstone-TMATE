package com.tmate.user;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.CustomViewHolder> {

    private ArrayList<FavoritesData> arrayList;

    public FavoritesAdapter(ArrayList<FavoritesData> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public FavoritesAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorites_item_list,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesAdapter.CustomViewHolder holder, int position) {
        holder.bm_name.setText(arrayList.get(position).getBm_name());
        holder.bm_date.setText(arrayList.get(position).getBm_date());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String curName = holder.bm_name.getText().toString();
                Toast.makeText(v.getContext(), curName, Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                remove(holder.getAdapterPosition());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public void remove(int position) {
        try {
            arrayList.remove(position);
            notifyItemRemoved(position);
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }

    public void addItem(FavoritesData data) {
        arrayList.add(data);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected TextView bm_name;
        protected TextView bm_date;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.bm_name = (TextView) itemView.findViewById(R.id.bm_name);
            this.bm_date = (TextView) itemView.findViewById(R.id.bm_date);
        }
    }
}
