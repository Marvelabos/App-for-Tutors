package com.example.adamstelmaszyk.mojerozliczenia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by adamstelmaszyk on 04.05.2018.
 */

public class DataBaseHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "rozliczenie.db";

    public static final String TABLE_NAME_SALARY = "korki_t";
    public static final String TABLE_NAME_SPENDING = "spending_t";

    public static final String COL_1 = "ID";
    public static final String COL_2 = "DATA";
    public static final String COL_3 = "KWOTA";

    public static final String CREATE_TABLE_SALARY = "CREATE TABLE "+
            TABLE_NAME_SALARY + "(" + COL_1 + " INTEGER PRIMARY KEY," + COL_2 +
             " TEXT," + COL_3 + " TEXT" + ")";

    public static final String CREATE_TABLE_SPENDING = "CREATE TABLE "+
            TABLE_NAME_SPENDING + "(" + COL_1 + " INTEGER PRIMARY KEY," + COL_2 +
            " TEXT," + COL_3 + " TEXT" + ")";


    public DataBaseHelper(Context context) { super(context, DATABASE_NAME, null, 1);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SALARY);
        db.execSQL(CREATE_TABLE_SPENDING);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_SALARY);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_SPENDING);
        onCreate(db);
    }

    public boolean insertData(String data, String kwota, int what_table)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,data);
        contentValues.put(COL_3,kwota);
        long result;

        if(what_table==0)
        {
            result = db.insert(TABLE_NAME_SALARY, null, contentValues);
        }
        else
        {
            result = db.insert(TABLE_NAME_SPENDING, null, contentValues);
        }

        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public Cursor getAllData(int what_table)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        if(what_table==0)
        {
            res = db.rawQuery("select * from " + TABLE_NAME_SALARY, null);
        }
        else
        {
            res = db.rawQuery("select * from " + TABLE_NAME_SPENDING, null);
        }
        return res;
    }

    public boolean updateDate(String id, String data, String kwota, int what_table)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_1,id);
        contentValues.put(COL_2,data);
        contentValues.put(COL_3,kwota);

        if(what_table==0)
        {
            db.update(TABLE_NAME_SALARY, contentValues, "ID = ?",new String[] {id} );
        }
        else
        {
            db.update(TABLE_NAME_SPENDING, contentValues, "ID = ?",new String[] {id} );
        }
        return true;

    }

    public Integer deleteData(String id, int what_table)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        if(what_table==0)
        {
            return db.delete(TABLE_NAME_SALARY, "ID = ?", new String[]{id});
        }
        else
        {
            return db.delete(TABLE_NAME_SPENDING, "ID = ?", new String[]{id});
        }

    }



}
