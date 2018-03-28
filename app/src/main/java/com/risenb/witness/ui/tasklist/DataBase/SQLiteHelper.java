package com.risenb.witness.ui.tasklist.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "videodata.db";//数据库名
    private static final int DATABASE_VERSION = 10002;//数据库版本

    private static SQLiteHelper instance;

    public static final String CREATE_VIDEO = "create table videoinfo ("
            + "id integer primary key autoincrement, "
            + "path text, "
            + "taskid text, "
            + "isupload text, "
            + "page text, "
            + "sort text, "
            + "finish text,"
            +"position text,"
            + "thumbnailpath text)";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SQLiteHelper Instance(Context context) {
        if (instance == null) {
            instance = new SQLiteHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("CREATE_VIDEO",CREATE_VIDEO);
        db.execSQL(CREATE_VIDEO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
