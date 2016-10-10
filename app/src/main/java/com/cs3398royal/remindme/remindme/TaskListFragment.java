package com.cs3398royal.remindme.remindme;

import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

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
        textBox = (EditText) taskListView.findViewById(R.id.editText);
        addButton = (Button) taskListView.findViewById(R.id.button);
        addButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    addItem(v);
                    TaskListFragment.super.setSelection(TaskListFragment.super.getListAdapter().getCount() - 1);
                }
            });
        return taskListView;
        }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Create ArrayList for items to be added to task list
        //String right now, in the future probably Item class
        ArrayList<Task> taskList = new ArrayList<>();
        //Need to implement our own ArrayAdapter to take Item class and do custom ItemRow
        EditText textBox;
        //Populate list with something to test
        taskList.add(new Task("0", "Test Item 1"));
        taskList.add(new Task("1", "Test Item 2"));
        taskList.add(new Task("2", "Test Item 3"));

        //create array adapter, getActivity returns the current activity
        adapter = new TaskListAdapter(getActivity(), android.R.layout.simple_list_item_1, taskList);
        //Have to set list adapter by calling ListFragment.setListAdapter()
        super.setListAdapter(adapter);



    }

    public void addItem (View view){
        String item = textBox.getText().toString();
        //000 is ID placeholder until proper ID generation can be implemented
        if(item.length() != 0) {
            Task task = new Task("000", item);
            adapter.add(task);
            textBox.setText("");
        }
    }

}
