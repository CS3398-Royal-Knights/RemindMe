package com.cs3398royal.remindme.remindme;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by Chris on 10/13/2016.
 */

public class TaskListApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //Initialize DBFlow for Database Management
        FlowManager.init(new FlowConfig.Builder(this).build());
    }
}
