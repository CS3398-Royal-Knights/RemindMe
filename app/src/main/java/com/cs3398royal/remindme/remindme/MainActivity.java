package com.cs3398royal.remindme.remindme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

import static com.cs3398royal.remindme.remindme.R.id.imageButton;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private ArrayList<Task> list = new ArrayList<>();
    private EditText textBox;
    TaskListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //associates mListView with the list view in the XML
        mListView = (ListView) findViewById(R.id.to_do_list_view);

        ArrayList<Task> list = new ArrayList<>();
        Task testTask = new Task("1111", "This is the first task");
        Task testTask2 = new Task("222", "This is the second task");
        Task testTask3 = new Task("333", "This is the third task");
        list.add(testTask);
        list.add(testTask2);
        list.add(testTask3);

        adapter = new TaskListAdapter(this, R.layout.list_item, list);
        mListView.setAdapter(adapter);


        Button addButton = (Button)findViewById(R.id.button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem(view);
                mListView.setSelection(mListView.getAdapter().getCount()-1);
            }


        });


    }

    public void addItem (View view){
        textBox = (EditText) findViewById(R.id.editText);
        String item = textBox.getText().toString();
        Task task = new Task("000", item);
        adapter.add(task);
        textBox.setText("");
    }


}
