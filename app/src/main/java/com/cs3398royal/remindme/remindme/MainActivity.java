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
