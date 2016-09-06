package com.ltbaogt.vocareminder.vocareminder.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.colorpicker.ColorPickerDialog;
import com.android.colorpicker.ColorPickerSwatch;
import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.bean.Word;
import com.ltbaogt.vocareminder.vocareminder.database.bl.OALBLL;
import com.ltbaogt.vocareminder.vocareminder.database.helper.OALDatabaseOpenHelper;
import com.ltbaogt.vocareminder.vocareminder.define.Define;
import com.ltbaogt.vocareminder.vocareminder.fragment.FragmentDialogEditWord;
import com.ltbaogt.vocareminder.vocareminder.fragment.FragmentSetting;
import com.ltbaogt.vocareminder.vocareminder.fragment.FragmentListWord;
import com.ltbaogt.vocareminder.vocareminder.listener.OALSimpleOnGestureListener;
import com.ltbaogt.vocareminder.vocareminder.service.OALService;
import com.ltbaogt.vocareminder.vocareminder.shareref.OALShareReferenceHepler;

public class MainActivity extends AppCompatActivity implements FragmentDialogEditWord.OnCreateOrUpdateWodListener {

    public static final String TAG = Define.TAG + "MainActivity";
    private final static String MAIN_FRAGMENT_TAG = "fragment_main";
    private final static String EDIT_FRAGMENT_TAG = "fragment_edit_word";
    Toolbar mToolbar;
    DrawerLayout mDrawer;

