package com.ltbaogt.vocareminder.vocareminder.database.bl;

import android.content.Context;

import com.ltbaogt.vocareminder.vocareminder.bean.Word;
import com.ltbaogt.vocareminder.vocareminder.database.helper.OALDatabaseOpenHelper;

import java.util.ArrayList;

/**
 * Created by My PC on 06/08/2016.
 */

public class OALBLL {

    Context mContext;

    public static void initDatabase(Context ctx) {
        OALDatabaseOpenHelper dbHelper = new OALDatabaseOpenHelper(ctx);
        dbHelper.openDatabase();
    }
    public static void delDatabase(Context ctx) {
        OALDatabaseOpenHelper dbHelper = new OALDatabaseOpenHelper(ctx);
        dbHelper.deleteDatabase();
    }

    public OALBLL(Context ctx) {
        mContext = ctx;
    }

    public void addNewWord(Word w) {
        OALDatabaseOpenHelper dbHelper = new OALDatabaseOpenHelper(mContext);
        dbHelper.insertWord(w);
    }

    public Word getWordById(int id) {
        OALDatabaseOpenHelper dbHelp = new OALDatabaseOpenHelper(mContext);
        return dbHelp.getWordById(id);
    }

    public int getCount() {
        OALDatabaseOpenHelper dbHelp = new OALDatabaseOpenHelper(mContext);
        return dbHelp.getCount();
    }

    public void updateWord(Word w) {
        OALDatabaseOpenHelper dbHelp = new OALDatabaseOpenHelper(mContext);
        dbHelp.updateWord(w);
    }

    public void deleteWordById(int id) {
        OALDatabaseOpenHelper dbHelp = new OALDatabaseOpenHelper(mContext);
        dbHelp.deleteWordById(id);
    }

    public ArrayList<Word> getAllWordsOrderByNameInList() {
        OALDatabaseOpenHelper dbHelp = new OALDatabaseOpenHelper(mContext);
        return dbHelp.getAllWordsOrderByNameInList();
    }

//    public Word randomWord() {
//        OALDatabaseOpenHelper dbHelp = new OALDatabaseOpenHelper(mContext);
//        return dbHelp.randomWord();
//    }
    public void undoWordById(int id) {
        OALDatabaseOpenHelper dbHelp = new OALDatabaseOpenHelper(mContext);
        dbHelp.undoWordById(id);
    }
}
