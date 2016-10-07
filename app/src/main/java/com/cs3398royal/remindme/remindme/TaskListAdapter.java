package com.cs3398royal.remindme.remindme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Brandon on 10/6/2016.
 * edited 10/7/16 - Brandon
 */

public class TaskListAdapter extends ArrayAdapter<Task> {


    private List<Task> tasks;

    public TaskListAdapter(Context context, int resource, List<Task> objects) {
        super(context, resource, objects);
        tasks = objects;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.list_item, parent, false);
        }

        Task task = tasks.get(position);
        //TextView taskText = (TextView) convertView.findViewById(R.id.taskText);
        //taskText.setText(task.getTaskName());
        CheckBox cb = (CheckBox)convertView.findViewById(R.id.checkBox);
        cb.setText(task.getTaskName());

        ImageButton rmBtn = (ImageButton)convertView.findViewById(R.id.imageButton);
        rmBtn.setTag(position);
        rmBtn.setOnClickListener(new AdapterView.OnClickListener()
        {
            public void onClick(View v)
            {
                tasks.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

}
