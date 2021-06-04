//package com.tmate.user.Fragment;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.tmate.user.R;
//import com.tmate.user.adapter.TogetherRequestAdapter;
//import com.tmate.user.data.TogetherRequest;
//import com.tmate.user.net.DataService;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class TogetherRequestFragment extends Fragment {
//    private TogetherRequestAdapter adapter;
//
//    private ArrayList<TogetherRequest> arrayList;
//
//    Call<List<TogetherRequest>> request;
//
//    private String merchant_uid;
//
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//        View v = inflater.inflate(R.layout.fragment_together_request,container,false);
//
//        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.together_request_rv);
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(linearLayoutManager);
//
//        arrayList = new ArrayList<>();
//
//        adapter = new TogetherRequestAdapter();
//        recyclerView.setAdapter(adapter);
//
//        if (getArguments() != null) {
//            merchant_uid = getArguments().getString("merchant_uid");
//        }
//
//        getData();
//        return v;
//
//    }
//
//    private void getData() {
//
//        request = DataService.getInstance().matchAPI.getTogetherRequest(merchant_uid);
//
//        request.enqueue(new Callback<List<TogetherRequest>>() {
//            @Override
//            public void onResponse(Call<List<TogetherRequest>> call, Response<List<TogetherRequest>> response) {
//                if (response.code() == 200) {
//                    List<TogetherRequest> list = response.body();
//                    Log.i("넘어오는 리스트", list.toString());
//                    for (int i = 0; i < list.size(); i++) {
//
//                        TogetherRequest togetherRequest = new TogetherRequest();
//                        togetherRequest.setDislike("0");
//                        togetherRequest.setLike("0");
//                        togetherRequest.setMerchant_uid(list.get(i).getMerchant_uid());
//                        togetherRequest.setM_n_use(list.get(i).getM_n_use());
//                        togetherRequest.setM_t_use(list.get(i).getM_t_use());
//                        togetherRequest.setId(list.get(i).getId());
//                        togetherRequest.setM_name(list.get(i).getM_name());
//                        togetherRequest.setM_birth(list.get(i).getM_birth());
//
//                        adapter.addItem(togetherRequest);
//                    }
//
//                    adapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<TogetherRequest>> call, Throwable t) {
//                t.printStackTrace();
//            }
//        });
//    }
//}