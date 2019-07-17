package com.ahmetc.ezbergec.Fragments;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmetc.ezbergec.Adapters.soruAdapter;
import com.ahmetc.ezbergec.R;
import com.ahmetc.ezbergec.dao.DataBaseHelper;
import com.ahmetc.ezbergec.dao.Sorulardao;
import com.ahmetc.ezbergec.Models.Sorular;

import java.util.ArrayList;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;


public class sorularFragment extends Fragment {
    private View rootView;
    private TextView sorularSoruYok;
    private RecyclerView sorularRecyclerView;
    private soruAdapter soruadapter;
    private DataBaseHelper dbh;
    private Sorulardao sorulardao;
    private ArrayList<Sorular> sorularArrayList;
    private FloatingActionButton sorularFab;
    private int ders_id;
    private EditText scanSoruAd, scanSoruCevap, scanSoruIpucu;
    private static final int REQ_CODE_SPEECH_INPUT=100;
    private int scanner = 1; // 1: Soru, Cevap, Ipucu;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sorular, container, false);
        sorulardao = new Sorulardao();
        setHasOptionsMenu(true);
        init();

        Bundle bundle = getArguments();
        if(bundle != null) {
            ders_id = bundle.getInt("ders_id");
        }
        dbh = new DataBaseHelper(getActivity());
        sorularRecyclerView.setHasFixedSize(true);
        sorularRecyclerView.setItemAnimator(new DefaultItemAnimator());
        sorularRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        sorularRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        registerForContextMenu(sorularRecyclerView);


        sorularArrayList = sorulardao.tumSorular(dbh,ders_id); // Tüm soruları getir.
        updateSoruYok();

        sorularFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.soru_ekle);

                final String empty = "#^AMAD^#";
                Button soru_onayla = dialog.findViewById(R.id.soru_onayla);
                Button soru_vazgec = dialog.findViewById(R.id.soru_vazgec);
                FloatingActionButton soruIpucuVoice = dialog.findViewById(R.id.soruIpucuVoice);
                FloatingActionButton soruAdVoice = dialog.findViewById(R.id.soruAdVoice);
                FloatingActionButton soruCevapVoice = dialog.findViewById(R.id.soruCevapVoice);
                scanSoruAd = dialog.findViewById(R.id.scanSoruAd);
                scanSoruCevap = dialog.findViewById(R.id.scanSoruCevap);
                scanSoruIpucu = dialog.findViewById(R.id.scanSoruIpucu);

                soruIpucuVoice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        scanner = 3;
                        startVoiceInput();
                    }
                });
                soruAdVoice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        scanner = 1;
                        startVoiceInput();
                    }
                });
                soruCevapVoice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        scanner = 2;
                        startVoiceInput();
                    }
                });

                soru_vazgec.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                soru_onayla.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String soru_ad = (scanSoruAd.getText().toString().isEmpty())?empty:scanSoruAd.getText().toString().trim();
                        String soru_cevap = (scanSoruCevap.getText().toString().isEmpty())?empty:scanSoruCevap.getText().toString().trim();
                        String soru_ipucu = (scanSoruIpucu.getText().toString().isEmpty())?" ":scanSoruIpucu.getText().toString().trim();

                        if(!soru_ad.equals(empty) && !soru_cevap.equals(empty)) {
                            //Tamamlandi
                            sorulardao.ekleSoru(dbh, soru_ad, soru_cevap,soru_ipucu,0,ders_id);
                            sorularArrayList = sorulardao.tumSorular(dbh, ders_id);
                            updateSoruYok();
                            Toast.makeText(getActivity(), soru_ad+" "+getString(R.string.soruEklendi), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                        else if(soru_ad.equals(empty)) {
                            //Eksik SoruAd
                            Toast.makeText(getActivity(), getString(R.string.soruNotBos), Toast.LENGTH_SHORT).show();
                        }
                        else if(soru_cevap.equals(empty)) {
                            //Eksik Cevap
                            Toast.makeText(getActivity(), getString(R.string.cevapNotBos), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });
        return rootView;
    }
    private void init() {
        sorularRecyclerView = rootView.findViewById(R.id.sorularRecyclerView);
        sorularSoruYok = rootView.findViewById(R.id.sorularSoruYok);
        sorularFab = rootView.findViewById(R.id.sorularFab);
    }
    public void aramaYap(String s) {
        sorularArrayList = sorulardao.findSoru(dbh,s,ders_id);
        updateSoruYok();
    }
    public void updateSoruYok()  {
        soruadapter = new soruAdapter(getActivity(), sorularArrayList);
        sorularRecyclerView.setAdapter(soruadapter);

        if(sorulardao.sayiSoru(dbh,ders_id) < 1) {
            sorularSoruYok.setVisibility(View.VISIBLE);
        }
        else {
            sorularSoruYok.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.action_search).setVisible(true);
        menu.findItem(R.id.action_info).setVisible(false);
        menu.findItem(R.id.action_reset).setVisible(false);
        menu.findItem(R.id.action_share).setVisible(true);

        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                aramaYap(s);
                return false;
            }
        });
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
            default:return false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sorulardao.garbageCollector(dbh);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if(resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    switch (scanner) {
                        case 1:
                            scanSoruAd.setText(result.get(0));
                            break;
                        case 2:
                            scanSoruCevap.setText(result.get(0));
                            break;
                        case 3:
                            scanSoruIpucu.setText(result.get(0));
                            break;
                        default:break;
                    }
                }
            }
        }
    }
    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,getString(R.string.eklemekIstek));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.maalesefDestek), Toast.LENGTH_SHORT).show();
        }
    }
}
