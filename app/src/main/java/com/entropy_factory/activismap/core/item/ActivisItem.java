package com.entropy_factory.activismap.core.item;

import com.entropy_factory.activismap.core.db.ActivisCategory;
import com.entropy_factory.activismap.core.db.ActivisType;
import com.entropy_factory.activismap.core.db.model.Entity;
import com.google.maps.android.clustering.ClusterItem;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

import java.io.Serializable;

/**
 * Created by ander on 2/11/16.
 */
public interface ActivisItem extends IGeoPoint, ClusterItem, Entity, Serializable {

     String getTitle();
     String getDescription();
     String getStatus();
     ActivisCategory[] getCategories();
     ActivisType getType();
     String getImageUrl();
     double getLatitude();
     double getLongitude();
     long getStartDate();
     long getEndDate();
     long getCreatorId();
     long getParticipants();
     long getLikes();
     long getDislikes();
     boolean isSubscribed();
     OverlayItem toOverlayItem();
     Long save();
}
