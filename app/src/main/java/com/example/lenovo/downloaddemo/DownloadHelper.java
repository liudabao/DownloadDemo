package com.example.lenovo.downloaddemo;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by lenovo on 2016/6/21.
 */
public class DownloadHelper {
    MySqliteHelper helper;

    public DownloadHelper(Context context, String db, int version){
       // helper=new MySqliteHelper(context, "person.db", null, 2);
        helper=new MySqliteHelper(context, db, null, version);

    }

    public void saveSqlite(ContentValues values){
        SQLiteDatabase db=helper.getWritableDatabase();
        db.beginTransaction();
        db.insert("download", null, values);
        Log.e("insert", values.get("id")+"");
        db.setTransactionSuccessful();
        //db.execSQL("insert into person (id,name,age)values(?,?,?)",new String[]{"1","test","10"});
        db.endTransaction();
    }

    public void update( String id, int length) {
        SQLiteDatabase db=helper.getWritableDatabase();
        db.beginTransaction();
        ContentValues values=new ContentValues();
        values.put("length", length);
        //db.execSQL("update person set age=? where id=?",new String[]{"222","1"});
        db.update("download", values, "id=?", new String[]{id});
        db.setTransactionSuccessful();
        db.endTransaction();

    }

    public int query(int id) {
        int length=0;
        SQLiteDatabase db=helper.getReadableDatabase();
        db.beginTransaction();
        Cursor cursor=db.query("download", null, null, null, null, null, null);
        if(cursor.moveToFirst()){
            do{
               // Log.e("name", cursor.getString(cursor.getColumnIndex("name")));
               // Log.e("age", ""+cursor.getInt(cursor.getColumnIndex("age")));
               // Log.e("id", ""+cursor.getInt(cursor.getColumnIndex("id")));
                if(id==cursor.getInt(cursor.getColumnIndex("id"))){
                    Log.e("length "+id, ""+cursor.getInt(cursor.getColumnIndex("length")));
                    length=cursor.getInt(cursor.getColumnIndex("length"));
                }
            }while(cursor.moveToNext());
        }
        cursor.close();
        //db.rawQuery("select * from person",null);
        db.setTransactionSuccessful();
        db.endTransaction();
        return length;
    }

    public boolean isExist(int id) {
        boolean find=false;
        SQLiteDatabase db=helper.getReadableDatabase();
        db.beginTransaction();
        Cursor cursor=db.query("download", null, null, null, null, null, null);
        if(cursor.moveToFirst()){
            do{
                // Log.e("name", cursor.getString(cursor.getColumnIndex("name")));
                // Log.e("age", ""+cursor.getInt(cursor.getColumnIndex("age")));

                if(id==cursor.getInt(cursor.getColumnIndex("id"))){
                    Log.e("id", ""+cursor.getInt(cursor.getColumnIndex("id")));
                    find=true;
                    break;
                }
            }while(cursor.moveToNext());
        }
        cursor.close();
        //db.rawQuery("select * from person",null);
        db.setTransactionSuccessful();
        db.endTransaction();
        return find;
    }

    public void delete(String id) {
        SQLiteDatabase db=helper.getWritableDatabase();
        db.beginTransaction();
        //db.execSQL("delete from person where id =?", new String[]{"1"});
        db.delete("download", "id=?", new String[]{id});
        db.setTransactionSuccessful();
        db.endTransaction();
    }



}
