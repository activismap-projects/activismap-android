package com.entropy_factory.activismap.core.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.entropy_factory.activismap.core.activis.Activis;
import com.entropy_factory.activismap.core.db.ActivisCategory;

import java.io.Serializable;

/**
 * Created by ander on 5/10/16.
 */
public abstract class ActivisEventReceiver extends BroadcastReceiver {

    private static final String TAG = "ActivisEventReceiver";

    private static final String BASE_ACTION = ActivisEventReceiver.class.getPackage().getName();
    public static final String ACTION_REFRESH = BASE_ACTION + ".ACTION_REFRESH";
    public static final String ACTION_CHANGE_CATEGORY = BASE_ACTION + ".ACTION_CHANGE_CATEGORY";
    public static final String ACTION_SELECT_ALL = BASE_ACTION + ".ACTION_SELECT_ALL";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null ) {
            if (action.equals(ACTION_REFRESH)) {
                onRefresh();
            } else if (action.equals(ACTION_CHANGE_CATEGORY)) {
                Serializable s = intent.getExtras().getSerializable("category");
                ActivisCategory ac = null;

                if (s != null) {
                    ac = (ActivisCategory) s;
                }
                onCategory(ac);
            } else if (action.equals(ACTION_SELECT_ALL)) {
                onSelectAll();
            }
        }
    }

    public void onRefresh() {

    }

    public void onCategory(ActivisCategory category) {

    }

    public void onSelectAll() {

    }
}
