package com.ahmetc.ezbergec.Activities;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import com.ahmetc.ezbergec.Fragments.istatisklerFragment;
import com.ahmetc.ezbergec.Fragments.sinavFragment;
import com.ahmetc.ezbergec.Fragments.sorularFragment;
import com.ahmetc.ezbergec.R;
import com.ahmetc.ezbergec.dao.DataBaseHelper;

import java.util.ArrayList;

public class detayDers extends AppCompatActivity {
    private TabLayout dersTabLayout;
    private ViewPager dersDetayViewPager;
    private Toolbar dersDetayToolbar;
    private String ders_ad;
    private int ders_id;
    private DataBaseHelper dbh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detay_ders);

        dersDetayToolbar = findViewById(R.id.dersDetayToolbar);
        dersDetayViewPager = findViewById(R.id.dersDetayViewPager);
        dersTabLayout = findViewById(R.id.dersDetayTabLayout);
        dbh = new DataBaseHelper(this);

        ders_ad = getIntent().getStringExtra("ders_ad");
        ders_id = getIntent().getIntExtra("ders_id",0);

        dersDetayToolbar.setTitle(ders_ad);
        setSupportActionBar(dersDetayToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        sorularFragment sorularfragment = new sorularFragment();
        sinavFragment sinavfragment = new sinavFragment();
        istatisklerFragment istatisklerfragment = new istatisklerFragment();


        adapter.FragmentEkle(sorularfragment, "Sorular");
        adapter.FragmentEkle(sinavfragment, "Sınav");
        adapter.FragmentEkle(istatisklerfragment,"İstatistikler");

        Bundle bundle = new Bundle();
        bundle.putInt("ders_id",ders_id);
        bundle.putString("ders_ad",ders_ad);

        sorularfragment.setArguments(bundle);
        sinavfragment.setArguments(bundle);
        istatisklerfragment.setArguments(bundle);

        dersDetayViewPager.setAdapter(adapter);
        dersTabLayout.setupWithViewPager(dersDetayViewPager);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);

        return super.onCreateOptionsMenu(menu);
    }
    class FragmentAdapter extends FragmentPagerAdapter
    {
        private ArrayList<Fragment> fragmentArrayList = new ArrayList<Fragment>();
        private ArrayList<String> fragmentBaslik = new ArrayList<String>();

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return fragmentArrayList.get(i);
        }

        @Override
        public int getCount() {
            return fragmentArrayList.size();
        }
        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentBaslik.get(position);
        }
        public void FragmentEkle(Fragment fragment, String baslik)
        {
            fragmentArrayList.add(fragment);
            fragmentBaslik.add(baslik);
        }
    }
}
