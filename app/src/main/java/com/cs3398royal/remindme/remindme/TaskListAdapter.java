package com.cs3398royal.remindme.remindme;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

    @Override
    public void add(Task task) {
        super.add(task);
        task.save();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.task_list_item, parent, false);
        }

        final Task task = tasks.get(position);
        CheckBox cb = (CheckBox)convertView.findViewById(R.id.checkBox);
        cb.setText(task.getTaskName());
        //cb.setPaintFlags(cb.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {

                if (isChecked) {
                    task.setChecked(true);
                    task.save();
                    buttonView.setPaintFlags(buttonView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                else {
                    task.setChecked(false);
                    task.save();
                    buttonView.setPaintFlags(buttonView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                }

            }
        });

        cb.setChecked(task.isChecked());

        ImageButton rmBtn = (ImageButton)convertView.findViewById(R.id.imageButton);
        //rmBtn.setTag(position);
        rmBtn.setOnClickListener(new AdapterView.OnClickListener()
        {
            public void onClick(View v)
            {
                tasks.get(position).delete();
                tasks.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

}
