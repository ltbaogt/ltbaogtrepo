package com.ryutb.speakingtime.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ryutb.speakingtime.bean.AlarmObject;
import com.ryutb.speakingtime.util.Define;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by My PC on 06/08/2016.
 */

public class VCDatabaseOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = Define.createTAG("VCDatabaseOpenHelper");
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "db.s3db";
    private static final String DATABASE_PATH_SUFFIX = "/databases/";

    private Context mContext;
//    private OnDatabaseCreateCompleted mOnDatabaseCreateCompleted;

//    public interface OnDatabaseCreateCompleted {
//        void onDatabaseCreated();
//    }

//    public void setOnDatabaseCreateCompleted(OnDatabaseCreateCompleted l) {
//        mOnDatabaseCreateCompleted = l;
//    }

    public VCDatabaseOpenHelper(Context ctx) {
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

    public void openDatabase() throws SQLException {
        Log.d(TAG, ">>>openDatabase START");
        File dbFile = mContext.getDatabasePath(DATABASE_NAME);
        boolean isExists = dbFile.exists();
        Log.d(TAG, ">>>SQLiteDatabase isExists= " + isExists);
        if (!isExists) {
            try {
                copyDbFromAsset();
            } catch (IOException e) {
            }
        }
        Log.d(TAG, ">>>openDatabase END");
//        if (mOnDatabaseCreateCompleted != null) {
//            mOnDatabaseCreateCompleted.onDatabaseCreated();
//        }
//        return SQLiteDatabase.openDatabase(dbFile.getPath(), null,
//                SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    public void deleteDatabase() {
        File dbFile = mContext.getDatabasePath(DATABASE_NAME);
        if (dbFile.exists()) {
            dbFile.delete();
        }
    }

    //Insert new alarm
    public void insertAlarm(AlarmObject newAlarm) {
        Log.d(TAG, ">>>insertWord START");
        if (newAlarm.getAlarmId() == -1) {
            ArrayList<AlarmObject> list = getAllAlarms();
            SQLiteDatabase db = getWritableDatabase();

            int candidateId = 0;
            for (int i = 0; i < list.size(); i++) {
                AlarmObject wTemp = list.get(i);
                if (candidateId >= wTemp.getAlarmId()) {
                    candidateId++;
                }
            }
            if (candidateId <= Integer.MAX_VALUE && candidateId >= Integer.MIN_VALUE) {
                newAlarm.setAlarmId(candidateId);
                ContentValues cv = newAlarm.getContentValues();
                db.insert(AlarmObject.TABLE_NAME, null, cv);
            }
            db.close();
        } else {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues cv = newAlarm.getContentValues();
            db.insert(AlarmObject.TABLE_NAME, null, cv);
            db.close();
        }

        Log.d(TAG, ">>>insertWord END, newWord= " + newAlarm.toString());
    }

    public int getReservedWordId() {
        Log.d(TAG, ">>>insertWord START");
        ArrayList<AlarmObject> list = getAllAlarms();
        SQLiteDatabase db = getWritableDatabase();

        int candidateId = 0;
        for (int i = 0; i < list.size(); i++) {
            AlarmObject wTemp = list.get(i);
            if (candidateId >= wTemp.getAlarmId()) {
                candidateId++;
            }
        }
        if (candidateId <= Integer.MAX_VALUE && candidateId >= Integer.MIN_VALUE) {
        }
        db.close();
        Log.d(TAG, ">>>insertWord END, reservedId= " + candidateId);
        return candidateId;
    }

    public AlarmObject getAlarmById(int id) {
        Log.d(TAG, ">>>getWordById id= " + id);
        Cursor cs = getWordByIdInCursor(id);
        if (cs.moveToFirst()) {
            AlarmObject w = new AlarmObject();
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
        String sqlString = "select * from " + AlarmObject.TABLE_NAME + " where " + AlarmObject.COL_NAME_ALARM_ID + " = ?";
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
            Cursor c = db.rawQuery("select count(*) from " + AlarmObject.TABLE_NAME, null);
            if (c.moveToFirst()) {
                ret = c.getInt(0);
            }
            c.close();
            db.close();
        } catch (SQLException e) {
        }

        Log.d(TAG, ">>>getCount END, ret= " + ret);
        return ret;
    }

    public ArrayList<AlarmObject> getAllAlarms() {
        ArrayList<AlarmObject> list = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("select * from " + AlarmObject.TABLE_NAME, null);
        if (c.moveToFirst()) {
            do {
                AlarmObject w = new AlarmObject();
                w.initFromCursor(c);
                list.add(w);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return list;
    }

    public void updateWord(AlarmObject w) {
        SQLiteDatabase db = getWritableDatabase();
        db.update(AlarmObject.TABLE_NAME, w.getContentValues(), AlarmObject.COL_NAME_ALARM_ID + "=" + "?", new String[]{w.getAlarmId() + ""});
        Log.d(TAG, ">>>updateWord word= " + w.toString());
    }


    //Delete word forever, cannot undo
    public int cancelAlarmById(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int affectedRows = db.delete(AlarmObject.TABLE_NAME, AlarmObject.COL_NAME_ALARM_ID + "=?", new String[]{String.valueOf(id)});
        Log.d(TAG, "cancelAlarmById affectedRows=" + affectedRows);
        return affectedRows;
    }

//    public int[] getListWordId() {
//        int[] reList = null;
//        SQLiteDatabase db = getWritableDatabase();
//        Cursor cs = db.query(AlarmObject.TABLE_NAME, new String[]{AlarmObject.COL_NAME_ALARM_ID}, AlarmObject.WHERE_DELETE, null, null, null, null);
//        int i = 0;
//        if (cs.moveToFirst()) {
//            reList = new int[cs.getCount()];
//            do {
//                reList[i] = cs.getInt(Word.COL_WORD_ID_INDEX);
//                Log.d(TAG, ">>>getListWordId Word[" + i + "]= " + reList[i]);
//                i++;
//            } while (cs.moveToNext());
//        }
//        cs.close();
//        db.close();
//        return reList;
//    }
}
