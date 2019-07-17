package com.ahmetc.ezbergec.Activities;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmetc.ezbergec.R;
import com.ahmetc.ezbergec.dao.DataBaseHelper;
import com.ahmetc.ezbergec.dao.Derslerdao;
import com.ahmetc.ezbergec.dao.Sorulardao;
import com.ahmetc.ezbergec.Models.Sorular;

import java.util.ArrayList;
import java.util.HashSet;

public class Sinav extends AppCompatActivity {
    private int ders_id;
    private String ders_ad;
    private boolean kategori, tip;
    private TextView sinavSoru, sinavSayac;
    private TextInputEditText sinavCevap;
    private LinearLayout linearLayout;
    private Button secenekA, secenekB, secenekC, secenekD;
    private FloatingActionButton sinavSend, sinavSkip, sinavIpucu;
    private ArrayList<Sorular> sorular;
    private ArrayList<Sorular> yanlisSecenekler;
    private Sorular dogruSoru;
    private int dogru_cevap = 0, yanlis_cevap = 0, currentSoru = 0;
    private int sorusayisi;
    private DataBaseHelper dbh;
    private Sorulardao sorulardao;
    private Derslerdao derslerdao;
    private int secenek;
    private int kisi_secenek;
    private Drawable defaultDr;
    private boolean isChecked = false;

