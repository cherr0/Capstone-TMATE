package com.tmate.driver.services;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.skt.Tmap.TMapTapi;
import com.tmate.driver.GpsTracker;
import com.tmate.driver.R;
import com.tmate.driver.activity.PaymentActivity;
import com.tmate.driver.data.Dispatch;
import com.tmate.driver.net.DataService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class driving_overlay extends Service implements View.OnLongClickListener {
    private View mView = null;
    private WindowManager mWm = null;
    private int root;
    Button btn_before_take;
    Button btn_take_complete;
    Button call;

    // 레트로핏 관련
    // 탑승 대기중 -> 탑승 중
    Call<Dispatch> request;
    double finish_lat;
    double finish_lng;

    // 회원코드를 가져온다.
    Call<String> request3;
    String memberPhoneNo;

    SharedPreferences pref;
    String d_id;

    // 쓰레드 관련
    GpsTracker gpsTracker;
    Call<Boolean> request2;
    private boolean isRunning;
    Handler handler;
    Positioning positioning;

    @Override
    public IBinder onBind(Intent intent) { return null; }

    @Override
    public void onCreate() {
        super.onCreate();

        pref = getSharedPreferences("loginDriver", MODE_PRIVATE);
        d_id = pref.getString("d_id", "");
        Log.d("잘 찍히 나요? ", d_id);

        // Android O 이상일 경우 Foreground 서비스를 실행
        // Notification channel 설정.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final String strId = getString(R.string.app_name);
            final String strTitle = getString(R.string.app_name);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = notificationManager.getNotificationChannel(strId);
            if (channel == null) {
                channel = new NotificationChannel(strId, strTitle, NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
            }

            Notification notification = new NotificationCompat.Builder(this, strId).build();
            startForeground(1, notification);
        }

        LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflater 를 사용하여 layout 을 가져오자
        mWm = (WindowManager) getSystemService(WINDOW_SERVICE);
        // 윈도우매니저 설정

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O?
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                // Android O 이상인 경우 TYPE_APPLICATION_OVERLAY 로 설정
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        |WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);


        params.gravity = Gravity.RIGHT;
        // 위치 지정
        mView = inflate.inflate(R.layout.overlay_button, null);
        // view_in_service.xml layout 불러오기
        // mView.setOnTouchListener(onTouchListener);
        // Android O 이상의 버전에서는 터치리스너가 동작하지 않는다. ( TYPE_APPLICATION_OVERLAY 터치 미지원)

        // 쓰레드 작업
        handler = new Handler();
        positioning = new Positioning();
        isRunning = true;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isRunning) {
                        handler.post(positioning);
                        Thread.sleep(1000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        isRunning = true;
        thread.start();

        btn_before_take =  (Button) mView.findViewById(R.id.btn_before_take);
        btn_take_complete = mView.findViewById(R.id.btn_take_complete);
        call =  (Button) mView.findViewById(R.id.overlay_call);

        btn_before_take.setOnLongClickListener(this);
        btn_take_complete.setOnLongClickListener(this);
        call.setOnLongClickListener(this);

        mWm.addView(mView, params); // 윈도우에 layout 을 추가 한다.
    }

    public void getM_idFromServer(String d_id) {
        request3 = DataService.getInstance().call.getUsingM_idByD_id(d_id);
        request3.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 200 && response.body() != null) {
                    String m_id = response.body();
                    memberPhoneNo = m_id.substring(2, 5) + "-" + m_id.substring(5, 9) + "-" + m_id.substring(9, 13);
                    Log.d("이용중인 회원 번호 : ", memberPhoneNo);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public class Positioning implements Runnable {
        @Override
        public void run() {
            gpsTracker = new GpsTracker(driving_overlay.this);
            double m_lat = gpsTracker.getLatitude();
            double m_lng = gpsTracker.getLongitude();
            request2 = DataService.getInstance().call.modifyDriverPosition(m_lat, m_lng, d_id);
            request2.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.code() == 200 && response.body() != null) {
                        Toast.makeText(driving_overlay.this, "현재 기사 위도 : " + m_lat, Toast.LENGTH_SHORT).show();
                        Toast.makeText(driving_overlay.this, "현재 기사 경도 : " + m_lng, Toast.LENGTH_SHORT).show();
                        isRunning = true;
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }


    private void killAWindowService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true); // Foreground service 종료
        }

        if(mView != null) {
            mWm.removeView(mView); // View 초기화
            mView = null;
        }
        mWm = null;
    }
    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()){
            case R.id.btn_before_take :

                request = DataService.getInstance().call.modifyDispatchBoarding(d_id);
                request.enqueue(new Callback<Dispatch>() {
                    @Override
                    public void onResponse(Call<Dispatch> call, Response<Dispatch> response) {
                        if (response.code() == 200 && response.body() != null) {
                            Dispatch body = response.body();
                            Log.d("넘어오는 도착지 정보 : " ,body.toString());
                            finish_lat = body.getFinish_lat();
                            finish_lng = body.getFinish_lng();
                            Log.d("test","onClick ");
                            TMapTapi tmaptapi = new TMapTapi(getApplication());
                            Log.d("도착지 위도", String.valueOf(finish_lat));
                            Log.d("도착지 경도", String.valueOf(finish_lng));
                            tmaptapi.invokeNavigate("", (float) finish_lng, (float) finish_lat,0,true);
                            btn_before_take.setVisibility(View.GONE);
                            btn_take_complete.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<Dispatch> call, Throwable t) {
                        t.printStackTrace();
                    }
                });

                break;
            case R.id.overlay_call :
                Log.d("test","onClick ");
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+memberPhoneNo));
                startActivity(mIntent.addFlags(FLAG_ACTIVITY_NEW_TASK));
                break;
            case R.id.btn_take_complete :
                Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));
                ActivityManager am = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
                am.killBackgroundProcesses (getPackageName());
                stopSelf();
                break;
        }
        return false;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        killAWindowService();
        if(request != null) request.cancel();
        if(request2 != null) request2.cancel();

    }


}