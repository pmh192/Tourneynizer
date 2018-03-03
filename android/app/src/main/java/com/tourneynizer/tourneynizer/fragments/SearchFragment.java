package com.tourneynizer.tourneynizer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.adapters.UserListAdapter;
import com.tourneynizer.tourneynizer.model.User;
import com.tourneynizer.tourneynizer.services.UserService;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private static final String USERS = "com.tourneynizer.tourneynizer.model.User[]";

    private UserListAdapter listAdapter;
    private UserService userService;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userService = new UserService();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        SearchView searchView = view.findViewById(R.id.searchBar);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                listAdapter.clear();
                userService.getUserFromEmail(query, new UserService.OnUserLoadedListener() {
                    @Override
                    public void onUserLoaded(User user) {
                        if (user != null) {
                            listAdapter.add(user);
                        }
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listAdapter.clear();
                return false;
            }
        });
        listAdapter = new UserListAdapter(getActivity());
        if (savedInstanceState != null) {
            ArrayList<User> users = savedInstanceState.getParcelableArrayList(USERS);
            listAdapter.addAll(users);
        }
        ListView userList = view.findViewById(R.id.userList);
        userList.setAdapter(listAdapter);
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                goToProfile(listAdapter.getItem(i));
            }
        });
        return view;
    }

    public void goToProfile(User user) {
        Fragment fragment = UserProfileFragment.newInstance(user);
        ((RootFragment) getParentFragment()).pushFragment(fragment);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(USERS, listAdapter.getAll());
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
