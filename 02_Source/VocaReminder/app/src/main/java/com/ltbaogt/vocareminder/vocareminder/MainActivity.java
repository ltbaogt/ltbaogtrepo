package com.ltbaogt.vocareminder.vocareminder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.ltbaogt.vocareminder.vocareminder.bean.Word;
import com.ltbaogt.vocareminder.vocareminder.database.bl.OALBLL;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = Define.TAG + "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void createNewDB(View v) {
        OALBLL.delDatabase(this);
        OALBLL.initDatabase(this);
    }

    public void insertDB(View v) {
        EditText etName = (EditText) findViewById(R.id.et_name);
        EditText etMeaning = (EditText) findViewById(R.id.et_meaning);
        String name = etName.getText().toString();
        String meaning = etMeaning.getText().toString();
        Log.d(TAG, ">>>insertDB name = " + name
                + "meaning" + meaning);
        Word w = new Word();
        w.setWordName(name);
        w.setDefault_Meaning(meaning);
        OALBLL bl = new OALBLL(this);
        bl.addNewWord(w);
    }

    public void startVRService(View v) {
        if (!VRService.isStarted()) {
            startService(new Intent(this, VRService.class));
        }
    }

    public void stopVRService(View v) {
        stopService(new Intent(this, VRService.class));
    }

    public void chooseColor(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout viewGroup = (LinearLayout) findViewById(R.id.popup_outer);
        View popup_layout = inflater.inflate(R.layout.popup_color_chooser_layout, viewGroup, false);

        PopupWindow colorChooserPopup = new PopupWindow(this);
        colorChooserPopup.setContentView(popup_layout);
        colorChooserPopup.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        colorChooserPopup.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        colorChooserPopup.setFocusable(true);
        int OFFSET_X = -20;
        int OFFSET_Y = 95;
        colorChooserPopup.showAtLocation(popup_layout, Gravity.NO_GRAVITY, 200, 200);


    }
}
