package com.ltbaogt.vocareminder.vocareminder.database.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ltbaogt.vocareminder.vocareminder.bean.Setting;
import com.ltbaogt.vocareminder.vocareminder.bean.Word;
import com.ltbaogt.vocareminder.vocareminder.define.Define;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by My PC on 06/08/2016.
 */

public class OALDatabaseOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = Define.TAG + "OALDatabaseOpenHelper";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "db.s3db";
    private static final String DATABASE_PATH_SUFFIX = "/databases/";

    //private static final String COL_LASTUPDATE= "LastUpdate";

    private static final String TABLE_NAME_TBL_TAG = "tbl_Group_ID";
    private static final int COL_TAG_ID_INDEX = 0;
    private static final int COL_TAG_NAME_INDEX = 1;



    private Context mContext;
    private OnDatabaseCreateCompleted mOnDatabaseCreateCompleted;
    public interface OnDatabaseCreateCompleted {
        void onDatabaseCreated();
    }

    public void setOnDatabaseCreateCompleted(OnDatabaseCreateCompleted l) {
        mOnDatabaseCreateCompleted = l;
    }

    public OALDatabaseOpenHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = ctx;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private String getDatabasePath() {
        return mContext.getApplicationInfo().dataDir + DATABASE_PATH_SUFFIX + DATABASE_NAME;
    }

    private void copyDbFromAsset() throws IOException {
        Log.d(TAG, ">>>copyDbFromAsset START");
        InputStream is = mContext.getAssets().open(DATABASE_NAME);

        //Create empty db
        String outFileName = getDatabasePath();
        Log.d(TAG, ">>>getDatabasePath outFileName= " + outFileName);
        File f = new File(mContext.getApplicationInfo().dataDir + DATABASE_PATH_SUFFIX);
        if (!f.exists()) {
            f.mkdir();
        }
        OutputStream os = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }

        os.flush();
        os.close();
        is.close();
        Log.d(TAG, ">>>copyDbFromAsset END");
    }

    public SQLiteDatabase openDatabase() throws SQLException {
        Log.d(TAG, ">>>openDatabase START");
        File dbFile = mContext.getDatabasePath(DATABASE_NAME);
        boolean isExists = dbFile.exists();
        Log.d(TAG, ">>>SQLiteDatabase isExists= " + isExists);
        if (!isExists) {
            try {
                copyDbFromAsset();
            } catch (IOException e) {
                Log.d(TAG, Log.getStackTraceString(e));
            }
        }
        Log.d(TAG, ">>>openDatabase END");
        if (mOnDatabaseCreateCompleted != null) {
            mOnDatabaseCreateCompleted.onDatabaseCreated();
        }
        return SQLiteDatabase.openDatabase(dbFile.getPath(), null,
                SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    public void deleteDatabase() {
        File dbFile = mContext.getDatabasePath(DATABASE_NAME);
        if (dbFile.exists()) {
            dbFile.delete();
        }
    }
    //Insert new word
    public void insertWord(Word newWord) {
        Log.d(TAG, ">>>insertWord START");
        if (newWord.getWordId() == -1) {
            ArrayList<Word> list = getAllWords();
            SQLiteDatabase db = getWritableDatabase();

            int candidateId = 0;
            for (int i = 0; i < list.size(); i++) {
                Word wTemp = list.get(i);
                if (candidateId >= wTemp.getWordId()) {
                    candidateId++;
                }
            }
            if (candidateId <= Integer.MAX_VALUE && candidateId >= Integer.MIN_VALUE) {
                newWord.setWordId(candidateId);
                ContentValues cv = getContentValues(newWord);
                db.insert(Word.TABLE_NAME_TBL_WORD, null, cv);
            }
            db.close();
        } else {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues cv = getContentValues(newWord);
            db.insert(Word.TABLE_NAME_TBL_WORD, null, cv);
        }

        Log.d(TAG, ">>>insertWord END, newWord= " + newWord.toString());
    }

    private ContentValues getContentValues(Word w) {
        ContentValues cv = new ContentValues();
        cv.put(Word.COL_WORD_ID, w.getWordId());
        cv.put(Word.COL_WORDNAME, w.getWordName());
        cv.put(Word.COL_PRONUNCIATION, w.getPronunciation());
        cv.put(Word.COL_TYPE_ID, w.getType_ID());
        cv.put(Word.COL_DEFAULT_MEANING, w.getDefault_Meaning());
        cv.put(Word.COL_SENTENCE, w.getSentence());
        cv.put(Word.COL_PRIORITY, w.getPriority());
        cv.put(Word.COL_COUNT, w.getCount());
        cv.put(Word.COL_GROUP_ID, w.getGroup_ID());
        cv.put(Word.COL_DELETED, w.isDeleted());
        cv.put(Word.COL_POSITION, w.getPosition());
        cv.put(Word.COL_MP3_URL, w.getMp3Url());
        return cv;
    }

    public Word getWordById(int id) {
        Log.d(TAG, ">>>getWordById id= " + id);
        Cursor cs = getWordByIdInCursor(id);
        if (cs.moveToFirst()) {
            Word w = new Word();
            w.initFromCursor(cs);
            Log.d(TAG, ">>>getWordById Word= " + w.toString());
            return w;
        }
        cs.close();
        return null;
    }

    public Cursor getWordByIdInCursor(int id) {
        Log.d(TAG, ">>>getWordById id= " + id);
        SQLiteDatabase db = getWritableDatabase();
        String sqlString = "select * from " + Word.TABLE_NAME_TBL_WORD + " where " + Word.COL_WORD_ID + " = ?";
        Cursor cs = db.rawQuery(sqlString, new String[]{id + ""});
        cs.moveToFirst();
        db.close();
        return cs;
    }
    public int getCount() {
        Log.d(TAG, ">>>getCount START");
        int ret = -1;
        try {
            SQLiteDatabase db = getWritableDatabase();
            Cursor c = db.rawQuery("select count(*) from " + Word.TABLE_NAME_TBL_WORD, null);
            if (c.moveToFirst()) {
                ret = c.getInt(0);
            }
            c.close();
            db.close();
        } catch (SQLException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }

        Log.d(TAG, ">>>getCount END, ret= " + ret);
        return ret;
    }

    public ArrayList<Word> getAllWords() {
        ArrayList<Word> list = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("select * from " + Word.TABLE_NAME_TBL_WORD +" order by " + Word.COL_WORD_ID, null);
        if (c.moveToFirst()) {
            do{
                Word w = new Word();
                w.initFromCursor(c);
                list.add(w);
            } while(c.moveToNext());
        }
        c.close();
        db.close();
        return list;
    }

    public ArrayList<Word> getAllWordsOrderByNameInList() {
        ArrayList<Word> list = new ArrayList<>();
        Cursor c = getAllWordsOrderByNameInCursor(false);
        if (c.moveToFirst()) {
            do{
                Word w = new Word();
                w.initFromCursor(c);
                list.add(w);
            } while(c.moveToNext());
        }
        c.close();
        return list;
    }

    public ArrayList<Word> getAllWordsForBackup() {
        ArrayList<Word> list = new ArrayList<>();
        Cursor c = getAllWordsOrderByNameInCursor(true);
        if (c.moveToFirst()) {
            do{
                Word w = new Word();
                w.initFromCursor(c);
                list.add(w);
            } while(c.moveToNext());
        }
        c.close();
        return list;
    }
    public Cursor getAllWordsOrderByNameInCursor(boolean isIncludeDel) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "select * from " + Word.TABLE_NAME_TBL_WORD + " where " + Word.WHERE_DELETE + " order by " + Word.COL_WORDNAME;
        if (isIncludeDel) {
            sql = "select * from " + Word.TABLE_NAME_TBL_WORD + " order by " + Word.COL_WORDNAME;
        }
        Cursor cs = db.rawQuery(sql, null);
        cs.moveToFirst();
        db.close();
        return cs;
    }

    public Cursor getArchivedWordsOrderByNameInCursor() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cs = db.rawQuery("select * from " + Word.TABLE_NAME_TBL_WORD + " where " + Word.WHERE_ARCHIVED + " order by " + Word.COL_WORDNAME, null);
        cs.moveToFirst();
        db.close();
        return cs;
    }

    public void updateWord(Word w) {
        SQLiteDatabase db = getWritableDatabase();
        db.update(Word.TABLE_NAME_TBL_WORD, getContentValues(w), Word.COL_WORD_ID + "=" + "?", new String[]{w.getWordId() + ""});
        Log.d(TAG, ">>>updateWord word= " + w.toString());
    }
    public void deleteWordById(int id) {
        Log.d(TAG,">>>deleteWordById START, id= " + id);
        updateDeletion(id, 1);
        Log.d(TAG,">>>deleteWordById END");
    }

    public void undoWordById(int id) {
        Log.d(TAG,">>>deleteWordById START, id= " + id);
        updateDeletion(id, 0);
        Log.d(TAG,">>>deleteWordById END");
    }

    private void updateDeletion(int id, int state) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Word.COL_DELETED, state);
        db.update(Word.TABLE_NAME_TBL_WORD,cv, Word.COL_WORD_ID + "=" + "?",new String[]{id + ""});
        db.close();
    }

    //Delete word forever, cannot undo
    public void deleteForever(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Word.TABLE_NAME_TBL_WORD, Word.COL_WORD_ID + "=?", new String[]{String.valueOf(id)});
    }
    public int[] getListWordId() {
        int[] reList = null;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cs = db.query(Word.TABLE_NAME_TBL_WORD,new String[]{Word.COL_WORD_ID}, Word.WHERE_DELETE, null, null,null, null);
        int i = 0;
        if (cs.moveToFirst()) {
            reList = new int[cs.getCount()];
            do {
                reList[i] = cs.getInt(Word.COL_WORD_ID_INDEX);
                Log.d(TAG, ">>>getListWordId Word[" + i + "]= " + reList[i]);
                i++;
            } while (cs.moveToNext());
        }
        cs.close();
        db.close();
        return reList;
    }


