package com.cs3398royal.remindme.remindme;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {

    private RelativeLayout layout;
    private EditText dateText;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        layout = (RelativeLayout) findViewById(R.id.content_add_task);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //handles the Calendar pop up when the user presses the Due date box
        dateText = (EditText) findViewById(R.id.editText2);
        dateText.setInputType(InputType.TYPE_NULL);
        Calendar newCalendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("E, MMM dd, yyyy", Locale.US);
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener(){
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateText.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
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
        switch (id) {
            case R.id.action_add:
                Intent i = new Intent(AddTaskActivity.this, MainActivity.class);
                i.putExtra("taskName", task);
                MainActivity.mainReference.finish();
                startActivity(i);
                finish();
                return true;
        }
        return false;
    }


}
