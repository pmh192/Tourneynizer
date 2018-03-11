package com.tourneynizer.tourneynizer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.adapters.UserListAdapter;
import com.tourneynizer.tourneynizer.model.Team;
import com.tourneynizer.tourneynizer.model.User;
import com.tourneynizer.tourneynizer.services.UserService;

import java.util.ArrayList;

public class SearchFragment extends UIQueueFragment {

    private static final String USERS = "com.tourneynizer.tourneynizer.model.User[]";
    private static final String TEAM = "com.tourneynizer.tourneynizer.model.Team";

    private Team team;
    private UserListAdapter listAdapter;
    private UserService userService;
    private SwipeRefreshLayout swipeRefresher;
    private SearchView searchView;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    public static SearchFragment newInstance(Team t) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putParcelable(TEAM, t);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            team = getArguments().getParcelable(TEAM);
        }
        userService = new UserService();
        listAdapter = new UserListAdapter(getActivity());
        if (savedInstanceState != null) {
            listAdapter.clear();
            ArrayList<User> users = savedInstanceState.getParcelableArrayList(USERS);
            if (users != null) {
                listAdapter.addAll(users);
            } else {
                loadAllUsers();
            }
        } else {
            loadAllUsers();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        swipeRefresher = view.findViewById(R.id.swipeRefresher);
        swipeRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String text = searchView.getQuery().toString();
                if (text.equals("")) {
                    loadAllUsers();
                } else {
                    refresh(text);
                }
            }
        });
        searchView = view.findViewById(R.id.searchBar);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) {
                    loadAllUsers();
                } else {
                    refresh(newText);
                }
                return false;
            }
        });
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
        Fragment fragment = UserProfileFragment.newInstance(user, team);
        ((RootFragment) getParentFragment()).pushFragment(fragment);
    }

    public void loadAllUsers() {
        listAdapter.clear();
        if (swipeRefresher != null) {
            swipeRefresher.setRefreshing(true);
        }
        userService.getAll(new UserService.OnUsersLoadedListener() {
            @Override
            public void onUsersLoaded(final User[] users) {
                performUITask(new Runnable() {
                    @Override
                    public void run() {
                        if (users != null) {
                            listAdapter.addAll(users);
                        }
                        if (swipeRefresher != null) {
                            swipeRefresher.setRefreshing(false);
                        }
                    }
                });
            }
        });
    }

    public void refresh(String s) {
        listAdapter.clear();
        if (swipeRefresher != null) {
            swipeRefresher.setRefreshing(true);
        }
        userService.getUserFromEmail(s, new UserService.OnUserLoadedListener() {
            @Override
            public void onUserLoaded(final User user) {
                performUITask(new Runnable() {
                    @Override
                    public void run() {
                        if (user != null) {
                            listAdapter.add(user);
                        }
                        if (swipeRefresher != null) {
                            swipeRefresher.setRefreshing(false);
                        }
                    }
                });
            }
        });
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
