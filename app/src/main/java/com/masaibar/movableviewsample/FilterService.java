package com.masaibar.movableviewsample;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import static android.view.WindowManager.*;


public class FilterService extends Service {

    public static void start(Context context) {
        Intent intent = new Intent(context, FilterService.class);
        context.startService(intent);
    }

    public static void stop(Context context) {
        Intent intent = new Intent(context, FilterService.class);
        context.stopService(intent);
    }

    private View mView;
    private boolean mIsLongClicked = false;
    private WindowManager mWindowManager;
    private LayoutParams mLayoutParams;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        addMiniFilter();

        return START_STICKY;
    }

    private void addMiniFilter() {
        Context context = getApplicationContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        final Point size = DisplayUtil.getWindowSize(context);
        final int width = size.x;
        final int height = size.y;

         mLayoutParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                LayoutParams.TYPE_SYSTEM_ALERT,//アラートレイヤーに表示
                LayoutParams.FLAG_NOT_FOCUSABLE |
                        LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT
        );

        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        mView = layoutInflater.inflate(R.layout.layout_filter_mini, null);
        mView.setBackgroundColor(Color.argb(100, 0, 0, 0));
        mWindowManager.addView(mView, mLayoutParams);

        mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.d("!!!", "onLongClicked");
                mIsLongClicked = true;
                return true;
            }
        });

        mView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        onActionMove(width, height, mLayoutParams, motionEvent);
                        break;
                    case MotionEvent.ACTION_UP:
                        onActionUp();
                        break;

                    default:
                        break;
                }

                return false; //trueだとOnLongClickedが呼ばれなくなってしまう
            }
        });
    }

    private void onActionMove(int width, int height, LayoutParams params, MotionEvent event) {
        if (!mIsLongClicked) {
            return;
        }

        int x = (int) event.getRawX();
        int y = (int) event.getRawY();

        int centerX = x - (width / 2);
        int centerY = y - (height / 2);

        params.x = centerX;
        params.y = centerY;

        mWindowManager.updateViewLayout(mView, params);

        Log.d("!!!", String.format("x = %s, y = %s", x, y));
    }

    private void onActionUp() {
        mIsLongClicked = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWindowManager != null) {
            mWindowManager.removeView(mView);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
