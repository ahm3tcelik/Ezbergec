package com.ahmetc.ezbergec.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmetc.ezbergec.R;
import com.ahmetc.ezbergec.dao.DataBaseHelper;
import com.ahmetc.ezbergec.dao.Derslerdao;
import com.ahmetc.ezbergec.dao.Sorulardao;

public class istatisklerFragment extends Fragment {
    private View rootView;
    private int ders_id, soru_sayi = 0;
    private int dogru_sayi = 0, yanlis_sayi = 0;
    private int cozulen_soru;
    private String ders_ad;
    private DataBaseHelper dbh;
    private Derslerdao derslerdao;
    private Sorulardao sorulardao;
    private TextView txtProgress, istatistikDersAdi,istatistikSoruSayi,istatistikCozulen;
    private TextView istatistikDogru, istatistikYanlis;
    private ProgressBar progressBar;
    private int pStatus = 0;
    private Handler handler = new Handler();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_istatiskler, container, false);
        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        if(bundle != null) {
            ders_id = bundle.getInt("ders_id");
            ders_ad = bundle.getString("ders_ad");
        }
        dbh = new DataBaseHelper(getActivity());
        derslerdao = new Derslerdao();
        sorulardao = new Sorulardao();
        init();

        return rootView;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_share).setVisible(true);
        menu.findItem(R.id.action_reset).setVisible(true);
        menu.findItem(R.id.action_info).setVisible(false);

        dogru_sayi = derslerdao.dogruSayi(dbh,ders_id);
        yanlis_sayi = derslerdao.yanlisSayi(dbh,ders_id);
        soru_sayi = sorulardao.sayiSoru(dbh,ders_id);
        cozulen_soru = dogru_sayi + yanlis_sayi;
        istatistikDersAdi.setText(ders_ad);
        istatistikSoruSayi.setText(getString(R.string.soruSayisi)+" "+soru_sayi);
        istatistikCozulen.setText(getString(R.string.cozulenSoru)+" "+cozulen_soru);
        istatistikDogru.setText(getString(R.string.i_dogru)+" "+dogru_sayi);
        istatistikYanlis.setText(getString(R.string.i_yanlis)+" "+yanlis_sayi);

        new Thread(new Runnable() {
            @Override
            public void run() {
                int payda;
                if(cozulen_soru == 0) payda = 1;
                else payda = cozulen_soru;
                for(pStatus=0; pStatus <= dogru_sayi*100/payda; pStatus++) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(pStatus);
                            txtProgress.setText((pStatus)+" %");
                        }
                    });
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
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
            case R.id.action_reset:
                derslerdao.istatistikSifirla(dbh,ders_id);
                istatistikCozulen.setText(getString(R.string.cozulenSoru)+" "+"0");
                istatistikDogru.setText(getString(R.string.i_dogru)+" "+"0");
                istatistikYanlis.setText(getString(R.string.i_yanlis)+" "+"0");
                Toast.makeText(getActivity().getApplicationContext(),getString(R.string.istatistikReset), Toast.LENGTH_SHORT).show();
                return true;
            default:return false;
        }
    }
    private void init() {
        txtProgress = rootView.findViewById(R.id.txtProgress);
        progressBar = rootView.findViewById(R.id.progressBar);
        txtProgress = rootView.findViewById(R.id.txtProgress);
        istatistikDersAdi = rootView.findViewById(R.id.istatistikDersAdi);
        istatistikSoruSayi = rootView.findViewById(R.id.istatistikSoruSayi);
        istatistikCozulen = rootView.findViewById(R.id.istatistikCozulen);
        istatistikDogru = rootView.findViewById(R.id.istatistikDogru);
        istatistikYanlis = rootView.findViewById(R.id.istatistikYanlis);
    }
}