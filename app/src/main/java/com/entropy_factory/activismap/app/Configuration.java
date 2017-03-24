package com.entropy_factory.activismap.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.maps.model.LatLng;

import static com.entropy_factory.activismap.app.ActivisApplication.INSTANCE;

/**
 * Created by Andersson G. Acosta on 24/03/17.
 */
public class Configuration {

    private static final String TAG = "Configuration";

    public static final String PREFS_KEY_MY_LOCATION = "my_location";
    public static final String PREFS_KEY_MY_LOCATION_ADDRESS = "my_location_address";

    private SharedPreferences prefs;

    public Configuration(SharedPreferences prefs) {
        this.prefs = prefs;
    }

    public void setLocation(LatLng location) {
        prefs.edit().putString(PREFS_KEY_MY_LOCATION, location.latitude + "," + location.longitude).apply();
    }

    public LatLng getLocation() {
        String loc = prefs.getString(PREFS_KEY_MY_LOCATION, "");

        if (!loc.isEmpty()) {
            Double lat = Double.parseDouble(loc.split(",")[0]);
            Double lon = Double.parseDouble(loc.split(",")[1]);

            return new LatLng(lat, lon);
        }

        return null;
    }


    public void setLocationAddress(String address) {
        prefs.edit().putString(PREFS_KEY_MY_LOCATION_ADDRESS, address).apply();
    }

    public String getLocationAddress() {
        return prefs.getString(PREFS_KEY_MY_LOCATION_ADDRESS, "");
    }


    public Configuration(Context context) {
        this(PreferenceManager.getDefaultSharedPreferences(context));
    }

    public static Configuration getInstance() {
        return new Configuration(INSTANCE);
    }
}
