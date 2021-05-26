package com.tmate.user.adapter;

import android.app.Dialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.internal.GmsClientEventManager;
import com.tmate.user.R;
import com.tmate.user.data.ReviewData;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewHolder>{
    ArrayList<ReviewData> items = new ArrayList<>();
    Dialog dialog;

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //전개자(Inflater)를 통해 얻은 참조 객체를 통해 뷰홀더 객체 생성
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);

        return new ReviewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ReviewHolder holder, int position) {
        holder.onBind(items.get(position));

        holder.member_like.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                holder.member_dislike.setChecked(false); //싫어요 off
                if(isChecked) {
                    holder.member_like.setChecked(true);
                } else if(!isChecked){
                    holder.member_like.setChecked(false);

                }
            }
        });
        holder.member_dislike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                holder.member_like.setChecked(false); //좋아요 off
                if(isChecked) {
                    holder.member_dislike.setChecked(true);
                } else if (!isChecked){
                    holder.member_dislike.setChecked(false);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public void addItem(ReviewData data) {
        // 외부에서 item을 추가시킬 함수입니다.
        items.add(data);
    }

    public void clear() {items.clear();}
}
class ReviewHolder extends RecyclerView.ViewHolder {
    TextView mi_m_name;
    CircleImageView review_picture;
    ToggleButton member_like;
    ToggleButton member_dislike;
    ImageView review_finish;
    TextView r_to_seat;

    public ReviewHolder(@NonNull View itemView) {
        super(itemView);
        mi_m_name = itemView.findViewById(R.id.review_m_name);
        review_finish = itemView.findViewById(R.id.review_finish);
        review_picture = itemView.findViewById(R.id.review_picture);
        member_like = itemView.findViewById(R.id.member_like);
        member_dislike = itemView.findViewById(R.id.member_dislike);
        r_to_seat = itemView.findViewById(R.id.r_to_seat);
    }
    void onBind(ReviewData data) {
        mi_m_name.setText(data.getName());
        r_to_seat.setText(data.getR_to_seat());
        review_picture.setImageResource(data.getReview_picture());
    }

}
