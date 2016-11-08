package com.cs3398royal.remindme.remindme;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 10/6/2016.
 */

public class TaskListFragment extends ListFragment {
    public TaskListFragment() {

    }

    Button addButton;
    EditText textBox;
    TaskListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View taskListView = inflater.inflate(R.layout.task_list_view_layout, container, false);
        return taskListView;
        }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        String data;
        if (savedInstanceState == null) {
            Bundle extras = getActivity().getIntent().getExtras();
            if(extras == null) {
                data= null;
            } else {
                data= extras.getString("taskName");
            }
        } else {
            data= (String) savedInstanceState.getSerializable("taskName");
        }




            //Create ArrayList for items to be added to task list
        //String right now, in the future probably Item class
        List<Task> taskList;
        //Need to implement our own ArrayAdapter to take Item class and do custom ItemRow
        EditText textBox;

        //This only selects all list tasks in the database
        taskList = SQLite.select().from(Task.class).queryList();

        //create array adapter, getActivity returns the current activity
        adapter = new TaskListAdapter(getActivity(), android.R.layout.simple_list_item_1, taskList);
        //Have to set list adapter by calling ListFragment.setListAdapter()
        if(data != null)
        {
            Task task = new Task(data);
            adapter.add(task);
        }
        super.setListAdapter(adapter);



    }

    public void addItem (View view){
        String item = textBox.getText().toString();
        //000 is ID placeholder until proper ID generation can be implemented
        if(item.length() != 0) {
            Task task = new Task(item);
            adapter.add(task);
            textBox.setText("");
        }
    }

}
