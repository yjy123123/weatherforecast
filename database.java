package com.bignerdranch.android.weather_forecast;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class database extends SQLiteOpenHelper {
    public static final String CREATE_Weather = "create table weathers (id integer primary key autoincrement, "
            + " weather text,date date, maxtemp integer, mintemp integer, picnum text,cond_d text,cond_n text,humidity text,pressure text,wind text)";
      private Context mContext;
       public database(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_Weather);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
           db.execSQL("drop table if exists weathers");
           onCreate(db);
    }
    public void start(SQLiteDatabase db) {
        db.execSQL("drop table if exists weathers");
        db.execSQL(CREATE_Weather);
        Log.d("test","ok");
    }
}
