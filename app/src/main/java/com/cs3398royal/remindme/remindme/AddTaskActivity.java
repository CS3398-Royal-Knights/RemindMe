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

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {

    private EditText dateText;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private Calendar mDueDate;
    private long mListId;
    private String mDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

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
        Spinner spinner = (Spinner)findViewById(R.id.list_dropdown);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.lists_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
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
        EditText descBox = (EditText) findViewById(R.id.editText4);
        mDescription = descBox.getText().toString();
        //TODO: Check input fields and add other values to task to complete implementation
            //Due Date - DONE
            //Description - DONE
            //List
            //Others as needed
        if(mDueDate != null) {
            tempTask.setDueDate(mDueDate.getTime());
        } else {
            tempTask.setDueDate(null);
        }
        if(mDescription.trim().length() > 0) {
            tempTask.setDescription(mDescription.trim());
        }
        return tempTask;
    }
}
