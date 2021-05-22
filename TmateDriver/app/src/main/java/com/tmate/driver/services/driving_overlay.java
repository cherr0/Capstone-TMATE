package com.tmate.driver.services;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.skt.Tmap.TMapTapi;
import com.tmate.driver.R;
import com.tmate.driver.activity.PaymentActivity;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class driving_overlay extends Service implements View.OnLongClickListener {
    private View mView = null;
    private WindowManager mWm = null;
    private int root;
    Button btn_before_take;
    Button btn_take_complete;
    Button call;

    @Override
    public IBinder onBind(Intent intent) { return null; }

    @Override
    public void onCreate() {
        super.onCreate();
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

        btn_before_take =  (Button) mView.findViewById(R.id.btn_before_take);
        btn_take_complete = mView.findViewById(R.id.btn_take_complete);
        call =  (Button) mView.findViewById(R.id.overlay_call);

        btn_before_take.setOnLongClickListener(this);
        btn_take_complete.setOnLongClickListener(this);
        call.setOnLongClickListener(this);

        mWm.addView(mView, params); // 윈도우에 layout 을 추가 한다.
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
                Log.d("test","onClick ");
                TMapTapi tmaptapi = new TMapTapi(getApplication());
                boolean isTmapApp = tmaptapi.isTmapApplicationInstalled(); //앱 설치했는지 판단
                tmaptapi.invokeNavigate("", 128.5829737f, 35.8861837f,0,true);
                btn_before_take.setVisibility(View.GONE);
                break;
            case R.id.overlay_call :
                Log.d("test","onClick ");
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:012-3456-7890"));
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
        killAWindowService();
        super.onDestroy();
    }


}