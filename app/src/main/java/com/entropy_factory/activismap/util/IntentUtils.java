package com.entropy_factory.activismap.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import com.entropy_factory.activismap.ui.tool.MapsActivity;
import com.google.android.gms.maps.model.LatLng;

import static com.entropy_factory.activismap.ui.tool.MapsActivity.DEFAULT_POINT;

/**
 * Created by ander on 30/09/16.
 */
public class IntentUtils {

    private static final String TAG = "IntentUtils";

    public static final int PICK_IMAGE = 1;

    public static void openGallery(Activity activity) {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        activity.startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    public static void openMap(Activity activity) {
        openMap(activity, null);
    }

    public static void openMap(Activity activity, LatLng defaultPoint) {
        Intent mapIntent = new Intent(activity, MapsActivity.class);

        if (defaultPoint != null) {
            mapIntent.putExtra(DEFAULT_POINT, defaultPoint);
        }
        activity.startActivityForResult(mapIntent, MapsActivity.PICK_LOCATION);
    }

    public static void openNavigation(Activity activity, LatLng location) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q=" + location.latitude + "," + location.longitude));
        activity.startActivity(intent);
    }
}
