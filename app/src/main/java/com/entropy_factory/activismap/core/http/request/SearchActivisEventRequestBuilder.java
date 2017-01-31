package com.entropy_factory.activismap.core.http.request;

import com.entropy_factory.activismap.core.db.ActivisCategory;
import com.entropy_factory.activismap.core.db.ActivisType;
import com.entropy_factory.activismap.core.http.request.base.BaseGetRequestBuilder;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.osmdroid.util.BoundingBox;

import java.util.Map;

/**
 * Created by ander on 4/10/16.
 */
public class SearchActivisEventRequestBuilder extends BaseGetRequestBuilder {

    private static final String TAG = "SearchActivisEventRequest";

    private ActivisType type;
    private ActivisCategory category;
    private long startDate;
    private long endDate;
    private LatLngBounds area;
    private int limit;
    private int offset;

    public SearchActivisEventRequestBuilder() {
    }

    public SearchActivisEventRequestBuilder(ActivisType type) {
        this.type = type;
    }

    public void setType(ActivisType type) {
        this.type = type;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public void setArea(LatLngBounds area) {
        this.area = area;
    }

    public void setArea(BoundingBox area) {
        this.area = new LatLngBounds.Builder()
                .include(new LatLng(area.getLatNorth(), area.getLonEast()))
                .include(new LatLng(area.getLatSouth(), area.getLonWest()))
                .build();
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setCategory(ActivisCategory category) {
        this.category = category;
    }

    @Override
    public String getURL() {
        return super.getURL() + "v1/public/search";
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> params = super.getParams();
        if (type != null) {
            params.put("type", type.toString());
        }

        if (area != null) {
            String searchArea = String.valueOf(area.northeast.latitude) + "," + area.northeast.longitude +
                    "@" + area.southwest.latitude + "," + area.southwest.longitude;
            params.put("area", searchArea);
        }

        if (startDate > 0) {
            params.put("start_date", String.valueOf(startDate));
        }

        if (endDate > 0) {
            params.put("end_date", String.valueOf(endDate));
        }

        if (offset > 0) {
            params.put("offset", String.valueOf(offset));
        }

        if (limit > 0) {
            params.put("limit", String.valueOf(limit));
        }

        if (category != null) {
            params.put("category", category.toString());
        }

        return params;
    }
}
