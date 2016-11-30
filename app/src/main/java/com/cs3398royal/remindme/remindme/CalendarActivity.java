package com.cs3398royal.remindme.remindme;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Locale;

/**
 * Created by taylorhelton on 11/8/16.
 */

public class CalendarActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_view_layout);

        //Set up toolbar for this activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//        //Create a spinner dropdown for displaying lists that a task can be added to
//        Spinner listSpinner = (Spinner)findViewById(R.id.list_dropdown);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.lists_array, android.R.layout.simple_spinner_item);
//        // Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        // Apply the adapter to the spinner
//        listSpinner.setAdapter(adapter);
//
//        //Create a spinner for giving the task a priority
//        Spinner prioritySpinner = (Spinner)findViewById(R.id.priority_spinner);
//        ArrayAdapter<CharSequence> priorityAdapter = ArrayAdapter.createFromResource(this,
//                R.array.priorities_array, android.R.layout.simple_spinner_item);
//        // Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        // Apply the adapter to the spinner
//        prioritySpinner.setAdapter(priorityAdapter);


    }



    public boolean onOptionsItemSelected(MenuItem item){

        if(item.getItemId() == R.id.home) {
            Intent productIntent = new Intent(CalendarActivity.this, MainActivity.class);
            startActivity(productIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
