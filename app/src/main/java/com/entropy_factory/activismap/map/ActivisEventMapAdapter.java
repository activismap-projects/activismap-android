package com.entropy_factory.activismap.map;

import android.app.Activity;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.entropy_factory.activismap.R;
import com.entropy_factory.activismap.core.db.ActivisEvent;
import com.entropy_factory.activismap.util.TimeUtils;
import com.entropy_factory.activismap.widget.ProfileImageView;
import com.entropy_factory.activismap.widget.RemoteImageView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by ander on 4/10/16.
 */
public class ActivisEventMapAdapter implements GoogleMap.InfoWindowAdapter {

    private static final String TAG = "ActivisEventMapAdapter";

    private Activity activity;

    public ActivisEventMapAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        long id = Long.parseLong(marker.getTitle());
        final ActivisEvent event = ActivisEvent.findByRemoteId(id);

        View v = getLayoutInflater().inflate(R.layout.map_event_info, null);
        TextView title = (TextView) v.findViewById(R.id.title);
        TextView desc = (TextView) v.findViewById(R.id.desc);
        TextView date = (TextView) v.findViewById(R.id.info_date);
        RemoteImageView image = (RemoteImageView) v.findViewById(R.id.image);

        title.setText(event.getTitle());
        desc.setText(event.getDescription());
        date.setText(TimeUtils.getTimeString(event.getStartDate()) + " - " + TimeUtils.getTimeString(event.getEndDate()));
        image.loadRemoteImage(event.getImageUrl());
        return v;
    }

    private LayoutInflater getLayoutInflater() {
        return activity.getLayoutInflater();
    }

    private String getString(@StringRes int res) {
        return activity.getString(res);
    }

    private String getString(@StringRes int res, Object... args) {
        return activity.getString(res, args);
    }
}
