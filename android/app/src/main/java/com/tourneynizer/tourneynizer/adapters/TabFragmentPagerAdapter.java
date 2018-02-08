package com.tourneynizer.tourneynizer.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.data.Tournament;
import com.tourneynizer.tourneynizer.fragments.TournamentInfoFragment;
import com.tourneynizer.tourneynizer.fragments.TournamentListFragment;
import com.tourneynizer.tourneynizer.fragments.UserProfileFragment;

import java.util.ArrayList;

/**
 * Created by ryanwiener on 2/8/18.
 */

public class TabFragmentPagerAdapter extends FragmentPagerAdapter implements TournamentListFragment.OnTournamentSelectedListener {

    public static final int NUM_TABS = 2;
    private static final ArrayList<Fragment> FRAGMENTS = new ArrayList<>(NUM_TABS);

    private FragmentManager fragmentManager;

    public TabFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentManager = fm;
        TournamentListFragment tournamentList = TournamentListFragment.newInstance();
        tournamentList.setListener(this);
        FRAGMENTS.add(tournamentList);
        FRAGMENTS.add(UserProfileFragment.newInstance());
    }

    @Override
    public Fragment getItem(int position) {
        return FRAGMENTS.get(position);
    }

    @Override
    public int getCount() {
        return NUM_TABS;
    }

    @Override
    public void onTournamentSelected(Tournament tournament) {
        fragmentManager.beginTransaction().replace(R.id.viewPager, TournamentInfoFragment.newInstance(tournament), "0").addToBackStack(null).commit();
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
