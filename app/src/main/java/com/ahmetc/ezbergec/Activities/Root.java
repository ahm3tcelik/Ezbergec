package com.ahmetc.ezbergec.Activities;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmetc.ezbergec.Adapters.dersAdapter;
import com.ahmetc.ezbergec.R;
import com.ahmetc.ezbergec.RecyclerItemTouchHelper;
import com.ahmetc.ezbergec.dao.DataBaseHelper;
import com.ahmetc.ezbergec.dao.Derslerdao;
import com.ahmetc.ezbergec.dao.Sorulardao;
import com.ahmetc.ezbergec.Models.Dersler;

import java.util.ArrayList;
import java.util.Locale;

public class Root extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener,SearchView.OnQueryTextListener {
    private DataBaseHelper dbh;
    private dersAdapter dersadapter;
    private ConstraintLayout rootLayout;
    private TextView rootDersYok;
    private ArrayList<Dersler> derslerArrayList;
    private FloatingActionButton rootFab;
    private Derslerdao derslerdao;
    private Sorulardao sorulardao;
    private RecyclerView rootRecyclerView;
    private EditText scanDersad;
    private static final int REQ_CODE_SPEECH_INPUT=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);
        derslerdao = new Derslerdao();
        sorulardao = new Sorulardao();
        dbh = new DataBaseHelper(this);

        rootLayout = findViewById(R.id.rootLayout);
        Toolbar rootToolbar = findViewById(R.id.rootToolbar);
        rootFab = findViewById(R.id.rootFab);
        rootDersYok = findViewById(R.id.rootDersYok);

        rootRecyclerView = findViewById(R.id.rootRecyclerView);
        rootRecyclerView.setHasFixedSize(true);
        rootRecyclerView.setItemAnimator(new DefaultItemAnimator());
        rootRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rootRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        setSupportActionBar(rootToolbar);
        derslerArrayList = derslerdao.tumDersler(dbh);
        dersadapter = new dersAdapter(Root.this, derslerArrayList);
        rootRecyclerView.setAdapter(dersadapter);
        updateDersYok();

        final ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(ItemTouchHelper.START, ItemTouchHelper.LEFT, Root.this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rootRecyclerView);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback1 = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.START,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Row is swiped from recycler view
                // remove it from adapter
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        // attaching the touch helper to recycler view
        new ItemTouchHelper(itemTouchHelperCallback1).attachToRecyclerView(rootRecyclerView);


        rootFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Root.this);
                dialog.setContentView(R.layout.ders_ekle);

                FloatingActionButton dersVoice = dialog.findViewById(R.id.dersVoice);
                Button ders_onayla = dialog.findViewById(R.id.ders_onayla);
                Button ders_vazgec = dialog.findViewById(R.id.ders_vazgec);
                scanDersad = dialog.findViewById(R.id.scanDersad);

                dersVoice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startVoiceInput();
                    }
                });

                ders_vazgec.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                ders_onayla.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ders_ad = (scanDersad.getText().toString().isEmpty())?"#^AMAD^#":scanDersad.getText().toString();
                        if(ders_ad.equals("#^AMAD^#")) {
                            Toast.makeText(Root.this, getString(R.string.dersnotBos), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            derslerdao.ekleDers(dbh, ders_ad);
                            derslerArrayList = derslerdao.tumDersler(dbh);
                            dersadapter = new dersAdapter(Root.this, derslerArrayList);
                            rootRecyclerView.setAdapter(dersadapter);
                            updateDersYok();
                            Toast.makeText(Root.this, ders_ad+" "+getString(R.string.dersiEklendi), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if(resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    scanDersad.setText(result.get(0));
                }
                break;
            }
        }
    }
    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,getString(R.string.lutfenDers));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(this, getString(R.string.maalesefDestek), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);
        menu.findItem(R.id.action_reset).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_info:
                startActivity(new Intent(Root.this,Info.class));
                break;
            case R.id.action_share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = getString(R.string.paylas);
                sharingIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
                startActivity(Intent.createChooser(sharingIntent,getString(R.string.app_name)));
                break;
            default: return false;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof dersAdapter.CardViewTutucu) {

            // get the removed item name to display it in snack bar
            String name = derslerArrayList.get(viewHolder.getAdapterPosition()).getDers_ad();

            // backup of removed item for undo purpose
            final Dersler deletedItem = derslerArrayList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();
            // remove the item from recycler view

            dersadapter.dersKaldir(deletedItem, viewHolder.getAdapterPosition());


            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(rootLayout, name + " "+getString(R.string.dersiSilindi), Snackbar.LENGTH_LONG);
            snackbar.setAction(getString(R.string.geriAl), new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    dersadapter.dersKaldirVazgec(deletedItem, deletedIndex);
                    updateDersYok();
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
            updateDersYok();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        derslerArrayList = derslerdao.tumDersler(dbh);
        dersadapter = new dersAdapter(Root.this, derslerArrayList);
        rootRecyclerView.setAdapter(dersadapter);
        updateDersYok();
    }

    @Override
    protected void onPause() {
        super.onPause();
        derslerdao.garbageCollector(dbh);
    }
    public void updateDersYok() {
        if(derslerArrayList.size() < 1) {
            rootDersYok.setVisibility(View.VISIBLE);
        }
        else {
            rootDersYok.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        aramaYap(s);
        return false;
    }
    public void aramaYap(String s) {
        derslerArrayList = new Derslerdao().findDers(dbh, s);
        dersadapter = new dersAdapter(this, derslerArrayList);
        rootRecyclerView.setAdapter(dersadapter);
        updateDersYok();
    }
}
