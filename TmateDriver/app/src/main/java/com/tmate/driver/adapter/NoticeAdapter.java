package com.tmate.driver.adapter;
import android.content.Intent;
import android.util.Log;
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
import com.tmate.driver.net.DataService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NoticeAdapter extends RecyclerView.Adapter<NoticeHolder> {

    ArrayList<Notice> items = new ArrayList<>();

    // 타임스탬프 String 변환용
    private SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd", Locale.KOREA);

    private Call<Notice> request;

    @NonNull
    @Override
    public NoticeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notice, parent, false);
        return new NoticeHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull NoticeHolder holder, int position) {
        holder.onBind(items.get(position));

        String bd_id = items.get(position).getBd_id();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request = DataService.getInstance().common.getNoticeDetail(bd_id);

                request.enqueue(new Callback<Notice>() {

                    @Override
                    public void onResponse(Call<Notice> call, Response<Notice> response) {
                        if(response.isSuccessful()) {
                            if(response.code() == 200) {
                                Notice notice = response.body();
                                Log.i("공지사항 내용", notice.toString());
                                Intent intent = new Intent(v.getContext(), NoticeDetailActivity.class);


                                intent.putExtra("title",notice.getBd_title());
                                intent.putExtra("date", sdf.format(notice.getBd_cre_date()));
                                intent.putExtra("content", notice.getBd_contents());

                                v.getContext().startActivity(intent);
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<Notice> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
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