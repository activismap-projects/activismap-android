package com.entropy_factory.activismap.core.http.request.base;

import java.util.Map;

import static com.entropy_factory.activismap.app.Constants.API.CLIENT_ID;
import static com.entropy_factory.activismap.app.Constants.API.CLIENT_SECRET;

/**
 * Created by ander on 20/04/16.
 */
public abstract class BaseGrantRequestBuilder extends BasePostRequestBuilder {

    private static final String TAG = "BaseGrantApiRequest";

    protected static final String GRANT_TYPE_PASSWORD = "password";
    protected static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";
    protected static final String GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";

    protected abstract String getGrantType();

    @Override
    public String getURL() {
        return super.getURL() + "oauth/v2/token";
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> params = super.getParams();
        params.put("grant_type", getGrantType());
        params.put("client_id", CLIENT_ID);
        params.put("client_secret", CLIENT_SECRET);
        return params;
    }
}
