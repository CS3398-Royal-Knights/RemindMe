package com.cs3398royal.remindme.remindme;

import android.content.Context;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.migration.BaseMigration;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

/**
 * Created by Chris on 10/13/2016.
 */

@Database(name = TaskListDatabase.DATABASE_NAME, version = TaskListDatabase.DATABASE_VERSION)
public class TaskListDatabase {
    public static final String DATABASE_NAME = "TaskListDatabase";
    public static final int DATABASE_VERSION = 14;

    @Migration(version = 14, database = TaskListDatabase.class)
    public static class MigrateVersion2 extends BaseMigration {
        @Override
        public void migrate(DatabaseWrapper database) {
            ModelAdapter myAdapter = FlowManager.getModelAdapter(Task.class);
            database.execSQL("DROP TABLE IF EXISTS " + myAdapter.getTableName());
            database.execSQL(myAdapter.getCreationQuery());
        }
    }
}
