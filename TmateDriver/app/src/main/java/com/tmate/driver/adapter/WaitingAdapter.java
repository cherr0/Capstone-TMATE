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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skt.Tmap.TMapTapi;
import com.skt.Tmap.TMapView;
import com.tmate.driver.GpsTracker;
import com.tmate.driver.R;
import com.tmate.driver.data.CallHistory;
import com.tmate.driver.net.DataService;
import com.tmate.driver.services.driving_overlay;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WaitingAdapter extends RecyclerView.Adapter<WaitingHolder> {
    ArrayList<CallHistory> items = new ArrayList<>();
    private int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 100;
    Context context;
    private TMapView tMapView = null;


    private SharedPreferences pref;
    String d_id;
    String merchant_uid;
    double m_lttd;
    double m_lngtd;
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
        m_lttd = gpsTracker.getLatitude();
        m_lngtd = gpsTracker.getLongitude();

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
                    Toast.makeText(context, "tmap을 깔아주십시오", Toast.LENGTH_SHORT).show();
                    ArrayList result = tmaptapi.getTMapDownUrl();
                    String uri = result.get(0).toString();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(uri));
                    context.startActivity(i);
                    System.out.println("되니??" + result);
                } else {
                    merchant_uid = holder.cw_merchant_uid.getText().toString();
                    request = DataService.getInstance().call.modifyHistoryByDriver(merchant_uid, d_id, m_lttd, m_lngtd);
                    request.enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if (response.code() == 200 & response.body() != null) {
                                float h_s_lttd = Float.valueOf(holder.cw_h_s_lttd.getText().toString());
                                float h_s_lngtd = Float.valueOf(holder.cw_h_s_lngtd.getText().toString());
                                Log.d("TMAP으로 넘어가는 위도&경도 : ", h_s_lttd + "&" + h_s_lngtd);

                                checkPermission();
                                tmaptapi.invokeNavigate("", h_s_lngtd,h_s_lttd,0,true);
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
            intent.putExtra("경로",3);
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



    public void addItem(CallHistory data) {
        items.add(data);
    }



}

class WaitingHolder extends RecyclerView.ViewHolder {
    TextView h_flag;
    TextView cw_merchant_uid;
    TextView tv_distance;
    TextView personnel;
    TextView matching_s_place;
    TextView matching_e_place;
    Button matching_btn_refusal;
    Button matching_btn_accept;


    TextView cw_h_s_lttd;
    TextView cw_h_s_lngtd;
    TextView cw_h_f_lttd;
    TextView cw_h_f_lngtd;

    void onBind(CallHistory data) {

        switch (data.getMerchant_uid().substring(18)){
            case "1":
                h_flag.setText("일반");
                break;
            default:
                h_flag.setText("동승");
                break;
        }

        tv_distance.setText(String.valueOf(data.getDistance1()));
        personnel.setText(String.valueOf(data.getTo_people()));
        matching_s_place.setText(data.getH_s_place());
        matching_e_place.setText(data.getH_f_place());
        cw_merchant_uid.setText(data.getMerchant_uid());
        cw_h_s_lttd.setText(String.valueOf(data.getH_s_lttd()));
        cw_h_s_lngtd.setText(String.valueOf(data.getH_s_lngtd()));
        cw_h_f_lttd.setText(String.valueOf(data.getH_f_lttd()));
        cw_h_f_lngtd.setText(String.valueOf(data.getH_f_lngtd()));

    }

    public WaitingHolder(@NonNull View itemView) {
        super(itemView);
        h_flag = itemView.findViewById(R.id.cw_h_flag);
        tv_distance = itemView.findViewById(R.id.tv_distance);
        personnel = itemView.findViewById(R.id.personnel);
        matching_s_place = itemView.findViewById(R.id.matching_s_place);
        matching_e_place = itemView.findViewById(R.id.matching_e_place);
        matching_btn_accept = itemView.findViewById(R.id.matching_btn_accept);
        matching_btn_refusal = itemView.findViewById(R.id.matching_btn_refusal);
        cw_merchant_uid = itemView.findViewById(R.id.cw_merchant_uid);
        cw_h_s_lttd = itemView.findViewById(R.id.cw_h_s_lttd);
        cw_h_s_lngtd = itemView.findViewById(R.id.cw_h_s_lngtd);
        cw_h_f_lttd = itemView.findViewById(R.id.cw_h_f_lttd);
        cw_h_f_lngtd = itemView.findViewById(R.id.cw_h_f_lngtd);

    }
}
