package com.cs3398royal.remindme.remindme;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by Chris on 11/7/2016.
 */

public class TaskDataProviderFragment extends Fragment {
    private TaskDataProvider mDataProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        mDataProvider = new TaskDataProvider();
    }

    public TaskDataProvider getDataProvider() {
        return mDataProvider;
    }
}
