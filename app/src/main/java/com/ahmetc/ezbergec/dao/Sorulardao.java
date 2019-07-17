package com.ahmetc.ezbergec.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ahmetc.ezbergec.Models.Dersler;
import com.ahmetc.ezbergec.Models.Sorular;

import java.util.ArrayList;

public class Sorulardao {

    public ArrayList<Sorular> soruGetir(DataBaseHelper dbh, int ders_id) {
        ArrayList<Sorular> sonuc = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = dbh.getWritableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("SELECT soru_id,soru_ad,soru_cevap,soru_ipucu,soru_kontrol,soru_silinen,Dersler.ders_silinen,Dersler.ders_dogru,Dersler.ders_yanlis,Dersler.ders_id,Dersler.ders_ad FROM Sorular,Dersler WHERE Sorular.ders_id = Dersler.ders_id and soru_silinen=0 and Dersler.ders_id="+ders_id+" ORDER BY RANDOM() ", null);

        while (c.moveToNext()) {
            Dersler ders = new Dersler(
                    c.getInt(c.getColumnIndex("ders_id")),
                    c.getString(c.getColumnIndex("ders_ad")),
                    c.getInt(c.getColumnIndex("ders_silinen")),
                    c.getInt(c.getColumnIndex("ders_dogru")),
                    c.getInt(c.getColumnIndex("ders_yanlis")));
            Sorular soru = new Sorular(
                    c.getInt(c.getColumnIndex("soru_id")),
                    c.getString(c.getColumnIndex("soru_ad")),
                    c.getString(c.getColumnIndex("soru_cevap")),
                    c.getString(c.getColumnIndex("soru_ipucu")),
                    c.getInt(c.getColumnIndex("soru_kontrol")),
                    c.getInt(c.getColumnIndex("soru_silinen")),
                    ders);
            sonuc.add(soru);
        }
        return sonuc;
    }
    public ArrayList<Sorular> yanlisGetir(DataBaseHelper dbh, int ders_id, int soru_id) {
        ArrayList<Sorular> sonuc = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = dbh.getWritableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("SELECT soru_id,soru_ad,soru_cevap,soru_ipucu,soru_kontrol,soru_silinen,Dersler.ders_silinen,Dersler.ders_dogru,Dersler.ders_yanlis,Dersler.ders_id,Dersler.ders_ad FROM Sorular,Dersler WHERE Sorular.ders_id = Dersler.ders_id and soru_silinen=0 and Dersler.ders_id="+ders_id+" and soru_id !="+soru_id+" ORDER BY RANDOM() LIMIT 3", null);

        while (c.moveToNext()) {
            Dersler ders = new Dersler(
                    c.getInt(c.getColumnIndex("ders_id")),
                    c.getString(c.getColumnIndex("ders_ad")),
                    c.getInt(c.getColumnIndex("ders_silinen")),
                    c.getInt(c.getColumnIndex("ders_dogru")),
                    c.getInt(c.getColumnIndex("ders_yanlis")));
            Sorular soru = new Sorular(
                    c.getInt(c.getColumnIndex("soru_id")),
                    c.getString(c.getColumnIndex("soru_ad")),
                    c.getString(c.getColumnIndex("soru_cevap")),
                    c.getString(c.getColumnIndex("soru_ipucu")),
                    c.getInt(c.getColumnIndex("soru_kontrol")),
                    c.getInt(c.getColumnIndex("soru_silinen")),
                    ders);
            sonuc.add(soru);
        }
        return sonuc;
    }
    public ArrayList<Sorular> tumSorular(DataBaseHelper dbh,int ders_id) {
        ArrayList<Sorular> sonuc = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = dbh.getWritableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("SELECT soru_id,soru_ad,soru_cevap,soru_ipucu,soru_kontrol,soru_silinen,Dersler.ders_dogru,Dersler.ders_yanlis,Dersler.ders_silinen,Dersler.ders_id,Dersler.ders_ad FROM Sorular,Dersler WHERE Sorular.ders_id = Dersler.ders_id and soru_silinen=0 and Dersler.ders_id="+ders_id+" ORDER BY soru_id ASC", null);

        while (c.moveToNext()) {
            Dersler ders = new Dersler(
                    c.getInt(c.getColumnIndex("ders_id")),
                    c.getString(c.getColumnIndex("ders_ad")),
                    c.getInt(c.getColumnIndex("ders_silinen")),
                    c.getInt(c.getColumnIndex("ders_dogru")),
                    c.getInt(c.getColumnIndex("ders_yanlis"))
                    );
            Sorular soru = new Sorular(
                    c.getInt(c.getColumnIndex("soru_id")),
                    c.getString(c.getColumnIndex("soru_ad")),
                    c.getString(c.getColumnIndex("soru_cevap")),
                    c.getString(c.getColumnIndex("soru_ipucu")),
                    c.getInt(c.getColumnIndex("soru_kontrol")),
                    c.getInt(c.getColumnIndex("soru_silinen")),
                    ders);
            sonuc.add(soru);
        }
        return sonuc;
    }
    public ArrayList<Sorular> findSoru(DataBaseHelper dbh, String key, int ders_id) {
        ArrayList<Sorular> sonuc = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = dbh.getWritableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("SELECT soru_id, soru_ad, soru_cevap, soru_ipucu, soru_kontrol, soru_silinen,Dersler.ders_silinen,Dersler.ders_dogru,Dersler.ders_yanlis, Dersler.ders_id, Dersler.ders_ad FROM Sorular,Dersler WHERE Dersler.ders_id = Sorular.ders_id and soru_silinen=0 and Dersler.ders_id="+ders_id+" and soru_ad like '%"+key+"%'", null);

        while (c.moveToNext()) {
            Dersler ders = new Dersler(c.getInt(c.getColumnIndex("ders_id")),
                    c.getString(c.getColumnIndex("ders_ad")),
                    c.getInt(c.getColumnIndex("ders_silinen")),
                    c.getInt(c.getColumnIndex("ders_dogru")),
                    c.getInt(c.getColumnIndex("ders_yanlis")));
            Sorular soru = new Sorular(
                    c.getInt(c.getColumnIndex("soru_id")),
                    c.getString(c.getColumnIndex("soru_ad")),
                    c.getString(c.getColumnIndex("soru_cevap")),
                    c.getString(c.getColumnIndex("soru_ipucu")),
                    c.getInt(c.getColumnIndex("soru_kontrol")),
                    c.getInt(c.getColumnIndex("soru_silinen")),
                    ders);
            sonuc.add(soru);
        }
        return sonuc;
    }
    public ArrayList<Sorular> yanlisSoruGetir(DataBaseHelper dbh,int ders_id) {
        ArrayList<Sorular> sonuc = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = dbh.getWritableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("SELECT soru_id,soru_ad,soru_cevap,soru_ipucu,soru_kontrol,soru_silinen,Dersler.ders_id,Dersler.ders_ad, Dersler.ders_dogru,Dersler.ders_yanlis,Dersler.ders_silinen FROM Sorular,Dersler WHERE Sorular.ders_id = Dersler.ders_id and soru_silinen=0 and Dersler.ders_id="+ders_id+" and soru_kontrol=1 ORDER BY RANDOM()", null);
        while(c.moveToNext()) {
            Dersler ders = new Dersler(
                    c.getInt(c.getColumnIndex("ders_id")),
                    c.getString(c.getColumnIndex("ders_ad")),
                    c.getInt(c.getColumnIndex("ders_silinen")),
                    c.getInt(c.getColumnIndex("ders_dogru")),
                    c.getInt(c.getColumnIndex("ders_yanlis")));
            Sorular soru = new Sorular(
                    c.getInt(c.getColumnIndex("soru_id")),
                    c.getString(c.getColumnIndex("soru_ad")),
                    c.getString(c.getColumnIndex("soru_cevap")),
                    c.getString(c.getColumnIndex("soru_ipucu")),
                    c.getInt(c.getColumnIndex("soru_kontrol")),
                    c.getInt(c.getColumnIndex("soru_silinen")),
                    ders);
            sonuc.add(soru);
        }
        return sonuc;
    }
    public int sayiSoru(DataBaseHelper dbh, int ders_id) {
        SQLiteDatabase sqLiteDatabase = dbh.getWritableDatabase();
        int sonuc = 0;
        Cursor c = sqLiteDatabase.rawQuery("SELECT count (*) as sonuc FROM Sorular WHERE soru_silinen=0 and ders_id="+ders_id, null);
        while (c.moveToNext()) {
            sonuc = c.getInt(c.getColumnIndex("sonuc"));
        }
        return sonuc;
    }
    public void ekleSoru(DataBaseHelper dbh, String soru_ad, String soru_cevap, String soru_ipucu, int soru_kontrol, int ders_id) {
        SQLiteDatabase sqLiteDatabase = dbh.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("soru_ad",soru_ad);
        values.put("soru_cevap",soru_cevap);
        values.put("soru_ipucu",soru_ipucu);
        values.put("soru_kontrol",soru_kontrol);
        values.put("soru_silinen",0);
        values.put("ders_id",ders_id);
        sqLiteDatabase.insertOrThrow("Sorular",null, values);
        sqLiteDatabase.close();
    }
    public void soruKontrol(DataBaseHelper dbh, int soru_kontrol, int soru_id) {

        SQLiteDatabase sqLiteDatabase = dbh.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("soru_kontrol",soru_kontrol);
        sqLiteDatabase.update("Sorular",values,"soru_id=?", new String[]{String.valueOf(soru_id)});
        sqLiteDatabase.close();
    }
    public int yanlisSayiSoru(DataBaseHelper dbh, int ders_id) {
        SQLiteDatabase sqLiteDatabase = dbh.getWritableDatabase();
        int sonuc = 0;
        Cursor c =sqLiteDatabase.rawQuery("SELECT count(*) as sonuc From Sorular WHERE soru_silinen=0 and ders_id="+ders_id+" and soru_kontrol=1", null);
        while (c.moveToNext()) {
            sonuc = c.getInt(c.getColumnIndex("sonuc"));
        }
        return sonuc;
    }
    public void silSoru(DataBaseHelper dbh, int soru_id) {
        SQLiteDatabase sqLiteDatabase = dbh.getWritableDatabase();
        sqLiteDatabase.delete("Sorular","soru_id=?", new String[]{String.valueOf(soru_id)});
        sqLiteDatabase.close();
    }
    public void guncelleSoru(DataBaseHelper dbh, String soru_ad, String soru_cevap, String soru_ipucu, int soru_id) {
        SQLiteDatabase sqLiteDatabase = dbh.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("soru_ad",soru_ad);
        values.put("soru_cevap", soru_cevap);
        values.put("soru_ipucu",soru_ipucu);
        values.put("soru_kontrol",0);
        values.put("soru_silinen",0);
        sqLiteDatabase.update("Sorular",values,"soru_id = ?", new String[]{String.valueOf(soru_id)});
        sqLiteDatabase.close();
    }
    public void ekleGarbage(DataBaseHelper dbh, int soru_id) {
        SQLiteDatabase sqLiteDatabase = dbh.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("soru_silinen",1);

        sqLiteDatabase.update("Sorular",values,"soru_id = ?", new String[]{String.valueOf(soru_id)});
        sqLiteDatabase.close();
    }
    public void cikarGarbage(DataBaseHelper dbh, int soru_id) {
        SQLiteDatabase sqLiteDatabase = dbh.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("soru_silinen",0);

        sqLiteDatabase.update("Sorular",values,"soru_id = ?",new String[]{String.valueOf(soru_id)});
        sqLiteDatabase.close();
    }
    public void garbageCollector(DataBaseHelper dbh) {
        SQLiteDatabase sqLiteDatabase = dbh.getWritableDatabase();
        sqLiteDatabase.delete("Sorular","soru_silinen = ?", new String[]{String.valueOf(1)});
        sqLiteDatabase.close();
    }
}
