package com.ltbaogt.vocareminder.vocareminder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startVRService(View v) {
        if (!VRService.isStarted()) {
            startService(new Intent(this, VRService.class));
        }
    }

    public void stopVRService(View v) {
        stopService(new Intent(this, VRService.class));
    }
}
