package com.cs3398royal.remindme.remindme;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Chris on 11/7/2016.
 */

public class TaskDataProvider {
    private List<Task> mData;
    private Task mLastRemovedTask;
    private int mLastRemovedPosition;

    public TaskDataProvider() {
        mData = new LinkedList<>();
        //Initialize list to all tasks in database, this is like
        //displaying "all lists"
        mData = SQLite.select().from(Task.class).queryList();
    }

    public int getCount() {
        return mData.size();
    }

    public Task getItem(int index) {
        if(index < 0 || index >= getCount()) {
            throw new IndexOutOfBoundsException("Index " + index + "does not exist.");
        }
        return mData.get(index);
    }

    /**
     * Inserts the last removed task from the list back into the list,
     * allowing the user to un-do the removal of a task from the list
     *
     * @return  Returns the index that the item was added back to.
     */
    public int undoLastRemoval() {
        if(mLastRemovedTask != null) {
            int insertedPosition;
            if(mLastRemovedPosition >= 0 && mLastRemovedPosition < mData.size()) {
                insertedPosition = mLastRemovedPosition;
            }
            else {
                insertedPosition = mData.size();
            }
            mData.add(insertedPosition, mLastRemovedTask);

            mLastRemovedTask.save();

            mLastRemovedPosition = -1;
            mLastRemovedTask = null;
            return insertedPosition;
        }
        else {
            return -1;
        }
    }

    public void moveTask(int from, int to) {
        if(from == to) {
            return;
        }
        //Remove the task from the list
        final Task item = mData.remove(from);

        //Add it back in the new position
        mData.add(to, item);
        mLastRemovedPosition = -1;
    }

    public void swapTask(int from, int to) {
        if(from == to) {
            return;
        }
        //Swap the items in from & to
        Collections.swap(mData, to, from);
        mLastRemovedPosition = -1;
    }

    public void removeItem(int position) {
        final Task removedTask = mData.remove(position);

        removedTask.delete();

        mLastRemovedTask = removedTask;
        mLastRemovedPosition = position;
    }

    /**
     * Transitions a task's checked state to true if it's not already checked,
     * and false if not. Also updates the task's new checked state in the DB
     * @param position  The position in the list that the task is located.
     */
    public void transitionCheckedState(int position) {
        final Task checkTask = mData.get(position);

        if(checkTask.isChecked()) {
            checkTask.setChecked(false);
            checkTask.save();
        }
        else {
            checkTask.setChecked(true);
            checkTask.save();
        }
    }

/*    public void loadListFromDB(TaskList list) {
        //Remove all tasks from the data provider list
        //Load all tasks from the database that are associated with the input list
    }*/

}
