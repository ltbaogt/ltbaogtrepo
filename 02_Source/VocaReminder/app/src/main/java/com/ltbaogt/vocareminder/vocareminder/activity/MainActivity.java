package com.ltbaogt.vocareminder.vocareminder.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.colorpicker.ColorPickerDialog;
import com.android.colorpicker.ColorPickerSwatch;
import com.google.gson.Gson;
import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.bean.ConvertWord;
import com.ltbaogt.vocareminder.vocareminder.bean.Word;
import com.ltbaogt.vocareminder.vocareminder.database.bl.OALBLL;
import com.ltbaogt.vocareminder.vocareminder.database.helper.OALDatabaseOpenHelper;
import com.ltbaogt.vocareminder.vocareminder.define.Define;
import com.ltbaogt.vocareminder.vocareminder.fragment.FragmentDialogEditWord;
import com.ltbaogt.vocareminder.vocareminder.fragment.FragmentListWord;
import com.ltbaogt.vocareminder.vocareminder.fragment.FragmentSetting;
import com.ltbaogt.vocareminder.vocareminder.provider.ProviderWrapper;
import com.ltbaogt.vocareminder.vocareminder.service.OALService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FragmentDialogEditWord.OnCreateOrUpdateWodListener {

    public static final String TAG = Define.TAG + "MainActivity";
    private final static String MAIN_FRAGMENT_TAG = "fragment_main";
    private final static String EDIT_FRAGMENT_TAG = "fragment_edit_word";
    private static final String BACKUP_FOLDER = "/reminder";
    private static final String BACKUP_FILE = "/reminder.json";
    Toolbar mToolbar;
    DrawerLayout mDrawer;

    private FragmentListWord mFragmentListWord;
    private FragmentSetting mFragmentSetting;

    private ProviderWrapper mProviderWrapper;

    private boolean mIsAttached;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer);
        setSupportActionBar(mToolbar);
        mFragmentListWord = new FragmentListWord();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, mFragmentListWord, MAIN_FRAGMENT_TAG).commit();
        setupDrawer();
        mProviderWrapper = new ProviderWrapper(getApplicationContext());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, ">>>onNewIntent START");
        super.onNewIntent(intent);
        if (intent.hasExtra(Define.EXTRA_QUICK_ADD)) {
           if(mIsAttached) {
               Word w = new Word();
               showDialogNewWord(w);
           }
        }
        Log.d(TAG, ">>>onNewIntent END");
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        mIsAttached = true;
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mIsAttached = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getIntent().hasExtra(Define.EXTRA_QUICK_ADD)) {
            Word w = new Word();
            showDialogNewWord(w);
        }
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
                case R.id.logout:
                    mainActivity.finish();
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
    public void backup() {
        OALBLL bl = new OALBLL(getApplicationContext());
        ArrayList<ConvertWord> words = convertToAnonymousWord(bl.getAllWordsOrderByNameInList());
        Gson gsonParser = new Gson();
        try {
            if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                Log.d(TAG, ">>>backupVocabulary SDCard isn't found");
                return;
            }
            String storagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + BACKUP_FOLDER;
            Log.d(TAG, ">>>backupVocabulary" + storagePath);
            File storageDirectory = new File(storagePath);
            if (storageDirectory.exists()) {
                Log.d(TAG, ">>>backupVocabulary location is found");
            } else {
                storageDirectory.mkdir();
                Log.d(TAG, ">>>backupVocabulary create new directory <reminder>");
            }
            String backFilePath = storagePath + BACKUP_FILE;
            File backupFile = new File(backFilePath);
            if (backupFile.exists()) {
                Log.d(TAG, ">>>backupVocabulary override old file");
            } else {
                backupFile.createNewFile();
            }
            FileWriter fw = new FileWriter(backupFile);
            BufferedWriter bw = new BufferedWriter(fw);
            for (ConvertWord w : words) {
                bw.write(gsonParser.toJson(w));
                bw.newLine();
            }
            bw.close();
            fw.close();
            getSharedPreferences(Define.REF_KEY, Context.MODE_PRIVATE)
                    .edit()
                    .putString(Define.BACKUP_PATH, backFilePath)
                    .commit();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void backupVocabulary(View v) {
        Log.d(TAG, ">>>backupVocabulary START");
        showConfirmDialog(R.string.popup_title_backup_vocabularies, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                backup();
            }
        });
        Log.d(TAG, ">>>backupVocabulary END");
    }

    public void restoreVocabulary(View v) {
        showConfirmDialog(R.string.popup_title_restore_vocabularies, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                restore();
            }
        });
    }

    public void restore() {
        OALDatabaseOpenHelper db = new OALDatabaseOpenHelper(getApplicationContext());
        try {
            if (db.getCount() <= 0) {
                String backupFilePath = getSharedPreferences(Define.REF_KEY, Context.MODE_PRIVATE)
                        .getString(Define.BACKUP_PATH, "");
                if (backupFilePath.isEmpty()) {
                    backupFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + BACKUP_FOLDER + BACKUP_FILE;
                }
                File backupFile = new File(backupFilePath);
                if (backupFile.exists()) {
                    BufferedReader br = new BufferedReader(new FileReader(backupFile));
                    String object;
                    Gson jsonParser = new Gson();
                    while ((object = br.readLine()) != null) {
                        Word w = ConvertWord.toWord(jsonParser.fromJson(object, ConvertWord.class));
                        db.insertWord(w);
                    }
                } else {
                    Log.d(TAG, "Cannot find backup file");
                }
            } else {
                Log.d(TAG, "You have vocabulary in database");
            }
            db.close();
        } catch (Exception e) {
            Log.d(TAG, "An Error occurs when restoring");
        }
    }
    public void startVRService() {
        mProviderWrapper.setServiceRunningStatus(1);
        startService(new Intent(this, OALService.class));
    }

    public void stopVRService() {
        mProviderWrapper.setServiceRunningStatus(0);
        stopService(new Intent(this, OALService.class));
    }

    public void chooseColor(View v) {

        int currentColor = mProviderWrapper.getColorTheme() == -1 ? ContextCompat.getColor(this, R.color.amber) : mProviderWrapper.getColorTheme();

        int[] colors = new int[]{ContextCompat.getColor(this, R.color.amber)
                , ContextCompat.getColor(this, R.color.deep_orange)
                , ContextCompat.getColor(this, R.color.purple)
                , ContextCompat.getColor(this, R.color.indigo)
                , ContextCompat.getColor(this, R.color.green_grey)
                , ContextCompat.getColor(this, R.color.teal)};
        ColorPickerDialog colorPickerDialog = new ColorPickerDialog();
        colorPickerDialog.initialize(R.string.setting_title_choose_color, colors, currentColor, 3, colors.length);
        colorPickerDialog.show(getFragmentManager(), "color_chooser");
        colorPickerDialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                mProviderWrapper.updateColorTheme(color);
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

    public ProviderWrapper getProviderWrapper() {
        return mProviderWrapper;
    }

    private ArrayList<ConvertWord> convertToAnonymousWord(ArrayList<Word> list) {
        ArrayList<ConvertWord> convertedList = new ArrayList<>();
        for (Word w : list) {
            ConvertWord cW = ConvertWord.fromWord(w);
            convertedList.add(cW);
        }
        return convertedList;
    }

    private ArrayList<Word> convertToWord(ArrayList<ConvertWord> list) {
        ArrayList<Word> convertedList = new ArrayList<>();
        for (ConvertWord w : list) {
            Word word = ConvertWord.toWord(w);
            convertedList.add(word);
        }
        return convertedList;
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawers();
            return;
        }
        super.onBackPressed();
    }
}
