package com.cs3398royal.remindme.remindme;

import android.os.Parcel;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Date;

/**
 * Created by Chris on 11/7/2016.
 */

public class TaskDataProvider {
    //A list of the tasks in the currently loaded list (or all tasks in the DB)
    private List<Task> mData;
    private Task mLastRemovedTask;
    private int mLastRemovedPosition;
    /* added by Taurino Tostado 11.29.16*/
    private List<TaskList> mLists;


    public TaskDataProvider() {
        mData = new LinkedList<>();
        //Initialize list to all tasks in database, this is like
        //displaying "all lists"
        mData = SQLite.select().from(Task.class).queryList();
        /*Added by Taurino Tostado 11.29.16*/
        //Load
        mLists = SQLite.select().from(TaskList.class).queryList();
    }

    public int getCount() {
        return mData.size();
    }

    public Task getItem(int index) {
        if (index < 0 || index >= getCount()) {
            throw new IndexOutOfBoundsException("Index " + index + "does not exist.");
        }
        return mData.get(index);
    }

    /**
     * Inserts the last removed task from the list back into the list,
     * allowing the user to un-do the removal of a task from the list
     *
     * @return Returns the index that the item was added back to.
     */
    public int undoLastRemoval() {
        if (mLastRemovedTask != null) {
            int insertedPosition;
            if (mLastRemovedPosition >= 0 && mLastRemovedPosition < mData.size()) {
                insertedPosition = mLastRemovedPosition;
            } else {
                insertedPosition = mData.size();
            }
            //If the task was removed with its options menu pinned open,
            //we want to unpin it so it doesn't come back with its options menu
            //open
            if (mLastRemovedTask.isPinned()) {
                mLastRemovedTask.setPinned(false);
            }
            mData.add(insertedPosition, mLastRemovedTask);

            mLastRemovedTask.save();

            mLastRemovedPosition = -1;
            mLastRemovedTask = null;
            return insertedPosition;
        } else {
            return -1;
        }
    }

    public void moveTask(int from, int to) {
        if (from == to) {
            return;
        }
        //Remove the task from the list
        final Task item = mData.remove(from);

        //Add it back in the new position
        mData.add(to, item);
        mLastRemovedPosition = -1;
    }

    public void moveToEnd(int pos) {
        final Task item = mData.remove(pos);
        mData.add(getCount() - 1, item);
    }

    public void moveToTop(int pos) {
        final Task item = mData.remove(pos);
        mData.add(0, item);
    }

    public void swapTask(int from, int to) {
        if (from == to) {
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
     *
     * @param position The position in the list that the task is located.
     */
    public void transitionCheckedState(int position) {
        final Task checkTask = mData.get(position);

        if (checkTask.isChecked()) {
            checkTask.setChecked(false);
            checkTask.save();
        } else {
            checkTask.setChecked(true);
            checkTask.save();
        }
    }


    /**
     * Adds a task to the list that the task is associated with and saves
     * it to the database. If the list that the task is associated with is the
     * currently loaded list, the task is added to the mData list, if not it is
     * simply saved to the database.
     *
     * @param task The Task object to be added. This Task object must have a non-null
     *             TaskList field.
     * @return Returns the index of the newly added task. Returns -1 if the task was added
     * to a different list than the current list.
     */
    public int addTask(Task task) {
        //TODO: check list associated with task to see if it's the current list & add if it is, else save to DB only
        //(save to DB will automatically save the item properly, and it will be loaded
        //next time the list is loaded)

        //By default we'll add the task to the top of the list
        Task newTask = task;
        int insertedPos = 0;
        mData.add(insertedPos, newTask);
        task.save();
        return insertedPos;
    }

    public void loadListWithID(long listId) {
        //Remove all tasks from the data provider list
        //Load all tasks from the database that are associated with the input list
        //Set mCurrLoadedList to input param
        TaskList tempList = null;
        for (int i = 0; i < mLists.size(); i++) {
            if (mLists.get(i).getListId() == listId) {
                tempList = mLists.get(i);
            }
        }
        if (tempList != null) {
            mData.clear();
            mData = tempList.getChildTasks();
        }
    }

    public void sortTasksByPriority(){
        for(int i = 0; i < mData.size(); i++)
        {
            if( mData.get(i).getTaskPriority() == 1 )
            {
                moveToTop(i);
            }
        }

        for(int i = 0; i < mData.size(); i++)
        {
            if( mData.get(i).getTaskPriority() == 2 )
            {
                moveToTop(i);
            }
        }

        for(int i = 0; i < mData.size(); i++)
        {
            if( mData.get(i).getTaskPriority() == 3 )
            {
                moveToTop(i);
            }
        }

    }

    public void sortTasksByAlpha(){
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++)
        {
            names.add(mData.get(i).getTaskName());
        }
        Collections.sort(names);
        Collections.reverse(names);
        for (int i = 0; i < names.size(); i++)
        {
            for (int j = 0; j < mData.size(); j++)
            {
                if(names.get(i).equals( mData.get(j).getTaskName()))
                {
                    moveToTop(j);
                }
            }
        }
    }

    public void sortTasksByDate(){
        ArrayList<Date> dates = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++)
        {
            dates.add(mData.get(i).getDueDate());
        }
        Collections.sort(dates);
        Collections.reverse(dates);
        for (int i = 0; i < dates.size(); i++)
        {
            for (int j = 0; j < mData.size(); j++)
            {
                if(dates.get(i).equals( mData.get(j).getDueDate()))
                {
                    moveToTop(j);
                }
            }
        }

    }
}
