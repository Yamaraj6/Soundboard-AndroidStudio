package com.slyfoxstudios.korwinsoundboard;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by user on 05.03.2017.
 */
public class DatabaseBackend extends SQLiteOpenHelper {
    private static String DB_PATH;

    private static String DB_NAME = "SoundBar.db";

    private SQLiteDatabase myDataBase ;

    private final Context myContext ;

    public DatabaseBackend(Context context)
    {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
        DB_PATH = context.getResources().getString(R.string.path_to_database);
    }


    public void CreateDatabse() throws IOException
    {
        boolean dbExist = checkDatabase();

            this.getReadableDatabase();
            try
            {
                copyDatabase();
            }
            catch(IOException e)
            {
                throw new Error("Error copying db");
            }

    }
    //check if db already exists
    private boolean checkDatabase()
    {
        SQLiteDatabase checkDB = null;
        try
        {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READONLY);
        }
        catch(SQLiteException e)
        {
            //db doesnt exist
        }

        if(checkDB!=null)
            checkDB.close();

        return checkDB != null ? true : false;
    }


    private void copyDatabase() throws IOException
    {
        //open local db from assets
        String myPath = DB_PATH + DB_NAME;
        myContext.deleteDatabase(myPath);
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        OutputStream myOutput = new FileOutputStream(outFileName);

        //tranfer bytes from inputfile to outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length= myInput.read(buffer))>0)
        {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException{

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
