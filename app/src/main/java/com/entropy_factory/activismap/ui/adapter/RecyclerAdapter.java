package com.entropy_factory.activismap.ui.adapter;

import android.app.Activity;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ander on 18/10/16.
 */
public abstract class RecyclerAdapter<VH extends RecyclerView.ViewHolder, Item> extends RecyclerView.Adapter<VH>{

    private static final String TAG = "RecyclerAdapter";

    protected Activity activity;
    private List<Item> itemList;

    public RecyclerAdapter(Activity activity) {
        this.activity = activity;
        this.itemList = new ArrayList<>();
    }

    public RecyclerAdapter(Activity activity, List<Item> itemList) {
        this.activity = activity;
        this.itemList = itemList;
    }

    protected List<Item> getItemList() {
        return itemList;
    }

    public Item getItem(int position) {
        return itemList.get(position);
    }

    public void notifyDataChanged() {
        this.itemList = getItemList();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    protected String getString(@StringRes int res, Object... args) {
        return activity.getString(res, args);
    }

    protected String getString(@StringRes int res) {
        return activity.getString(res);
    }

    protected LayoutInflater getLayoutInflater() {
        return activity.getLayoutInflater();
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addItem(Item item) {
        this.itemList.add(item);
        notifyDataSetChanged();
    }

    public void remove(Item item) {
        this.itemList.remove(item);
        notifyDataSetChanged();
    }

    public void addAll(Collection<? extends Item> items) {
        this.itemList.addAll(items);
        notifyDataSetChanged();
    }

    public boolean contains(Item item) {
        return this.itemList.contains(item);
    }

    public void sort(Comparator<Item> comparator) {
        Collections.sort(itemList, comparator);
        notifyDataSetChanged();
    }

    public void clear() {
        this.itemList.clear();
        notifyDataSetChanged();
    }


}
