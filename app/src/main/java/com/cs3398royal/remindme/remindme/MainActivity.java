package com.cs3398royal.remindme.remindme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import static com.cs3398royal.remindme.remindme.R.id.button;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    public ArrayList<String> list = new ArrayList<>();
    public ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //associates mListView with the list view in the XML
        mListView = (ListView) findViewById(R.id.to_do_list_view);

        //array filled with dummy values to be displayed - only temporary
       //String [] toDoList = {"1st Item To Do", "2nd Item To Do", "3rd Item To Do", "4th Item To Do", "5th Item To Do", "6th Item To Do",
        //       "7th Item To Do", "8th Item To Do", "9th Item To Do", "10th Item To Do", "11th Item To Do", "12th Item To Do"};

        ArrayList<String> list = new ArrayList<>();
        list.add("something");
        list.add("something else");
        list.add("something else 2");

        //an ArrayAdapter basically converts an array into a list that can then populate a listView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        mListView.setAdapter(adapter);


        Button addButton = (Button)findViewById(button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem(view);
            }


        });

    }

    public void addItem (View view){
        adapter.add("Add something by pressing the button");
    }
}
