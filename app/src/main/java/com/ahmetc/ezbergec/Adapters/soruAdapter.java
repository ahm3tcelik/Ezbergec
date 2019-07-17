package com.ahmetc.ezbergec.Adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmetc.ezbergec.R;
import com.ahmetc.ezbergec.dao.DataBaseHelper;
import com.ahmetc.ezbergec.dao.Sorulardao;
import com.ahmetc.ezbergec.Models.Sorular;

import java.util.ArrayList;


public class soruAdapter extends RecyclerView.Adapter<soruAdapter.CardViewTutucu>{

    private Context context;
    private ArrayList<Sorular> sorularArrayList;
    private Sorulardao sorulardao = new Sorulardao();
    private EditText scanSoruAd, scanSoruCevap, scanSoruIpucu;

    public soruAdapter(Context context, ArrayList<Sorular> sorularArrayList) {
        this.context = context;
        this.sorularArrayList = sorularArrayList;
    }

    @NonNull
    @Override
    public CardViewTutucu onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.soru_card,viewGroup,false);
        return new CardViewTutucu(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewTutucu cardViewTutucu, int i) {
        final Sorular soru = sorularArrayList.get(i);
        cardViewTutucu.soruCard.setText(soru.getSoru_ad());
        cardViewTutucu.cevapCard.setText(soru.getSoru_cevap());
        final int deletedIndex = i;

        cardViewTutucu.soruCardView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.soru_goster_sil);
                Button soruLongGoster = dialog.findViewById(R.id.soruLongGoster);
                final Button soruLongSil = dialog.findViewById(R.id.soruLongSil);
                dialog.show();
                soruLongSil.setEnabled(true);
                soruLongGoster.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showCard(soru);
                        dialog.dismiss();
                    }
                });
                soruLongSil.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        soruLongSil.setEnabled(false);
                        deleteCard(soru, deletedIndex, v);
                    }
                });
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        soruLongSil.setEnabled(true);
                    }
                });
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        soruLongSil.setEnabled(true);
                    }
                });
                return false;
            }
        });


        cardViewTutucu.soruCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCard(soru);
            }
        });

        cardViewTutucu.deleteSoru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCard(soru,deletedIndex, v);
            }
        });
        cardViewTutucu.editSoru.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                DataBaseHelper dbh = new DataBaseHelper(context);
                final Dialog dialog = new Dialog(context);
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

                scanSoruAd.setText(soru.getSoru_ad());
                scanSoruCevap.setText(soru.getSoru_cevap());
                scanSoruIpucu.setText(soru.getSoru_ipucu());

                soruIpucuVoice.setVisibility(View.GONE);
                soruAdVoice.setVisibility(View.GONE);
                soruCevapVoice.setVisibility(View.GONE);

                soru_vazgec.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                soru_onayla.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DataBaseHelper dbh = new DataBaseHelper(context.getApplicationContext());
                        String soru_ad = (scanSoruAd.getText().toString().isEmpty())?empty:scanSoruAd.getText().toString().trim();
                        String soru_cevap = (scanSoruCevap.getText().toString().isEmpty())?empty:scanSoruCevap.getText().toString().trim();
                        String soru_ipucu = (scanSoruIpucu.getText().toString().isEmpty())?" ":scanSoruIpucu.getText().toString().trim();

                        if(!soru_ad.equals(empty) && !soru_cevap.equals(empty)) {
                            //Tamamlandi
                            sorulardao.guncelleSoru(dbh,soru_ad,soru_cevap,soru_ipucu,soru.getSoru_id());
                            sorularArrayList = sorulardao.tumSorular(dbh, soru.getDers().getDers_id());
                            notifyDataSetChanged();
                            dialog.dismiss();

                            Toast.makeText(context, "Soru düzenlendi", Toast.LENGTH_SHORT).show();


                        }
                        else if(soru_ad.equals(empty)) {
                            //Eksik SoruAd
                            Toast.makeText(context, "Soru boş bırakılamaz", Toast.LENGTH_SHORT).show();
                        }
                        else if(soru_cevap.equals(empty)) {
                            //Eksik Cevap
                            Toast.makeText(context, "Cevap boş bırakılamaz", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });
    }
    public void soruKaldir(Sorular soru, int position) {
        DataBaseHelper dbh = new DataBaseHelper(context.getApplicationContext());
        sorulardao.ekleGarbage(dbh, soru.getSoru_id());
        sorularArrayList.remove(position);
        notifyItemRemoved(position);
    }
    public void soruKaldirVazgec(Sorular soru, int position) {
        DataBaseHelper dbh = new DataBaseHelper(context.getApplicationContext());
        sorulardao.cikarGarbage(dbh, soru.getSoru_id());
        sorularArrayList.add(position, soru);
        notifyItemInserted(position);
    }

    @Override
    public int getItemCount() {
        return sorularArrayList.size();
    }

    public class CardViewTutucu extends RecyclerView.ViewHolder
    {
        //findViewById
        private TextView soruCard, cevapCard;
        private FloatingActionButton editSoru, deleteSoru;
        private CardView soruCardView;

        public CardViewTutucu(View v)
        {
            super(v);
            soruCardView = v.findViewById(R.id.soruCardView);
            soruCard = v.findViewById(R.id.soruCard);
            cevapCard = v.findViewById(R.id.cevapCard);
            editSoru = v.findViewById(R.id.editSoru);
            deleteSoru = v.findViewById(R.id.deleteSoru);

        }
    }
    public void showCard(Sorular soru) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.soru_view);
        TextView soruViewAd = dialog.findViewById(R.id.soruViewAd);
        TextView soruViewCevap = dialog.findViewById(R.id.soruViewCevap);
        TextView soruViewIpucu = dialog.findViewById(R.id.soruViewIpucu);
        Button soruViewOk = dialog.findViewById(R.id.soruViewOk);

        soruViewAd.setText(soru.getSoru_ad());
        soruViewCevap.setText(soru.getSoru_cevap());
        soruViewIpucu.setText(soru.getSoru_ipucu());

        soruViewOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void deleteCard(Sorular s, int i, View v) {
        final Sorular soru = s;
        String name = soru.getSoru_ad();
        final int deletedIndex = i;
        new soruAdapter(context, sorularArrayList).soruKaldir(soru, deletedIndex);


        Snackbar snackbar = Snackbar
                .make(v, name + " dersi silindi.", Snackbar.LENGTH_LONG);
        snackbar.setAction("GERİ AL", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // undo is selected, restore the deleted item
                new soruAdapter(context,sorularArrayList).soruKaldirVazgec(soru,deletedIndex);
                notifyDataSetChanged();
            }
        });
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();
        notifyDataSetChanged();
    }
}
