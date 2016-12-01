package com.cs3398royal.remindme.remindme;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.parceler.Transient;

import java.util.Date;
import java.util.List;

/**
 * Created by ttostado on 11/29/2016.
 */
@Table(database = TaskListDatabase.class)
public class TaskList extends BaseModel {
    @Column
    @PrimaryKey(autoincrement = true)
    private long listId;

    @Column
    private String listName;

    List<Task> childTasks;

    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "childTasks")
    public List<Task> getChildTasks() {
        if (childTasks == null || childTasks.isEmpty()) {
            childTasks = SQLite.select().from(Task.class).where(Task_Table.parentListId.eq(listId)).queryList();
        }
        return childTasks;
    }

    public long getListId() { return listId; }

    public void setListId(long id) { listId = id; }

    public String getListName() { return listName; }

    public void setListName(String name) { listName = name; }


}
