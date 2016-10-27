package com.cs3398royal.remindme.remindme;


import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<String> navTitles;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();

        //Initialize views
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //Initialize NavigationView and setup click actions to inflate our views
        nvDrawer = (NavigationView) findViewById(R.id.left_drawer);
        setupDrawerContent(nvDrawer);

        //Set up ActionBarDrawerToggle
        setupDrawerToggle();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //When clicked, the AddTaskActivity is opened
                Intent i = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivity(i);
            }
        });


        //Load the first task list fragment into the container
        //This will be changed dynamically with the navigation drawer
        //once multiple lists are implemented
        TaskListFragment taskList = new TaskListFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, taskList);
        transaction.commit();
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
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
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
     */
    public void selectDrawerItem(MenuItem item) {
        //Highlight selected item, we won't do this for add list item
        item.setChecked(true);
        //Set the action bar title to item title, again not for add list item
        setTitle(item.getTitle());
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

