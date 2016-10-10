package com.cs3398royal.remindme.remindme;

/**
 * Created by Brandon on 10/6/2016.
 */

public class Task {
    private String taskId;
    private String taskName;
    private boolean isChecked;

    public String getTaskId(){return taskId;}

    public String getTaskName(){return taskName;}

    public boolean isChecked() {return isChecked;}

    public void setCheck(boolean bool){isChecked = bool;}

    public String toString(){return "Task name: " + getTaskName() + " isCheked: " + isChecked();}

    public Task (String taskID, String taskName)
    {
        this.taskId = taskID;
        this.taskName = taskName;
        isChecked = false;
    }

}
