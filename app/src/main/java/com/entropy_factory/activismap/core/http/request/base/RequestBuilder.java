package com.entropy_factory.activismap.core.http.request.base;

import com.entropy_factory.activismap.core.db.User;
import com.zanjou.http.debug.Logger;
import com.zanjou.http.request.ParameterBag;
import com.zanjou.http.request.Request;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static com.entropy_factory.activismap.app.Constants.API.BASE_URL;

/**
 * Created by ander on 11/04/16.
 */
public abstract class RequestBuilder {

    public static final String POST = "POST";
    public static final String GET = "GET";
    public static final String DELETE = "DELETE";
    public static final String PUT = "PUT";

    private static final String TAG = "BaseApiRequest";

    protected abstract String getMethod();

    protected String getURL() {
        return BASE_URL;
    }

    protected Map<String, String> getParams() {
        return new HashMap<>();
    }

    protected Map<String, File> getFiles() {
        return new HashMap<String, File>();
    }

    protected Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        if (User.getUser() != null) {
            headers.put("Authorization", "Bearer " + User.getUser().getAccessToken());
        }

        return headers;
    }

    public final Request build() {
        Request request = Request.create(getURL())
                .setMethod(getMethod());
        request.setLogger(new Logger(Logger.ERROR));
        addHeaders(request, getHeaders());
        addParams(request, getParams());
        addFiles(request, getFiles());

        return request;

    }

    private void addHeaders(Request request, Map<String, String> headers) {

        for (String k : headers.keySet()) {
            request.addHeader(k, headers.get(k));
        }
    }

    private void addParams(Request request, Map<String, String> params) {

        for (String k : params.keySet()) {
            request.addParameter(k, params.get(k));
        }
    }

    private void addFiles(Request request, Map<String, File> files) {

        for (String k : files.keySet()) {
            request.addParameter(k, files.get(k));
        }
    }

}
