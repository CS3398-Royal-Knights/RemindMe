package com.cs3398royal.remindme.remindme;

/**
 * Created by Brandon on 10/6/2016.
 */

public class Task {
    private String taskId;
    private String taskName;

    public String getTaskId(){return taskId;}

    public String getTaskName(){return taskName;}

    public Task (String taskID, String taskName)
    {
        this.taskId = taskID;
        this.taskName = taskName;
    }

}
