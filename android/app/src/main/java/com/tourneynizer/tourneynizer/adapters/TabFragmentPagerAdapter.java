package com.tourneynizer.tourneynizer.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.tourneynizer.tourneynizer.model.User;
import com.tourneynizer.tourneynizer.fragments.CreateTournamentFragment;
import com.tourneynizer.tourneynizer.fragments.RootFragment;
import com.tourneynizer.tourneynizer.fragments.SearchFragment;
import com.tourneynizer.tourneynizer.fragments.TournamentListFragment;
import com.tourneynizer.tourneynizer.fragments.UserProfileFragment;

import java.sql.Time;

/**
 * Created by ryanwiener on 2/9/18.
 */

public class TabFragmentPagerAdapter extends FragmentPagerAdapter {

    private static final int NUM_TABS = 5;

    private User user;
    private RootFragment currentFragment;

    public TabFragmentPagerAdapter(User u, FragmentManager fm) {
        super(fm);
        user = u;
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
                rootFragment.setBaseFragment(UserProfileFragment.newInstance(user));
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

    public boolean popCurrent() {
        return currentFragment.popFragment();
    }

    public void popToRoot() {
        currentFragment.popToRoot();
    }

    @Override
    public int getCount() {
        return NUM_TABS;
    }
}
