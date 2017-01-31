package com.entropy_factory.activismap.core.http.handler;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.StringRes;
import android.util.Log;

import com.entropy_factory.activismap.R;
import com.entropy_factory.activismap.util.DialogFactory;
import com.zanjou.http.request.RequestStateListener;
import com.zanjou.http.response.JsonResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lluis on 1/24/15.
 */
public abstract class DialogResponseListener extends JsonResponseListener implements RequestStateListener {
    private static final String TAG = "DialogApiHandler";

    private Context context;
    private CharSequence waitingTitle;
    private CharSequence waitingMessage;
    private CharSequence errorMessage;
    private ProgressDialog pDialog;

    @Override
    public void onErrorResponse(JSONObject jsonObject) throws JSONException {
        onErrorResponse(jsonObject.toString());
    }

    @Override
    public void onParseError(JSONException e) {
        e.printStackTrace();
        CharSequence error = (errorMessage == null) ? e.getMessage() : errorMessage;
        DialogFactory.error(
                this.context,
                "Error",
                error).show();
    }

    private void onErrorResponse(String errorMessage){
        CharSequence error = (this.errorMessage == null) ? errorMessage : this.errorMessage;
        DialogFactory.error(this.context, "Error", error).show();
    }

    public Dialog getProgressDialog(){
        pDialog = DialogFactory.progress(context, waitingTitle, waitingMessage);
        pDialog.setIcon(R.mipmap.ic_launcher);
        return pDialog;
    }

    public DialogResponseListener(Context activity, CharSequence waitingTitle, CharSequence waitingMessage){
        this.context = activity;
        this.waitingTitle = waitingTitle;
        this.waitingMessage = waitingMessage;
    }

    public DialogResponseListener(Context activity, CharSequence waitingTitle, CharSequence waitingMessage, CharSequence errorMessage){
        this(activity, waitingTitle, waitingMessage);
        this.errorMessage = errorMessage;
    }

    public DialogResponseListener(Context activity, @StringRes int titleId, @StringRes int messageId) {
        this.context = activity;
        this.waitingTitle = this.context.getString(titleId);
        this.waitingMessage = this.context.getString(messageId);
    }

    public DialogResponseListener(Context activity, @StringRes int titleId, @StringRes int messageId, @StringRes int errorMessageId) {
        this(activity, titleId, messageId);
        this.errorMessage = this.context.getString(errorMessageId);
    }


    @Override
    public void onStart() {
        Log.e(TAG, "onStartCalled");
        Dialog dialog = getProgressDialog();
        if (dialog != null) {
            dialog.show();
        }
    }

    @Override
    public void onFinish() {
        if (this.pDialog != null && this.pDialog.isShowing()) {
            this.pDialog.dismiss();
        }

    }

    @Override
    public void onConnectionError(Exception e) {

    }
}
