package com.tmate.driver.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.driver.R;
import com.tmate.driver.data.Car;

import java.util.ArrayList;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarHolder> {
    ArrayList<Car> items = new ArrayList<>();
    private SparseBooleanArray selectedItems = new SparseBooleanArray(0);
    private static int prePosition = -1;
    public Context context;



    @NonNull
    @Override
    public CarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_list, parent, false);
        context = parent.getContext();
        return new CarHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CarHolder holder, int position) {
        holder.onBind(items.get(position));

        // 이전 포지션과 현재 포지션 값이 같을 때
        if(prePosition == position) {
            holder.itemView.setBackgroundResource(R.drawable.item_car_clicked);
            holder.car_item_checkBox.setChecked(true);
            prePosition = position;
        }else {
            holder.itemView.setBackgroundResource(R.drawable.item_car_unclicked);
            holder.car_item_checkBox.setChecked(false);
        }
    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Car data) {
        items.add(data);
    }
    public class CarHolder extends RecyclerView.ViewHolder {
        TextView carNo;
        TextView carColor;
        TextView carKind;
        TextView carModel;
        TextView d_id;
        CheckBox car_item_checkBox;
        CardView car_card_view;
        private int position;



        void onBind(Car data) {
            carNo.setText(data.getCar_no());
            carColor.setText(data.getCar_color());
            carModel.setText(data.getCar_model());
            d_id.setText(data.getM_id());

            switch (data.getCar_kind()) {
                case "0" :
                    carKind.setText("소형");
                    break;
                case "1" :
                    carKind.setText("중형");
                    break;
                case "2" :
                    carKind.setText("대형");
                    break;
            }

            switch (data.getCar_color()) {
                case "black":
                    carColor.setText("검은색");
                    break;
                case "gray":
                    carColor.setText("회색");
                    break;
                case "white":
                    carColor.setText("흰색");
                    break;
                case "orange":
                    carColor.setText("주황색");
                    break;
                case "yellow":
                    carColor.setText("노란색");
                    break;
            }

            car_card_view.setOnClickListener(v -> {
                position = getAdapterPosition();
                prePosition = position;
                v.setBackgroundResource(R.drawable.item_car_clicked);
                car_item_checkBox.setChecked(true);
                if (car_item_checkBox.isChecked()) {
                    setPreference("carNo", data.getCar_no());
                    Log.d("CarAdapter", "carNo : " + getPreferenceString("carNo"));
                }
                notifyDataSetChanged();
            });
        }

        public CarHolder(@NonNull View itemView) {
            super(itemView);
            carNo = itemView.findViewById(R.id.car_no);
            carColor = itemView.findViewById(R.id.car_color);
            carKind = itemView.findViewById(R.id.car_kind);
            carModel = itemView.findViewById(R.id.car_model);
            car_item_checkBox = itemView.findViewById(R.id.car_item_checkBox);
            car_card_view = itemView.findViewById(R.id.car_card_view);
            d_id = itemView.findViewById(R.id.d_id);

        }

        public String getPreferenceString(String key) {
            SharedPreferences pref = context.getSharedPreferences("loginDriver", Context.MODE_PRIVATE);
            return pref.getString(key, "");
        }

        // 데이터 저장 함수
        public void setPreference(String key, String value) {
            SharedPreferences pref = context.getSharedPreferences("loginDriver", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(key, value);
            editor.apply();
        }
    }
}

