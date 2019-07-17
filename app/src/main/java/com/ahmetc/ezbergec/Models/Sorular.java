package com.ahmetc.ezbergec.Models;

public class Sorular {
    private int soru_id;
    private String soru_ad;
    private String soru_cevap;
    private String soru_ipucu;
    private int soru_kontrol;

    private int soru_silinen;
    private Dersler ders;

    public Sorular() {}

    public Sorular(int soru_id, String soru_ad, String soru_cevap, String soru_ipucu, int soru_kontrol, int soru_silinen, Dersler ders) {
        this.soru_id = soru_id;
        this.soru_ad = soru_ad;
        this.soru_cevap = soru_cevap;
        this.soru_ipucu = soru_ipucu;
        this.soru_kontrol = soru_kontrol;
        this.soru_silinen = soru_silinen;
        this.ders = ders;
    }

    public int getSoru_id() {
        return soru_id;
    }

    public void setSoru_id(int soru_id) {
        this.soru_id = soru_id;
    }

    public String getSoru_ad() {
        return soru_ad;
    }

    public void setSoru_ad(String soru_ad) {
        this.soru_ad = soru_ad;
    }

    public String getSoru_cevap() {
        return soru_cevap;
    }

    public void setSoru_cevap(String soru_cevap) {
        this.soru_cevap = soru_cevap;
    }

    public String getSoru_ipucu() {
        return soru_ipucu;
    }

    public void setSoru_ipucu(String soru_ipucu) {
        this.soru_ipucu = soru_ipucu;
    }

    public int getSoru_kontrol() {
        return soru_kontrol;
    }

    public void setSoru_kontrol(int soru_kontrol) {
        this.soru_kontrol = soru_kontrol;
    }

    public int getSoru_silinen() {
        return soru_silinen;
    }

    public void setSoru_silinen(int soru_silinen) {
        this.soru_silinen = soru_silinen;
    }

    public Dersler getDers() {
        return ders;
    }

    public void setDers(Dersler ders) {
        this.ders = ders;
    }
}
