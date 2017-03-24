package com.entropy_factory.activismap.ui.main;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import com.entropy_factory.activismap.R;
import com.entropy_factory.activismap.app.Configuration;
import com.entropy_factory.activismap.util.IntentUtils;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static com.entropy_factory.activismap.ui.tool.MapsActivity.PICK_LOCATION;

/**
 * Created by Andersson G. Acosta on 10/01/17.
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    private static final String TAG = "SettingsFragment";

    private Preference myLocation;
    private Configuration conf;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings);

        conf = Configuration.getInstance();

        myLocation = findPreference("my_location");
        myLocation.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                IntentUtils.openMap(getActivity(), conf.getLocation());
                return true;
            }
        });

        LatLng location = conf.getLocation();
        String address = conf.getLocationAddress();
        setLocation(location, address);
    }

    private void setLocation(LatLng location, String address) {
        conf.setLocation(location);
        conf.setLocationAddress(address);
        Log.e(TAG, "Address: " + address);
        if (address == null || address.isEmpty()) {
            myLocation.setSummary(location.latitude + ", " + location.longitude);
        } else {
            myLocation.setSummary(address);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_LOCATION) {
                Bundle extras = data.getExtras();
                LatLng location = new LatLng(extras.getDouble("latitude"), extras.getDouble("longitude"));
                String address = data.getExtras().getString("address", "");

                setLocation(location, address);

            }
        }
    }
}
