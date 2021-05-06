package com.tmate.driver.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
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
import com.tmate.driver.R;
import com.tmate.driver.data.Waiting;
import com.tmate.driver.services.driving_overlay;

import java.util.ArrayList;

public class WaitingAdapter extends RecyclerView.Adapter<WaitingHolder> {
    ArrayList<Waiting> items = new ArrayList<>();
    private int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 100;
    Context context;
    private TMapView tMapView = null;


    @NonNull
    @Override
    public WaitingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_waiting, parent, false);
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
                    checkPermission();
                    tmaptapi.invokeNavigate("", 128.5058153f, 35.8356814f,0,true);
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



    public void addItem(Waiting data) {
        items.add(data);
    }



}

class WaitingHolder extends RecyclerView.ViewHolder {
    TextView merchant_uid;
    TextView tv_distance;
    TextView personnel;
    TextView matching_s_place;
    TextView matching_e_place;
    Button matching_btn_refusal;
    Button matching_btn_accept;

    void onBind(Waiting data) {
        merchant_uid.setText(data.getMerchant_uid());
        tv_distance.setText(data.getTv_distance());
        personnel.setText(data.getPersonnel());
        matching_s_place.setText(data.getMatching_s_place());
        matching_e_place.setText(data.getMatching_e_place());
    }

    public WaitingHolder(@NonNull View itemView) {
        super(itemView);
        merchant_uid = itemView.findViewById(R.id.merchant_uid);
        tv_distance = itemView.findViewById(R.id.tv_distance);
        personnel = itemView.findViewById(R.id.personnel);
        matching_s_place = itemView.findViewById(R.id.matching_s_place);
        matching_e_place = itemView.findViewById(R.id.matching_e_place);
        matching_btn_accept = itemView.findViewById(R.id.matching_btn_accept);
        matching_btn_refusal = itemView.findViewById(R.id.matching_btn_refusal);

    }
}
