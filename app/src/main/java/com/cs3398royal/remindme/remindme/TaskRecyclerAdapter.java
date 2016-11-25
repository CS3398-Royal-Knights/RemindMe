package com.cs3398royal.remindme.remindme;

import android.app.usage.UsageEvents;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultAction;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionDefault;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionMoveToSwipedDirection;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionRemoveItem;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableSwipeableItemViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.utils.RecyclerViewAdapterUtils;

import java.util.EventListener;

/**
 * Created by Chris on 11/7/2016.
 */

public class TaskRecyclerAdapter
        extends RecyclerView.Adapter<TaskViewHolder>
        implements SwipeableItemAdapter<TaskViewHolder> {

    private TaskViewHolder mHolder;
    private TaskDataProvider mProvider;
    private EventListener mEventListener;
    private View.OnClickListener mItemViewOnClickListener;
    private View.OnClickListener mSwipeableViewContainerOnClickListener;
    private View.OnClickListener mSwipeOptionEditOnClickListener;
    private View.OnClickListener mSwipeOptionDeleteOnClickListener;

    public interface EventListener {
        void onItemRemoved(int position);

        void onItemCheckedStateChanged(int position, boolean checkedState);

        void onItemPinned(int position);

        void onItemViewClicked(View v, boolean pinned);

        void onItemViewEditOptionClicked(int position);
    }

    public TaskRecyclerAdapter(TaskDataProvider dataProvider) {
        mProvider = dataProvider;
        mItemViewOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSwipeableViewContainerClick(v);
            }
        };
        mSwipeableViewContainerOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSwipeableViewContainerClick(v);
            }
        };
        mSwipeOptionEditOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSwipeOptionEditClick(v);
            }
        };
        mSwipeOptionDeleteOnClickListener= new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSwipeOptionDeleteClick(v);
            }
        };
        setHasStableIds(true);
    }

    private void onSwipeOptionDeleteClick(View v) {
        //Find the position of the item that had its delete button clicked
        //First we have to get the parent view of the option button that was clicked
        //  (this is the task item that we swiped on originally)
        View itemView = RecyclerViewAdapterUtils.getParentViewHolderItemView(v);
        //Second we find the recycler view that this task item belongs to
        //  (this is the list of all tasks currently being displayed)
        RecyclerView rView = RecyclerViewAdapterUtils.getParentRecyclerView(itemView);
        //Lastly we find the position of this task item within the list of tasks being displayed
        int pos = rView.getChildAdapterPosition(itemView);

        //If the position is a valid position in the list we remove it from the data set
        //and then notify the adapter that the position was removed, and call the onItemRemoved
        //Event listener to perform our action after the item was removed. This listener currently is
        //implemented in the MainActivity class as a function that creates an UNDO snackbar
        if(pos != RecyclerView.NO_POSITION) {
            //Remove Item from provider and notify
            this.mProvider.removeItem(pos);
            this.notifyItemRemoved(pos);
            this.mEventListener.onItemRemoved(pos);
        }
    }

    private void onSwipeOptionEditClick(View v) {
        //Find the position of the item that had its edit button clicked
        View itemView = RecyclerViewAdapterUtils.getParentViewHolderItemView(v);
        RecyclerView rView = RecyclerViewAdapterUtils.getParentRecyclerView(itemView);
        int pos = rView.getChildAdapterPosition(itemView);

        if(pos != RecyclerView.NO_POSITION) {
            mEventListener.onItemViewEditOptionClicked(pos);
        }
    }
    private void onItemViewClick(View v) {
        if(mEventListener != null) {
            mEventListener.onItemViewClicked(v, true);
        }
    }

    private void onSwipeableViewContainerClick(View v) {
        if(mEventListener != null) {
            mEventListener.onItemViewClicked(RecyclerViewAdapterUtils.getParentViewHolderItemView(v), false);
        }
    }

    /**
     * Returns the ID associated with the task at the position
     * @param position  The position of the task in the RecyclerView list
     * @return
     */
    @Override
    public long getItemId(int position) {
        return mProvider.getItem(position).getTaskId();
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.task_recycler_item, parent, false);
        return new TaskViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task task = mProvider.getItem(position);

        //set listeners
        holder.itemView.setOnClickListener(mItemViewOnClickListener);
        holder.mContainer.setOnClickListener(mSwipeableViewContainerOnClickListener);
        holder.mSwipeOptionEdit.setOnClickListener(mSwipeOptionEditOnClickListener);
        holder.mSwipeOptionRemove.setOnClickListener(mSwipeOptionDeleteOnClickListener);

        //set task text
        holder.mTextView.setText(task.getTaskName());

        //If the task is checked as completed, set the text to strikethrough
        if(task.isChecked()) {
            holder.mTextView.setPaintFlags(holder.mTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else {
            holder.mTextView.setPaintFlags(holder.mTextView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }

        final int swipeState = holder.getSwipeStateFlags();

        if((swipeState & SwipeableItemConstants.STATE_FLAG_IS_UPDATED) != 0) {
            int bgResId;
            if((swipeState & SwipeableItemConstants.STATE_FLAG_IS_ACTIVE) != 0) {
                bgResId = R.drawable.bg_item_swiping_active_state;
            }
            else if((swipeState & SwipeableItemConstants.STATE_FLAG_SWIPING) != 0) {
                bgResId = R.drawable.bg_item_swiping_state;
            }
            else {
                bgResId = R.drawable.bg_item_normal_state;
            }
            holder.mContainer.setBackgroundResource(bgResId);
        }

        //set swiping properties
        holder.setMaxLeftSwipeAmount(-TaskViewHolder.OPTIONS_AREA_PROPORTION);
        holder.setSwipeItemHorizontalSlideAmount(
                task.isPinned() ? -TaskViewHolder.OPTIONS_AREA_PROPORTION : 0);
    }
    @Override
    public int getItemCount() {
        return mProvider.getCount();
    }

    /**
     * Simply set the swipe reaction type to can swipe both directions.
     * @param holder
     * @param pos
     * @param x
     * @param y
     * @return
     */
    @Override
    public int onGetSwipeReactionType(TaskViewHolder holder, int pos, int x, int y) {
        return SwipeableItemConstants.REACTION_CAN_SWIPE_BOTH_H;
    }

    @Override
    public void onSetSwipeBackground(TaskViewHolder holder, int pos, int type) {
        int bgRes = 0;
        switch(type) {
            case SwipeableItemConstants.DRAWABLE_SWIPE_NEUTRAL_BACKGROUND:
                bgRes = R.drawable.bg_swipe_item_neutral;
                break;
            case SwipeableItemConstants.DRAWABLE_SWIPE_LEFT_BACKGROUND:
                bgRes = R.drawable.bg_swipe_item_left_remove;
                break;
            case SwipeableItemConstants.DRAWABLE_SWIPE_RIGHT_BACKGROUND:
                bgRes = R.drawable.bg_swipe_item_right_check;
                break;
        }
        holder.itemView.setBackgroundResource(bgRes);
    }

    @Override
    public SwipeResultAction onSwipeItem(TaskViewHolder holder, final int pos, int result) {
        switch (result) {
            case SwipeableItemConstants.RESULT_SWIPED_LEFT:
                //If we've swiped far enough to auto remove the item, remove it
                if (holder.lastSwipeAmount < (-TaskViewHolder.REMOVE_ITEM_THRESHOLD)) {
                    return new SwipeLeftRemoveAction(this, pos);
                }
                else {
                    //Otherwise we pin it to keep the options open
                    return new SwipeLeftPinningAction(this, pos);
                }
            case SwipeableItemConstants.RESULT_SWIPED_RIGHT:
                if(mProvider.getItem(pos).isPinned()) {
                    //If the item is pinned, we unpin it
                    return new UnpinResultAction(this, pos);
                }
                else {
                    //Item is not pinned, so we set it checked if the user has swiped far enough
                    //to trigger the action.
                    if(holder.lastSwipeAmount > TaskViewHolder.CHECK_ITEM_THRESHOLD) {
                        return new SwipeRightCheckAction(this, pos);
                    }
                }
            case SwipeableItemConstants.RESULT_CANCELED:
            default:
                if(pos != RecyclerView.NO_POSITION) {
                    return new UnpinResultAction(this, pos);
                }
                else {
                    return null;
                }
        }
    }

    public EventListener getEventListener() {
        return mEventListener;
    }

    public void setEventListener(EventListener eventListener) {
        mEventListener = eventListener;
    }

    private static class SwipeLeftPinningAction extends SwipeResultActionMoveToSwipedDirection {
        TaskRecyclerAdapter a;
        int pos;

        public SwipeLeftPinningAction(TaskRecyclerAdapter adapter, int pos) {
            this.a = adapter;
            this.pos = pos;
        }
        @Override
        protected void onPerformAction() {
            super.onPerformAction();
            a.mProvider.getItem(pos).setPinned(true);
            a.notifyItemChanged(pos);
        }
    }

    private static class SwipeLeftRemoveAction extends SwipeResultActionRemoveItem {
        private TaskRecyclerAdapter mAdapter;
        private final int mPos;
        SwipeLeftRemoveAction(TaskRecyclerAdapter adapter, int pos) {
            mAdapter = adapter;
            mPos = pos;
        }
        @Override
        protected void onPerformAction() {
            super.onPerformAction();

            mAdapter.mProvider.removeItem(mPos);
            mAdapter.notifyItemRemoved(mPos);
        }
        @Override
        protected void onSlideAnimationEnd() {
            super.onSlideAnimationEnd();

            if(mAdapter.mEventListener != null) {
                mAdapter.mEventListener.onItemRemoved(mPos);
            }
        }
        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            mAdapter = null;
        }
    }

    private static class UnpinResultAction extends SwipeResultActionDefault {
        private TaskRecyclerAdapter mAdapter;
        private final int mPos;

        UnpinResultAction(TaskRecyclerAdapter adapter, int pos) {
            mAdapter = adapter;
            mPos = pos;
        }
        @Override
        protected void onPerformAction() {
            super.onPerformAction();
            Task task = mAdapter.mProvider.getItem(mPos);
            if(task.isPinned()) {
                task.setPinned(false);
                mAdapter.notifyItemChanged(mPos);
            }
        }
        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            mAdapter = null;
        }
    }

    private static class SwipeRightCheckAction extends SwipeResultActionMoveToSwipedDirection {
        private TaskRecyclerAdapter mAdapter;
        private final int mPos;
        private boolean mCheckedState;

        SwipeRightCheckAction(TaskRecyclerAdapter adapter, int pos) {
            mAdapter = adapter;
            mPos = pos;
        }
        @Override
        protected void onPerformAction() {
            super.onPerformAction();
            //Change the checked state on the item and save the state to the database
            mAdapter.mProvider.transitionCheckedState(mPos);
            mCheckedState = mAdapter.mProvider.getItem(mPos).isChecked();
            mAdapter.notifyItemChanged(mPos);
        }
        @Override
        protected void onSlideAnimationEnd() {
            super.onSlideAnimationEnd();

            if(mAdapter.mEventListener != null) {
                mAdapter.mEventListener.onItemCheckedStateChanged(mPos, mCheckedState);
            }
        }
        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            mAdapter = null;
        }
    }
}
