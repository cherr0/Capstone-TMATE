package com.tmate.user.adapter;

import android.app.Dialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

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

        holder.btn_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(holder.btn_review.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_review);
                dialog.show();
                ImageView good = dialog.findViewById(R.id.r_good);

                AlertDialog.Builder builder = new AlertDialog.Builder(holder.btn_review.getContext());
                final ArrayList<String> selectedItems= new ArrayList<String>();
                TextView titleMName = dialog.findViewById(R.id.title_m_name);
                titleMName.setText(holder.mi_m_name.getText());
                TextView contentMName = dialog.findViewById(R.id.content_m_name);
                contentMName.setText(holder.mi_m_name.getText());
                good.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        final String[] result = holder.btn_review.getContext().getResources().getStringArray(R.array.member_like_review);
                        builder.setTitle("좋아요 한 사유를 선택해주세요");
                        builder.setMultiChoiceItems(R.array.member_like_review, null, new DialogInterface.OnMultiChoiceClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int pos, boolean isChecked)
                            {
                                if(isChecked == true) // Checked 상태일 때 추가
                                {
                                    try {
                                        selectedItems.add(result[pos]);
                                    } catch (IndexOutOfBoundsException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else
                                {
                                    try {
                                        selectedItems.remove(result[pos]);
                                    } catch (IndexOutOfBoundsException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                        builder.setPositiveButton("완료", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(selectedItems != null && !selectedItems.isEmpty()) {
                                    holder.review_finish.setImageResource(R.drawable.review_checked);
                                }
                            }
                        });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                });
                ImageView bad = dialog.findViewById(R.id.r_bad);
                bad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        final String[] result = holder.btn_review.getContext().getResources().getStringArray(R.array.member_bad_review);
                        builder.setTitle("싫어요 한 사유를 선택해주세요");
                        builder.setMultiChoiceItems(R.array.member_bad_review, null, new DialogInterface.OnMultiChoiceClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int pos, boolean isChecked)
                            {
                                if(isChecked == true) // Checked 상태일 때 추가
                                {
                                    try {
                                        selectedItems.add(result[pos]);
                                    } catch (IndexOutOfBoundsException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else
                                {
                                    try {
                                        selectedItems.remove(result[pos]);
                                    } catch (IndexOutOfBoundsException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                        builder.setPositiveButton("완료", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(selectedItems != null && !selectedItems.isEmpty()) {
                                    holder.review_finish.setImageResource(R.drawable.review_checked);
                                }
                            }
                        });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                });

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
    Button btn_review;
    ImageView review_finish;
    TextView r_to_seat;

    public ReviewHolder(@NonNull View itemView) {
        super(itemView);
        mi_m_name = itemView.findViewById(R.id.review_m_name);
        review_finish = itemView.findViewById(R.id.review_finish);
        review_picture = itemView.findViewById(R.id.review_picture);
        btn_review = itemView.findViewById(R.id.btn_review);
        r_to_seat = itemView.findViewById(R.id.r_to_seat);
    }
    void onBind(ReviewData data) {
        mi_m_name.setText(data.getName());
        r_to_seat.setText(data.getR_to_seat());
        review_picture.setImageResource(data.getReview_picture());
    }

}
