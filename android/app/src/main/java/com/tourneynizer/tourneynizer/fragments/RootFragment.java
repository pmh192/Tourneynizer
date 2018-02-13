package com.tourneynizer.tourneynizer.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tourneynizer.tourneynizer.R;

public class RootFragment extends Fragment {

    private Fragment baseFragment;

    public RootFragment() {
        // Required empty public constructor
    }

    public static RootFragment newInstance() {
        RootFragment fragment = new RootFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_root, container, false);
    }

    public void pushFragment(Fragment fragment) {
        getChildFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).addToBackStack(null).commit();
    }

    public boolean popFragment() {
        if (getChildFragmentManager().getBackStackEntryCount() > 0) {
            getChildFragmentManager().popBackStackImmediate();
            return true;
        }
        return false;
    }

    public void popToRoot() {
        while (getChildFragmentManager().getBackStackEntryCount() > 0) {
            getChildFragmentManager().popBackStackImmediate();
        }
    }

    public void setBaseFragment(Fragment fragment) {
        baseFragment = fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (getChildFragmentManager().getBackStackEntryCount() <= 0 && baseFragment != null) {
            getChildFragmentManager().beginTransaction().replace(R.id.fragmentContainer, baseFragment).commit();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
