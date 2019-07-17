package com.ahmetc.ezbergec.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ahmetc.ezbergec.Models.Dersler;

import java.util.ArrayList;

public class Derslerdao {
    private SQLiteDatabase sqLiteDatabase;

    public ArrayList<Dersler> tumDersler(DataBaseHelper dbh) {

        ArrayList<Dersler> sonuc = new ArrayList<>();
        sqLiteDatabase = dbh.getWritableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM Dersler WHERE ders_silinen=0", null);

        while(c.moveToNext()) {
            Dersler ders = new Dersler(
                    c.getInt(c.getColumnIndex("ders_id")),
                    c.getString(c.getColumnIndex("ders_ad")),
                    c.getInt(c.getColumnIndex("ders_silinen")),
                    c.getInt(c.getColumnIndex("ders_dogru")),
                    c.getInt(c.getColumnIndex("ders_yanlis"))
                    );
            sonuc.add(ders);
        }
        return sonuc;
    }
    public ArrayList<Dersler> findDers(DataBaseHelper dbh, String key) {
        ArrayList<Dersler> sonuc = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = dbh.getWritableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM Dersler WHERE ders_ad like '%"+key+"%'", null);

        while (c.moveToNext()) {
            Dersler ders = new Dersler(
                    c.getInt(c.getColumnIndex("ders_id")),
                    c.getString(c.getColumnIndex("ders_ad")),
                    c.getInt(c.getColumnIndex("ders_silinen")),
                    c.getInt(c.getColumnIndex("ders_dogru")),
                    c.getInt(c.getColumnIndex("ders_yanlis"))
            );
            sonuc.add(ders);
        }
        return sonuc;
    }
    public void ekleDers(DataBaseHelper dbh, String ders_ad) {
        sqLiteDatabase = dbh.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ders_ad",ders_ad);
        values.put("ders_silinen",0);
        values.put("ders_dogru",0);
        values.put("ders_yanlis",0);
        sqLiteDatabase.insertOrThrow("Dersler",null, values);
        sqLiteDatabase.close();
    }
    public int sayiDers(DataBaseHelper dbh) {
        SQLiteDatabase sqLiteDatabase = dbh.getWritableDatabase();
        int sonuc = 0;
        Cursor c = sqLiteDatabase.rawQuery("SELECT count (*) as sonuc FROM Dersler", null);
        while (c.moveToNext()) {
            sonuc = c.getInt(c.getColumnIndex("sonuc"));
        }
        return sonuc;
    }
    public void silDers(DataBaseHelper dbh, int id) {
        SQLiteDatabase sqLiteDatabase = dbh.getWritableDatabase();
        sqLiteDatabase.delete("Dersler","ders_id=?", new String[]{String.valueOf(id)});
        sqLiteDatabase.close();
    }
    public void guncelleDers(DataBaseHelper dbh, String ders_ad, int id) {
        SQLiteDatabase sqLiteDatabase = dbh.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ders_ad",ders_ad);
        values.put("ders_silinen",0);
        sqLiteDatabase.update("Dersler",values,"ders_id = ?", new String[]{String.valueOf(id)});
        sqLiteDatabase.close();
    }
    public void ekleGarbage(DataBaseHelper dbh, int ders_id) {
        SQLiteDatabase sqLiteDatabase = dbh.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ders_silinen",1);

        sqLiteDatabase.update("Dersler",values,"ders_id = ?", new String[]{String.valueOf(ders_id)});
        sqLiteDatabase.close();
    }
    public void arttirDogru(DataBaseHelper dbh, int ders_id) {
        SQLiteDatabase sqLiteDatabase = dbh.getWritableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("SELECT ders_dogru FROM Dersler WHERE ders_id="+ders_id,null);
        int dogru = 0;
        while(c.moveToNext()) {
            dogru = c.getInt(c.getColumnIndex("ders_dogru")) + 1;
        }
        ContentValues values = new ContentValues();
        values.put("ders_dogru",dogru);

        sqLiteDatabase.update("Dersler",values,"ders_id = ?", new String[]{String.valueOf(ders_id)});
        sqLiteDatabase.close();
    }
    public void istatistikSifirla(DataBaseHelper dbh, int ders_id) {
        SQLiteDatabase sqLiteDatabase = dbh.getWritableDatabase();
        Cursor c = sqLiteDatabase.rawQuery(" SELECT ders_yanlis, ders_dogru FROM Dersler WHERE ders_id="+ ders_id,null);
        ContentValues values = new ContentValues();
        values.put("ders_dogru", 0);
        values.put("ders_yanlis", 0);

        sqLiteDatabase.update("Dersler", values, "ders_id = ?", new String[]{String.valueOf(ders_id)});
        sqLiteDatabase.close();
    }
    public void arttirEksi(DataBaseHelper dbh, int ders_id) {
        SQLiteDatabase sqLiteDatabase = dbh.getWritableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("SELECT ders_yanlis FROM Dersler WHERE ders_id="+ders_id,null);
        int yanlis = 0;
        while(c.moveToNext()) {
            yanlis = c.getInt(c.getColumnIndex("ders_yanlis")) +1;
        }
        ContentValues values = new ContentValues();
        values.put("ders_yanlis",yanlis);

        sqLiteDatabase.update("Dersler",values,"ders_id = ?", new String[]{String.valueOf(ders_id)});
        sqLiteDatabase.close();
    }
    public int dogruSayi(DataBaseHelper dbh, int ders_id) {
        SQLiteDatabase sqLiteDatabase = dbh.getWritableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("SELECT ders_dogru FROM Dersler WHERE ders_id="+ders_id,null);
        int dogru = 0;
        while(c.moveToNext()) {
            dogru = c.getInt(c.getColumnIndex("ders_dogru"));
        }
        return dogru;
    }
    public int yanlisSayi(DataBaseHelper dbh, int ders_id) {
        SQLiteDatabase sqLiteDatabase = dbh.getWritableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("SELECT ders_yanlis FROM Dersler WHERE ders_id="+ders_id,null);
        int yanlis = 0;
        while(c.moveToNext()) {
            yanlis = c.getInt(c.getColumnIndex("ders_yanlis"));
        }
        return yanlis;
    }
    public void cikarGarbage(DataBaseHelper dbh, int ders_id) {
        SQLiteDatabase sqLiteDatabase = dbh.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ders_silinen",0);

        sqLiteDatabase.update("Dersler",values,"ders_id = ?",new String[]{String.valueOf(ders_id)});
        sqLiteDatabase.close();
    }
    public void garbageCollector(DataBaseHelper dbh) {
        SQLiteDatabase sqLiteDatabase = dbh.getWritableDatabase();
        sqLiteDatabase.delete("Dersler","ders_silinen = ?", new String[]{String.valueOf(1)});
        sqLiteDatabase.close();
    }
}
