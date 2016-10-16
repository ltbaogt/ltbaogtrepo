package com.ltbaogt.vocareminder.vocareminder.bean;

/**
 * Created by MyPC on 12/09/2016.
 */
public class Setting {

    public String getName() {
        return mName;
    }

    public void setName(String Name) {
        this.mName = Name;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String Value) {
        this.mValue = Value;
    }

    private String mName;
    private String mValue;

    public Setting(String n, String v) {
        mName = n;
        mValue = v;
    }

    /*******************
    DB SECTION
     ******************/
    public static final String TABLE_NAME =         "Setting";
    public static final String COL_NAME =           "Setting_Name";
    public static final String COL_VALUE =          "Setting_Value";
    public static final int COL_INDEX_NAME =        0;
    public static final int COL_INDEX_VALUE =       1;

    public static String sqlSelectForGetValueForKey(String key) {
        return "Select * from " + Setting.TABLE_NAME + " where " + Setting.COL_NAME + " = ?";
    }

}