    private FragmentListWord mFragmentListWord;
    private FragmentSetting mFragmentSetting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        OALDatabaseOpenHelper db = new OALDatabaseOpenHelper(this);
        db.openDatabase();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer);
        setSupportActionBar(mToolbar);
        mFragmentListWord = new FragmentListWord();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, mFragmentListWord, MAIN_FRAGMENT_TAG).commit();
        setupDrawer();

    }

    private void setupDrawer() {
        NavigationView drawer = (NavigationView) findViewById(R.id.navigation_view);
        drawer.setNavigationItemSelectedListener(new NavigationViewListener(this, mDrawer, mFragmentListWord, mFragmentSetting));
    }

    @Override
    public void onSave(Word w) {
        Log.d(TAG, ">>>onSave SRART");
        //Fragment that is holding RecyclerView.
        // This fragment will invoke RecyclerView get its adapter to add new Word to Adapter's ArrayList
        mFragmentListWord.addNewWord(w);
        Log.d(TAG, ">>>onSave END");
    }

    @Override
    public void onUpdate(Word w) {
        //Fragment that is holding RecyclerView.
        // This fragment will invoke RecyclerView get its adapter to update Word in Adapter's ArrayList
        mFragmentListWord.updateWord(w);
    }

    //Using static class instead of static class in order to GC
    private static class NavigationViewListener implements NavigationView.OnNavigationItemSelectedListener {

        private DrawerLayout drawer;
        private Fragment list;
        private Fragment setting;
        private MainActivity mainActivity;
        public NavigationViewListener(MainActivity c, DrawerLayout d, Fragment m, Fragment e) {
            drawer = d;
            list = m;
            setting = e;
            mainActivity = c;
        }
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            int id = item.getItemId();
            switch (id) {
                case R.id.home:
                    if (list != null) {
                        mainActivity.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_content, list, MAIN_FRAGMENT_TAG)
                                .commit();
                        if (list instanceof FragmentListWord) {
                            ((FragmentListWord) list).hideTagPanel();
                        }
                    }
                    drawer.closeDrawers();
                    break;
                case R.id.settings:
                    setting = new FragmentSetting();
                    mainActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_content, setting, EDIT_FRAGMENT_TAG)
                            .commit();
                    drawer.closeDrawers();
                    break;
                case R.id.tag:
                    drawer.closeDrawers();
                    if (list != null) {
                        ((FragmentListWord) list).toggleTagPanel();
                    }
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
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView)menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mFragmentListWord.getWordAdapter().getFilter().filter(newText);
                return true;
            }
        });

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
                showDialogNewWord(new Word());
                break;
            case R.id.action_filter_raw_word:
                if (mFragmentListWord != null) {
                    if (getString(R.string.action_filter_raw_word)
                            .equalsIgnoreCase(item.getTitle().toString())) {
                        mFragmentListWord.filterRawWords();
                        item.setTitle(R.string.action_filter_no_filter);
                    } else if (getString(R.string.action_filter_no_filter)
                            .equalsIgnoreCase(item.getTitle().toString())) {
                        mFragmentListWord.noFilter();
                        item.setTitle(R.string.action_filter_raw_word);
                    }
                }
                break;
            case R.id.action_preview:
                Intent previewIntent = new Intent(Define.VOCA_ACTION_OPEN_VOCA_REMINDER);
                sendBroadcast(previewIntent);
                break;
        }
        Log.d(TAG, ">>>onOptionsItemSelected END");
        return super.onOptionsItemSelected(item);
    }

    public void createNewDB(View v) {
        showConfirmDialog(R.string.clean_up_vocabularies, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                OALBLL.delDatabase(MainActivity.this);
                OALBLL.initDatabase(MainActivity.this);
            }
        });
    }

    public void startVRService() {
        if (!OALService.isStarted()) {
            startService(new Intent(this, OALService.class));
        }
    }

    public void stopVRService() {
        stopService(new Intent(this, OALService.class));
    }

    public void chooseColor(View v) {

        OALShareReferenceHepler sp = new OALShareReferenceHepler(MainActivity.this);
        int currentColor = sp.getThemeColor() == -1 ? ContextCompat.getColor(this, R.color.amber) : sp.getThemeColor();

        int[] colors = new int[]{ContextCompat.getColor(this, R.color.amber)
                , ContextCompat.getColor(this, R.color.deep_orange)
                , ContextCompat.getColor(this, R.color.purple)
                , ContextCompat.getColor(this, R.color.indigo)
                , ContextCompat.getColor(this, R.color.green_grey)
                , ContextCompat.getColor(this, R.color.teal)};
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
        mFragmentListWord = null;
        mFragmentSetting = null;
        NavigationView drawer = (NavigationView) findViewById(R.id.navigation_view);
        drawer.setNavigationItemSelectedListener(null);
        Log.d(TAG, ">>>onDestroy START");
    }

    public void showDialog(Bundle b) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentDialogEditWord editWordDialog = new FragmentDialogEditWord();
        editWordDialog.setArguments(b);
        editWordDialog.show(fm, "tag");
        editWordDialog.setOnCreateOrUpdateWodListener(this);
    }

    public void showDialogNewWord(Word w) {
        Bundle b = new Bundle();
        b.putParcelable(Define.WORD_OBJECT_PARCELABLE, w);
        b.putInt(Define.POPUP_TYPE, Define.POPUP_NEW_WORD);
        b.putString(Define.POPUP_TITLE, getString(R.string.popup_title_new_word));
        b.putString(Define.POPUP_BUTTON_01, getString(R.string.popup_button_cancel_word));
        b.putString(Define.POPUP_BUTTON_02, getString(R.string.popup_button_new_word));
        showDialog(b);
    }
    public void showDialogEditWord(Word w) {
        Bundle b = new Bundle();
        b.putParcelable(Define.WORD_OBJECT_PARCELABLE, w);
        b.putInt(Define.POPUP_TYPE, Define.POPUP_EIDT_WORD);
        b.putString(Define.POPUP_TITLE, getString(R.string.popup_title_edit_word));
        b.putString(Define.POPUP_BUTTON_01, getString(R.string.popup_button_cancel_word));
        b.putString(Define.POPUP_BUTTON_02, getString(R.string.popup_button_edit_word));
        showDialog(b);
    }

    public void showConfirmDialog(int resId, DialogInterface.OnClickListener okAction) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(resId);
        dialog.setNegativeButton(R.string.btn_discard, null);
        dialog.setPositiveButton(R.string.btn_ok, okAction);
        dialog.show();
    }

}
