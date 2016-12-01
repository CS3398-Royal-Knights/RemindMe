package com.cs3398royal.remindme.remindme;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

//Create some tags to make finding our fragments a little easier


public class CalendarActivity extends AppCompatActivity {

//    private static final String FRAGMENT_TAG_DATA_PROVIDER = "data provider";
//    private static final String FRAGMENT_TASK_LIST = "task list";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_view_layout);

        //Set up toolbar for this activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Intent i = getIntent();
        Parcels.unwrap(i.getParcelableExtra("tasklist"));

//       getSupportFragmentManager().beginTransaction().add(new TaskDataProviderFragment(),
//                FRAGMENT_TAG_DATA_PROVIDER).commit();
//        getSupportFragmentManager().beginTransaction().add(R.id.content_frame, new TaskRecyclerViewFragment(),
//                FRAGMENT_TASK_LIST).commit();


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.week_view, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.Weekview:
                Intent i = new Intent(CalendarActivity.this, WeekActivity.class);
                startActivity(i);
                break;
            case R.id.home:
                Intent productIntent = new Intent(CalendarActivity.this, MainActivity.class);
                startActivity(productIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);

    }
}