package com.ltbaogt.vocareminder.vocareminder.database.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ltbaogt.vocareminder.vocareminder.define.Define;
import com.ltbaogt.vocareminder.vocareminder.bean.Word;

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

    private static final String TABLE_NAME_TBL_WORD = "tbl_Word";

    private static final int COL_WORD_ID_INDEX = 0;
    private static final int COL_WORDNAME_INDEX = 1;
    private static final int COL_PRONUNCIATION_INDEX = 2;
    private static final int COL_TYPE_ID_INDEX = 3;
    private static final int COL_DEFAULT_MEANING_INDEX = 4;
    private static final int COL_SENTENCE_INDEX = 5;
    private static final int COL_PRIORITY_INDEX = 6;
    private static final int COL_COUNT_INDEX = 7;
    private static final int COL_GROUP_ID_INDEX = 8;
    private static final int COL_DELETED_INDEX = 9;

    private static final String COL_WORD_ID = "Word_ID";
    private static final String COL_WORDNAME = "WordName";
    private static final String COL_PRONUNCIATION = "Pronunciation";
    private static final String COL_TYPE_ID = "Type_ID";
    private static final String COL_DEFAULT_MEANING = "Default_Meaning";
    private static final String COL_SENTENCE = "Sentence";
    private static final String COL_PRIORITY = "Priority";
    private static final String COL_COUNT = "Count";
    private static final String COL_GROUP_ID = "Group_ID";
    private static final String COL_DELETED = "Deleted";
    //private static final String COL_LASTUPDATE= "LastUpdate";


    private Context mContext;

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
        ArrayList<Word> list = getAllWords();
        SQLiteDatabase db = getWritableDatabase();

        int candidateId = 0;
        for(int i = 0; i < list.size(); i++) {
            Word wTemp = list.get(i);
            if (candidateId >= wTemp.getWordId()) {
                candidateId++;
            }
        }
        if (candidateId <= Integer.MAX_VALUE && candidateId >= Integer.MIN_VALUE) {
            newWord.setWordId(candidateId);
            ContentValues cv = getContentValues(newWord);
            db.insert(TABLE_NAME_TBL_WORD, null, cv);
        }
        db.close();
        Log.d(TAG, ">>>insertWord END, newWord= " + newWord.toString());
    }

    private ContentValues getContentValues(Word w) {
        ContentValues cv = new ContentValues();
        cv.put(COL_WORD_ID, w.getWordId());
        cv.put(COL_WORDNAME, w.getWordName());
        cv.put(COL_PRONUNCIATION, w.getPronunciation());
        cv.put(COL_TYPE_ID, w.getType_ID());
        cv.put(COL_DEFAULT_MEANING, w.getDefault_Meaning());
        cv.put(COL_SENTENCE, w.getSentence());
        cv.put(COL_PRIORITY, w.getPriority());
        cv.put(COL_COUNT, w.getCount());
        cv.put(COL_GROUP_ID, w.getGroup_ID());
        cv.put(COL_DELETED, w.isDeleted());
        return cv;
    }

    public Word getWordById(int id) {
        Log.d(TAG, ">>>getWordById id= " + id);
        SQLiteDatabase db = getWritableDatabase();
        String sqlString = "select * from " + TABLE_NAME_TBL_WORD + " where " + COL_WORD_ID + " = ?";
        Cursor cs = db.rawQuery(sqlString, new String[]{id + ""});
        if (cs.moveToFirst()) {
            Word w = new Word();
            w.setWordId(cs.getInt(COL_WORD_ID_INDEX));
            w.setWordName(cs.getString(COL_WORDNAME_INDEX));
            w.setDefault_Meaning(cs.getString(COL_DEFAULT_MEANING_INDEX));
            Log.d(TAG, ">>>getWordById Word= " + w.toString());
            return w;
        }
        cs.close();
        db.close();
        return null;
    }

    public int getCount() {
        Log.d(TAG, ">>>getCount START");
        int ret = -1;
        try {
            SQLiteDatabase db = getWritableDatabase();
            Cursor c = db.rawQuery("select count(*) from " + TABLE_NAME_TBL_WORD, null);
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
        Cursor c = db.rawQuery("select * from " + TABLE_NAME_TBL_WORD +" order by " + COL_WORD_ID, null);
        if (c.moveToFirst()) {
            do{
                Word w = new Word();
                w.setWordId(c.getInt(COL_WORD_ID_INDEX));
                w.setWordName(c.getString(COL_WORDNAME_INDEX));
                w.setDefault_Meaning(c.getString(COL_DEFAULT_MEANING_INDEX));
                list.add(w);
            } while(c.moveToNext());
        }
        c.close();
        db.close();
        return list;
    }

    public ArrayList<Word> getAllWordsOrderByName() {
        ArrayList<Word> list = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("select * from " + TABLE_NAME_TBL_WORD +" order by " + COL_WORDNAME, null);
        if (c.moveToFirst()) {
            do{
                Word w = new Word();
                w.setWordId(c.getInt(COL_WORD_ID_INDEX));
                w.setWordName(c.getString(COL_WORDNAME_INDEX));
                w.setPronunciation(c.getString(COL_PRONUNCIATION_INDEX));
                w.setDefault_Meaning(c.getString(COL_DEFAULT_MEANING_INDEX));
                list.add(w);
            } while(c.moveToNext());
        }
        c.close();
        db.close();
        return list;
    }

    public void updateWord(Word w) {
        SQLiteDatabase db = getWritableDatabase();
        int count = db.update(TABLE_NAME_TBL_WORD, getContentValues(w), COL_WORD_ID + "=" + "?", new String[]{w.getWordId() + ""});
        Log.d(TAG, ">>>updateWord count= " + count);
    }
    public void deleteWordById(int id) {
        Log.d(TAG,">>>deleteWordById START, id= " + id);
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME_TBL_WORD, COL_WORD_ID + "=" + "?", new String[]{id + ""});
        Log.d(TAG,">>>deleteWordById END");
    }

    private int[] getListWordId() {
        int[] reList = null;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cs = db.query(TABLE_NAME_TBL_WORD,new String[]{COL_WORD_ID},null, null, null,null, null);
        int i = 0;
        if (cs.moveToFirst()) {
            reList = new int[cs.getCount()];
            do {
                reList[i] = cs.getInt(COL_WORD_ID_INDEX);
                Log.d(TAG, ">>>getListWordId Word[" + i + "]= " + reList[i]);
                i++;
            } while (cs.moveToNext());
        }
        cs.close();
        db.close();
        return reList;
    }

    public Word randomWord() {
        Log.d(TAG, ">>>randomWord START");
        int[] listWordId = getListWordId();
        if (listWordId != null) {
            Random random = new Random();
            int randomId = random.nextInt(listWordId.length);
            Log.d(TAG, ">>>randomWord randomNumber is " + randomId);
            Word w = getWordById(listWordId[randomId]);
            Log.d(TAG, ">>>randomWord END, word after random " + w.toString());
            return w;
        }
        Log.d(TAG, ">>>randomWord END");
        return null;
    }
}
