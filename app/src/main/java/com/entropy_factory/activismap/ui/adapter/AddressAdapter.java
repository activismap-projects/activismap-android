package com.entropy_factory.activismap.ui.adapter;

import android.app.Activity;
import android.location.Address;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.entropy_factory.activismap.R;
import com.entropy_factory.activismap.ui.adapter.item.AddressItem;
import com.entropy_factory.activismap.ui.adapter.item.ProfileItem;

import java.util.List;

/**
 * Created by Andersson G. Acosta on 17/02/17.
 */
public class AddressAdapter extends BaseFlexibleAdapter<AddressItem> {

    private static final String TAG = "AddressAdapter";

    public AddressAdapter(@Nullable List<AddressItem> items, Activity activity) {
        super(items, activity);
    }

    public AddressAdapter(@Nullable List<AddressItem> items, @Nullable Object listeners, Activity activity) {
        super(items, listeners, activity);
    }

    public AddressAdapter(@Nullable List<AddressItem> items, @Nullable Object listeners, boolean stableIds, Activity activity) {
        super(items, listeners, stableIds, activity);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater();
        return new AddressItem.ViewHolder(inflater.inflate(viewType, parent, false), this);
    }
}
