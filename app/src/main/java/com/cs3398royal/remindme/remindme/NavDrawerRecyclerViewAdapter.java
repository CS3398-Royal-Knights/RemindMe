package com.cs3398royal.remindme.remindme;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Chris on 10/8/2016.
 *
 * This class is an extension of the RecyclerView.Adapter class for use with the RecyclerView
 * that is implemented for the Navigation Drawer. This class will need to undergo extensive changes
 * to add the ability to create multiple TaskLists.
 *
 * Namely, the constructor will need to take in a list of TaskList objects that each have at least
 * a unique identifier (long) and a title. Once this is implemented, we can possibly do things like
 * dynamically create a color coding icon for each TaskList and display it in an ImageView in the
 * nav_drawer_list_item layout
 */

public class NavDrawerRecyclerViewAdapter extends RecyclerView.Adapter<NavDrawerRecyclerViewAdapter.ViewHolder> {
    ArrayList<String> titles;
    TypedArray icons;
    Context context;

    NavDrawerRecyclerViewAdapter(ArrayList<String> titles , Context context){

        this.titles = titles;
        this.context = context;
    }
    /*
    *This ViewHolder class implements View.OnClickListener to handle click events on items in the list.
    * If itemType is 1, it implies that the view is a nav_drawer_list_item with a TextView, so
    * we set navTitle to the TextView in that layout with id nav_drawer_item_title
    *
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Create local variables
        TextView navTitle; //Title of the navigation list item
        Context context;

        public ViewHolder(View drawerItem, int itemType, Context context) {

            super(drawerItem);
            this.context = context;
            drawerItem.setOnClickListener(this);
            if (itemType == 1) {
                navTitle = (TextView) itemView.findViewById(R.id.nav_drawer_item_title);
                //Can add other things too like icons, checkboxes, etc
            }
        }
        /*
        *Override the onClick method for the OnClickListener to inflate a new fragment
        * with list items containing the Tasks are in the TaskList that that was clicked
         */
        @Override
        public void onClick(View v) {
            MainActivity mainActivity = (MainActivity) context;
            mainActivity.mDrawerLayout.closeDrawers();
            //Begin loading a new fragment
            //FragmentTransaction fragmentTransaction = mainActivity.getSupportFragmentManager().beginTransaction();
            /*TODO: Have item click load new list fragment into the activity
            *       create list fragment by loading the proper task list
            *       based on the unique identifier of the item clicked in
            *       nav drawer
            * */
        }
    }

    /*
    * Override the onCreateViewHolder method of the RecyclerView.Adapter class to inflate our
    * custom list items and header
    * */
    @Override
    public NavDrawerRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(viewType == 1) {
            View itemLayout = layoutInflater.inflate(R.layout.nav_drawer_list_item, null);
            return new ViewHolder(itemLayout, viewType, context);
        }
        else if(viewType == 0) { //if the view is a header
            //TODO: This is where the code goes to set up the header for nav menu,
            //      Basically do same as above except use itemHeader and inflate the
            //      nav_drawer_header layout
        }
        //If not one of these viewType, return null
        return null;
    }
    /*
    *Called by RecyclerView.Adapter to display the data at the specified position in the data set
    * (array of strings).
     */
    @Override
    public void onBindViewHolder(NavDrawerRecyclerViewAdapter.ViewHolder holder, int position) {
        //If we want to add the header, we need to check to be sure item at position is not the
        //header before setting text, we assume that the header is position 0
        holder.navTitle.setText(titles.get(position));
    }

    /*
    *Return the total number of items in the Navigation Drawer List.
    * If we want to include the header, we need to +1 the count
    * because the header is not counted in the list of item titles
     */
    @Override
    public int getItemCount() {
        return titles.size();
    }

    /*
    *Returns 0 if the position of the item in the NavDrawer is 0 (only used if we implement header)
    * Returns 1 otherwise, indicating that the item in the NavDrawer is a nav_drawer_list_item
     */
    @Override
    public int getItemViewType(int position) {
        //if(position==0) return 0; This will be used only if we include the header
        return 1;
    }

    /*
    *Returns the unique identifier of the TaskList object located at "position"
    * This function will be used once we implement the ability to have multiple
    * Task Lists, since each list will need to have a unique id associated with it
    * so that the lists can be created dynamically, and so that we know which TaskList
    * to query the database for
     */
//    @Override
//    public long getItemId(int position) {
//        //TODO: Return the unique identifier for the TaskList at position
//    }
}