//    public Word randomWord() {
//        Log.d(TAG, ">>>randomWord START");
//        int[] listWordId = getListWordId();
//        if (listWordId != null) {
//            Random random = new Random();
//            int randomId = random.nextInt(listWordId.length);
//            Log.d(TAG, ">>>randomWord randomNumber is " + randomId);
//            Word w = getWordById(listWordId[randomId]);
//            Log.d(TAG, ">>>randomWord END, word after random " + w.toString());
//            return w;
//        }
//        Log.d(TAG, ">>>randomWord END");
//        return null;
//    }

    public Cursor randomWordInCursor() {
        Cursor cs = null;
        int[] listWordId = getListWordId();
        if (listWordId != null) {
            Random random = new Random();
            int randomId = random.nextInt(listWordId.length);
            cs = getWordByIdInCursor(listWordId[randomId]);
            Log.d(TAG, ">>>randomWord randomNumber is " + randomId);
        }
        return cs;
    }

    public Cursor getSettingValueForKey(String key) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cs = db.rawQuery(Setting.sqlSelectForGetValueForKey(key), new String[]{key});
        cs.moveToFirst();
        return cs;
    }

    public int setSettingValueForKey(String key, String value) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Setting.COL_VALUE, value);
        return db.update(Setting.TABLE_NAME, cv,Setting.COL_NAME + " = ?", new String[]{key});
    }
}
