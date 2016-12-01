package com.cs3398royal.remindme.remindme;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {

    private EditText dateText;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private Calendar mDueDate;
    private long mListId;
    private String mDescription;
    Spinner listSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Intent i = getIntent();
        List<TaskList> taskLists = Parcels.unwrap(i.getParcelableExtra("taskLists"));

        //Set up toolbar for this activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //handles the Calendar pop up when the user presses the Due date box
        dateText = (EditText) findViewById(R.id.editText2);
        dateText.setInputType(InputType.TYPE_NULL);
        Calendar newCalendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("E, MMM dd, yyyy", Locale.US);
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener(){
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
                mDueDate = Calendar.getInstance();
                mDueDate.set(year, monthOfYear, dayOfMonth);
                dateText.setText(dateFormatter.format(mDueDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        //Create a spinner dropdown for displaying lists that a task can be added to
        listSpinner = (Spinner)findViewById(R.id.list_dropdown);
        TaskList noneList = new TaskList();
        noneList.setListName("None");
        noneList.setListId(-2);
        taskLists.add(0, noneList);
        ArrayAdapter<TaskList> adapter = new ArrayAdapter<TaskList>(this, android.R.layout.simple_spinner_item, taskLists);
        // Specify the layout to use when the list of choices appears
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        listSpinner.setAdapter(adapter);
        listSpinner.setSelection(0);

        //Create a spinner for giving the task a priority
        Spinner prioritySpinner = (Spinner)findViewById(R.id.priority_spinner);
        ArrayAdapter<CharSequence> priorityAdapter = ArrayAdapter.createFromResource(this,
                R.array.priorities_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        prioritySpinner.setAdapter(priorityAdapter);
        prioritySpinner.setSelection(0);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_task_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        EditText text = (EditText) findViewById(R.id.editText);
        String task = text.getText().toString();
        if (task.trim().length() == 0)
            return false;
        switch (id) {
            case R.id.action_add:
                Task newTask = createTaskFromInputFields(task.trim());
                Intent i = new Intent();

                //Wrap the new task in a Parcel and put it as Extra.
                i.putExtra("taskObject", Parcels.wrap(newTask));
                //Done wrapping, can now set the result and send new
                //task to the list
                setResult(RESULT_OK, i);
                finish();
                return true;
        }
        return false;
    }

    /**
     * Takes the name of the task to be created, and returns a Task
     * with class fields that correspond to the values of the user
     * input fields in the activity
     *
     * @param taskName  The name of the task to be created. This value
     *                  must not be null.
     *
     * @return  A Task object with fields that correspond to the user
     *          input.
     */
    Task createTaskFromInputFields(@NonNull String taskName) {
        Task tempTask = new Task(taskName);
        TaskList tempList;
        EditText descBox = (EditText) findViewById(R.id.editText4);
        mDescription = descBox.getText().toString();
        Spinner priority = (Spinner)findViewById(R.id.priority_spinner);
        String priorityLevel = priority.getSelectedItem().toString();
        if(priorityLevel.equals("None")) {
            tempTask.setTaskPriority(0);
        }
        else if(priorityLevel.equals("Low"))
        {
            tempTask.setTaskPriority(1);
        }
        else if (priorityLevel.equals("Medium"))
        {
            tempTask.setTaskPriority(2);
        }
        else if (priorityLevel.equals("High"))
        {
            tempTask.setTaskPriority(3);
        }
        if(mDueDate != null) {
            tempTask.setDueDate(mDueDate.getTime());
        } else {
            tempTask.setDueDate(null);
        }
        if(mDescription.trim().length() > 0) {
            tempTask.setDescription(mDescription.trim());
        }
        if(!(listSpinner.getSelectedItem() == null)) {
            tempList = (TaskList) listSpinner.getSelectedItem();
            tempTask.setParentListId(tempList.getListId());
        }
        return tempTask;
    }
}
