package com.entropy_factory.activismap.core.item;

import android.support.graphics.drawable.VectorDrawableCompat;
import android.util.Log;

import com.entropy_factory.activismap.core.db.ActivisCategory;
import com.entropy_factory.activismap.core.db.ActivisEvent;
import com.entropy_factory.activismap.core.db.ActivisType;
import com.entropy_factory.activismap.core.db.Company;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.List;

import static com.entropy_factory.activismap.app.ActivisApplication.INSTANCE;

/**
 * Created by ander on 29/09/16.
 */
public class ActivisTempEvent implements ActivisItem {

    private static final String TAG = "ActivityEvent";
    private static final long serialVersionUID = 2682004824379800679L;

    protected long serverId;
    protected long createdDate;
    protected long lastUpdate;
    protected String identifier;
    private String title;
    private String description;
    private String status;
    private String categories;
    private String type;
    private String imageUrl;
    private double latitude;
    private double longitude;
    private long startDate;
    private long endDate;
    private long creatorId;
    private long participants;
    private long likes;
    private long dislikes;
    boolean subscribed = false;

    private CompanyTemp company;

    public ActivisTempEvent() {
        super();
    }

    public long getServerId() {
        return serverId;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public String getIdentifier() {
        return identifier;
    }

    public ActivisTempEvent setServerId(long serverId) {
        this.serverId = serverId;
        return this;
    }

    public ActivisTempEvent setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public ActivisTempEvent setIdentifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public ActivisTempEvent setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public ActivisTempEvent setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ActivisTempEvent setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public ActivisTempEvent setStatus(String status) {
        this.status = status;
        return this;
    }

    public ActivisCategory[] getCategories() {
        String[] cats = categories.split(",");
        return ActivisCategory.valueOf(cats);
    }

    public ActivisTempEvent setCategories(String categories) {
        this.categories = categories;
        return this;
    }

    public ActivisType getType() {
        return ActivisType.valueOf(type);
    }

    public ActivisTempEvent setType(String type) {
        this.type = type;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ActivisTempEvent setImageUrl(String imageUrl) {
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

    public ActivisTempEvent setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public ActivisTempEvent setLongitude(double longitude) {
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

    public ActivisTempEvent setStartDate(long startDate) {
        this.startDate = startDate;
        return this;
    }

    public long getEndDate() {
        return endDate;
    }

    public ActivisTempEvent setEndDate(long endDate) {
        this.endDate = endDate;
        return this;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public ActivisTempEvent setCreatorId(long creatorId) {
        this.creatorId = creatorId;
        return this;
    }

    public long getParticipants() {
        return participants;
    }

    public ActivisTempEvent setParticipants(long participants) {
        this.participants = participants;
        return this;
    }

    public long getLikes() {
        return likes;
    }

    public ActivisTempEvent setLikes(long likes) {
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

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    @Override
    public OverlayItem toOverlayItem() {
        OverlayItem oi =  new OverlayItem(String.valueOf(this.serverId), this.title, this.description, this);
        oi.setMarker(VectorDrawableCompat.create(INSTANCE.getResources(), getType().getIcon(), null));
        return oi;
    }

    public ActivisTempEvent setDislikes(long dislikes) {
        this.dislikes = dislikes;
        return this;
    }

    public void setCompany(CompanyTemp company) {
        this.company = company;
    }

    public CompanyTemp getCompany() {
        return company;
    }

    @Override
    public Long save() {
        ActivisEvent dbEvent = ActivisEvent.findByRemoteId(getServerId());

        if (dbEvent == null) {
            dbEvent = new ActivisEvent();
        }

        dbEvent.setServerId(getServerId());
        dbEvent.setIdentifier(getIdentifier());
        dbEvent.setCreatedDate(getCreatedDate());
        dbEvent.setLastUpdate(getLastUpdate());
        dbEvent.setTitle(getTitle());
        dbEvent.setDescription(getDescription());
        dbEvent.setCategories(getCategories());
        dbEvent.setType(getType().toString());
        dbEvent.setLikes(getLikes());
        dbEvent.setDislikes(getDislikes());
        dbEvent.setStartDate(getStartDate());
        dbEvent.setEndDate(getEndDate());
        dbEvent.setImageUrl(getImageUrl());
        dbEvent.setLatitude(getLatitude());
        dbEvent.setLongitude(getLongitude());
        dbEvent.setCreatorId(getCreatorId());
        dbEvent.setParticipants(getParticipants());

        long companyId = getCompany().save();
        Company c = Company.findByInternalId(companyId);

        dbEvent.setCompany(c);

        return dbEvent.save();
    }

    public static List<ActivisItem> update(JSONArray jsonArray) throws JSONException {
        List<ActivisItem> events = new ArrayList<>();
        for (int x = 0; x < jsonArray.length(); x++) {
            ActivisTempEvent ate = update(jsonArray.getJSONObject(x));
            events.add(ate);
        }

        return events;
    }

    public static ActivisTempEvent update(JSONObject jsonObject) throws JSONException {

        ActivisTempEvent activisEvent = new ActivisTempEvent();

        long id = jsonObject.getLong("id");
        long created = jsonObject.getLong("created");
        String identifier = jsonObject.getString("identifier");

        activisEvent.setServerId(id)
                .setCreatedDate(created)
                .setIdentifier(identifier);

        long updated = jsonObject.getLong("last_update");
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

        CompanyTemp c = CompanyTemp.update(jsonObject.getJSONObject("company"));
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

        if (jsonObject.has("likes")) {
            activisEvent.setLikes(jsonObject.getLong("likes"));
        }

        if (jsonObject.has("dislikes")) {
            activisEvent.setDislikes(jsonObject.getLong("dislikes"));
        }

        if (jsonObject.has("participants")) {
            activisEvent.setParticipants(jsonObject.getLong("participants"));
        }

        return activisEvent;
    }


}
