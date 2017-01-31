package com.entropy_factory.activismap.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;


/**
 * Created by personal on 1/14/15.
 */
public class DialogFactory {

    private static final String TAG = "DialogFactory";

    private static HashMap<Class<? extends Context>, ArrayList<Dialog>> pool = new HashMap<>();

    private static void addDialog(Class<? extends Context> clazz, Dialog d) {
        ArrayList<Dialog> dialogs = pool.get(clazz);
        if (dialogs == null) {
            dialogs = new ArrayList<>();
        }

        dialogs.add(d);
        pool.put(clazz, dialogs);
    }

    private static AlertDialog.Builder alertBuilder(Context context) {
        return new AlertDialog.Builder(context);
    }

    public static ProgressDialog progress(Context context, CharSequence title, CharSequence message, boolean cancelable) {
        ProgressDialog p = new ProgressDialog(context);
        p.setTitle(title);
        p.setMessage(message);
        p.setCancelable(cancelable);
        //p.setIcon(R.mipmap.ic_soldier);

        addDialog(context.getClass(), p);
        return p;
    }

    public static ProgressDialog progress(Context context, CharSequence title, CharSequence message) {
        return progress(context, title, message, false);
    }

    public static ProgressDialog progress(Context context, @StringRes int title, @StringRes int message, boolean cancelable) {
        return progress(context, context.getString(title), context.getString(message), cancelable);
    }

    public static ProgressDialog progress(Context context, @StringRes int title, @StringRes int message) {
        return progress(context, context.getString(title), context.getString(message));
    }

    public static AlertDialog alert(Context context, CharSequence title, CharSequence message) {
        AlertDialog.Builder aBuilder = alertBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog aDialog = aBuilder.create();
        addDialog(context.getClass(), aDialog);
        return aDialog;
    }

    public static AlertDialog alert(Context context, @StringRes int title, CharSequence message) {
        return alert(context, context.getString(title), message);
    }

    public static AlertDialog alert(Context context, CharSequence title, @StringRes int message) {
        return alert(context, title, context.getString(message));
    }

    public static AlertDialog alert(Context context, @StringRes int title, @StringRes int message) {
        return alert(context, context.getString(title), context.getString(message));
    }

    public static AlertDialog alert(Context context, CharSequence title, View customView) {
        AlertDialog.Builder aBuilder = alertBuilder(context)
                .setTitle(title)
                .setView(customView);
        AlertDialog ad = aBuilder.create();
        addDialog(context.getClass(), ad);
        return ad;
    }

    public static AlertDialog alert(Context context, @StringRes int title, View customView) {
        return alert(context, context.getString(title), customView);
    }

    public static AlertDialog list(Context context, @StringRes int title, String... options) {
        return list(context, context.getString(title), options);
    }

    public static AlertDialog list(Context context, CharSequence title, String... options) {
        AlertDialog.Builder aBuilder = alertBuilder(context)
                .setTitle(title)
                .setItems(options, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        AlertDialog ad = aBuilder.create();
        addDialog(context.getClass(), ad);
        return ad;
    }

    public static AlertDialog successAlert(Context context, @StringRes int title, View customView) {
        AlertDialog ad =alert(context, title, customView);
        //ad.setIcon(context.getResources().getDrawable(R.mipmap.ic_circle_green));
        return ad;
    }

    public static AlertDialog successAlert(Context context, @StringRes int title, @StringRes int message) {
        AlertDialog ad =alert(context, title, message);
        //ad.setIcon(context.getResources().getDrawable(R.mipmap.ic_circle_green));
        return ad;
    }

    public static AlertDialog successAlert(Context context, @StringRes int title, CharSequence message) {
        AlertDialog ad =alert(context, title, message);
        //ad.setIcon(context.getResources().getDrawable(R.mipmap.ic_circle_green));
        return ad;
    }

    public static AlertDialog successAlert(Context context, CharSequence title, @StringRes int message) {
        AlertDialog ad = alert(context, title, message);
        //ad.setIcon(context.getResources().getDrawable(R.mipmap.ic_circle_green));
        return ad;
    }

    public static AlertDialog successAlert(Context context, CharSequence title, CharSequence message) {
        AlertDialog ad = alert(context, title, message);
        //ad.setIcon(context.getResources().getDrawable(R.mipmap.ic_circle_green));
        return ad;
    }

    public static AlertDialog warn(Context context, CharSequence title, CharSequence message){
        AlertDialog ad = alert(context, title, message);
        ad.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return ad;
    }

    public static AlertDialog warn(Context context, @StringRes int title, @StringRes int message){
        return warn(context, context.getString(title), context.getString(message));
    }

    public static AlertDialog warn(Context context, CharSequence title, @StringRes int message){
        return warn(context, title, context.getString(message));
    }

    public static AlertDialog warn(Context context, @StringRes int title, CharSequence message){
        return warn(context, context.getString(title), message);
    }

    public static AlertDialog error(Context context, CharSequence title, CharSequence message){
        AlertDialog dialog = alert(context, title, message);
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return dialog;

    }

    public static void removeDialogsFrom(Class<? extends Context> clazz) {
        ArrayList<Dialog> dialogs = pool.get(clazz);
        if (dialogs != null) {
            try {
                for (Dialog d : dialogs) {
                    if (d.isShowing()) {
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
