package com.ahmetc.ezbergec.Models;

public class Dersler {
    private int ders_id;
    private String ders_ad;
    private int ders_silinen;
    private int ders_dogru;
    private int ders_yanlis;

    public Dersler(int ders_id, String ders_ad, int ders_silinen, int ders_dogru, int ders_yanlis) {
        this.ders_id = ders_id;
        this.ders_ad = ders_ad;
        this.ders_silinen = ders_silinen;
        this.ders_dogru = ders_dogru;
        this.ders_yanlis = ders_yanlis;
    }

    public int getDers_id() {
        return ders_id;
    }

    public void setDers_id(int ders_id) {
        this.ders_id = ders_id;
    }

    public String getDers_ad() {
        return ders_ad;
    }

    public void setDers_ad(String ders_ad) {
        this.ders_ad = ders_ad;
    }

    public int getDers_silinen() {
        return ders_silinen;
    }

    public void setDers_silinen(int ders_silinen) {
        this.ders_silinen = ders_silinen;
    }
    public int getDers_dogru() {
        return ders_dogru;
    }

    public void setDers_dogru(int ders_dogru) {
        this.ders_dogru = ders_dogru;
    }

    public int getDers_yanlis() {
        return ders_yanlis;
    }

    public void setDers_yanlis(int ders_yanlis) {
        this.ders_yanlis = ders_yanlis;
    }
}
