package com.cs3398royal.remindme.remindme;


import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.net.Uri;
import android.graphics.Color;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import org.parceler.Parcels;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private View headerLayout;
    private Toolbar mToolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<String> navTitles;
    private FloatingActionButton fab;

    //Create some tags to make finding our fragments a little easier
    private static final String FRAGMENT_TAG_DATA_PROVIDER = "data provider";
    private static final String FRAGMENT_TASK_LIST = "task list";

    //Request codes for Intent transactions
    static final int REQUEST_ADD_TASK = 1;
    static final int REQUEST_EDIT_TASK = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();

        //recieves data when the add activity task is the most recent activity
//        String data;
//        if (savedInstanceState == null) {
//            Bundle extras = getIntent().getExtras();
//            if(extras == null) {
//                data= null;
//            } else {
//                data= extras.getString("taskName");
//            }
//        } else {
//            data= (String) savedInstanceState.getSerializable("taskName");
//        }
//        System.out.println(data + " Has been entered");

        //Initialize views
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //Initialize NavigationView and setup click actions to inflate our views
        nvDrawer = (NavigationView) findViewById(R.id.left_drawer);
        headerLayout = nvDrawer.getHeaderView(0);
        setupDrawerContent(nvDrawer);
        setHeaderLayoutClickListener(headerLayout);

        //Set up ActionBarDrawerToggle
        setupDrawerToggle();

        //Set up Floating Action Button
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //When clicked, the AddTaskActivity is opened
                Intent i = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivityForResult(i, REQUEST_ADD_TASK);
            }
        });

        /**
         * Here is where we initialize the new recycler view. We are going to create two fragments.
         * The first fragment is just a data provider, it maintains a Linked List of all the tasks
         * that belong to a particular list, and mediates the interaction between the database and
         * the fragment/activity trying to access the tasks. This fragment has no visible view
         * associated with it, so you will not see it in the app, it's simply a helper fragment.
         *
         * The second fragment is our task list, which is populated by the tasks currently in the
         * data provider. This fragment is inflated as a RecyclerView, and replaces the former ListView
         * as our main app content.
         */
        getSupportFragmentManager().beginTransaction().add(new TaskDataProviderFragment(),
                FRAGMENT_TAG_DATA_PROVIDER).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.content_frame, new TaskRecyclerViewFragment(),
                FRAGMENT_TASK_LIST).commit();


    }

    /**
     * Overrides the onOptionsItemSelected method,
     * checks to see if the ActionBarDrawerToggle
     * sees the menu item as itself, and if it does
     * we allow it to handle the action. If not, let
     * the super class handle it.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();
        switch (id) {
            case R.id.calendarView:
                startActivity(new Intent(MainActivity.this, CalendarActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //Send configuration changes to drawer toggle so it can update
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Captures a result from an activity that this activity created through
     * an intent. This function is used to pass information between this activity
     * and other activities by passing an intent back once the created activity has
     * finished.
     * @param requestCode   The request code that this activity sent when creating
     *                      the other activity.
     *
     * @param resultCode    An integer that is the result code that the created activity
     *                      sends back to this activity upon finishing.
     *
     * @param data  An Intent that contains the information being passed back to this
     *              activity by the created activity upon finishing. This Intent can contain
     *              extras including strings and possibly a parcelable.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Check to see if the result we get is from the AddTaskActivity
        if(requestCode == REQUEST_ADD_TASK) {
            //Check to see if the AddTaskActivity completed successfully
            if(resultCode == RESULT_OK) {
                int newIndex;
                newIndex = getDataProvider().addTask((Task) Parcels.unwrap(data.getParcelableExtra("taskObject")));
                //If the index that the Task was added to is a valid index we
                // tell the RecyclerView that the data set was updated.
                if(newIndex >= 0) {
                    final Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TASK_LIST);
                    ((TaskRecyclerViewFragment) fragment).notifyItemInserted(newIndex);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.calendar_view, menu);

        return true;
    }

    /**
     * This function will be called when an item is removed from the list.
     * Currently what will happen is a snackbar will appear that notifies the
     * user that they removed an item from the list, and will give them the option
     * to undo this action.
     */
    public void onItemRemoved(int pos) {
        Snackbar snackbar = Snackbar.make(
                findViewById(R.id.main_content),
                R.string.task_item_removed_text,
                Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snackbar_undo_text, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemUndoActionClicked();
            }
        });
        snackbar.show();
    }

    /**
     * Do something when an Item in the task list has its options
     * menu pinned to the open state.
     *
     * @param pos The position in the list of the item with its menu pinned open
     */
    public void onItemPinned(int pos) {
        //Currently do nothing. Some useful things we could do are to make some
        //parts of the app unuseable
    }

    /**
     * Do something when a particular task in the list of tasks is clicked.
     * @param pos   An int that is the position of the item in the RecyclerView
     */
    public void onItemClicked(int pos) {
        //Currently only creates a Snackbar with the Due Date for testing purposes.
        // If we decide to do something when a task is clicked, the code for it goes here.
        //Some things we could do are maybe expand the item to show the "details" for the
        //item??
        Date taskDueDate = getDataProvider().getItem(pos).getDueDate();
        String dueDateStr;
        if(taskDueDate != null) {
            dueDateStr = "Task is due: " + DateFormat.getDateInstance().format(taskDueDate);
        } else {
            dueDateStr = "Task has no due date.";
        }
        int priority = getDataProvider().getItem(pos).getTaskPriority();
        String priorityStr="";
        if(priority == 1)
            priorityStr = "; Task is low priority";
        if(priority == 2)
            priorityStr = "; Task is medium priority";
        if(priority == 3)
            priorityStr = "; Task is high priority";
        dueDateStr += priorityStr;
        Snackbar snackbar = Snackbar.make(
                findViewById(R.id.main_content),
                dueDateStr,
                Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    /**
     * This function will create a fragment or an activity that allows the user to edit the details
     * of the task that is at the position position. It will then update the task at the position
     * in the data provider, and notify the recycler view that the task has changed
     *
     * @param position  An int that is the position of the item in the RecyclerView list.
     */
    public void onItemViewEditOptionClicked(int position) {
        //TODO: create an edit task fragment/activity for editing the details of a task

        //Currently we will create a snackbar that shows that we clicked the edit button
        //for a task.
        String editPressed = "You pressed edit for item " + getDataProvider().getItem(position).getTaskName();
        Snackbar snackbar = Snackbar.make(
                findViewById(R.id.main_content),
                editPressed,
                Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    /**
     * Do something when a task that is currently loaded has its checked state changed.
     * @param position  The position of the task in the list
     * @param checkedState  The new checked state for the task that was changed.
     *                      Set to true if the task is "completed" and false otherwise.
     */
    public void onItemCheckedStateChanged(int position, boolean checkedState) {
        //For testing purposes we will create a snackbar that just tells us we (un)checked the task.
        Snackbar snackbar;
        String taskCompleted = "You set the task \"" + getDataProvider().getItem(position).getTaskName()
                + "\" as completed!";
        String taskIncomplete = "You set the task \"" + getDataProvider().getItem(position).getTaskName()
                + "\" as not completed!";
        if(checkedState) {
            snackbar = Snackbar.make(
                    findViewById(R.id.main_content),
                    taskCompleted,
                    Snackbar.LENGTH_LONG);
        }
        else {
            snackbar = Snackbar.make(
                    findViewById(R.id.main_content),
                    taskIncomplete,
                    Snackbar.LENGTH_LONG);
        }
        snackbar.show();
    }

    /**
     * Undoes a task item removal by adding it back to the list and notifying
     * the recycler view container of the insertion of the item.
     */
    private void onItemUndoActionClicked() {
        //Ask the data provider to insert the last item that it removed back into
        //the list
        int pos = getDataProvider().undoLastRemoval();
        if(pos >= 0) {
            //If the data provider has successfully added the item back to the list, we notify the
            //recycler view that the item was added back to the list so it can update the visual list
            //it's displaying
            final Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TASK_LIST);
            ((TaskRecyclerViewFragment) fragment).notifyItemInserted(pos);
        }
    }

    public TaskDataProvider getDataProvider() {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_DATA_PROVIDER);
        return ((TaskDataProviderFragment) fragment).getDataProvider();
    }

    /**
     * This method sets up an ItemSelectedListener for our
     * navigation view that captures the menu item that was
     * selected and passes it to selectDrawerItem to be
     * processed. Here we will also populate the drawer
     * with our lists with dynamically created MenuItems.
     * Refer to this reference for more:
     * https://developer.android.com/reference/android/view/Menu.html#add(int, int, int, int)
     */
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override//@NonNull means the MenuItem object passed to this function can't be null
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
        navigationView.setItemIconTintList(null);
        //Add code to load lists from the database, create menu items, and add them to the
        //navigation menu
    }

    /**
     * setHeaderLayoutClickListener finds the nav_drawer_new_list
     * button that is in the header of the navigation drawer and
     * creates a click handler that inflates a New List Activity
     * so the user can add a list.
     */
    private void setHeaderLayoutClickListener(View headerLayout) {
        ImageButton addListButton;
        addListButton = (ImageButton) headerLayout.findViewById(R.id.nav_drawer_new_list);
        addListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawers();
                //TODO: Make this click handler create an intent to a New List Activity
                Snackbar youDidIt = Snackbar.make(findViewById(R.id.main_content),
                        "You pressed the Add List Button!", Snackbar.LENGTH_LONG);
                youDidIt.show();
            }
        });
    }

    /**
     * In the selectDrawerItem method we will first
     * determine which list we have selected by getting
     * its item id with item.getItemId(). This ItemId is
     * set when populating the menu with our lists, it is the
     * same as the database key. We will then query the
     * database for each task associated with this itemId
     * and load them into a new fragment and inflate it.
     * There will be a couple of special case menu items that
     * will have unique Ids out of the range of the database
     * Ids (menu items like the Add List item and Settings
     * item), we'll handle these with a switch case.
     *
     * @param item A MenuItem object that is the Item that was selected
     *             in the NavigationView Menu. This Item will have a title,
     *             item id, and possibly a SubMenu that the MenuItem is associated
     *             with. We can check if the MenuItem is part of a SubMenu by invoking
     *             hasSubMenu() on the item.
     */
    public void selectDrawerItem(MenuItem item) {
        //Highlight selected item, we won't do this for add list item
        if(item.isCheckable())
            item.setChecked(true);
        //Set the action bar title to item title, again not for add list item
        setTitle(item.getTitle());

        //Pass the list ID (MenuItem.getId()) to the DataProvider to load all tasks associated
            //with that list
        //Create a new TaskRecyclerViewFragment, passing getDataProvider() to the consturctor
        //Replace the current Fragment with new Recycler Fragment

        //Close the nav drawer
        mDrawerLayout.closeDrawers();
    }

    /**
     * setupToolbar() creates a new toolbar object, and then
     * sets the action bar for the activity to the new toolbar.
     * It then sets DisplayShowHomeEnabled to true to show the
     * hamburger icon.
     */
    void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.nav_drawer_toolbar);
        setSupportActionBar(mToolbar);
        //If the action bar exists, we set it to show the home button on the toolbar
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    /**
     * setupDrawerToggle creates a new ActionBarDrawerToggle object
     * and sets the object as the DrawerLayout's DrawerListener.
     * This allows the hamburger icon to animate when opening
     * the drawer.
     */
    void setupDrawerToggle() {
        //Create a new drawertoggle object
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

    }
}

