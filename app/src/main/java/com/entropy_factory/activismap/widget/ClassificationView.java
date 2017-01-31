package com.entropy_factory.activismap.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.StateListDrawable;
import android.media.Image;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.entropy_factory.activismap.R;
import com.entropy_factory.activismap.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static android.view.Gravity.CENTER;

/**
 * Created by ander on 11/11/16.
 */
public class ClassificationView<T extends ActivisView> extends CardView {

    private static final String TAG = "ActivisTypeView";

    private final OnTouchListener ITEM_LISTENER = new OnTouchListener() {

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            currentView = view;
            return GESTURE_DETECTOR.onTouchEvent(event);
        }
    };

    private final GestureDetector GESTURE_DETECTOR = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            currentView.setPressed(true);
            T item = (T) currentView.getTag();

            if (onClassificationClickListener != null) {
                onClassificationClickListener.onClassificationClick(currentView, item);
            }

            if (lastPressedView != null && !lastPressedView.equals(currentView)) {
                lastPressedView.setPressed(false);
            }

            lastPressedView = currentView;
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            if (onClassificationClickListener != null) {
                T item = (T) currentView.getTag();
                onClassificationClickListener.onClassificationLongClick(currentView, item);
            }
        }
    });

    public interface OnClassificationClickListener<T> {
        void onClassificationClick(View v, T item);
        boolean onClassificationLongClick(View v, T item);
    }

    private OnClassificationClickListener<T> onClassificationClickListener;
    private LinearLayout content;

    private List<T> classifications;
    private int itemSpacing;
    private int itemPadding;
    private int itemSize;
    private View currentView;
    private View lastPressedView;

    public ClassificationView(Context context) {
        super(context);
        initialize(null);
    }

    public ClassificationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(attrs);
    }

    public ClassificationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(attrs);
    }

    private void initialize(AttributeSet attrs) {
        classifications = new ArrayList<>();

        if (attrs != null) {
            content = new LinearLayout(getContext(), attrs);
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ClassificationView);

            itemSpacing = a.getDimensionPixelSize(R.styleable.ClassificationView_itemSpacing, Utils.convertDpToPixels(getContext(), 3));
            itemPadding = a.getDimensionPixelSize(R.styleable.ClassificationView_itemPadding, 0);
            itemSize = a.getDimensionPixelSize(R.styleable.ClassificationView_itemSize, Utils.convertDpToPixels(getContext(), 10));
            a.recycle();
        } else {
            content = new LinearLayout(getContext());
            content.setOrientation(LinearLayout.VERTICAL);
        }

        content.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        content.setGravity(CENTER);
        invalidateViews();
    }

    private void invalidateViews() {
        removeAllViews();

        boolean isVertical = content.getOrientation() == LinearLayout.VERTICAL;

        LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(itemSize, itemSize);

        if (isVertical) {
            itemParams.setMargins(0, itemSpacing/2, 0, itemSpacing/2);
            content.setPadding(itemSpacing, itemSpacing/2, itemSpacing, itemSpacing/2);
        } else {
            itemParams.setMargins(itemSpacing/2, 0, itemSpacing/2, 0);
            content.setPadding(itemSpacing/2, itemSpacing, itemSpacing/2, itemSpacing);
        }

        for (ActivisView v : classifications) {
            AppCompatImageView iv = new AppCompatImageView(getContext());
            iv.setBackgroundResource(R.drawable.view_pressed_effect);
            iv.setImageResource(v.getIcon());
            iv.setLayoutParams(itemParams);
            iv.setPadding(itemPadding, itemPadding, itemPadding, itemPadding);
            iv.setOnTouchListener(ITEM_LISTENER);
            iv.setTag(v);
            content.addView(iv);
        }
        addView(content);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_UP:
                return true;
        }

        return false;
    }

    public void setOrientation(@LinearLayoutCompat.OrientationMode int orientation) {
        content.setOrientation(orientation);
        invalidateViews();
    }

    public OnClassificationClickListener<T> getOnClassificationClickListener() {
        return onClassificationClickListener;
    }

    public void setOnClassificationClickListener(OnClassificationClickListener<T> onClassificationClickListener) {
        this.onClassificationClickListener = onClassificationClickListener;
    }

    public void addItem(T item) {
        classifications.add(item);
        invalidateViews();
    }

    public void removeItem(T item) {
        classifications.remove(item);
        invalidateViews();
    }

    public void clearClassifications() {
        classifications.clear();
        invalidateViews();
    }

    public void addAll(Collection<? extends T> c) {
        classifications.addAll(c);
        invalidateViews();
    }

    public void addAll(T... items) {
        addAll(Arrays.asList(items));
    }

    public int getItemSpacing() {
        return itemSpacing;
    }

    public void setItemSpacing(int itemSpacing) {
        this.itemSpacing = itemSpacing;
        invalidateViews();
    }

    public int getItemSize() {
        return itemSize;
    }

    public void setItemSize(int itemSize) {
        this.itemSize = itemSize;
        invalidateViews();
    }
}
