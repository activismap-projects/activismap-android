package com.entropy_factory.activismap.core.http.request.base;

/**
 * Created by ander on 11/04/16.
 */
public class BasePutRequestBuilder extends RequestBuilder {

    private static final String TAG = "BasePutApiRequest";

    @Override
    public String getMethod() {
        return PUT;
    }
}
