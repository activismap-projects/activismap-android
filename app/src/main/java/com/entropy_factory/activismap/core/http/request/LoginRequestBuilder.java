package com.entropy_factory.activismap.core.http.request;

import com.entropy_factory.activismap.core.http.request.base.BaseGrantRequestBuilder;

import java.util.Map;

/**
 * Created by ander on 4/10/16.
 */
public class LoginRequestBuilder extends BaseGrantRequestBuilder {

    private static final String TAG = "LoginRequest";

    private String username;
    private String password;

    public LoginRequestBuilder(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    protected String getGrantType() {
        return GRANT_TYPE_PASSWORD;
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> params = super.getParams();
        params.put("username", username);
        params.put("password", password);

        return params;
    }
}
