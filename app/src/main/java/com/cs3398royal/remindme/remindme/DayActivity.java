package com.cs3398royal.remindme.remindme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * Created by taylorhelton on 12/1/16.
 */

public class DayActivity extends AppCompatActivity {

    private TextView textViewDate;
    public String NextPreWeekday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.day_view_layout);

        //Set up toolbar for this activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        textViewDate = (TextView) findViewById(R.id.textViewDate);
        NextPreWeekday = getDay();
        textViewDate.setText(CommonMethod.convertWeekDaysMouth(NextPreWeekday)+"\n");

    }

    public String getDay() {

        Calendar now = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String days;
            days= format.format(now.getTime());


        return days;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.calendar_view, menu);
        getMenuInflater().inflate(R.menu.week_view,menu);
        getMenuInflater().inflate(R.menu.day_view, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();
        switch (id) {
            case R.id.calendarView:
                Intent i = new Intent(DayActivity.this, CalendarActivity.class);
                startActivity(i);
                break;
            case R.id.Weekview:
                Intent Day = new Intent(DayActivity.this, WeekActivity.class);
                startActivity(Day);
                break;
            case R.id.home:
                Intent productIntent = new Intent(DayActivity.this, MainActivity.class);
                startActivity(productIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

