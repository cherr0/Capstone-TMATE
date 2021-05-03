package com.tmate.driver.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.driver.R;
import com.tmate.driver.data.JoinBan;
import com.tmate.driver.net.DataService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlacklistManagementAdapter extends RecyclerView.Adapter<BlackListHolder>{
    ArrayList<JoinBan> items = new ArrayList<>();

    // 블랙리스트 삭제
    Call<Boolean> request;

    Context context;
    int selectedIndex;

   @NonNull
    @Override
    public BlackListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_black_list_management, parent, false);
        context = parent.getContext();
        return new BlackListHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull BlackListHolder holder, int position) {
        holder.onBind(items.get(position));
        final BlackListHolder blackListHolder = holder;

        holder.blackDelete.setOnClickListener(v -> {
            final AlertDialog.Builder builder = new AlertDialog.Builder(holder.blackDelete.getContext());
            builder.setTitle("해제");
            builder.setMessage("블랙리스트에서 해당 고객을 삭제 하시겠습니까?");
            builder.setPositiveButton("예",
                    (dialog, which) -> {
                        selectedIndex = blackListHolder.getAdapterPosition();
                        Log.d("BlacklistAdapter",items.get(selectedIndex).toString());
                        String d_id = items.get(selectedIndex).getD_id();
                        String m_id = items.get(selectedIndex).getM_id();
                        request = DataService.getInstance().driver.removeBlacklist(d_id,m_id);
                        request.enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                items.remove(selectedIndex);
                                notifyItemRemoved(selectedIndex);
                                notifyItemChanged(selectedIndex, items.size());
                            }

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {
                                Toast.makeText(context, "삭제 요청이 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                t.printStackTrace();
                            }
                        });
                    });
            builder.setNegativeButton("아니요",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            builder.show();

        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    public void addItem(JoinBan data) {
        items.add(data);
    }



}

class BlackListHolder extends RecyclerView.ViewHolder {
    ImageView blackDelete;
    TextView black_name;
    TextView black_date;
    TextView black_content;

    void onBind(JoinBan data) {
        black_name.setText(data.getM_name());
        black_date.setText(data.getBan_reg_date().toString());
        black_content.setText(data.getBan_reason());
    }

    public BlackListHolder(@NonNull View itemView) {
        super(itemView);
        black_name = itemView.findViewById(R.id.black_name);
        black_date = itemView.findViewById(R.id.black_date);
        black_content = itemView.findViewById(R.id.black_content);
        blackDelete = itemView.findViewById(R.id.black_delete);
    }
}
