package com.tmate.driver.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skt.Tmap.TMapTapi;
import com.skt.Tmap.TMapView;
import com.tmate.driver.GpsTracker;
import com.tmate.driver.R;
import com.tmate.driver.data.CallHistory;
import com.tmate.driver.data.Dispatch;
import com.tmate.driver.net.DataService;
import com.tmate.driver.services.driving_overlay;

import org.w3c.dom.Text;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WaitingAdapter extends RecyclerView.Adapter<WaitingHolder> {
    ArrayList<Dispatch> items = new ArrayList<>();
    private int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 100;
    Context context;
    private TMapView tMapView;


    private SharedPreferences pref;
    String d_id;
    String merchant_uid;
    double m_lat;
    double m_lng;
    GpsTracker gpsTracker;

    // 레트로 핏
    Call<Boolean> request;

    @NonNull
    @Override
    public WaitingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_waiting, parent, false);

        pref = context.getSharedPreferences("loginDriver", Context.MODE_PRIVATE);
        d_id = pref.getString("d_id", "");

        gpsTracker = new GpsTracker(context);
        m_lat = gpsTracker.getLatitude();
        m_lng = gpsTracker.getLongitude();

        return new WaitingHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull WaitingHolder holder, int position) {
        holder.onBind(items.get(position));
        holder.matching_btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TMapTapi tmaptapi = new TMapTapi(context);
                boolean isTmapApp = tmaptapi.isTmapApplicationInstalled(); //앱 설치했는지 판단
                if(!isTmapApp) {
                    Toast.makeText(context, "tmap 을 깔아주십시오", Toast.LENGTH_SHORT).show();
                    ArrayList result = tmaptapi.getTMapDownUrl();
                    String uri = result.get(0).toString();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(uri));
                    context.startActivity(i);
                } else {
                    merchant_uid = holder.cw_dp_id.getText().toString();
                    request = DataService.getInstance().call.modifyHistoryByDriver(merchant_uid, d_id, m_lat, m_lng);
                    request.enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if (response.code() == 200 & response.body() != null) {
                                float start_lat = Float.valueOf(holder.cw_h_s_lttd.getText().toString());
                                float start_lng = Float.valueOf(holder.cw_h_s_lngtd.getText().toString());
                                Log.d("TMAP 으로 넘어가는 위도 & 경도 : ", start_lat + "&" + start_lng);

                                checkPermission();
                                tmaptapi.invokeNavigate("", start_lng, start_lat,0,true);
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });

                }
            }
        });

    }
    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {   // 마시멜로우 이상일 경우
            if (!Settings.canDrawOverlays(context)) {              // 다른앱 위에 그리기 체크
                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, uri);
                ((Activity)context).startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            } else {
                startMain();
            }
        } else {
            startMain();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (!Settings.canDrawOverlays(context)) {
                ((Activity)context).finish();
            } else {
                startMain();
            }
        }
    }
    void startMain() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent intent = new Intent(context, driving_overlay.class);
            context.startForegroundService(intent);
        } else {
            Toast.makeText(context, "불가능", Toast.LENGTH_SHORT).show();
        }
        ((Activity)context).finish();
    }



    @Override


    public int getItemCount() {
        return items.size();
    }



    public void addItem(Dispatch data) {
        items.add(data);
    }



}

class WaitingHolder extends RecyclerView.ViewHolder {
    TextView h_flag;
    TextView cw_dp_id;
    TextView tv_distance;
    TextView personnel;
    TextView matching_s_place;
    TextView matching_e_place;
    TextView tv_do_hurry,tv_do_navi, tv_do_quiet;
    ImageView do_hurry;
    ImageView do_navi;
    ImageView do_quiet;
    Button matching_btn_accept;

    TextView matching_m_id;
    TextView cw_h_s_lttd;
    TextView cw_h_s_lngtd;
    TextView cw_h_f_lttd;
    TextView cw_h_f_lngtd;

    void onBind(Dispatch data) {

        switch (data.getDp_id().substring(18)){
            case "1":
                h_flag.setText("일반");
                break;
            default:
                h_flag.setText("동승");
                break;
        }
        // 급 운행 옵션
        Log.d("WaitingAdapter", "급가속 : " + data.getDo_hurry());
        Log.d("WaitingAdapter", "네비 : " + data.getDo_navi());
        Log.d("WaitingAdapter", "조용 : " + data.getDo_quiet());
        if (data.getDo_hurry() == 0) {
            tv_do_hurry.setText("급정거, 급가속 운행은 괜찮아요");
            do_hurry.setImageResource(R.drawable.on);
        } else {
            tv_do_hurry.setText("급정거, 급가속 운행은 싫어요");
            do_hurry.setImageResource(R.drawable.off);
        }
        // 네비게이션 유무
        if (data.getDo_navi() == 0) {
            tv_do_navi.setText("네비게이션 상관없어요");
            do_navi.setImageResource(R.drawable.on);
        } else {
            tv_do_navi.setText("네비게이션 따라 이동해 주세요");
            do_navi.setImageResource(R.drawable.off);
        }
        // 조용운전 유무
        if (data.getDo_quiet() == 0) {
            tv_do_quiet.setText("소란스러워도 괜찮아요");
            do_quiet.setImageResource(R.drawable.on);
        } else {
            tv_do_quiet.setText("조용히 가고 싶어요");
            do_quiet.setImageResource(R.drawable.off);
        }
        tv_distance.setText(String.valueOf(data.getDistance()));
        personnel.setText(String.valueOf(data.getCur_people()));
        matching_s_place.setText(data.getStart_place());
        matching_e_place.setText(data.getFinish_place());
        cw_dp_id.setText(data.getDp_id());
        cw_h_s_lttd.setText(String.valueOf(data.getStart_lat()));
        cw_h_s_lngtd.setText(String.valueOf(data.getStart_lng()));
        cw_h_f_lttd.setText(String.valueOf(data.getFinish_lat()));
        cw_h_f_lngtd.setText(String.valueOf(data.getFinish_lng()));
        matching_m_id.setText(data.getM_id());

    }

    public WaitingHolder(@NonNull View itemView) {
        super(itemView);
        h_flag = itemView.findViewById(R.id.cw_h_flag);
        tv_distance = itemView.findViewById(R.id.tv_distance);
        personnel = itemView.findViewById(R.id.personnel);
        matching_s_place = itemView.findViewById(R.id.matching_s_place);
        matching_e_place = itemView.findViewById(R.id.matching_e_place);
        do_hurry = itemView.findViewById(R.id.do_hurry);
        tv_do_hurry = itemView.findViewById(R.id.tv_do_hurry);
        do_navi = itemView.findViewById(R.id.do_navi);
        tv_do_navi = itemView.findViewById(R.id.tv_do_navi);
        do_quiet = itemView.findViewById(R.id.do_quiet);
        tv_do_quiet = itemView.findViewById(R.id.tv_do_quiet);
        matching_btn_accept = itemView.findViewById(R.id.matching_btn_accept);
        cw_dp_id = itemView.findViewById(R.id.cw_dp_id);
        cw_h_s_lttd = itemView.findViewById(R.id.cw_h_s_lttd);
        cw_h_s_lngtd = itemView.findViewById(R.id.cw_h_s_lngtd);
        cw_h_f_lttd = itemView.findViewById(R.id.cw_h_f_lttd);
        cw_h_f_lngtd = itemView.findViewById(R.id.cw_h_f_lngtd);
        matching_m_id = itemView.findViewById(R.id.matching_m_id);

    }
}
