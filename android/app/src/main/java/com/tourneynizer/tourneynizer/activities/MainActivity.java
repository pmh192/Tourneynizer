package com.tourneynizer.tourneynizer.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.adapters.TabFragmentPagerAdapter;
import com.tourneynizer.tourneynizer.data.Tournament;
import com.tourneynizer.tourneynizer.fragments.TournamentInfoFragment;
import com.tourneynizer.tourneynizer.fragments.TournamentListFragment;
import com.tourneynizer.tourneynizer.fragments.UserProfileFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity {//implements TournamentInfoFragment.OnTournamentRequestListener, TournamentListFragment.OnTournamentSelectedListener, TabLayout.OnTabSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        PagerAdapter pagerAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        /*
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, TournamentListFragment.newInstance(), "0").commit();
        */
    }
/*
    @Override
    public void onTournamentSelected(Tournament tournament) {
        TournamentInfoFragment tournamentInfoFragment = TournamentInfoFragment.newInstance(tournament);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment, tournamentInfoFragment, "0");
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void onTournamentRequested() {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(Integer.toString(tab.getPosition()));
        if (fragment == null) {
            fragment = getBaseFragment(tab.getPosition());
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment, fragment, Integer.toString(tab.getPosition()));
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        String tag = Integer.toString(tab.getPosition());
        while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry backStackEntry = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1);
            Log.d("Entry name:", backStackEntry.toString());
            getSupportFragmentManager().popBackStackImmediate();
        }
        /*
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, getBaseFragment(tab.getPosition()), tag);
        transaction.addToBackStack(null);
        transaction.commit();
        */
/*
    }

    private Fragment getBaseFragment(int position) {
        switch (position) {
            case 0:
                return TournamentListFragment.newInstance();
            case 1:
                return UserProfileFragment.newInstance();
            default:
                return null;
        }
    }
    */
}
