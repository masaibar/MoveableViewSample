package com.masaibar.movableviewsample;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button_start_mini).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilterService.start(getApplicationContext());
            }
        });

        findViewById(R.id.button_stop_mini).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilterService.stop(getApplicationContext());
                Point size = DisplayUtil.getWindowSize(getApplicationContext());
                Log.d("!!!!!", String.format("width = %s, height = %s", size.x, size.y));
            }
        });
    }
}
