package com.ltbaogt.vocareminder.vocareminder.activity;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
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
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.colorpicker.ColorPickerDialog;
import com.android.colorpicker.ColorPickerSwatch;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.gson.Gson;
import com.ltbaogt.vocareminder.vocareminder.R;
import com.ltbaogt.vocareminder.vocareminder.bean.ConvertWord;
import com.ltbaogt.vocareminder.vocareminder.bean.Word;
import com.ltbaogt.vocareminder.vocareminder.database.bl.OALBLL;
import com.ltbaogt.vocareminder.vocareminder.database.helper.OALDatabaseOpenHelper;
import com.ltbaogt.vocareminder.vocareminder.define.Define;
import com.ltbaogt.vocareminder.vocareminder.fragment.FragmentAbout;
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
    private final static String ABOUT_FRAGMENT_TAG = "fragment_about";
    private final static String ARCHIVED_FRAGMENT_TAG = "fragment_archived";
    public static final String BACKUP_FOLDER = "/reminder";
    public static final String BACKUP_FILE = "/reminder.voca";
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_FOR_BACKUP = 1;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_FOR_RESOTRE = 2;
    public static final int REQUEST_CODE_SETTING_DRAW_OVERLAY = 3;
    Toolbar mToolbar;
    DrawerLayout mDrawer;

    private FragmentListWord mFragmentListWord;
    private FragmentSetting mFragmentSetting;

    private ProviderWrapper mProviderWrapper;

    private boolean mIsAttached;

    private FragmentDialogEditWord mNewOrEditDialog;
    private int mNumberOfClick;

    private InterstitialAd mInterstitialAd;
    private AdRequest mAdRequest;

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
        loadAdBanner();
        //getOnwer();
    }

    public CoordinatorLayout getCoordinatorLayout() {
        return (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
    }
//    private void getOnwer() {
//        final String[] SELF_PROJECTION = new String[]{ContactsContract.CommonDataKinds.Phone._ID, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
//        Cursor cs = getContentResolver().query(ContactsContract.Profile.CONTENT_URI, SELF_PROJECTION, null, null, null);
//        String onwer = "Hello";
//        if (cs != null && cs.moveToFirst()) {
//            String o = cs.getString(1);
//            if (!TextUtils.isEmpty(onwer)) {
//                onwer = o;
//
//            }
//        }
//        NavigationView navView = (NavigationView) findViewById(R.id.navigation_view);
//        if (navView != null) {
//            View headerLayout = navView.getHeaderView(0);
//            TextView tvEmail = (TextView) headerLayout.findViewById(R.id.tv_email);
//                tvEmail.setText(onwer);
//        }
//    }

    //Load Ads banner
    private void loadAdBanner() {
        ViewGroup adBanner = (ViewGroup) findViewById(R.id.ad_banner_layout);
        AdView adView = new AdView(getApplicationContext());
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getString(R.string.ad_unit_banner));
        adBanner.addView(adView);

        mAdRequest = new AdRequest.Builder().addTestDevice("EA2540727A561FCA20EE335403F98E81").build();
        adView.loadAd(mAdRequest);

        mInterstitialAd = new InterstitialAd(getApplicationContext());
        mInterstitialAd.setAdUnitId(getString(R.string.ad_unit_full));
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mInterstitialAd.show();
            }

        });

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
        mFragmentSetting = new FragmentSetting();
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
                    if (setting != null) {
                        mainActivity.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_content, setting, EDIT_FRAGMENT_TAG)
                                .commit();
                    }
                    drawer.closeDrawers();
                    break;
                case R.id.about:
                    FragmentAbout fa = new FragmentAbout();
                    mainActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_content, fa, ABOUT_FRAGMENT_TAG)
                            .commit();
                    drawer.closeDrawers();
                    break;
                case R.id.trash:
                    FragmentListWord archivedList = new FragmentListWord();
                    archivedList.isArchivedSrceen(true);
                    mainActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_content, archivedList, ARCHIVED_FRAGMENT_TAG)
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
                mNumberOfClick++;
                if (isOnline() && (mNumberOfClick == Define.CLICK_COUNT_TO_SHOW_FULL_ADD)) {
                    mNumberOfClick = 0;
                    mInterstitialAd.loadAd(mAdRequest);
                } else {
                    if (canDrawOverlays()) {
                        Intent previewIntent = new Intent(Define.VOCA_ACTION_OPEN_VOCA_REMINDER);
                        sendBroadcast(previewIntent);
                    } else {
                        startActivityForDrawOverlay();
                    }
                }
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

    private ProgressBar getLoadingIndicator() {
        return ((ProgressBar)findViewById(R.id.loading_indicator));
    }
    public void backup() {
        getLoadingIndicator().setVisibility(View.VISIBLE);
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
            if (mFragmentSetting != null) {
                mFragmentSetting.setBackupFile(backupFile.getName());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            getLoadingIndicator().setVisibility(View.GONE);
            showSnackBar(R.string.snackbar_backup_completed);
        }
    }

    public void backupVocabulary(View v) {
        Log.d(TAG, ">>>backupVocabulary START");
        if (canWriteExternalStorage()) {
            showConfirmDialog(R.string.popup_title_backup_vocabularies, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    backup();
                }
            });
        } else {
            showReqPrmWriteExtStoragePopup(REQUEST_CODE_WRITE_EXTERNAL_STORAGE_FOR_BACKUP);
        }
        Log.d(TAG, ">>>backupVocabulary END");
    }

    public void restoreVocabulary(View v) {
        if (canWriteExternalStorage()) {
            showConfirmDialog(R.string.popup_title_restore_vocabularies, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    restore();
                }
            });
        } else {
            showReqPrmWriteExtStoragePopup(REQUEST_CODE_WRITE_EXTERNAL_STORAGE_FOR_RESOTRE);
        }
    }

    public void restore() {
        getLoadingIndicator().setVisibility(View.VISIBLE);
        OALDatabaseOpenHelper db = new OALDatabaseOpenHelper(getApplicationContext());
        try {
            if (db.getCount() <= 0) {
                File backupFile = isBackupExisted();
                if (backupFile != null) {
                    BufferedReader br = new BufferedReader(new FileReader(backupFile));
                    String object;
                    Gson jsonParser = new Gson();
                    while ((object = br.readLine()) != null) {
                        Word w = ConvertWord.toWord(jsonParser.fromJson(object, ConvertWord.class));
                        db.insertWord(w);
                    }
                } else {
                    Log.d(TAG, "Cannot find backup file. Reason: Backup doesn't exist");
                }
            } else {
                Log.d(TAG, "Nothing to backup");
            }
            db.close();

        } catch (Exception e) {
            Log.d(TAG, "An Error occurs when restoring");
        } finally {
            getLoadingIndicator().setVisibility(View.GONE);
            showSnackBar(R.string.snackbar_restore_completed);
        }
    }

    private File isBackupExisted() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            String backupFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + MainActivity.BACKUP_FOLDER + MainActivity.BACKUP_FILE;
            File backupFile = new File(backupFilePath);
            if (backupFile.exists()) {
                return backupFile;
            }
            return null;
        } else {
            return null;
        }
    }

    private void showReqPrmWriteExtStoragePopup(final int requestFeatureCode) {
        showConfirmDialog(R.string.popup_title_explain_why_grant_write_external_storage, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        requestFeatureCode);
            }
        });
    }

    public void startVRService() {
        mProviderWrapper.setServiceRunningStatus(OALService.SERVICE_RUNNING_YES);
        startService(new Intent(this, OALService.class));
    }

    public void stopVRService() {
        mProviderWrapper.setServiceRunningStatus(OALService.SERVICE_RUNNING_NO);
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
        if (mNewOrEditDialog != null && mNewOrEditDialog.getDialog() != null && mNewOrEditDialog.getDialog().isShowing()) {
            mNewOrEditDialog.getDialog().dismiss();
        }
        FragmentManager fm = getSupportFragmentManager();
        mNewOrEditDialog = new FragmentDialogEditWord();
        mNewOrEditDialog.setArguments(b);
        mNewOrEditDialog.show(fm, "tag");
        mNewOrEditDialog.setOnCreateOrUpdateWodListener(this);
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

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public void setAdsVisibility(int visibility) {
        View v = findViewById(R.id.ad_banner_layout);
        if (v != null) {
            v.setVisibility(visibility);
        }
    }

    private boolean canWriteExternalStorage() {
        boolean canWrite;
        int isGranted = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        canWrite = (isGranted == PackageManager.PERMISSION_GRANTED)? true : false;
        Log.d(TAG, ">>>canWriteExternalStorage can write external storage= " + canWrite);
        return canWrite;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, ">>>onRequestPermissionsResult grantResult= " + grantResults[0]);
        if (REQUEST_CODE_WRITE_EXTERNAL_STORAGE_FOR_BACKUP == requestCode) {
            if (PackageManager.PERMISSION_GRANTED == grantResults[0]) {
                backup();
            }
        } else if (REQUEST_CODE_WRITE_EXTERNAL_STORAGE_FOR_RESOTRE == requestCode) {
            if (PackageManager.PERMISSION_GRANTED == grantResults[0]) {
                restore();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void showSnackBar(int strId) {
        String str = getString(strId);
        Snackbar snackbar = Snackbar.make(getCoordinatorLayout(), str, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_CODE_SETTING_DRAW_OVERLAY == requestCode) {
            if(canDrawOverlays()) {
                Log.d(TAG, "onActivityResult app can draw overlay from now");
                if (mFragmentSetting != null) {
                    mFragmentSetting.setStartStopServiceToggle(true);
                    startVRService();
                }
            } else {
                Log.d(TAG, "onActivityResult app doesn't be granted to draw overlay");
                if (mFragmentSetting != null) {
                    mFragmentSetting.setStartStopServiceToggle(false);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Check App can draw overlay or not
     */
    private boolean canDrawOverlays() {
        boolean canDraw = true;
        if (Build.VERSION.SDK_INT >= 23) {
            canDraw = Settings.canDrawOverlays(this);
        }
        Log.d(TAG, ">>>canDrawOverlays canDraw= " + canDraw);
        return canDraw;
    }

    public void startActivityForDrawOverlay() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, MainActivity.REQUEST_CODE_SETTING_DRAW_OVERLAY);
    }
}
