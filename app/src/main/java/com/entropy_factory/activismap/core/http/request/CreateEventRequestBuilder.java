package com.entropy_factory.activismap.core.http.request;

import android.text.TextUtils;

import com.entropy_factory.activismap.core.db.ActivisType;
import com.entropy_factory.activismap.core.db.Company;
import com.entropy_factory.activismap.core.http.request.base.BasePostRequestBuilder;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.util.Map;

/**
 * Created by ander on 3/10/16.
 */
public class CreateEventRequestBuilder extends BasePostRequestBuilder {

    private static final String TAG = "CreateEventRequest";

    private Company company;
    private String title;
    private String description;
    private String categories;
    private ActivisType type;
    private LatLng location;
    private long sDate;
    private long eDate;
    private File image;
    private String imageUrl;

    public CreateEventRequestBuilder(Company company, String title, String description, String categories, ActivisType type, LatLng location, long sDate, long eDate) {
        this.company = company;
        this.title = title;
        this.description = description;
        this.categories = categories;
        this.type = type;
        this.location = location;
        this.sDate = sDate;
        this.eDate = eDate;
    }

    public void setImage(File image) {
        this.image = image;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String getURL() {
        return super.getURL() + "v1/event";
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> params = super.getParams();
        params.put("company_id", String.valueOf(company.getIdentifier()));
        params.put("title", title);
        params.put("description", description);
        params.put("categories", categories);
        params.put("type", type.toString());
        params.put("lat", String.valueOf(location.latitude));
        params.put("lon", String.valueOf(location.longitude));
        params.put("start_date", String.valueOf(sDate));
        params.put("end_date", String.valueOf(eDate));

        if (!TextUtils.isEmpty(imageUrl)) {
            params.put("image", imageUrl);
        }
        return params;
    }

    @Override
    public Map<String, File> getFiles() {
        Map<String, File> files = super.getFiles();
        if (image != null) {
            files.put("image", image);
        }
        return files;
    }
}
