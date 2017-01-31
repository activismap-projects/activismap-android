package com.entropy_factory.activismap.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

public class GpsChecker {

    private Activity context;

    public GpsChecker(Activity context) {
        this.context = context;
    }

    public boolean isActivated() {
        LocationManager locationM = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationM.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public void activate(){
        context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }
}
