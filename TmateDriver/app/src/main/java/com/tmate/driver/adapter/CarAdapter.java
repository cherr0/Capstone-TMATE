package com.tmate.driver.adapter;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.driver.R;
import com.tmate.driver.data.CarData;

import java.util.ArrayList;

public class CarAdapter extends RecyclerView.Adapter<CarHolder> {
    ArrayList<CarData> items = new ArrayList<>();
    private ArrayList<CarData> arrayList;



    @NonNull
    @Override
    public CarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_list, parent, false);
        return new CarHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CarHolder holder, int position) {
        holder.onBind(items.get(position));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(holder.delete.getContext());
                builder.setTitle("삭제");
                builder.setMessage("해당 항목을 삭제하시겠습니까?");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                items.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, items.size());

                            }
                        });
                builder.setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
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



    public void addItem(CarData data) {
        items.add(data);
    }



}

class CarHolder extends RecyclerView.ViewHolder {
    TextView carNo;
    TextView carVin;
    TextView carColor;
    TextView carKind;
    TextView carModel;
    ImageView delete;

    void onBind(CarData data) {
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
        delete = itemView.findViewById(R.id.delete);
    }
}