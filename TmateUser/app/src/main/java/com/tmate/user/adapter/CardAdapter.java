package com.tmate.user.adapter;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.tmate.user.R;
import com.tmate.user.data.CardData;
import java.util.ArrayList;
public class CardAdapter extends  RecyclerView.Adapter<CardHolder>{
    ArrayList<CardData> items = new ArrayList<>();
    public int r = 0;
    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list, parent, false);
        return new CardHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, int position) {
        holder.onBind(items.get(position));
        holder.cardDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(holder.cardDelete.getContext());
                builder.setTitle("삭제");
                builder.setMessage("해당 항목을 삭제하시겠습니까?");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                items.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, items.size());
                            }
                        });
                builder.setNegativeButton("아니오",
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
    public void addItem(CardData data) {
        items.add(data);
    }
}
class CardHolder extends RecyclerView.ViewHolder {
    ImageView bank_mark;
    TextView bank_name;
    TextView card_no;
    Button cardDelete;
    void onBind(CardData data) {
        bank_mark.setImageResource(data.getCardmark());
        bank_name.setText(data.getBankName());
        card_no.setText(data.getCardNo());
    }
    public CardHolder(@NonNull View itemView) {
        super(itemView);
        bank_mark = itemView.findViewById(R.id.bank_mark);
        bank_name= itemView.findViewById(R.id.bank_name);
        cardDelete = itemView.findViewById(R.id.deleteCard);
        card_no = itemView.findViewById(R.id.card_no);
    }
}