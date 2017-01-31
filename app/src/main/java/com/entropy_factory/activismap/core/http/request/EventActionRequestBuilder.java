package com.entropy_factory.activismap.core.http.request;

import com.entropy_factory.activismap.core.anotations.EventAction;
import com.entropy_factory.activismap.core.http.request.base.BaseGetRequestBuilder;
import com.entropy_factory.activismap.core.item.ActivisItem;

import java.util.Map;

/**
 * Created by ander on 11/11/16.
 */
public class EventActionRequestBuilder extends BaseGetRequestBuilder {

    private static final String TAG = "ActivisEventActionRequest";

    private ActivisItem item;
    private String action;

    public EventActionRequestBuilder(ActivisItem item, @EventAction String action) {
        this.item = item;
        this.action = action;
    }

    @Override
    public String getURL() {
        return super.getURL() + "v1/public/event/" + item.getIdentifier();
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> params = super.getParams();
        params.put("action", action);
        return params;
    }
}
