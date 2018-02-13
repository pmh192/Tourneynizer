package com.tourneynizer.tourneynizer.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;

import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.fragments.CreateTournamentFragment;
import com.tourneynizer.tourneynizer.fragments.RootFragment;
import com.tourneynizer.tourneynizer.fragments.SearchFragment;
import com.tourneynizer.tourneynizer.fragments.TournamentListFragment;
import com.tourneynizer.tourneynizer.fragments.UserProfileFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryanwiener on 2/9/18.
 */

public class TabFragmentPagerAdapter extends FragmentPagerAdapter {

    private static final int NUM_TABS = 5;
    private final List<RootFragment> FRAGMENTS = new ArrayList<>(NUM_TABS);

    private Context context;

    public TabFragmentPagerAdapter(Context cont, FragmentManager fm) {
        super(fm);
        context = cont;
        List<Fragment> baseFragments = new ArrayList<>(NUM_TABS);
        baseFragments.add(TournamentListFragment.newInstance());
        baseFragments.add(TournamentListFragment.newInstance());
        baseFragments.add(SearchFragment.newInstance());
        baseFragments.add(CreateTournamentFragment.newInstance());
        baseFragments.add(UserProfileFragment.newInstance());
        for (int i = 0; i < NUM_TABS; i++) {
            FRAGMENTS.add(RootFragment.newInstance());
            FRAGMENTS.get(i).setBaseFragment(baseFragments.get(i));
        }
    }

    @Override
    public Fragment getItem(int position) {
        return FRAGMENTS.get(position);
    }

    public boolean popCurrent(int position) {
        return FRAGMENTS.get(position).popFragment();
    }

    @Override
    public int getCount() {
        return NUM_TABS;
    }

    /*
    @Override
    public CharSequence getPageTitle(int position) {
        return "Tab " + position;
    }
    */
}
