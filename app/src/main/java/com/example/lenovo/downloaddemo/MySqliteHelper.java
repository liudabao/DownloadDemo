package com.example.lenovo.downloaddemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by lenovo on 2016/6/21.
 */
public class MySqliteHelper extends SQLiteOpenHelper{

    public static String CREATE_DOWNLOAD="create table download (id integer primary key,length integer)";

    Context mContext;

    public MySqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        Log.e("DB", "create");
        db.execSQL(CREATE_DOWNLOAD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        Log.e("DB", "upgrade");
        switch (oldVersion) {
            case 1:
                //db.execSQL(CREATE_BOOK);
                break;
            case 2:
                break;
            default:
                break;
        }
    }

}
