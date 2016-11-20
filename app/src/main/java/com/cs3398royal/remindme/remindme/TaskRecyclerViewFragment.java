package com.cs3398royal.remindme.remindme;

import android.graphics.drawable.NinePatchDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.SwipeDismissItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.touchguard.RecyclerViewTouchActionGuardManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
/**
 * Created by Chris on 11/18/2016.
 */

public class TaskRecyclerViewFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter mWrappedAdapter;
    private  RecyclerViewSwipeManager mRecyclerViewSwipeManager;
    private RecyclerViewTouchActionGuardManager mRecyclerViewTouchActionGuardManager;

    public TaskRecyclerViewFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Inflate the view, creating a recycler manager in the process, and return that view
        //to the calling function
        return inflater.inflate(R.layout.task_recycler_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //get the current recyclerview in the current view for manipulation
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.task_recycler_view);
        //Create a new layoutmanager so that we can lay out the recycler view's children
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        //This basically stops the user from being able to scroll the list while they are swiping an item
        mRecyclerViewTouchActionGuardManager = new RecyclerViewTouchActionGuardManager();
        mRecyclerViewTouchActionGuardManager.setInterceptVerticalScrollingWhileAnimationRunning(true);
        mRecyclerViewTouchActionGuardManager.setEnabled(true);

        //Create our swipe manager to handle swipe actions
        mRecyclerViewSwipeManager = new RecyclerViewSwipeManager();

        //Create our Adapter and set up its event listeners
        final TaskRecyclerAdapter mTaskAdapter = new TaskRecyclerAdapter(getDataProvider());
        mTaskAdapter.setEventListener(new TaskRecyclerAdapter.EventListener() {
            @Override
            public void onItemRemoved(int pos) {
                //Allow the main activity to handle item removal
                //This allows us to easily change how to handle item removal by consolidating it
                //To the main activity (Currently we will create a snackbar and allow the user to
                //undo the removal)
                ((MainActivity) getActivity()).onItemRemoved(pos);
            }
            @Override
            public void onItemPinned(int pos) {
                //Currently do nothing when a task's menu is pinned open
                //We can change this later if we decide we need to
            }
            @Override
            public void onItemViewClicked(View v, boolean pinned) {
                //We will handle this from within the fragment
                onItemViewClick(v, pinned);
            }
            @Override
            public void onItemViewEditOptionClicked(int position) {
                //When the edit option is clicked we will allow the Main Activity to
                //Handle the event, this makes our code more reuseable and easier to maintain
                ((MainActivity) getActivity()).onItemViewEditOptionClicked(position);
            }
        });

        mAdapter = mTaskAdapter;

        //Create a wrapper for swiping
        mWrappedAdapter = mRecyclerViewSwipeManager.createWrappedAdapter(mTaskAdapter);

        final GeneralItemAnimator animator = new SwipeDismissItemAnimator();
        animator.setSupportsChangeAnimations(false);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mWrappedAdapter);
        mRecyclerView.setItemAnimator(animator);

        mRecyclerView.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(getContext(), R.drawable.list_divider_horizontal), true));

        //Attach Touch Action Guard and Swipe Manager to our recycler view to handle swiping actions
        mRecyclerViewTouchActionGuardManager.attachRecyclerView(mRecyclerView);
        mRecyclerViewSwipeManager.attachRecyclerView(mRecyclerView);
    }

    /**
     * This function simply cleans things up and sets everything in the fragment to null.
     * This will help us avoid any issues when pulling a new list from the DB and creating
     * a new RecyclerViewFragment from it.
     */
    @Override
    public void onDestroyView() {
        if(mRecyclerViewSwipeManager != null) {
            mRecyclerViewSwipeManager.release();
            mRecyclerViewSwipeManager = null;
        }

        if (mRecyclerViewTouchActionGuardManager != null) {
            mRecyclerViewTouchActionGuardManager.release();
            mRecyclerViewTouchActionGuardManager = null;
        }

        if (mRecyclerView != null) {
            mRecyclerView.setItemAnimator(null);
            mRecyclerView.setAdapter(null);
            mRecyclerView = null;
        }

        if (mWrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(mWrappedAdapter);
            mWrappedAdapter = null;
        }
        mAdapter = null;
        mLayoutManager = null;

        super.onDestroyView();
    }

    private void onItemViewClick(View v, boolean pinned) {
        int pos = mRecyclerView.getChildAdapterPosition(v);
        if(pos != RecyclerView.NO_POSITION) {
            //If the user actually clicked on an item that is
            //in the task list, we allow the main activity to handle it
            //
            //In reality, we will probably handle this in the fragment
            //This will probably have no function for the time being
            //      (for time constraints)
            ((MainActivity) getActivity()).onItemClicked(pos);
        }
    }

    public TaskDataProvider getDataProvider() {
        return ((MainActivity) getActivity()).getDataProvider();
    }

    public void notifyItemChanged(int pos) {
        mAdapter.notifyItemChanged(pos);
    }

    public void notifyItemInserted(int pos) {
        mAdapter.notifyItemInserted(pos);
        mRecyclerView.scrollToPosition(pos);
    }
}
