package com.entropy_factory.activismap.core.http.request;

import com.entropy_factory.activismap.core.http.request.base.BaseGetRequestBuilder;
import com.entropy_factory.activismap.core.item.ActivisItem;

import java.util.Map;

/**
 * Created by Andersson G. Acosta on 31/01/17.
 */
public class EventCommentsRequestBuilder extends BaseGetRequestBuilder {

    private static final String TAG = "CommentRequestBuilder";

    private ActivisItem item;
    private int limit = 20;
    private int offset = 0;

    public EventCommentsRequestBuilder(ActivisItem item) {
        this.item = item;
    }

    public EventCommentsRequestBuilder setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    public EventCommentsRequestBuilder setOffset(int offset) {
        this.offset = offset;
        return this;
    }

    @Override
    protected String getURL() {
        return super.getURL() + "v1/public/event/" + item.getIdentifier() + "/comments";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = super.getParams();
        params.put("event_id", String.valueOf(item.getServerId()));
        params.put("limit", String.valueOf(limit));
        params.put("offset", String.valueOf(offset));
        return params;
    }
}
