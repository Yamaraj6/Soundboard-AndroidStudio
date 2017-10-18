package com.slyfoxstudios.korwinsoundboard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.MediaPlayer;


/**
 * Created by ProgrammingKnowledge on 4/3/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "SoundBar.db";
    public static final String TABLE_NAME = "Sounds";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "SOUNDDIR";
    public static final String Col_4 = "DURATION";
    public static final String Col_5 = "GOTHICPART";
    MediaPlayer mp = new MediaPlayer();

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT, SOUNDDIR TEXT,DURATION INT, GOTHICPART INT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name,String soundDir, Integer duration, Integer gothicPart)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,soundDir);
        contentValues.put(Col_4,duration);
        contentValues.put(Col_5,gothicPart);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from "+TABLE_NAME,null);
    }

    public Cursor getDataToExpandableLists(Integer duration, Integer screenNumber)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from "+TABLE_NAME + " where DURATION = " +duration + " AND GOTHICPART = "+ screenNumber,null);
    }


    public Integer deleteData (String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,"ID = " + id, null);
      //  return db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }

    public String GetSoundDirById(String ID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor curs = db.rawQuery("select * from "+TABLE_NAME + " where ID = "+ID,null);
        String dir = "";

        if (curs.moveToFirst())
        {
            dir = curs.getString(2);
        }
        curs.close();
        return dir;
    }

    public String GetSoundTitleById(String ID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor curs = db.rawQuery("select * from "+TABLE_NAME + " where ID = "+ID,null);
        String dir = "";

        if (curs.moveToFirst())
        {
            dir = curs.getString(1);
        }
        curs.close();
        return dir;
    }
}