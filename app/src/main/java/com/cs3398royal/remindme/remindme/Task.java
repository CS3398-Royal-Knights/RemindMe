package com.cs3398royal.remindme.remindme;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.parceler.Transient;

import java.util.Date;

/**
 * Created by Brandon on 10/6/2016.
 */

@Table(database = TaskListDatabase.class)
@Parcel(value = Parcel.Serialization.BEAN, analyze = {Task.class})
public class Task extends BaseModel{
    @Column
    @PrimaryKey (autoincrement = true)
    private long taskId;

    @Column
    private String taskName;

    @Column
    private boolean isChecked;//set 0 if false, 1 if true

    @Column
    private Date dueDate;
    //Also represent the Due Date in time-since-epoch format so it can be sent in a Parcel from AddTaskActivity
    private long dueDateMs;


    private int taskPriority;

    @Column
    private String description;
    //private TaskList parentList;

    //Used for determining whether the edit/remove swipe action is pinned to open, no need to add to DB
    //or serialize when creating Parcel with Parceler
    private boolean isPinned;



    public long getTaskId(){return taskId;}

    public String getTaskName(){return taskName;}

    public int getTaskPriority(){return taskPriority;}

    public void setTaskPriority(int priority){taskPriority = priority;}

    public boolean isChecked() {return isChecked;}

    public void setChecked(boolean checkedState){isChecked = checkedState;}

    @Transient
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
        //Set dueDateMs to the time of dueDate in milliseconds
        if(dueDate == null)
            this.dueDateMs = -1;
        else
            this.dueDateMs = dueDate.getTime();
    }
    @Transient
    public Date getDueDate() {return this.dueDate;}
    //Set isPinned getter to transient because we don't need it to be serialized when creating a Parcel
    @Transient
    public boolean isPinned() {return isPinned;}
    //Set setPinned setter to transient because we don't need it to be serialized when creating a Parcel
    @Transient
    public void setPinned(boolean pinnedState) {isPinned = pinnedState;}

    public void setTaskName(String taskName){this.taskName = taskName;}

    public void setTaskId(long id){taskId = id;}

    public void setDueDateMs(long dueDate) {
        dueDateMs = dueDate;
        if(dueDate >= 0)
            this.dueDate = new Date(dueDate);
        else
            this.dueDate = null;
    }

    public long getDueDateMs() {return this.dueDateMs;}

    public void setDescription(String desc) {description = desc;}

    public String getDescription() {return description;}

    public String toString(){return "Task name: " + getTaskName() + " isCheked: " + isChecked();}

    @ParcelConstructor
    public Task (String taskName)
    {
        this.taskName = taskName;
        isChecked = false;
    }
    public Task() {
        //Empty constructor for making an empty class
    }

}
