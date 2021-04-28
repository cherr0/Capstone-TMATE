package com.tmate.user.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.user.Activity.NoticeDetailActivity;
import com.tmate.user.R;
import com.tmate.user.data.Notice;
import com.tmate.user.net.DataService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeHolder> {


    DataService dataService = DataService.getInstance();

    private ArrayList<Notice> items;

    // 타임스탬프 String 변환용
    SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd", Locale.KOREA);

    public NoticeAdapter(ArrayList<Notice> items){this.items = items;}

    @NonNull
    @Override
    public NoticeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_notice_item, parent, false);
        return new NoticeHolder(view);
    }

    @Override
    public int getItemCount() {
        return (null != items ? items.size() : 0);
    }

    public void addItem(Notice notice) {items.add(notice);}

    @Override
    public void onBindViewHolder(@NonNull NoticeHolder holder, int position) {

        holder.onBind(items.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bd_id = items.get(position).getBd_id();
                Log.d("notice", "공지사항 클릭됨 bd_id : " + bd_id);

                dataService.commonAPI.getNoticeDetail(bd_id).enqueue(new Callback<Notice>() {
                    @Override
                    public void onResponse(Call<Notice> call, Response<Notice> response) {
                        Log.d("notice", "response 값 받아옴 : " + response.body() + "response code : " + response.code());
                        if(response.isSuccessful()) {
                            if(response.code() == 200) {
                                Notice notice = response.body();
                                Log.d("공지사항 내용", notice.toString());
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

}
class NoticeHolder extends RecyclerView.ViewHolder{

    TextView notice_title;
    TextView notice_date;
    TextView notice_id;

    // 타임스탬프 String 변환용
    SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd", Locale.KOREA);

    public NoticeHolder(View itemView) {
        super(itemView);

        notice_id = (TextView) itemView.findViewById(R.id.notice_id);
        notice_title = (TextView) itemView.findViewById(R.id.notice_title);
        notice_date = (TextView) itemView.findViewById(R.id.notice_date);
    }

    void onBind(Notice data) {
        notice_id.setText(data.getBd_id());
        notice_title.setText(data.getBd_title());
        notice_date.setText(sdf.format(data.getBd_cre_date()));
    }
}