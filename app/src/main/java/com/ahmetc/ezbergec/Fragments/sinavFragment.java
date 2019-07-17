package com.ahmetc.ezbergec.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ahmetc.ezbergec.R;
import com.ahmetc.ezbergec.Activities.Sinav;
import com.ahmetc.ezbergec.dao.DataBaseHelper;
import com.ahmetc.ezbergec.dao.Sorulardao;

public class sinavFragment extends Fragment {
    private View rootView;
    private int ders_id;
    private String ders_ad;
    private DataBaseHelper dbh;
    private Sorulardao sorulardao;
    private RadioButton kategori_sadece;
    private RadioButton kategori_tum;
    private RadioButton soru_klasik;
    private RadioButton soru_test;
    private FloatingActionButton startFab;
    private TextView sinavlarSayiSoru;
    private boolean kategori = false; // false : Hepsi, true : sadece yanlış
    private boolean tip = false; // false : klasik, true : test

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sinav, container, false);
        init();
        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        if(bundle != null) {
            ders_id = bundle.getInt("ders_id");
            ders_ad = bundle.getString("ders_ad");
        }
        dbh = new DataBaseHelper(getActivity());
        sorulardao = new Sorulardao();
        sinavlarSayiSoru.setText(sorulardao.sayiSoru(dbh,ders_id)+" "+getString(R.string.soruMevcut));
        startFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int soru_sayi = sorulardao.sayiSoru(dbh,ders_id);
                int yanlisSoru_sayi = sorulardao.yanlisSayiSoru(dbh,ders_id);
                boolean checked = true;

                if(soru_sayi < 1) {
                    Snackbar.make(v,getString(R.string.soruEklemeniz),Snackbar.LENGTH_LONG).show();
                    checked = false;
                }
                else if(soru_sayi < 4 && soru_test.isChecked()) {
                    Snackbar.make(v,getString(R.string.test4),Snackbar.LENGTH_LONG).show();checked = false;
                }
                if(yanlisSoru_sayi < 1 && kategori_sadece.isChecked()) {
                    Snackbar.make(v,getString(R.string.yanlisYok),Snackbar.LENGTH_LONG).show();
                    checked = false;
                }
                if(checked) {

                    if(kategori_sadece.isChecked()) { kategori = true; }
                    if(soru_test.isChecked()) { tip = true; }
                    Intent intent = new Intent(getActivity(), Sinav.class);
                    intent.putExtra("ders_id",ders_id);
                    intent.putExtra("ders_ad",ders_ad);
                    intent.putExtra("kategori",kategori);
                    intent.putExtra("tip",tip);
                    startActivity(intent);
                }
            }
        });
        return rootView;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        sinavlarSayiSoru.setText(sorulardao.sayiSoru(dbh,ders_id)+" "+getString(R.string.soruMevcut));
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_share).setVisible(true);
        menu.findItem(R.id.action_info).setVisible(false);
        menu.findItem(R.id.action_reset).setVisible(false);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            case R.id.action_share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = getString(R.string.paylas);
                sharingIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
                startActivity(Intent.createChooser(sharingIntent,getString(R.string.app_name)));
                return true;
            default:
                return true;
        }
    }
    private void init() {
        kategori_sadece = rootView.findViewById(R.id.kategori_sadece);
        kategori_tum = rootView.findViewById(R.id.kategori_tum);
        soru_klasik = rootView.findViewById(R.id.soru_klasik);
        soru_test = rootView.findViewById(R.id.soru_test);
        startFab = rootView.findViewById(R.id.startFab);
        sinavlarSayiSoru = rootView.findViewById(R.id.sinavlarSayiSoru);
    }
}
