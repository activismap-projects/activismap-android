package com.entropy_factory.activismap.map;

import android.content.Context;

import com.entropy_factory.activismap.core.item.ActivisItem;
import com.google.android.gms.maps.GoogleMap;
import com.google.maps.android.MarkerManager;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by ander on 9/11/16.
 */
public class ActivisClusterManager extends ClusterManager<ActivisItem> {

    private static final String TAG = "ActivisClusterManager";

    private List<ActivisItem> items;

    public ActivisClusterManager(Context context, GoogleMap map) {
        super(context, map);
        this.items = new ArrayList<>();
    }

    public ActivisClusterManager(Context context, GoogleMap map, MarkerManager markerManager) {
        super(context, map, markerManager);
        this.items = new ArrayList<>();
    }

    @Override
    public void addItem(ActivisItem myItem) {
        items.add(myItem);
        super.addItem(myItem);
    }

    @Override
    public void addItems(Collection<ActivisItem> items) {
        this.items.addAll(items);
        super.addItems(items);
    }
}
