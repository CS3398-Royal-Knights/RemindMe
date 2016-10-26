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

        //Initialize DBFlow for Database Management upon creation
        //of the application. This allows us to use database function
        //while the views are still being created by the system.
        FlowManager.init(new FlowConfig.Builder(this).build());
    }
}
