package com.entropy_factory.activismap.core.http.handler;

import android.util.Log;

import com.zanjou.http.response.JsonResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Andersson G. Acosta on 10/01/17.
 */
public abstract class OkResponseListener extends JsonResponseListener {

    private static final String TAG = "OkResponseListener";

    @Override
    public void onErrorResponse(JSONObject jsonObject) throws JSONException {

    }

    @Override
    public void onParseError(JSONException e) {
        Log.e(TAG, "Error", e);
    }
}
