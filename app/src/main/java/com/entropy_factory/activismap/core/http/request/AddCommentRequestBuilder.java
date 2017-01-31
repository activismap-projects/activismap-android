package com.entropy_factory.activismap.core.http.request;

import com.entropy_factory.activismap.core.http.request.base.BaseGetRequestBuilder;
import com.entropy_factory.activismap.core.http.request.base.BasePostRequestBuilder;
import com.entropy_factory.activismap.core.item.ActivisItem;

import java.util.Map;

/**
 * Created by Andersson G. Acosta on 31/01/17.
 */
public class AddCommentRequestBuilder extends BasePostRequestBuilder {

    private static final String TAG = "CommentRequestBuilder";

    private ActivisItem item;
    private String comment;

    public AddCommentRequestBuilder(ActivisItem item, String comment) {
        this.item = item;
        this.comment = comment;
    }

    @Override
    protected String getURL() {
        return super.getURL() + "v1/public/event/" + item.getIdentifier() + "/addComment";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = super.getParams();
        params.put("comment", comment);
        return params;
    }
}
