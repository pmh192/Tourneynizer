package com.tourneynizer.tourneynizer.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.adapters.TabFragmentPagerAdapter;

public class MainActivity extends AppCompatActivity {

    TabFragmentPagerAdapter pagerAdapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        pagerAdapter = new TabFragmentPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onBackPressed() {
        if (!pagerAdapter.popCurrent(viewPager.getCurrentItem())) {
            super.onBackPressed();
        }
    }
}
