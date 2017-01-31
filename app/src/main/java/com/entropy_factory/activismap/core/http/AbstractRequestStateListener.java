package com.entropy_factory.activismap.core.http;

import com.zanjou.http.request.RequestStateListener;

/**
 * Created by Andersson G. Acosta on 19/01/17.
 */
public abstract class AbstractRequestStateListener implements RequestStateListener {

    private static final String TAG = "AbstractRequestStateListener";

    @Override
    public void onStart() {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onConnectionError(Exception e) {

    }
}
