package com.cs3398royal.remindme.remindme;

import android.app.Application;

import com.facebook.stetho.Stetho;
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

        /*Initialize Stetho for easy application debugging using Google Chrome
        While the app is running, go to chrome://inspect while the emulator is running
        and choose the device while the emulator is running.

        Once the application starts, the page will add the app to the devices. To view
        debugging info, click on the Inspect link underneath the app name. A new window
        will open with all the debugging info. To view the Database, go to the Resources
        tab in this window, and click on the dropdown arrow for WebSQL, then the dropdown
        arrow for TaskListDatabase.db, and then select the table you'd like to view.*/
        Stetho.initializeWithDefaults(this);
    }
}