    public HashSet<Sorular> shuffleSecenek;
    private ArrayList<Sorular> asilSorular;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinav);

        ders_id = getIntent().getIntExtra("ders_id",0);
        ders_ad = getIntent().getStringExtra("ders_ad");
        kategori = getIntent().getBooleanExtra("kategori",false); // false : Hepsi, true : sadece yanlış
        tip = getIntent().getBooleanExtra("tip",false); // false : klasik, true : test
        init();

        dbh = new DataBaseHelper(Sinav.this);
        shuffleSecenek = new HashSet<>();
        yanlisSecenekler = new ArrayList<>();
        sorulardao = new Sorulardao();
        derslerdao = new Derslerdao();
        defaultDr = secenekA.getBackground();

        if(kategori) {
            sorusayisi = sorulardao.yanlisSayiSoru(dbh,ders_id);
            sorular = sorulardao.yanlisSoruGetir(dbh,ders_id);
        }
        else {
            sorusayisi = sorulardao.sayiSoru(dbh,ders_id);
            sorular = sorulardao.soruGetir(dbh,ders_id);
        }
        uploadSoru();

        if(tip) {
            linearLayout.setVisibility(View.VISIBLE);
            sinavCevap.setVisibility(View.GONE);
            sinavSend.setVisibility(View.GONE);
        }
        else {
            linearLayout.setVisibility(View.INVISIBLE);
            sinavCevap.setVisibility(View.VISIBLE);
            sinavSend.setVisibility(View.VISIBLE);
        }
        sinavIpucu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ipucu = dogruSoru.getSoru_ipucu();
                if(ipucu.equals(" ")) { ipucu = "Bu soru için ipucu yok"; }
                AlertDialog.Builder alert = new AlertDialog.Builder(Sinav.this);
                alert.setTitle("İpucu");
                alert.setMessage(ipucu);
                alert.show();
            }
        });
        sinavSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isChecked) {
                    if(!tip) sinavCevap.setText("");
                    finishControl();
                } else {
                    Toast.makeText(Sinav.this, "Cevap boş bırakılamaz", Toast.LENGTH_SHORT).show();
                }
            }
        });
        sinavSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0); // Klavye gizle.

                dogruTestKontrol(sinavCevap.getText().toString());
                sinavCevap.setTextColor(Color.WHITE);
                if(sinavCevap.getText().toString().equalsIgnoreCase(dogruSoru.getSoru_cevap())) {
                    sinavCevap.setBackground(getResources().getDrawable(R.color.accent));
                }
                else {
                    sinavCevap.setBackground(getResources().getDrawable(R.color.bg_row_background));
                    sinavCevap.setText(dogruSoru.getSoru_cevap());
                }
                sinavCevap.setEnabled(false);
                isChecked = true;
                sinavSend.setEnabled(false);
            }
        });
        secenekA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dogruTestKontrol(secenekA.getText().toString());
                secenekA.setEnabled(false);
                secenekB.setEnabled(false);
                secenekC.setEnabled(false);
                secenekD.setEnabled(false);
                kisi_secenek = 1;
                isChecked = true;
                if(kisi_secenek != secenek) {
                    secenekA.setBackground(getResources().getDrawable(R.color.bg_row_background));
                    secenekA.setTextColor(Color.WHITE);
                }
            }
        });
        secenekB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dogruTestKontrol(secenekB.getText().toString());
                secenekA.setEnabled(false);
                secenekB.setEnabled(false);
                secenekC.setEnabled(false);
                kisi_secenek = 2;
                secenekD.setEnabled(false);
                isChecked = true;

                if(kisi_secenek != secenek) {
                    secenekB.setBackground(getResources().getDrawable(R.color.bg_row_background));
                    secenekB.setTextColor(Color.WHITE);
                }
            }
        });
        secenekC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dogruTestKontrol(secenekC.getText().toString());
                secenekA.setEnabled(false);
                secenekB.setEnabled(false);
                secenekC.setEnabled(false);
                secenekD.setEnabled(false);
                isChecked = true;
                kisi_secenek = 3;

                if(kisi_secenek != secenek) {
                    secenekC.setBackground(getResources().getDrawable(R.color.bg_row_background));
                    secenekC.setTextColor(Color.WHITE);
                }
            }
        });
        secenekD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dogruTestKontrol(secenekD.getText().toString());
                secenekA.setEnabled(false);
                secenekB.setEnabled(false);
                secenekC.setEnabled(false);
                secenekD.setEnabled(false);
                isChecked = true;
                kisi_secenek = 4;

                if(kisi_secenek != secenek) {
                    secenekD.setBackground(getResources().getDrawable(R.color.bg_row_background));
                    secenekD.setTextColor(Color.WHITE);
                }
            }
        });
    }
    private void init() {
        sinavSayac = findViewById(R.id.sinavSayac);
        sinavSoru = findViewById(R.id.sinavSoru);
        sinavCevap = findViewById(R.id.sinavCevap);
        linearLayout = findViewById(R.id.linearLayout);
        secenekA = findViewById(R.id.secenekA);
        secenekB = findViewById(R.id.secenekB);
        secenekC = findViewById(R.id.secenekC);
        secenekD = findViewById(R.id.secenekD);
        sinavSend = findViewById(R.id.sinavSend);
        sinavSkip = findViewById(R.id.sinavSkip);
        sinavIpucu = findViewById(R.id.sinavIpucu);
    }
    public void uploadSoru() {
        isChecked = false;
        sinavSayac.setText((1+currentSoru)+"/"+sorusayisi);
        dogruSoru = sorular.get(currentSoru);
        yanlisSecenekler = sorulardao.yanlisGetir(dbh,ders_id,dogruSoru.getSoru_id());
        sinavSoru.setText(dogruSoru.getSoru_ad());

        if(tip)
        {
            secenekA.setEnabled(true);
            secenekB.setEnabled(true);
            secenekC.setEnabled(true);
            secenekD.setEnabled(true);

            secenekA.setBackground(defaultDr);
            secenekB.setBackground(defaultDr);
            secenekC.setBackground(defaultDr);
            secenekD.setBackground(defaultDr);

            secenekA.setTextColor(getResources().getColor(R.color.primary));
            secenekB.setTextColor(getResources().getColor(R.color.primary));
            secenekC.setTextColor(getResources().getColor(R.color.primary));
            secenekD.setTextColor(getResources().getColor(R.color.primary));

            shuffleSecenek.clear();
            shuffleSecenek.add(dogruSoru);
            shuffleSecenek.add(yanlisSecenekler.get(0));
            shuffleSecenek.add(yanlisSecenekler.get(1));
            shuffleSecenek.add(yanlisSecenekler.get(2));

            asilSorular = new ArrayList<>();
            asilSorular.clear();
            asilSorular.addAll(shuffleSecenek);
            secenekA.setText(asilSorular.get(0).getSoru_cevap());
            secenekB.setText(asilSorular.get(1).getSoru_cevap());
            secenekC.setText(asilSorular.get(2).getSoru_cevap());
            secenekD.setText(asilSorular.get(3).getSoru_cevap());
            secenekCevap(secenekA,secenekB,secenekC,secenekD);
        }
        else {
            sinavCevap.setEnabled(true);
            sinavCevap.setTextColor(getResources().getColor(R.color.primary_text));
            sinavCevap.setBackground(defaultDr);
            sinavSend.setEnabled(true);
        }
    }
    private void secenekCevap(Button secenekA, Button secenekB, Button secenekC, Button secenekD) {
        if(secenekA.getText().toString().equalsIgnoreCase(dogruSoru.getSoru_cevap())) { secenek = 1; }
        else if(secenekB.getText().toString().equalsIgnoreCase(dogruSoru.getSoru_cevap())) { secenek = 2; }
        else if(secenekC.getText().toString().equalsIgnoreCase(dogruSoru.getSoru_cevap())) { secenek = 3; }
        else if(secenekD.getText().toString().equalsIgnoreCase(dogruSoru.getSoru_cevap())) { secenek = 4; }
    }
    private void dogruTestKontrol(String kisi_cevap) {
        String dogruCevapYazi = dogruSoru.getSoru_cevap();

        switch (secenek)
        {
            case 1:
                secenekA.setBackground(getResources().getDrawable(R.color.accent));
                secenekA.setTextColor(Color.WHITE);
                break;
            case 2:
                secenekB.setBackground(getResources().getDrawable(R.color.accent));
                secenekB.setTextColor(Color.WHITE);
                break;
            case 3:
                secenekC.setBackground(getResources().getDrawable(R.color.accent));
                secenekC.setTextColor(Color.WHITE);
                break;
            case 4:
                secenekD.setBackground(getResources().getDrawable(R.color.accent));
                secenekD.setTextColor(Color.WHITE);
                break;
        }

        if(dogruCevapYazi.equalsIgnoreCase(kisi_cevap)) { // Doğru Cevap
            dogru_cevap++;
            derslerdao.arttirDogru(dbh,ders_id);
            sorulardao.soruKontrol(dbh,0,dogruSoru.getSoru_id());
        }
        else { // Yanlış Cevap
            yanlis_cevap++;
            derslerdao.arttirEksi(dbh,ders_id);
            sorulardao.soruKontrol(dbh,1,dogruSoru.getSoru_id()); // soru_kontrol 1 = yanlış
        }
    }
    private void finishControl() {
        currentSoru++;
        if(currentSoru < sorusayisi) {
            uploadSoru();
        }
        else {
            Dialog dialog = new Dialog(Sinav.this);
            dialog.setContentView(R.layout.sinavsonuc);
            TextView sonucSorudan = dialog.findViewById(R.id.sonucSorudan);
            TextView sonucDogru = dialog.findViewById(R.id.sonucDogru);
            TextView sonucYanlis = dialog.findViewById(R.id.sonucYanlis);
            final Button sonucTekrar = dialog.findViewById(R.id.sonucTekrar);
            Button sonucDon = dialog.findViewById(R.id.sonucDon);

            if(yanlis_cevap > 0) {
                sonucTekrar.setEnabled(true);
            }
            else {
                sonucTekrar.setEnabled(false);
            }

            sonucSorudan.setText(sorusayisi+" SORUDAN");
            sonucDogru.setText(getString(R.string.i_dogru)+" "+dogru_cevap);
            sonucYanlis.setText(getString(R.string.i_yanlis)+" "+yanlis_cevap);
            sonucTekrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Sinav.this,Sinav.class);
                    intent.putExtra("ders_id",ders_id);
                    intent.putExtra("kategori",true);
                    intent.putExtra("tip",tip);
                    startActivity(intent);finish();

                }
            });
            sonucDon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Sinav.this,detayDers.class);
                    intent.putExtra("ders_id",ders_id);
                    intent.putExtra("ders_ad",ders_ad);
                    startActivity(intent);
                    finish();
                }
            });
            dialog.show();
        }
    }
}
