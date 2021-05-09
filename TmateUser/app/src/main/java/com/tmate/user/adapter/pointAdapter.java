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
    public void clear() {
        items.clear();
    }


}
class pointHolder extends RecyclerView.ViewHolder {
    
    TextView poTime;
    TextView poResult;
    TextView poExact;
    TextView poCourse;
    TextView tv_pointText;

    void onBind(PointData data) {

        if(data != null) {
            poTime.setVisibility(View.VISIBLE);
            poResult.setVisibility(View.VISIBLE);
            poExact.setVisibility(View.VISIBLE);
            poCourse.setVisibility(View.VISIBLE);
            tv_pointText.setVisibility(View.VISIBLE);

            String pointDate = data.getPo_time();
            poTime.setText(pointDate.substring(0,2)+"/"+pointDate.substring(2,4)+"/"+pointDate.substring(4,6)+" "+pointDate.substring(6,8)+":"+pointDate.substring(8,10)+":"+pointDate.substring(10,12));
            poResult.setText(data.getPo_result() + "");
//        mPoint.setText(data.get);
            poExact.setText(data.getPo_exact());
            poCourse.setText(data.getPo_course());
        } else{
            poTime.setVisibility(View.GONE);
            poResult.setVisibility(View.GONE);
            poExact.setVisibility(View.GONE);
            poCourse.setVisibility(View.GONE);
            tv_pointText.setVisibility(View.GONE);
        }
    }

    public pointHolder(@NonNull View itemView) {
        super(itemView);

        poTime = itemView.findViewById(R.id.poTime);
        poResult = itemView.findViewById(R.id.poResult);
        poExact = itemView.findViewById(R.id.poExact);
        poCourse = itemView.findViewById(R.id.poCourse);
        tv_pointText = itemView.findViewById(R.id.tv_pointText);
    }
}
