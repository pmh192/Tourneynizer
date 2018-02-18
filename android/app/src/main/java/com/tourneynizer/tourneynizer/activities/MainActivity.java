package com.tourneynizer.tourneynizer.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.adapters.TabFragmentPagerAdapter;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

	private int[] imageResId = {
			R.drawable.ic_events_tab,
			R.drawable.ic_tournaments_tab,
			R.drawable.ic_search_tab,
			R.drawable.ic_add_tab,
			R.drawable.ic_profile_tab
	};

	private TabFragmentPagerAdapter pagerAdapter;
	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TabLayout tabLayout = findViewById(R.id.tabLayout);
		viewPager = findViewById(R.id.viewPager);
        pagerAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
		tabLayout.addOnTabSelectedListener(this);
		viewPager.setAdapter(pagerAdapter);
		tabLayout.setupWithViewPager(viewPager);
		for (int i = 0; i < pagerAdapter.getCount(); i++) {
			tabLayout.getTabAt(i).setIcon(imageResId[i]);
		}
	}

	@Override
	public void onBackPressed() {
		if (!pagerAdapter.popCurrent(viewPager.getCurrentItem())) {
			super.onBackPressed();
		}
	}

	@Override
	public void onTabSelected(TabLayout.Tab tab) {

	}

	@Override
	public void onTabUnselected(TabLayout.Tab tab) {

	}

	@Override
	public void onTabReselected(TabLayout.Tab tab) {
		pagerAdapter.popToRoot(viewPager.getCurrentItem());
	}

	@Override
    public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
    }
}
