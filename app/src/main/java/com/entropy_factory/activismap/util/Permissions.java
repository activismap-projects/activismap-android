package com.entropy_factory.activismap.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.entropy_factory.activismap.R;

import java.util.Arrays;

/**
 * Created by ander on 1/09/16.
 */
public class Permissions {

    private static final String TAG = "Permissions";

    private static int checkPermissions(@NonNull Context context, @NonNull String... permissions) {
        int granted = 0;
        for (String s : permissions) {
            granted = ContextCompat.checkSelfPermission(context, s);
        }

        return granted;
    }

    public static boolean checkPermission(@NonNull final Activity context, @NonNull String explanation, final int requestCode, @NonNull final String... permissions) {
        int granted = checkPermissions(context, permissions);
        Log.e(TAG, "Check self " + Arrays.toString(permissions) + ": " + (granted == PackageManager.PERMISSION_GRANTED));
        if (granted != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "PERMISSION_DENIED");
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, permissions[0])) {
                Log.e(TAG, "Showing explanation");
                AlertDialog a = DialogFactory.alert(context, context.getString(R.string.enable_permissions), explanation);
                a.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                //a.show();
                ActivityCompat.requestPermissions(context, permissions, requestCode);
            } else {
                Log.e(TAG, "Requesting permissions");
                ActivityCompat.requestPermissions(context, permissions, requestCode);
            }
            return false;
        } else {
            return true;
        }

    }

    public static boolean checkLocationPermission(Activity activity) {
        return checkPermission(activity, activity.getString(R.string.explanations_location), 2,  Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    public static boolean checkStoragePermission(Activity activity) {
        return checkPermission(activity, activity.getString(R.string.explanations_storage), 1, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

}
