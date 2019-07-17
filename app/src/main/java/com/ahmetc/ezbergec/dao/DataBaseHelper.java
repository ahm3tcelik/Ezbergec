package com.ahmetc.ezbergec.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    public DataBaseHelper(Context context) {
        super(context, "ezber",null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE \"Sorular\" (\n" +
                "\t\"soru_id\"\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\t\"soru_ad\"\tTEXT,\n" +
                "\t\"soru_cevap\"\tTEXT,\n" +
                "\t\"soru_ipucu\"\tTEXT,\n" +
                "\t\"soru_kontrol\"\tINTEGER,\n" +
                "\t\"soru_silinen\"\tINTEGER,\n" +
                "\t\"ders_id\"\tINTEGER,\n" +
                "\tFOREIGN KEY(\"ders_id\") REFERENCES \"Dersler\"(\"ders_id\")\n" +
                ");");
        db.execSQL("CREATE TABLE \"Dersler\" (\n" +
                "\t\"ders_id\"\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\t\"ders_ad\"\tTEXT,\n" +
                "\t\"ders_dogru\"\tINTEGER,\n" +
                "\t\"ders_yanlis\"\tINTEGER,\n" +
                "\t\"ders_silinen\"\tINTEGER\n" +
                ");");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Sorular");
        db.execSQL("DROP TABLE IF EXISTS Dersler");
        onCreate(db);
    }
}
