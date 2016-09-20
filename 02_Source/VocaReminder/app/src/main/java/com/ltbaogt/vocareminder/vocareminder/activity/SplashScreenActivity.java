package com.ltbaogt.vocareminder.vocareminder.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.database.helper.OALDatabaseOpenHelper;
import com.ltbaogt.vocareminder.vocareminder.define.Define;

/**
 * Created by MyPC on 20/09/2016.
 */
public class SplashScreenActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen_layout);
        AssetManager assetManager = getApplicationContext().getAssets();
        Typeface typeface = Typeface.createFromAsset(assetManager, Define.TYPE_FACE_BOLD);
        TextView tvName = (TextView) findViewById(R.id.app_name);
        tvName.setTypeface(typeface);
        typeface = Typeface.createFromAsset(assetManager, Define.TYPE_FACE_REGILAR);
        TextView tvSlogan = (TextView) findViewById(R.id.slogan);
        tvSlogan.setTypeface(typeface);
    }

    @Override
    protected void onResume() {
        super.onResume();
        OALDatabaseOpenHelper db = new OALDatabaseOpenHelper(this);
        db.openDatabase();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivity);
                finish();
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        }, 2000);
    }
}
