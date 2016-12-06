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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.afollestad.materialdialogs.MaterialDialog;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    Spinner prioritySpinner;
    EditText taskNameText;
    EditText descBox;
    boolean isEditMode;
    int editablePriority;
    long editableParentListId;
    boolean isEditableTaskChecked;
    int editableTaskIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);


        dateText = (EditText) findViewById(R.id.edit_text_due_date);
        listSpinner = (Spinner)findViewById(R.id.list_dropdown);
        prioritySpinner = (Spinner)findViewById(R.id.priority_spinner);
        dateFormatter = new SimpleDateFormat("E, MMM dd, yyyy", Locale.US);
        taskNameText = (EditText) findViewById(R.id.edit_text_task_name);
        descBox = (EditText) findViewById(R.id.edit_text_task_description);

        Intent i = getIntent();
        List<TaskList> taskLists = Parcels.unwrap(i.getParcelableExtra("taskLists"));
        if(i.getIntExtra("request_code", 0) == 2) {
            isEditMode = true;
            setTitle("Edit Task");
            Task editableTask = (Task) Parcels.unwrap(i.getParcelableExtra("taskToEdit"));
            //Get Variables from task
            Date editableDueDate = editableTask.getDueDate();
            editablePriority = editableTask.getTaskPriority();
            String editableName = editableTask.getTaskName();
            String editableDesc = editableTask.getDescription();
            editableParentListId = editableTask.getParentListId();
            isEditableTaskChecked = editableTask.isChecked();
            editableTaskIndex = i.getIntExtra("task_index", -1);

            //Initialize the views
            if(editableDueDate != null) {
                dateText.setText(dateFormatter.format(editableDueDate));
                mDueDate = Calendar.getInstance();
                mDueDate.setTime(editableDueDate);
            }
            taskNameText.setText(editableName);
            descBox.setText(editableDesc);
        }
        //Set up toolbar for this activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        //handles the Calendar pop up when the user presses the Due date box

        dateText.setInputType(InputType.TYPE_NULL);
        Calendar newCalendar = Calendar.getInstance();
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

        TaskList noneList = new TaskList();
        noneList.setListName("None");
        noneList.setListId(-2);
        taskLists.add(0, noneList);
        ArrayAdapter<TaskList> adapter = new ArrayAdapter<TaskList>(this, R.layout.add_task_activity_spinner_item, taskLists);
        // Specify the layout to use when the list of choices appears
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        listSpinner.setAdapter(adapter);


        //Create a spinner for giving the task a priority

        ArrayAdapter<CharSequence> priorityAdapter = ArrayAdapter.createFromResource(this,
                R.array.priorities_array, R.layout.add_task_activity_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        prioritySpinner.setAdapter(priorityAdapter);


        if(!isEditMode) {
            listSpinner.setSelection(0);
            prioritySpinner.setSelection(0);
        }
        else {
            listSpinner.setSelection((int) editableParentListId);
            prioritySpinner.setSelection(editablePriority);
        }

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
        String task = taskNameText.getText().toString();
        switch (id) {
            case R.id.action_add:
                if (task.trim().length() == 0) {
                    new MaterialDialog.Builder(this)
                            .title("Whoops!")
                            .content("The Task Name field can't be empty. Give your task a name and try again.")
                            .positiveText("OK")
                            .show();
                    return false;
                }
                Task newTask = createTaskFromInputFields(task.trim());
                Intent i = new Intent();

                //Wrap the new task in a Parcel and put it as Extra.
                i.putExtra("taskObject", Parcels.wrap(newTask));
                i.putExtra("task_index", editableTaskIndex);
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

        mDescription = descBox.getText().toString();

        String priorityLevel = prioritySpinner.getSelectedItem().toString();
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
        if(isEditMode && isEditableTaskChecked) {
            tempTask.setChecked(true);
        }
        return tempTask;
    }
}
