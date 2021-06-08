package com.tmate.user.adapter;

import android.app.Dialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.user.R;
import com.tmate.user.data.Attend;
import com.tmate.user.data.ReviewVO;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewHolder>{
    ArrayList<Attend> items = new ArrayList<>();

    ReviewVO reviewVO;


    public ReviewAdapter(ReviewVO reviewVO) {
        this.reviewVO = reviewVO;
    }

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
        Log.d("ReviewAdapter","position 값 : " + position);

        holder.member_like.setOnCheckedChangeListener((buttonView, isChecked) -> {
            holder.member_dislike.setChecked(false); //싫어요 off
            if(isChecked) {
                holder.member_like.setChecked(true);
                switch (position) {
                    case 1:
                        reviewVO.setRe_one("0");
                        break;
                    case 2:
                        reviewVO.setRe_two("0");
                        break;
                    case 3:
                        reviewVO.setRe_three("0");
                        break;
                }
            } else {
                holder.member_like.setChecked(false);
                switch (position) {
                    case 1:
                        reviewVO.setRe_one("");
                        break;
                    case 2:
                        reviewVO.setRe_two("");
                        break;
                    case 3:
                        reviewVO.setRe_three("");
                        break;
                }

            }
        });
        holder.member_dislike.setOnCheckedChangeListener((buttonView, isChecked) -> {
            holder.member_like.setChecked(false); //좋아요 off
            if(isChecked) {
                holder.member_dislike.setChecked(true);
                switch (position) {
                    case 1:
                        reviewVO.setRe_one("0");
                        break;
                    case 2:
                        reviewVO.setRe_two("0");
                        break;
                    case 3:
                        reviewVO.setRe_three("0");
                        break;
                }
            } else {
                holder.member_dislike.setChecked(false);
                switch (position) {
                    case 1:
                        reviewVO.setRe_one("");
                        break;
                    case 2:
                        reviewVO.setRe_two("");
                        break;
                    case 3:
                        reviewVO.setRe_three("");
                        break;
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public void addItem(Attend data) {
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
    void onBind(Attend data) {
        mi_m_name.setText(data.getM_name());
        int seat = data.getSeat();
        switch (seat) {
            case 1:
                r_to_seat.setText("(보조석)");
                break;
            case 2:
                r_to_seat.setText("(왼쪽 뒷자석)");
                break;
            case 3:
                r_to_seat.setText("(오른쪽 뒷자석)");
                break;
        }
    }

}
