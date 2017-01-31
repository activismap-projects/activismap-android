package com.entropy_factory.activismap.core.http.request;

import com.entropy_factory.activismap.core.db.User;
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
        User user = User.getUser();
        if (user != null && !user.isAccessTokenExpired()) {
            return super.getURL() + "v1/event/" + item.getIdentifier() + "/addComment";
        }

        return super.getURL() + "v1/public/event/" + item.getIdentifier() + "/addComment";
    }

    @Override
    protected Map<String, String> getHeaders() {
        Map<String, String> headers = super.getHeaders();

        User user = User.getUser();
        if (user != null && !user.isAccessTokenExpired()) {
            headers.put("Authorization", "Bearer " + user.getAccessToken());
        }

        return headers;
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = super.getParams();
        params.put("comment", comment);
        return params;
    }
}
