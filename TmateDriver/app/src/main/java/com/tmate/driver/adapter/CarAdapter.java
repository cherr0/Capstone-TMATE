package com.tmate.driver.adapter;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.driver.R;
import com.tmate.driver.data.Car;

import java.util.ArrayList;

public class CarAdapter extends RecyclerView.Adapter<CarHolder> {
    ArrayList<Car> items = new ArrayList<>();
    private SparseBooleanArray selectedItems = new SparseBooleanArray(0);
    private int prePosition = -1;



    @NonNull
    @Override
    public CarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_list, parent, false);
        return new CarHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CarHolder holder, int position) {
        holder.onBind(items.get(position));

        // 이전 포지션과 현재 포지션 값이 같을 때
        if(prePosition == position) {
            holder.itemView.setBackgroundResource(R.drawable.item_car_clicked);
            prePosition = position;
        }else {
            holder.itemView.setBackgroundResource(R.drawable.item_car_unclicked);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prePosition = position;
                v.setBackgroundResource(R.drawable.item_car_clicked);

                notifyDataSetChanged();
            }
        });
    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Car data) {
        items.add(data);
    }
}

class CarHolder extends RecyclerView.ViewHolder {
    TextView carNo;
    TextView carVin;
    TextView carColor;
    TextView carKind;
    TextView carModel;


    void onBind(Car data) {
        carNo.setText(data.getCar_no());
        carVin.setText(data.getCar_vin());
        carColor.setText(data.getCar_color());
        carKind.setText(data.getCar_kind());
        carModel.setText(data.getCar_model());
    }

    public CarHolder(@NonNull View itemView) {
        super(itemView);
        carNo = itemView.findViewById(R.id.car_no);
        carVin = itemView.findViewById(R.id.car_vin);
        carColor = itemView.findViewById(R.id.car_color);
        carKind = itemView.findViewById(R.id.car_kind);
        carModel = itemView.findViewById(R.id.car_model);

    }
}