package com.ltbaogt.vocareminder.vocareminder.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.android.colorpicker.ColorPickerDialog;
import com.android.colorpicker.ColorPickerSwatch;
import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.adapter.DictionaryAdapter;
import com.ltbaogt.vocareminder.vocareminder.bean.Word;
import com.ltbaogt.vocareminder.vocareminder.database.bl.OALBLL;
import com.ltbaogt.vocareminder.vocareminder.define.Define;
import com.ltbaogt.vocareminder.vocareminder.fragment.FragmentDialogEditWord;
import com.ltbaogt.vocareminder.vocareminder.fragment.FragmentEditWord;
import com.ltbaogt.vocareminder.vocareminder.fragment.FragmentMain;
import com.ltbaogt.vocareminder.vocareminder.service.OALService;
import com.ltbaogt.vocareminder.vocareminder.shareref.OALShareReferenceHepler;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FragmentDialogEditWord.OnDismissDialogListener {

    public static final String TAG = Define.TAG + "MainActivity";
    private final static String MAIN_FRAGMENT_TAG = "fragment_main";
    private final static String EDIT_FRAGMENT_TAG = "fragment_edit_word";
    Toolbar mToolbar;
    DrawerLayout mDrawer;

    private FragmentMain mFragmentMain;
    private FragmentEditWord mFragmentEditWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer);
        setSupportActionBar(mToolbar);
        mFragmentMain = new FragmentMain();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, mFragmentMain, MAIN_FRAGMENT_TAG).commit();
        setupDrawer();

    }

    private void setupDrawer() {
        NavigationView drawer = (NavigationView) findViewById(R.id.navigation_view);
        drawer.setNavigationItemSelectedListener(new NavigationViewListener(this, mDrawer, mFragmentMain, mFragmentEditWord));
    }

    @Override
    public void onDismissDialog() {
        if (mFragmentMain != null) {
            OALBLL bl = new OALBLL(this);
            ArrayList<Word> mArrayList = bl.getAllWordsOrderByName();
            if (mArrayList.size() >= 0) {
                DictionaryAdapter da = new DictionaryAdapter(this, mArrayList);
                mFragmentMain.getRecyclerView().setAdapter(da);
            }

        }
    }

    //Using static class instead of static class in order to GC
    private static class NavigationViewListener implements NavigationView.OnNavigationItemSelectedListener {

        private DrawerLayout drawer;
        private Fragment main;
        private Fragment edit;
        private MainActivity mainActivity;
        public NavigationViewListener(MainActivity c, DrawerLayout d, Fragment m, Fragment e) {
            drawer = d;
            main = m;
            edit = e;
            mainActivity = c;
        }
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            int id = item.getItemId();
            switch (id) {
                case R.id.home:
                    if (main != null) {
                        mainActivity.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_content, main, MAIN_FRAGMENT_TAG)
                                .commit();
                    }
                    drawer.closeDrawers();
                    break;
                case R.id.settings:
                    edit = new FragmentEditWord();
                    mainActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_content, edit, EDIT_FRAGMENT_TAG)
                            .commit();
                    drawer.closeDrawers();
                    break;
            }
            return true;
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, ">>>onCreateOptionsMenu START");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        Log.d(TAG, ">>>onCreateOptionsMenu END");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, ">>>onOptionsItemSelected START");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();
        int id = item.getItemId();
        switch (id) {
            case R.id.action_add:
                showDialog(getString(R.string.popup_title_new_word)
                        , getString(R.string.popup_button_cancel_word)
                        , getString(R.string.popup_button_new_word), new Word());
                break;
        }
        Log.d(TAG, ">>>onOptionsItemSelected END");
        return super.onOptionsItemSelected(item);
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
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(meaning))
        {
            Log.d(TAG, ">>>insertDB error");
            return;
        }
        Log.d(TAG, ">>>insertDB name = " + name
                + "meaning" + meaning);
        Word w = new Word();
        w.setWordName(name);
        w.setDefault_Meaning(meaning);
        OALBLL bl = new OALBLL(this);
        bl.addNewWord(w);
        etName.setText("");
        etMeaning.setText("");
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

        OALShareReferenceHepler sp = new OALShareReferenceHepler(MainActivity.this);
        int currentColor = sp.getThemeColor() == -1 ? ContextCompat.getColor(this, R.color.amber) : sp.getThemeColor();

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Release resource
        mFragmentMain = null;
        mFragmentEditWord = null;
        NavigationView drawer = (NavigationView) findViewById(R.id.navigation_view);
        drawer.setNavigationItemSelectedListener(null);
        Log.d(TAG, ">>>onDestroy START");
    }

    public void showDialog(String title, Word w) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentDialogEditWord editWordDialog = new FragmentDialogEditWord();
        Bundle b = new Bundle();
        b.putParcelable(Define.WORD_OBJECT_PARCELABLE, w);
        b.putString(Define.POPUP_TITLE, title);
        editWordDialog.setArguments(b);
        editWordDialog.show(fm, "tag");
        editWordDialog.setOnDismissDialogListener(this);
    }

    public void showDialog(String title, String btn1, String btn2, Word w) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentDialogEditWord editWordDialog = new FragmentDialogEditWord();
        Bundle b = new Bundle();
        b.putParcelable(Define.WORD_OBJECT_PARCELABLE, w);
        b.putString(Define.POPUP_TITLE, title);
        b.putString(Define.POPUP_BUTTON_01, btn1);
        b.putString(Define.POPUP_BUTTON_02, btn2);
        editWordDialog.setArguments(b);
        editWordDialog.show(fm, "tag");
    }

}
