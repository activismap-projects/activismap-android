package com.entropy_factory.activismap.ui.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;

import com.entropy_factory.activismap.core.activis.Activis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;

/**
 * Created by ander on 21/10/16.
 */
public class BaseFlexibleAdapter<T extends IFlexible> extends FlexibleAdapter<T> {

    private static final String TAG = "BaseFlexibleAdapter";

    protected Activity activity;
    protected Activis client;

    public BaseFlexibleAdapter(@Nullable List<T> items, Activity activity) {
        super(items);
        this.activity = activity;
        this.client = new Activis(activity);
    }

    public BaseFlexibleAdapter(@Nullable List<T> items, @Nullable Object listeners, Activity activity) {
        super(items, listeners);
        this.activity = activity;
        this.client = new Activis(activity);
    }

    public BaseFlexibleAdapter(@Nullable List<T> items, @Nullable Object listeners, boolean stableIds, Activity activity) {
        super(items, listeners, stableIds);
        this.activity = activity;
        this.client = new Activis(activity);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mItemLongClickListener = listener;
    }


    protected String getString(@StringRes int res, Object... args) {
        return activity.getString(res, args);
    }

    protected String getString(@StringRes int res) {
        return activity.getString(res);
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
        this.client = new Activis(activity);
    }

    protected LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(activity);
    }

    public Activis getClient() {
        return client;
    }

    public void clear() {
        if (!isEmpty()) {
            removeRange(0, getItemCount()-1);
            notifyDataSetChanged();
        }
    }

    public boolean addItem(T item) {
        return addItem(getItemCount(), item);
    }

    public void addIfNotExist(Collection<T> items) {
        List<T> itemsToAdd = new ArrayList<>();
        for (T i : items) {
            if (!contains(i)) {
                itemsToAdd.add(i);
            }
        }

        addItems(getItemCount(), itemsToAdd);
    }
}
