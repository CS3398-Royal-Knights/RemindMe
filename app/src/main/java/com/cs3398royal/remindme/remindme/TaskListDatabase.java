package com.cs3398royal.remindme.remindme;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by Chris on 10/13/2016.
 */

@Database(name = TaskListDatabase.DATABASE_NAME, version = TaskListDatabase.DATABASE_VERSION)
public class TaskListDatabase {
    public static final String DATABASE_NAME = "TaskListDatabase";
    public static final int DATABASE_VERSION = 1;
}
