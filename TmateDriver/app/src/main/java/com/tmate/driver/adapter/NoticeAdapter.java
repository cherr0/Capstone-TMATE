package com.tmate.driver.adapter;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.driver.R;
import com.tmate.driver.activity.NoticeDetailActivity;
import com.tmate.driver.data.Notice;
import com.tmate.driver.data.NoticeData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


public class NoticeAdapter extends RecyclerView.Adapter<NoticeHolder> {
    ArrayList<Notice> items = new ArrayList<>();
    @NonNull
    @Override
    public NoticeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notice, parent, false);
        return new NoticeHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull NoticeHolder holder, int position) {
        holder.onBind(items.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NoticeDetailActivity.class);
                v.getContext().startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Notice notice) {
        items.add(notice);
    }
}
class NoticeHolder extends RecyclerView.ViewHolder {

    TextView notice_title;
    TextView notice_date;
    TextView notice_id;

    // 타임스탬프 String 변환용
    SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd", Locale.KOREA);

    void onBind(Notice data) {
        notice_id.setText(data.getBd_id());
        notice_title.setText(data.getBd_title());
        notice_date.setText(sdf.format(data.getBd_cre_date()));
    }

    public NoticeHolder(@NonNull View itemView) {
        super(itemView);

        notice_id = itemView.findViewById(R.id.notice_id);
        notice_title = itemView.findViewById(R.id.notice_title);
        notice_date = itemView.findViewById(R.id.notice_date);
    }
}