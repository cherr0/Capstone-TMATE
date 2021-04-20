package com.tmate.driver.adapter;

import android.animation.ValueAnimator;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.driver.R;
import com.tmate.driver.data.HistoryData;

import java.util.ArrayList;



public class HistoryAdapter extends RecyclerView.Adapter<HistoryHolder>{
    ArrayList<HistoryData> items = new ArrayList<>();
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    private int prePosition = -1;

    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull HistoryHolder holder, int position) {
        holder.onBind(items.get(position), position, selectedItems);
        holder.reasonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedItems.get(position)) {
                    // 펼쳐진 Item을 클릭 시
                    selectedItems.delete(position);
                } else {
                    // 직전의 클릭됐던 Item의 클릭상태를 지움
                    selectedItems.delete(prePosition);
                    // 클릭한 Item의 position을 저장
                    selectedItems.put(position, true);
                }
                // 해당 포지션의 변화를 알림
                if (prePosition != -1) notifyItemChanged(prePosition);
                notifyItemChanged(position);
                // 클릭된 position 저장
                prePosition = position;
            }
        });
        FragmentTransaction transaction;
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
    public void addItem(HistoryData data) {
        items.add(data);
    }

}
class HistoryHolder extends RecyclerView.ViewHolder{
    TextView hdate;
    TextView htogether;
    TextView re_amt;
    TextView history_username;
    TextView hstart;
    TextView hfinish;
    TextView htime;
    LinearLayout black_list_submit;
    TextView r_reason1, r_reason2, r_reason3, reasonView, r_code_L, r_code_H;
    ConstraintLayout const_reason;
    CardView cardview ;


    OnViewHolderItemClickListener onViewHolderItemClickListener;

    void onBind(HistoryData data, int position, SparseBooleanArray selectedItems) {
        hdate.setText(data.getHdate());
        htogether.setText(data.getHtogether());
        re_amt.setText(data.getRe_amt());
        history_username.setText(data.getHistory_username());
        hstart.setText(data.getHstart());
        hfinish.setText(data.getHfinish());
        htime.setText(data.getHtime());
        r_reason1.setText(data.getR_reason1());
        r_reason2.setText(data.getR_reason2());
        r_reason3.setText(data.getR_reason3());
        r_code_L.setText(data.getR_code_L());
        r_code_H.setText(data.getR_code_H());

        changeVisibility(selectedItems.get(position));
    }
    public HistoryHolder(@NonNull View itemView) {
        super(itemView);
        hdate = itemView.findViewById(R.id.hdate);
        htogether = itemView.findViewById(R.id.htogether);
        re_amt = itemView.findViewById(R.id.re_amt);
        history_username = itemView.findViewById(R.id.history_username);
        hstart = itemView.findViewById(R.id.hstart);
        hfinish = itemView.findViewById(R.id.hfinish);
        htime = itemView.findViewById(R.id.htime);
        black_list_submit = itemView.findViewById(R.id.black_list_submit);
        r_reason1 = itemView.findViewById(R.id.r_reason1);
        r_reason2 = itemView.findViewById(R.id.r_reason2);
        r_reason3 = itemView.findViewById(R.id.r_reason3);
        reasonView = itemView.findViewById(R.id.reasonView);
        const_reason = itemView.findViewById(R.id.const_reason);
        cardview = itemView.findViewById(R.id.cardview);
        r_code_L = itemView.findViewById(R.id.r_code_L);
        r_code_H = itemView.findViewById(R.id.r_code_H);

       reasonView.setOnClickListener(v -> {
           onViewHolderItemClickListener.onViewHolderItemClick();
       });
    }

    private void changeVisibility(final boolean isExpanded) {
        // ValueAnimator.ofInt(int... values)는 View가 변할 값을 지정, 인자는 int 배열
        final ValueAnimator va = isExpanded ? ValueAnimator.ofInt(0, 600) : ValueAnimator.ofInt(600, 0);
        // Animation이 실행되는 시간, n/1000초
        va.setDuration(500);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // imageView의 높이 변경
                const_reason.requestLayout();
                // imageView가 실제로 사라지게하는 부분
                const_reason.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            }
        });
        // Animation start
        va.start();
    }

    public void setOnViewHolderItemClickListener(OnViewHolderItemClickListener onViewHolderItemClickListener) {
        this.onViewHolderItemClickListener = onViewHolderItemClickListener;
    }
}