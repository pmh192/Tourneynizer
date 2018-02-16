package com.tourneynizer.tourneynizer.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.ViewGroup;

import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.data.User;
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

    private FragmentManager fragmentManager;
    private RootFragment currentFragment;

    public TabFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentManager = fm;
    }

    @Override
    public Fragment getItem(int position) {
        RootFragment rootFragment = RootFragment.newInstance();
        switch (position) {
            case 0:
                rootFragment.setBaseFragment(TournamentListFragment.newInstance());
                return rootFragment;
            case 1:
                rootFragment.setBaseFragment(TournamentListFragment.newInstance());
                return rootFragment;
            case 2:
                rootFragment.setBaseFragment(SearchFragment.newInstance());
                return rootFragment;
            case 3:
                rootFragment.setBaseFragment(CreateTournamentFragment.newInstance());
                return rootFragment;
            case 4:
                rootFragment.setBaseFragment(UserProfileFragment.newInstance(new User()));
                return rootFragment;
            default:
                return rootFragment;
        }
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (currentFragment != object) {
            currentFragment = ((RootFragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }

    public boolean popCurrent(int position) {
        return currentFragment.popFragment();
    }

    public void popToRoot(int position) {
        currentFragment.popToRoot();
    }

    @Override
    public int getCount() {
        return NUM_TABS;
    }
}
