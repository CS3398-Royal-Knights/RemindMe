package com.cs3398royal.remindme.remindme;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Brandon on 10/6/2016.
 */

@Table(database = TaskListDatabase.class)
public class Task extends BaseModel{
    @Column
    @PrimaryKey (autoincrement = true)
    private long taskId;

    @Column
    private String taskName;

    @Column
    private boolean isChecked;//set 0 if false, 1 if true
    //private List parentList;

    //Used for determining whether the edit/remove swipe action is pinned to open, no need to add to DB
    private boolean isPinned;

    public long getTaskId(){return taskId;}

    public String getTaskName(){return taskName;}

    public boolean isChecked() {return isChecked;}

    public void setChecked(boolean checkedState){isChecked = checkedState;}

    public boolean isPinned() {return isPinned;}

    public void setPinned(boolean pinnedState) {isPinned = pinnedState;}

    public void setTaskName(String taskName){this.taskName = taskName;}

    public void setTaskId(long id){taskId = id;}

    public String toString(){return "Task name: " + getTaskName() + " isCheked: " + isChecked();}

    public Task (String taskName)
    {
        this.taskName = taskName;
        isChecked = false;
    }
    public Task() {
        //Empty constructor for making an empty class
    }

}
