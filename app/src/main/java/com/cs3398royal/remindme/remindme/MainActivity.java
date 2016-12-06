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
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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


        //Create the data provider fragment before anything else is done, so we can start loading
        //things into the app


        //Initialize views
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //Initialize NavigationView and setup click actions to inflate our views
        nvDrawer = (NavigationView) findViewById(R.id.left_drawer);
        headerLayout = nvDrawer.getHeaderView(0);
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
                i.putExtra("taskLists", Parcels.wrap(getDataProvider().getLoadedLists()));
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
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();
        switch (id) {
            case R.id.calendarView:
                Intent i = new Intent(MainActivity.this, CalendarActivity.class);

                //Wrap the new task in a Parcel and put it as Extra.
                i.putExtra("tasklist", Parcels.wrap(getDataProvider().getTaskList()));
                //Done wrapping, can now set the result and send new
                //task to the list
                startActivity(i);
                break;
            case R.id.action_sort:
                //sort the tasks
                //MenuItem menuBtn = (MenuItem)findViewById(R.id.action_sort);
                View menuItemView = findViewById(R.id.action_sort);
                PopupMenu popup = new PopupMenu(MainActivity.this, menuItemView);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch (id) {
                            case R.id.priority:
                                getDataProvider().sortTasksByPriority();
                                updateRecyclerViewList();
                                break;
                            case R.id.alpha:
                                getDataProvider().sortTasksByAlpha();
                                updateRecyclerViewList();
                                break;
                            case R.id.date:
                                getDataProvider().sortTasksByDate();
                                updateRecyclerViewList();
                                break;

                        }
                        return true;
                    }
                });


                //getDataProvider().sortTasksByPriority();

                break;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
        setupDrawerContent(nvDrawer);
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
     *
     * @param requestCode The request code that this activity sent when creating
     *                    the other activity.
     * @param resultCode  An integer that is the result code that the created activity
     *                    sends back to this activity upon finishing.
     * @param data        An Intent that contains the information being passed back to this
     *                    activity by the created activity upon finishing. This Intent can contain
     *                    extras including strings and possibly a parcelable.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Check to see if the result is successful
        if (resultCode == RESULT_OK) {
            //Check to see if the result is an edit or an add
            if (requestCode == REQUEST_ADD_TASK) {
                int newIndex;
                Task newTask = Parcels.unwrap(data.getParcelableExtra("taskObject"));
                long newTaskParentListId = newTask.getParentListId();
                getDataProvider().addTask(newTask);
                //If the index that the Task was added to is a valid index we
                // tell the RecyclerView that the data set was updated.
                updateRecyclerViewList();
            }
            else if(requestCode == REQUEST_EDIT_TASK) {
                int taskIndex = data.getIntExtra("task_index", -1);
                Task editedTask = Parcels.unwrap(data.getParcelableExtra("taskObject"));
                if(taskIndex >= 0) {
                    getDataProvider().replaceItemWithTask(taskIndex, editedTask);
                    updateRecyclerViewList();
                }
                else {
                    String editError = "Something broke! We couldn't make the changes you were looking for :( please try again.";
                    Snackbar snackbar = Snackbar.make(
                            findViewById(R.id.main_content),
                            editError,
                            Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sort_task_menu, menu);
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
     *
     * @param pos An int that is the position of the item in the RecyclerView
     */
    public void onItemClicked(int pos) {
        //Currently only creates a Snackbar with the description for testing purposes.
        // If we decide to do something when a task is clicked, the code for it goes here.
        //Some things we could do are maybe expand the item to show the "details" for the
        //item??
        String descStr = getDataProvider().getItem(pos).getDescription();
        if (descStr != null && descStr.trim() != "") {
            Snackbar snackbar = Snackbar.make(
                    findViewById(R.id.main_content),
                    descStr,
                    Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    /**
     * This function will create a fragment or an activity that allows the user to edit the details
     * of the task that is at the position position. It will then update the task at the position
     * in the data provider, and notify the recycler view that the task has changed
     *
     * @param position An int that is the position of the item in the RecyclerView list.
     */
    public void onItemViewEditOptionClicked(int position) {
        Task editTask = getDataProvider().getItem(position);
        if (editTask != null) {
            Intent i = new Intent(MainActivity.this, AddTaskActivity.class);
            i.putExtra("taskLists", Parcels.wrap(getDataProvider().getLoadedLists()));
            i.putExtra("taskToEdit", Parcels.wrap(editTask));
            i.putExtra("request_code", REQUEST_EDIT_TASK);
            i.putExtra("task_index", position);
            startActivityForResult(i, REQUEST_EDIT_TASK);
        } else {
            String editError = "Something broke! We couldn't find the task you were looking for! :(";
            Snackbar snackbar = Snackbar.make(
                    findViewById(R.id.main_content),
                    editError,
                    Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    /**
     * Do something when a task that is currently loaded has its checked state changed.
     *
     * @param position     The position of the task in the list
     * @param checkedState The new checked state for the task that was changed.
     *                     Set to true if the task is "completed" and false otherwise.
     */
    public void onItemCheckedStateChanged(int position, boolean checkedState) {
        //For testing purposes we will create a snackbar that just tells us we (un)checked the task.
        Snackbar snackbar;
        String taskCompleted = "You set the task \"" + getDataProvider().getItem(position).getTaskName()
                + "\" as completed!";
        String taskIncomplete = "You set the task \"" + getDataProvider().getItem(position).getTaskName()
                + "\" as not completed!";
        if (checkedState) {
            snackbar = Snackbar.make(
                    findViewById(R.id.main_content),
                    taskCompleted,
                    Snackbar.LENGTH_LONG);
        } else {
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
        if (pos >= 0) {
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
     * Notifies the Recycler View that the list has changed in some way, and it should
     * re-draw the list
     */
    private void updateRecyclerViewList() {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TASK_LIST);
        ((TaskRecyclerViewFragment) fragment).notifyDataSetChanged();
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
                    @Override
//@NonNull means the MenuItem object passed to this function can't be null
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
        //Load lists into NavView
        final Menu navMenu = navigationView.getMenu();
        List<TaskList> listsFromDB = getDataProvider().getLoadedLists();
        if (!listsFromDB.isEmpty()) {
            for (TaskList list : listsFromDB) {
                //Unsafe casting from long to int, could break the entire app but hey it works
                MenuItem newItem = navMenu.add(R.id.nav_user_lists, (int) list.getListId(), 1, list.getListName());
                newItem.setCheckable(true);
                newItem.setIcon(R.drawable.ic_list_black_24dp);
            }
        }

        //Set All Tasks as our default list
        navMenu.findItem(R.id.nav_all_tasks_option).setChecked(true);
        selectDrawerItem(navMenu.findItem(R.id.nav_all_tasks_option));

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
                new MaterialDialog.Builder(MainActivity.this)
                        .title("Create a New List")
                        .content("Give your list a name: ")
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input("List Name", null, false, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                //Add new list to database and add also to the menu
                                long newListId = getDataProvider().createList(input.toString());
                                MenuItem newItem = nvDrawer.getMenu().add(R.id.nav_user_lists,
                                        (int) newListId, 1, input);
                                newItem.setCheckable(true);
                                newItem.setIcon(R.drawable.ic_list_black_24dp);
                                mDrawerLayout.openDrawer(Gravity.LEFT);
                            }
                        })
                        .negativeText("Cancel").show();
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
        //If all tasks option is selected, load all tasks
        if (item.getItemId() == R.id.nav_all_tasks_option) {
            if (item.isCheckable())
                item.setChecked(true);
            setTitle(item.getTitle());
            getDataProvider().loadAllTasks();
            //We create a completely new fragment in case the content frame has something other than a list
            //in it (like the settings page)
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new TaskRecyclerViewFragment(),
                    FRAGMENT_TASK_LIST).commit();
        }
        //If uncategorized is selected, load all tasks where the parent list id is -1
        else if (item.getItemId() == R.id.nav_uncategorized_tasks_option) {
            if (item.isCheckable())
                item.setChecked(true);
            setTitle(item.getTitle());
            getDataProvider().loadUncategorizedTasks();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new TaskRecyclerViewFragment(),
                    FRAGMENT_TASK_LIST).commit();
        }
        //If the item is one of the user's lists, load it from the database
        else if (item.getGroupId() == R.id.nav_user_lists) {
            if (item.isCheckable())
                item.setChecked(true);
            setTitle(item.getTitle());
            getDataProvider().loadTasksFromListWithId(item.getItemId());
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new TaskRecyclerViewFragment(),
                    FRAGMENT_TASK_LIST).commit();
        }
        else if(item.getItemId() == R.id.nav_settings_option) {
            Snackbar s = Snackbar.make(
                    findViewById(R.id.main_content),
                    "Sorry, settings is not implemented yet.",
                    Snackbar.LENGTH_LONG
            );
            s.show();
        }
        else if(item.getItemId() == R.id.nav_feedback_option) {
            Snackbar s = Snackbar.make(
                    findViewById(R.id.main_content),
                    "Sorry, feedback is not implemented yet.",
                    Snackbar.LENGTH_LONG
            );
            s.show();
        }

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
        if (getSupportActionBar() != null)
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

