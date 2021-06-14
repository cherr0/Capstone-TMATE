
package com.tmate.driver.adapter;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.driver.R;
import com.tmate.driver.activity.BlackListSelectActivity;
import com.tmate.driver.activity.NormalBlackListSelectActivity;
import com.tmate.driver.data.DriverHistory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryHolder>{
    ArrayList<DriverHistory> items = new ArrayList<>();
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
        int posit = holder.getLayoutPosition();

        holder.onBind(items.get(posit), posit, selectedItems);
        holder.reasonView.setOnClickListener(v -> {
            if (selectedItems.get(posit)) {
                // 펼쳐진 Item을 클릭 시
                selectedItems.delete(posit);
            } else {
                // 직전의 클릭됐던 Item의 클릭상태를 지움
                selectedItems.delete(prePosition);
                // 클릭한 Item의 position을 저장
                selectedItems.put(posit, true);
            }
            // 해당 포지션의 변화를 알림
            if (prePosition != -1) notifyItemChanged(prePosition);
            notifyItemChanged(posit);
            // 클릭된 position 저장
            prePosition = posit;
        });
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
    public void addItem(DriverHistory data) {
        items.add(data);
    }
    public class HistoryHolder extends RecyclerView.ViewHolder{
        TextView hdate;
        TextView htogether;
        TextView re_amt;
        TextView[] username = new TextView[3];
        TextView start_place, finish_place;
        TextView htime;
        LinearLayout black_list_submit;
        TextView[] r_reason = new TextView[3];
        TextView reasonView, r_code_L, r_code_H;
        ConstraintLayout const_reason;
        CardView cardview ;

        SimpleDateFormat dateSdf = new SimpleDateFormat("yy/MM/dd");
        SimpleDateFormat timeSdf = new SimpleDateFormat("hh:mm");


        OnViewHolderItemClickListener onViewHolderItemClickListener;

        void onBind(DriverHistory data, int position, SparseBooleanArray selectedItems) {
            String[] userList;
            String[] reviewList;
            if(data.getName() != null) {
                userList = data.getName().split("/");
                for (int i = 0; i < userList.length; i++) {
                    username[i].setText(userList[i]);
                }
            }
            if(data.getReason() != null) {
                reviewList = data.getReason().split("/");
                for (int i = 0; i < reviewList.length; i++) {
                    r_reason[i].setText(reviewList[i]);
                }
            }


            String history_time = timeSdf.format(data.getStart_time()) + " - " + timeSdf.format(data.getEnd_time());

            hdate.setText(dateSdf.format(data.getEnd_time())); // 배차일자
            htogether.setText(data.getTogether()); // 동승인원
            re_amt.setText(String.valueOf(data.getAll_fare())); // 금액
            start_place.setText(data.getStart_place()); // 출발지
            finish_place.setText(data.getFinish_place()); // 도착지
            htime.setText(history_time); // 운행 시간
            r_code_L.setText(String.valueOf(data.getLike_cnt())); // 좋아요 갯수
            r_code_H.setText(String.valueOf(data.getHate_cnt())); // 싫어요 갯수

            black_list_submit.setOnClickListener(v -> {
                if (data.getTogether().equals("동승")) {
                    Intent intent = new Intent(v.getContext(), BlackListSelectActivity.class);
                    intent.putExtra("dp_id", items.get(position).getDp_id());
                    v.getContext().startActivity(intent);
                } else {
                    Intent intent = new Intent(v.getContext(), NormalBlackListSelectActivity.class);
                    intent.putExtra("dp_id", items.get(position).getDp_id());
                    v.getContext().startActivity(intent);
                }
            });

            changeVisibility(selectedItems.get(position));
        }
        public HistoryHolder(@NonNull View itemView) {
            super(itemView);
            hdate = itemView.findViewById(R.id.hdate);
            htogether = itemView.findViewById(R.id.htogether);
            re_amt = itemView.findViewById(R.id.re_amt);
            username[0] = itemView.findViewById(R.id.client1);
            username[1] = itemView.findViewById(R.id.client2);
            username[2] = itemView.findViewById(R.id.client3);
            start_place = itemView.findViewById(R.id.hstart);
            finish_place = itemView.findViewById(R.id.hfinish);
            htime = itemView.findViewById(R.id.htime);
            black_list_submit = itemView.findViewById(R.id.black_list_submit);
            r_reason[0] = itemView.findViewById(R.id.r_reason1);
            r_reason[1] = itemView.findViewById(R.id.r_reason2);
            r_reason[2] = itemView.findViewById(R.id.r_reason3);
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

}
