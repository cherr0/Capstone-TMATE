package com.tmate.user.adapter;

import android.animation.ValueAnimator;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.user.Fragment.MatchingDetailFragment;
import com.tmate.user.R;
import com.tmate.user.data.MatchingDetailData;
import com.tmate.user.data.MatchingMember;
import com.tmate.user.data.TogetherRequest;
import com.tmate.user.net.DataService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchingDetailAdapter extends RecyclerView.Adapter<MatchingDetailHolder> {

    ArrayList<MatchingDetailData> items  = new ArrayList<>();
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    private int prePosition = -1;


    @NonNull
    @Override

    public MatchingDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //전개자(Inflater)를 통해 얻은 참조 객체를 통해 뷰홀더 객체 생성
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_matching_recycle, parent, false);

        return new MatchingDetailHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MatchingDetailHolder holder, int position) {
        holder.onBind(items.get(position), position, selectedItems);
        holder.item_matching_recycle.setOnClickListener(v -> {
            if (selectedItems.get(position)) {
                // 펼쳐진 Item을 클릭 시
                selectedItems.delete(position);
            } else {
                // 직전의 클릭됐던 Item의 클릭상태를 지움
                selectedItems.delete(prePosition);
                // 클릭한 Item의 position을 저장
                selectedItems.put(position, true);
            }
            // 해당 포지션의 변화를 알림
            if (prePosition != -1) notifyItemChanged(prePosition);
            notifyItemChanged(position);
            // 클릭된 position 저장
            prePosition = position;
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(MatchingDetailData data) {
        items.add(data);
    }


}
class MatchingDetailHolder extends RecyclerView.ViewHolder {
    TextView situation;
    TextView id_num;
    RecyclerView matchingDetailRecycle;
    RecyclerView requestRecycle;
    ConstraintLayout item_matching_recycle;
    ConstraintLayout item_matching_const;
    ArrayList<TogetherRequest> requestArrayList;
    ArrayList<MatchingMember> memberArrayList;
    TogetherRequestAdapter requestAdapter;
    MatchingMemberAdapter memberAdapter;
    int position;

    OnViewHolderItemClickListener onViewHolderItemClickListener;

    public MatchingDetailHolder(@NonNull View itemView) {
        super(itemView);
        situation = itemView.findViewById(R.id.situation);
        id_num = itemView.findViewById(R.id.id_num);
        matchingDetailRecycle = itemView.findViewById(R.id.item_matching_rv);
        requestRecycle = itemView.findViewById(R.id.together_request_rv);
        item_matching_recycle = itemView.findViewById(R.id.item_matching_recycle);
        item_matching_const = itemView.findViewById(R.id.item_matching_const);

        item_matching_recycle.setOnClickListener(v -> {
            onViewHolderItemClickListener.onViewHolderItemClick();
        });


    }
    void onBind(MatchingDetailData data, int position, SparseBooleanArray selectedItems) {
        situation.setText(data.getSituation());
        id_num.setText(data.getId_num());

        changeVisibility(selectedItems.get(position));

        LinearLayoutManager requestLinearLayoutManager = new LinearLayoutManager(itemView.getContext());
        matchingDetailRecycle.setLayoutManager(requestLinearLayoutManager);

        if (position == 1) {


            requestArrayList = new ArrayList<>();
            requestAdapter = new TogetherRequestAdapter();
            matchingDetailRecycle.setAdapter(requestAdapter);

            getRequestData();

        } else {

            memberArrayList = new ArrayList<>();
            memberAdapter = new MatchingMemberAdapter();
            matchingDetailRecycle.setAdapter(memberAdapter);

            getMemberData();
        }

        Log.d("MatchingDetailAdapter", "position" + position);
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
                item_matching_const.requestLayout();
                // imageView가 실제로 사라지게하는 부분
                item_matching_const.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            }
        });
        // Animation start
        va.start();
    }

    public void setOnViewHolderItemClickListener(OnViewHolderItemClickListener onViewHolderItemClickListener) {
        this.onViewHolderItemClickListener = onViewHolderItemClickListener;
    }

    private void getRequestData() {
        List<String> m_name = Arrays.asList(
                "박한수",
                "장원준"
        );
//        List<> m_birth = Arrays.asList(
//                "20대",
//                "10대"
//        );
        List<Integer> m_t_use = Arrays.asList(
                33,
                12
        );
        for (int i = 0; i < m_name.size(); i++) {

            TogetherRequest togetherRequest = new TogetherRequest();
            togetherRequest.setM_name(m_name.get(i));
//            togetherRequest.setM_birth(m_birth.get(i));
            togetherRequest.setM_t_use(m_t_use.get(i));

            requestAdapter.addItem(togetherRequest);
        }

        requestAdapter.notifyDataSetChanged();
    }

    private void getMemberData() {
        List<String> m_name = Arrays.asList(
                "김진수",
                "장원준",
                "강병현"
        );
        List<String> m_birth = Arrays.asList(
                "30대",
                "20대",
                "20대"
        );
        List<String> m_t_use = Arrays.asList(
                "14",
                "32",
                "27"
        );

        for (int i = 0; i < m_name.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            MatchingMember MatchingMember = new MatchingMember();
            MatchingMember.setM_name(m_name.get(i));
            MatchingMember.setM_birth(m_birth.get(i));
            MatchingMember.setM_t_use(m_t_use.get(i));

            // 각 값이 들어간 data를 adapter에 추가합니다.
            memberAdapter.addItem(MatchingMember);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        memberAdapter.notifyDataSetChanged();
    }

}






