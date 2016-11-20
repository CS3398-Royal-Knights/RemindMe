package com.cs3398royal.remindme.remindme;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableSwipeableItemViewHolder;

/**
 * Created by Chris on 11/7/2016.
 */

public class TaskViewHolder extends AbstractDraggableSwipeableItemViewHolder {
    static final float OPTIONS_AREA_PROPORTION = 0.3f;
    static final float REMOVE_ITEM_THRESHOLD = 0.67f;
    static final float CHECK_ITEM_THRESHOLD = 0.09f;
    public View mContainer;
    public View mSwipeOptionCheck;
    public View mSwipeOptionEdit;
    public View mSwipeOptionRemove;
    public TextView mTextView;
    float lastSwipeAmount;

    public TaskViewHolder(View v) {
        super(v);
        mContainer = v.findViewById(R.id.task_container);
        mSwipeOptionCheck = v.findViewById(R.id.viewItemChecked);
        mSwipeOptionEdit = v.findViewById(R.id.viewEditItem);
        mSwipeOptionRemove = v.findViewById(R.id.viewDeleteItem);
        mTextView = (TextView) v.findViewById(R.id.task_text);
    }
    @Override
    public View getSwipeableContainerView() {
        return mContainer;
    }
    @Override
    public void onSlideAmountUpdated(float xAmt, float yAmt, boolean isSwiping) {
        int itemWidth = itemView.getWidth();
        float optionItemWidth = itemWidth * OPTIONS_AREA_PROPORTION / 2;
        int offset = (int) (optionItemWidth + 0.5f);
        float p = Math.max(0, Math.min(OPTIONS_AREA_PROPORTION, -xAmt)) / OPTIONS_AREA_PROPORTION;

        if(mSwipeOptionEdit.getWidth() == 0) {
            setLayoutWidth(mSwipeOptionEdit, (int) (optionItemWidth + 0.5f));
            setLayoutWidth(mSwipeOptionRemove, (int) (optionItemWidth + 0.5f));
        }

        mSwipeOptionEdit.setTranslationX(-(int) (p * optionItemWidth * 2 + 0.5f) + offset);
        mSwipeOptionRemove.setTranslationX(-(int) (p * optionItemWidth * 1 + 0.5f) + offset);

        if(xAmt < (-REMOVE_ITEM_THRESHOLD)) {
            mContainer.setVisibility(View.INVISIBLE);
            mSwipeOptionEdit.setVisibility(View.INVISIBLE);
            mSwipeOptionRemove.setVisibility(View.INVISIBLE);
            //mSwipeOptionCheck.setVisibility(View.INVISIBLE);
        }
        else {
            mContainer.setVisibility(View.VISIBLE);
            mSwipeOptionEdit.setVisibility(View.VISIBLE);
            mSwipeOptionRemove.setVisibility(View.VISIBLE);
            //mSwipeOptionCheck.setVisibility(View.VISIBLE);
        }
        lastSwipeAmount = xAmt;
    }

    private static void setLayoutWidth(View v, int width) {
        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        layoutParams.width = width;
        v.setLayoutParams(layoutParams);
    }
}