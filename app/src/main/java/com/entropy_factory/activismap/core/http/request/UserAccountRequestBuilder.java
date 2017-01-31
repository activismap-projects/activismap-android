package com.entropy_factory.activismap.core.http.request;

import com.entropy_factory.activismap.core.http.request.base.BaseGetRequestBuilder;

/**
 * Created by ander on 4/10/16.
 */
public class UserAccountRequestBuilder extends BaseGetRequestBuilder {

    private static final String TAG = "UserAccountRequest";

    public static final int NO_ID = -1;
    private long id = NO_ID;

    public UserAccountRequestBuilder() {
    }

    public UserAccountRequestBuilder(long id) {
        this.id = id;
    }

    @Override
    public String getURL() {
        if (id == NO_ID) {
            return super.getURL() + "v1/user";
        } else {
            return super.getURL() + "v1/user/account/" + id;
        }
    }
}
