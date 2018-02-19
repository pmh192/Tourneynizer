package com.tourneynizer.tourneynizer.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.adapters.TabFragmentPagerAdapter;
import com.tourneynizer.tourneynizer.model.User;
import com.tourneynizer.tourneynizer.requesters.UserRequester;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

	private static final int[] imageResId = {
			R.drawable.ic_events_tab,
			R.drawable.ic_tournaments_tab,
			R.drawable.ic_search_tab,
			R.drawable.ic_add_tab,
			R.drawable.ic_profile_tab
	};
	public static final String USER = "com.tourneynizer.tourneynizer.model.User";

	private TabFragmentPagerAdapter pagerAdapter;
	private ViewPager viewPager;
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (getIntent() != null) {
			user = getIntent().getParcelableExtra(USER);
		}
		TabLayout tabLayout = findViewById(R.id.tabLayout);
		viewPager = findViewById(R.id.viewPager);
        pagerAdapter = new TabFragmentPagerAdapter(user, getSupportFragmentManager());
		tabLayout.addOnTabSelectedListener(this);
		viewPager.setAdapter(pagerAdapter);
		tabLayout.setupWithViewPager(viewPager);
		for (int i = 0; i < pagerAdapter.getCount(); i++) {
			tabLayout.getTabAt(i).setIcon(imageResId[i]);
		}
	}

	@Override
	public void onBackPressed() {
		if (!pagerAdapter.popCurrent()) {
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
		pagerAdapter.popToRoot();
	}

	@Override
    public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_activity_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.logOut) {
			UserRequester.logOut(getApplicationContext());
			startActivity(new Intent(this, LoginActivity.class));
			finishAffinity();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
	    //UserRequester.logOut(this);
		super.onDestroy();
	}
}
