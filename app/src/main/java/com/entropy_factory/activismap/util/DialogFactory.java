package com.entropy_factory.activismap.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.entropy_factory.activismap.R;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;


/**
 * Created by personal on 1/14/15.
 */
public class DialogFactory {

    private static final String TAG = "DialogFactory";

    private static HashMap<Class<? extends Context>, ArrayList<DialogInterface>> pool = new HashMap<>();

    private static void addDialog(Class<? extends Context> clazz, DialogInterface d) {
        ArrayList<DialogInterface> dialogs = pool.get(clazz);
        if (dialogs == null) {
            dialogs = new ArrayList<>();
        }

        dialogs.add(d);
        pool.put(clazz, dialogs);
    }

    private static MaterialDialog.Builder builder(Context activity) {
        return new MaterialDialog.Builder(activity);
    }

    public static MaterialDialog.Builder progress(final Context activity, CharSequence title, CharSequence message) {
        return new MaterialDialog.Builder(activity)
                .iconRes(R.mipmap.ic_launcher)
                .title(title)
                .content(message)
                .showListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        addDialog(activity.getClass(), dialogInterface);
                    }
                })
                .progress(true, 0);
    }

    public static MaterialDialog.Builder progress(Context activity, @StringRes int title, @StringRes int message) {
        return progress(activity, activity.getString(title), activity.getString(message));
    }

    public static MaterialDialog.Builder alert(Context activity, CharSequence title, CharSequence message) {
        return builder(activity)
                .iconRes(R.mipmap.ic_launcher)
                .title(title)
                .content(message)
                .positiveText(android.R.string.ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                });
    }

    public static MaterialDialog.Builder alert(Context activity, @StringRes int title, CharSequence message) {
        return alert(activity, activity.getString(title), message);
    }

    public static MaterialDialog.Builder alert(Context activity, CharSequence title, @StringRes int message) {
        return alert(activity, title, activity.getString(message));
    }

    public static MaterialDialog.Builder alert(Context activity, @StringRes int title, @StringRes int message) {
        return alert(activity, activity.getString(title), activity.getString(message));
    }

    public static MaterialDialog.Builder alert(final Context activity, CharSequence title, View customView) {
        return builder(activity)
                .iconRes(R.mipmap.ic_launcher)
                .title(title)
                .customView(customView, false)
                .positiveText(android.R.string.ok)
                .showListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        addDialog(activity.getClass(), dialogInterface);
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                });
    }

    public static MaterialDialog.Builder alert(Context activity, @StringRes int title, View customView) {
        return alert(activity, activity.getString(title), customView);
    }

    public static MaterialDialog.Builder successAlert(Context activity, @StringRes int title, View customView) {
        MaterialDialog.Builder ad =alert(activity, title, customView);
        //ad.iconRes(R.drawable.ic_circle_green);
        return ad;
    }

    public static MaterialDialog.Builder successAlert(Context activity, @StringRes int title, @StringRes int message) {
        MaterialDialog.Builder ad =alert(activity, title, message);
        //ad.iconRes(R.drawable.ic_circle_green);
        return ad;
    }

    public static MaterialDialog.Builder successAlert(Context activity, @StringRes int title, CharSequence message) {
        MaterialDialog.Builder ad =alert(activity, title, message);
        //ad.iconRes(R.drawable.ic_circle_green);
        return ad;
    }

    public static MaterialDialog.Builder successAlert(Context activity, CharSequence title, @StringRes int message) {
        MaterialDialog.Builder ad = alert(activity, title, message);
        //ad.iconRes(R.drawable.ic_circle_green);
        return ad;
    }

    public static MaterialDialog.Builder successAlert(Context activity, CharSequence title, CharSequence message) {
        MaterialDialog.Builder ad = alert(activity, title, message);
        //ad.iconRes(R.drawable.ic_circle_green);
        return ad;
    }

    public static MaterialDialog.Builder warn(Context activity, CharSequence title, CharSequence message){
        MaterialDialog.Builder ad = alert(activity, title, message);
        //ad.iconRes(R.drawable.ic_status_warning);
        return ad;
    }

    public static MaterialDialog.Builder warn(Context activity, @StringRes int title, @StringRes int message){
        return warn(activity, activity.getString(title), activity.getString(message));
    }

    public static MaterialDialog.Builder warn(Context activity, CharSequence title, @StringRes int message){
        return warn(activity, title, activity.getString(message));
    }

    public static MaterialDialog.Builder warn(Context activity, @StringRes int title, CharSequence message){
        return warn(activity, activity.getString(title), message);
    }

    public static MaterialDialog.Builder error(Context activity, @StringRes int title, @StringRes int message){
        return error(activity, activity.getString(title), activity.getString(message));
    }

    public static MaterialDialog.Builder error(Context activity, @StringRes int title, CharSequence message){
        return error(activity, activity.getString(title), message);
    }

    public static MaterialDialog.Builder error(Context activity, CharSequence title, @StringRes int message){
        return error(activity, title, activity.getString(message));
    }

    public static MaterialDialog.Builder error(Context activity, CharSequence title, CharSequence message){
        MaterialDialog.Builder dialog = alert(activity, title, message);
        //dialog.iconRes(R.drawable.ic_circle_red);

        return dialog;

    }

    public static void removeDialogsFrom(Class<? extends Context> clazz) {
        ArrayList<DialogInterface> dialogs = pool.get(clazz);
        if (dialogs != null) {
            try {
                for (DialogInterface d : dialogs) {
                    if (((Dialog) d).isShowing()) {
                        d.dismiss();
                    }

                    dialogs.remove(d);
                }
            } catch (ConcurrentModificationException e) {
                Log.e(TAG, "Concurrent modification in list: " + e.getMessage());
                removeDialogsFrom(clazz);
            }
        }
        pool.remove(clazz);
    }
}
