package com.entropy_factory.activismap.ui.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.entropy_factory.activismap.ui.adapter.item.CompanyItem;
import com.entropy_factory.activismap.ui.adapter.item.ProfileItem;

import java.util.List;

/**
 * Created by ander on 21/10/16.
 */
public class CompanyAdapter extends BaseFlexibleAdapter<CompanyItem> {

    private static final String TAG = "ProfileAdapter";

    public CompanyAdapter(@Nullable List<CompanyItem> items, Activity activity) {
        super(items, activity);
    }

    public CompanyAdapter(@Nullable List<CompanyItem> items, @Nullable Object listeners, Activity activity) {
        super(items, listeners, activity);
    }

    public CompanyAdapter(@Nullable List<CompanyItem> items, @Nullable Object listeners, boolean stableIds, Activity activity) {
        super(items, listeners, stableIds, activity);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater();
        return new ProfileItem.ViewHolder(inflater.inflate(viewType, parent, false), this);
    }
}
