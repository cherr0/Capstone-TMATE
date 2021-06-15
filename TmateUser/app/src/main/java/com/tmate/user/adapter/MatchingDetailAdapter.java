package com.tmate.user.adapter;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.user.Fragment.MatchingDetailFragment;
import com.tmate.user.R;
import com.tmate.user.data.Attend;
import com.tmate.user.data.MatchingDetailData;
import com.tmate.user.data.MatchingMember;
import com.tmate.user.data.TogetherRequest;
import com.tmate.user.net.DataService;
import com.tmate.user.ui.driving.DrivingModel;

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

    public DrivingModel mViewModel;

    public MatchingDetailAdapter(DrivingModel mViewModel) {
        this.mViewModel = mViewModel;
    }


    @NonNull
    @Override

    public MatchingDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //전개자(Inflater)를 통해 얻은 참조 객체를 통해 뷰홀더 객체 생성
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_matching_recycle, parent, false);
        return new MatchingDetailHolder(view,mViewModel);
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
    TextView mf_dp_id;
    int position;
    DrivingModel mViewModel;

    // 레트로핏
    Call<List<Attend>> getApplyerList;
    Call<List<Attend>> getPassengerList;
    String dp_id;



    OnViewHolderItemClickListener onViewHolderItemClickListener;

    public MatchingDetailHolder(@NonNull View itemView, DrivingModel model) {
        super(itemView);
        situation = itemView.findViewById(R.id.situation);
        id_num = itemView.findViewById(R.id.id_num);
        matchingDetailRecycle = itemView.findViewById(R.id.item_matching_rv);
        requestRecycle = itemView.findViewById(R.id.together_request_rv);
        item_matching_recycle = itemView.findViewById(R.id.item_matching_recycle);
        item_matching_const = itemView.findViewById(R.id.item_matching_const);
        mf_dp_id = itemView.findViewById(R.id.mf_dp_id);
        this.mViewModel = model;
        dp_id = this.mViewModel.dispatch.getDp_id();

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

    // 신청자 리스트
    private void getRequestData() {
        getApplyerList = DataService.getInstance().matchAPI.getApplyerList(dp_id);
        getApplyerList.enqueue(new Callback<List<Attend>>() {
            @Override
            public void onResponse(Call<List<Attend>> call, Response<List<Attend>> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        List<Attend> body = response.body();
                        for (int i = 0; i < body.size(); i++) {
                            TogetherRequest data = new TogetherRequest();
                            data.setDp_id(body.get(i).getDp_id());
                            data.setM_id(body.get(i).getM_id());
                            data.setM_birth(body.get(i).getM_birth());
                            Log.d("찍히는 생년월일 : ", String.valueOf(data.getM_birth()));
                            data.setM_name(body.get(i).getM_name());
                            data.setM_t_use(body.get(i).getM_t_use() + body.get(i).getM_n_use());
                            requestAdapter.addItem(data);
                        }
                        requestAdapter.notifyDataSetChanged();
                    }
                } else{
                    requestAdapter.addItem(null);
                }
            }

            @Override
            public void onFailure(Call<List<Attend>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    // 동승자 리스트
    private void getMemberData() {

        getPassengerList = DataService.getInstance().matchAPI.getPassengerList(dp_id);
        getPassengerList.enqueue(new Callback<List<Attend>>() {
            @Override
            public void onResponse(Call<List<Attend>> call, Response<List<Attend>> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        List<Attend> list = response.body();
                        for (int i = 0; i < list.size(); i++) {
                            MatchingMember member = new MatchingMember();
                            member.setDp_id(list.get(i).getDp_id());
                            member.setM_id(list.get(i).getM_id());
                            member.setM_name(list.get(i).getM_name());
                            member.setM_birth(list.get(i).getM_birth());
                            member.setM_t_use(list.get(i).getM_t_use() + list.get(i).getM_n_use() + "");
                            memberAdapter.addItem(member);
                        }
                        memberAdapter.notifyDataSetChanged();
                    } else {
                        memberAdapter.addItem(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Attend>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
}






