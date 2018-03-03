package com.tourneynizer.tourneynizer.fragments;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by ryanwiener on 3/3/18.
 */

public class UIQueueFragment extends Fragment {

    private Queue<Runnable> activityTasks;

    public UIQueueFragment() {
        super();
        activityTasks = new LinkedList<>();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        while (!activityTasks.isEmpty()) {
            activity.runOnUiThread(activityTasks.poll());
        }
    }

    public void performUITask(Runnable activityTask) {
        if (isAdded()) {
            getActivity().runOnUiThread(activityTask);
        } else {
            activityTasks.add(activityTask);
        }
    }
}
