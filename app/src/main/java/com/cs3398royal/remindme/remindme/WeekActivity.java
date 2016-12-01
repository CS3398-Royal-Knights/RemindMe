package com.cs3398royal.remindme.remindme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;



import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;



import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.GregorianCalendar;
import android.widget.TextView;



// *TextView


/**
 * Created by taylorhelton on 12/1/16.
 */

public class WeekActivity extends CalendarActivity{


    private TextView textViewDate;
    private TextView textViewSun;
    private TextView textViewMon;
    private TextView textViewTue;
    private TextView textViewWed;
    private TextView textViewThu;
    private TextView textViewFri;
    private TextView textViewSat;

    public String[] NextPreWeekday;

    public String firstDayOfWeek;
    public String lastDayOfWeek;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.week_view_layout);

        //Set up toolbar for this activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        textViewDate = (TextView) findViewById(R.id.textViewDate);
        textViewSun = (TextView) findViewById(R.id.textViewSun);
        textViewMon = (TextView) findViewById(R.id.textViewMon);
        textViewTue = (TextView) findViewById(R.id.textViewTue);
        textViewWed = (TextView) findViewById(R.id.textViewWed);
        textViewThu = (TextView) findViewById(R.id.textViewThu);
        textViewFri = (TextView) findViewById(R.id.textViewFri);
        textViewSat = (TextView) findViewById(R.id.textViewSat);

        NextPreWeekday = getWeekDay();
        firstDayOfWeek = CommonMethod.convertWeekDays(NextPreWeekday[0]);
        lastDayOfWeek = CommonMethod.convertWeekDays(NextPreWeekday[6]);
        textViewDate.setText(firstDayOfWeek + "-" + lastDayOfWeek + " "
                + CommonMethod.convertWeekDaysMouth(NextPreWeekday[6]));

        textViewSun.setText(CommonMethod.convertWeekDays(NextPreWeekday[0])
                + "\nSun");
        textViewMon.setText(CommonMethod.convertWeekDays(NextPreWeekday[1])
                + "\nMon");
        textViewTue.setText(CommonMethod.convertWeekDays(NextPreWeekday[2])
                + "\nTue");
        textViewWed.setText(CommonMethod.convertWeekDays(NextPreWeekday[3])
                + "\nWeb");
        textViewThu.setText(CommonMethod.convertWeekDays(NextPreWeekday[4])
                + "\nThu");
        textViewFri.setText(CommonMethod.convertWeekDays(NextPreWeekday[5])
                + "\nFri");
        textViewSat.setText(CommonMethod.convertWeekDays(NextPreWeekday[6])
                + "\nSat");

    }

    public String[] getWeekDay() {

        Calendar now = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String[] days = new String[7];
        int delta = -now.get(GregorianCalendar.DAY_OF_WEEK) + 1;
        now.add(Calendar.DAY_OF_MONTH, delta);
        for (int i = 0; i < 7; i++) {
            days[i] = format.format(now.getTime());
            now.add(Calendar.DAY_OF_MONTH, 1);
        }

        return days;

    }




    public boolean onOptionsItemSelected(MenuItem item){

        if(item.getItemId() == R.id.home) {
            Intent productIntent = new Intent(WeekActivity.this, CalendarActivity.class);
            startActivity(productIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
