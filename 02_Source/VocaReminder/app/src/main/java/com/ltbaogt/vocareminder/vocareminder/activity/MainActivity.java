package com.ltbaogt.vocareminder.vocareminder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.colorpicker.ColorPickerDialog;
import com.android.colorpicker.ColorPickerSwatch;
import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.bean.Word;
import com.ltbaogt.vocareminder.vocareminder.database.bl.OALBLL;
import com.ltbaogt.vocareminder.vocareminder.define.Define;
import com.ltbaogt.vocareminder.vocareminder.service.OALService;
import com.ltbaogt.vocareminder.vocareminder.shareref.OALShareReferenceHepler;

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
        if (!OALService.isStarted()) {
            startService(new Intent(this, OALService.class));
        }
    }

    public void stopVRService(View v) {
        stopService(new Intent(this, OALService.class));
    }

    public void chooseColor(View v) {
//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        //LinearLayout viewGroup = (LinearLayout) findViewById(R.id.popup_outer);
//        View popup_layout = inflater.inflate(R.layout.popup_color_chooser_layout, null);
//
//        PopupWindow colorChooserPopup = new PopupWindow(this);
//        colorChooserPopup.setContentView(popup_layout);
//        colorChooserPopup.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
//        colorChooserPopup.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
//        colorChooserPopup.setFocusable(true);
//        colorChooserPopup.showAtLocation(popup_layout, Gravity.NO_GRAVITY, 200, 200);

        OALShareReferenceHepler sp = new OALShareReferenceHepler(MainActivity.this);
        int currentColor = sp.getThemeColor() == -1 ?ContextCompat.getColor(this, R.color.amber):sp.getThemeColor();

        int[] colors = new int[]{ContextCompat.getColor(this, R.color.amber), ContextCompat.getColor(this, R.color.deep_orange), ContextCompat.getColor(this, R.color.purple)};
        ColorPickerDialog colorPickerDialog = new ColorPickerDialog();
        colorPickerDialog.initialize(R.string.app_name, colors, currentColor, 3, colors.length);
        colorPickerDialog.show(getFragmentManager(), "color_chooser");
        colorPickerDialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                OALShareReferenceHepler sp = new OALShareReferenceHepler(MainActivity.this);
                sp.setThemeColor(color);
            }
        });
    }
}
