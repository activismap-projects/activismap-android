package com.entropy_factory.activismap.core.db;

import android.text.TextUtils;
import android.util.Log;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.entropy_factory.activismap.core.db.base.BaseEntity;
import com.entropy_factory.activismap.core.item.ActivisItem;
import com.entropy_factory.activismap.util.TimeUtils;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.List;

/**
 * Created by ander on 29/09/16.
 */
@Table(name = "activis_event")
public class ActivisEvent extends BaseEntity implements ActivisItem {

    private static final String TAG = "ActivityEvent";
    private static final long serialVersionUID = -439447570348562479L;

    @Column(name = "title")
    private String title;

    @Column(name = "decription")
    private String description;

    @Column(name = "status")
    private String status;

    @Column(name = "cateogories")
    private String categories;

    @Column(name = "type")
    private String type;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "start_date")
    private long startDate;

    @Column(name = "end_date")
    private long endDate;

    @Column(name = "creator")
    private long creatorId;

    @Column(name = "participants")
    private long participants;

    @Column(name = "likes")
    private long likes;

    @Column(name = "dislikes")
    private long dislikes;

    @Column(name = "subscribed")
    private boolean subscribed;

    @Column(name = "company")
    private Company company;

    public ActivisEvent() {
        super();
    }

    public String getTitle() {
        return title;
    }

    public ActivisEvent setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ActivisEvent setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public ActivisEvent setStatus(String status) {
        this.status = status;
        return this;
    }

    public ActivisCategory[] getCategories() {
        String[] cats = categories.split(",");
        return ActivisCategory.valueOf(cats);
    }

    public ActivisEvent setCategories(String categories) {
        this.categories = categories;
        return this;
    }

    public ActivisEvent setCategories(ActivisCategory[] categories) {
        this.categories = TextUtils.join(",", categories).toUpperCase();
        return this;
    }

    public ActivisType getType() {
        return ActivisType.valueOf(type);
    }

    public ActivisEvent setType(String type) {
        this.type = type;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ActivisEvent setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    @Override
    public int getLatitudeE6() {
        return 0;
    }

    @Override
    public int getLongitudeE6() {
        return 0;
    }

    public double getLatitude() {
        return latitude;
    }

    public ActivisEvent setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public ActivisEvent setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(latitude, longitude);
    }

    public long getStartDate() {
        return startDate;
    }

    public ActivisEvent setStartDate(long startDate) {
        this.startDate = startDate;
        return this;
    }

    public long getEndDate() {
        return endDate;
    }

    public ActivisEvent setEndDate(long endDate) {
        this.endDate = endDate;
        return this;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public ActivisEvent setCreatorId(long creatorId) {
        this.creatorId = creatorId;
        return this;
    }

    public long getParticipants() {
        return participants;
    }

    public ActivisEvent setParticipants(long participants) {
        this.participants = participants;
        return this;
    }

    public long getLikes() {
        return likes;
    }

    public ActivisEvent setLikes(long likes) {
        this.likes = likes;
        return this;
    }

    public long getDislikes() {
        return dislikes;
    }

    @Override
    public boolean isSubscribed() {
        return subscribed;
    }

    public ActivisEvent setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
        return this;
    }

    public ActivisEvent setDislikes(long dislikes) {
        this.dislikes = dislikes;
        return this;
    }

    public Company getCompany() {
        return company;
    }

    public ActivisEvent setCompany(Company company) {
        this.company = company;
        return this;
    }

    @Override
    public OverlayItem toOverlayItem() {
        return new OverlayItem(this.title, this.description, this);
    }

    public static List<ActivisEvent> getAll() {
        return new Select().from(ActivisEvent.class).execute();
    }

    public static List<ActivisEvent> getSubscribed() {
        return new Select().from(ActivisEvent.class).where("subscribed = " + 1).execute();
    }

    public static List<ActivisEvent> forWeek() {
        return forDuration(TimeUtils.WEEK);
    }

    public static List<ActivisEvent> forMonth() {
        return forDuration(TimeUtils.MONTH);
    }

    public static List<ActivisEvent> forQuarter() {
        return forDuration(TimeUtils.MONTH * 3);
    }

    public static List<ActivisEvent> forDuration(long duration) {
        long now = System.currentTimeMillis();
        long endTime = now + duration;

        return new Select().from(ActivisEvent.class)
                .where("start_date >= " + now).and("start_date <= " + endTime)
                .or("end_date > " + now).and("end_date <= " + endTime)
                .execute();
    }

    public static void removeOld() {
        new Delete().from(ActivisEvent.class).where("end_date <= " + System.currentTimeMillis());
    }

    public static ActivisEvent findByRemoteId(Object id) {
        if (id != null) {
            if (id instanceof String) {
                return new Select().from(Company.class).where("identifier = " + id.toString()).executeSingle();
            } else if (id instanceof Long) {
                return new Select().from(ActivisEvent.class).where("server_id = " + id).executeSingle();
            }
        }

        return null;

    }

    public static ActivisEvent[] update(JSONArray jsonArray) throws JSONException {
        ActivisEvent[] events = new ActivisEvent[jsonArray.length()];
        for (int x = 0; x< jsonArray.length(); x++) {
            JSONObject jsonObject = jsonArray.getJSONObject(x);
            long id = jsonObject.getLong("id");
            ActivisEvent a = findByRemoteId(id);
            events[x] =  update(a, jsonObject);
        }

        return events;
    }


    public static ActivisEvent update(JSONObject jsonObject) throws JSONException {
        long id = jsonObject.getLong("id");
        ActivisEvent a = findByRemoteId(id);
        return update(a, jsonObject);
    }

    public static ActivisEvent update(ActivisEvent activisEvent, JSONObject jsonObject) throws JSONException {
        if (activisEvent == null) {
            activisEvent = new ActivisEvent();

            long id = jsonObject.getLong("id");
            long created = jsonObject.getLong("created");
            String identifier = jsonObject.getString("identifier");

            activisEvent.setServerId(id)
                    .setCreatedDate(created)
                    .setIdentifier(identifier);
        }

        long updated = jsonObject.getLong("last_update");
        if (updated > activisEvent.getLastUpdate()) {
            Log.d(TAG, updated + " / " + activisEvent.getLastUpdate() + ", " + jsonObject.toString());
            String title = jsonObject.getString("title");
            String description = jsonObject.getString("description");
            String status = jsonObject.getString("status");
            String categories = jsonObject.getString("categories");
            String type = jsonObject.getString("type");

            double lat = jsonObject.getDouble("latitude");
            double lon = jsonObject.getDouble("longitude");

            long startDate = jsonObject.getLong("start_date");
            long endDate = jsonObject.getLong("end_date");

            JSONObject creator = jsonObject.getJSONObject("creator");
            long creatorId = creator.getLong("id");

            activisEvent.setCreatorId(creatorId);

            Company c = Company.update(jsonObject.getJSONObject("company"));
            activisEvent.setCompany(c);

            activisEvent.setTitle(title)
                    .setDescription(description)
                    .setStatus(status)
                    .setCategories(categories)
                    .setType(type)
                    .setLatitude(lat)
                    .setLongitude(lon)
                    .setStartDate(startDate)
                    .setEndDate(endDate)
                    .setCreatorId(creatorId);

            if (jsonObject.has("image")) {
                activisEvent.setImageUrl(jsonObject.getString("image"));
            }

            activisEvent.save();
        }

        if (jsonObject.has("likes")) {
            activisEvent.setLikes(jsonObject.getLong("likes"));
        }

        if (jsonObject.has("dislikes")) {
            activisEvent.setDislikes(jsonObject.getLong("dislikes"));
        }

        if (jsonObject.has("participants")) {
            activisEvent.setParticipants(jsonObject.getLong("participants"));
        }

        activisEvent.save();

        return activisEvent;
    }
}
