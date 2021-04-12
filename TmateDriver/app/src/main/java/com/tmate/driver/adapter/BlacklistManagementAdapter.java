package com.tmate.driver.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.driver.R;
import com.tmate.driver.data.BlacklistManagementData;

import java.util.ArrayList;

public class BlacklistManagementAdapter extends RecyclerView.Adapter<BlackListHolder>{
    ArrayList<BlacklistManagementData> items = new ArrayList<>();


    @NonNull
    @Override
    public BlackListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_black_list_management, parent, false);
        return new BlackListHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull BlackListHolder holder, int position) {
        holder.onBind(items.get(position));

        holder.blackDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(holder.blackDelete.getContext());
                builder.setTitle("해제");
                builder.setMessage("블랙리스트에서 해당 고객을 삭제 하시겠습니까?");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                items.remove(position);
                                notifyItemRemoved(position);
                                notifyItemChanged(position, items.size());
                            }
                        });
                builder.setNegativeButton("아니요",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    public void addItem(BlacklistManagementData data) {
        items.add(data);
    }



}

class BlackListHolder extends RecyclerView.ViewHolder {
    ImageView blackDelete;
    TextView black_name;
    TextView black_date;
    TextView black_content;

    void onBind(BlacklistManagementData data) {
        black_name.setText(data.getBlack_name());
        black_date.setText(data.getBlack_date());
        black_content.setText(data.getBlack_content());
    }

    public BlackListHolder(@NonNull View itemView) {
        super(itemView);
        black_name = itemView.findViewById(R.id.black_name);
        black_date = itemView.findViewById(R.id.black_date);
        black_content = itemView.findViewById(R.id.black_content);
        blackDelete = itemView.findViewById(R.id.black_delete);
    }
}
