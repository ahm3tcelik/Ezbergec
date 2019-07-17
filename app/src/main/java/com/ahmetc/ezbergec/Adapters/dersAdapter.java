package com.ahmetc.ezbergec.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ahmetc.ezbergec.R;
import com.ahmetc.ezbergec.dao.DataBaseHelper;
import com.ahmetc.ezbergec.dao.Derslerdao;
import com.ahmetc.ezbergec.dao.Sorulardao;
import com.ahmetc.ezbergec.Activities.detayDers;
import com.ahmetc.ezbergec.Models.Dersler;

import java.util.ArrayList;

public class dersAdapter extends RecyclerView.Adapter<dersAdapter.CardViewTutucu>{

    private Context context;
    private ArrayList<Dersler> derslerArrayList;
    private DataBaseHelper dbh;
    private Derslerdao derslerdao = new Derslerdao();

    public class CardViewTutucu extends RecyclerView.ViewHolder
    {
        //findViewById
        public TextView dersSayiCard, dersAdCard;
        private FloatingActionButton dersButton;
        public RelativeLayout dersBackground;
        public ConstraintLayout dersForeground;


        public CardViewTutucu(View v)
        {
            super(v);
            dersSayiCard = v.findViewById(R.id.dersSayiCard);
            dersAdCard = v.findViewById(R.id.dersAdCard);
            dersBackground = v.findViewById(R.id.dersBackground);
            dersForeground = v.findViewById(R.id.dersForeground);
            dersButton = v.findViewById(R.id.dersButton);
        }
    }

    public dersAdapter(Context context, ArrayList<Dersler> derslerArrayList) {
        this.context = context;
        this.derslerArrayList = derslerArrayList;
    }
    @NonNull
    @Override
    public CardViewTutucu onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ders_card,viewGroup,false);
        dbh = new DataBaseHelper(context);
        return new CardViewTutucu(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewTutucu cardViewTutucu, int i) {
        final Dersler ders = derslerArrayList.get(i);
        int sayi = new Sorulardao().sayiSoru(dbh, ders.getDers_id());
        cardViewTutucu.dersAdCard.setText(String.valueOf(ders.getDers_ad()));
        cardViewTutucu.dersSayiCard.setText(sayi+" Soru");

        cardViewTutucu.dersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, detayDers.class);
                i.putExtra("ders_id",ders.getDers_id());
                i.putExtra("ders_ad",ders.getDers_ad());
                context.startActivity(i);
            }
        });
        cardViewTutucu.dersForeground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, detayDers.class);
                i.putExtra("ders_id",ders.getDers_id());
                i.putExtra("ders_ad",ders.getDers_ad());
                context.startActivity(i);
            }
        });
    }
    public void dersKaldir(Dersler ders, int position) {
        derslerdao.ekleGarbage(dbh, ders.getDers_id());
        derslerArrayList.remove(position);
        notifyItemRemoved(position);
    }
    public void dersKaldirVazgec(Dersler ders, int position) {
        derslerdao.cikarGarbage(dbh, ders.getDers_id());
        derslerArrayList.add(position, ders);
        notifyItemInserted(position);
    }

    @Override
    public int getItemCount() {
        return derslerArrayList.size();
    }

}
