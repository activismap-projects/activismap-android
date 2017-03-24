package com.entropy_factory.activismap.ui.tool;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.entropy_factory.activismap.R;
import com.entropy_factory.activismap.ui.adapter.AddressAdapter;
import com.entropy_factory.activismap.ui.adapter.item.AddressItem;
import com.entropy_factory.activismap.util.DialogFactory;
import com.entropy_factory.activismap.util.GpsChecker;
import com.entropy_factory.activismap.util.Permissions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import eu.davidea.flexibleadapter.FlexibleAdapter;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final int PICK_LOCATION = 365;
    public static final String DEFAULT_POINT = "defaut_point";

    private static final String TAG = "MapsActivity";

    private LocationManager locationManager;
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap map;
    private LatLng position;
    private String address;
    private AsyncTask locationTask;
    private LatLng defaultPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        buildGoogleApiClient();

        defaultPoint = getIntent().getParcelableExtra(DEFAULT_POINT);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        View currentLocation = findViewById(R.id.catch_location);
        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Permissions.checkLocationPermission(MapsActivity.this)) {
                    getMyLocation();
                }
            }
        });

        View handleLocation = findViewById(R.id.handle_location);
        handleLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position == null) {
                    setResult(RESULT_CANCELED);
                    finish();
                } else {
                    Intent data = new Intent();
                    data.putExtra("latitude", position.latitude);
                    data.putExtra("longitude", position.longitude);

                    if (address != null) {
                        data.putExtra("address", address);
                    }
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });

        View searchLocation = findViewById(R.id.search_location);
        searchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLocationDialog();
            }
        });

    }

    private void getMyLocation() {
        if (Permissions.checkLocationPermission(MapsActivity.this)) {
            GpsChecker gpsChecker = new GpsChecker(this);
            if (gpsChecker.isActivated()) {
                new SearchLocationTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                Toast.makeText(MapsActivity.this, R.string.searching_location, Toast.LENGTH_LONG).show();

                Criteria criteria = new Criteria();

                String provider = locationManager.getBestProvider(criteria, false);
                Location location = locationManager.getLastKnownLocation(provider);

                if (location != null) {
                    onLocationChanged(location);
                } else {
                    Log.e(TAG, "Location is null");
                }
            } else {
                gpsChecker.activate();
            }
        }

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }

    private void setMyLastLocation(GoogleMap map) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 16));
        map.addMarker(new MarkerOptions()
                .position(position)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
    }

    private void showLocationDialog() {
        View v = getLayoutInflater().inflate(R.layout.location_dialog, null);

        final SwipeRefreshLayout srl = (SwipeRefreshLayout) v.findViewById(R.id.location_refresh);
        srl.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        RecyclerView list = (RecyclerView) v.findViewById(R.id.location_list);
        EditText locationText = (EditText) v.findViewById(R.id.location_name);

        final AddressAdapter addressAdapter = new AddressAdapter(new ArrayList<AddressItem>(), this);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(addressAdapter);

        MaterialDialog.Builder lDialog = DialogFactory.alert(this, R.string.search_location, v);
        lDialog.negativeText(android.R.string.cancel)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                });

        locationText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchLocations(charSequence.toString(), addressAdapter, srl);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        final MaterialDialog dialog = lDialog.show();

        addressAdapter.setOnItemClickListener(new FlexibleAdapter.OnItemClickListener() {
            @Override
            public boolean onItemClick(int i) {
                AddressItem addressItem = addressAdapter.getItem(i);
                Address address = addressItem.getAddress();

                if (address.hasLatitude() && address.hasLongitude()) {
                    LatLng position = new LatLng(address.getLatitude(), address.getLongitude());

                    StringBuilder sb = new StringBuilder();
                    for (int x = 0; x < address.getMaxAddressLineIndex()+1; x++) {
                        sb.append(address.getAddressLine(x)).append(", ");
                    }

                    String addressString = sb.toString();
                    if (!addressString.isEmpty()) {
                        addressString = addressString.substring(0, addressString.length() -2);
                    }

                    moveCamera(addressString, position, 10);
                }
                dialog.dismiss();
                return true;
            }
        });
    }

    private void searchLocations(final String search, final AddressAdapter adapter, final SwipeRefreshLayout srl) {
        if (locationTask != null && !locationTask.isCancelled()) {
            locationTask.cancel(true);
        }

        locationTask = new AsyncTask<Void, Void, List<Address>>() {
            @Override
            protected void onPreExecute() {
                srl.setRefreshing(true);
            }

            @Override
            protected void onPostExecute(List<Address> addresses) {
                srl.setRefreshing(false);

                if (addresses != null) {
                    adapter.clear();
                    adapter.addItems(0, AddressItem.build(addresses));
                }
            }

            @Override
            protected List<Address> doInBackground(Void... voids) {
                try {
                    final Geocoder gc = new Geocoder(MapsActivity.this, Locale.getDefault());
                    return gc.getFromLocationName(search, 20);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void moveCamera(String address, LatLng position, float zoom) {
        CameraPosition cp = new CameraPosition.Builder()
                .target(position)
                .zoom(zoom)
                .tilt(map.getCameraPosition().tilt)
                .bearing(map.getCameraPosition().bearing)
                .build();

        this.position = position;
        this.address = address;
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
        map.clear();
        map.addMarker(new MarkerOptions().position(position).title(address));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Permissions.checkLocationPermission(this)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 1, this);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Permissions.checkLocationPermission(this)) {
            locationManager.removeUpdates(this);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getMyLocation();
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;

        this.map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                setLocation(latLng);
            }
        });

        if (defaultPoint != null) {
            String address = getLocationAddress(defaultPoint);
            moveCamera(address, defaultPoint, 7);
        }
    }

    public String getLocationAddress(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            for (Address a : addresses) {
                Log.e(TAG, "Address: " + a.toString());
            }
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(address.getAddressLine(i)).append(" ");
                }

                return  strReturnedAddress.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setLocation(LatLng latLng) {
        String title = getLocationAddress(latLng);
        moveCamera(title, latLng, 15);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e(TAG, "Location catched = " + location.toString());
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        setLocation(latLng);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private class SearchLocationTask extends AsyncTask<Void, Void, Void> {
        private MaterialDialog pDialog;
        private boolean searchingLocation = true;
        Location mLastLocation;

        public boolean isSearchingLocation() {
            return this.searchingLocation;
        }

        public void setSearchingLocation(boolean searchingLocation) {
            this.searchingLocation = searchingLocation;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = DialogFactory.progress(MapsActivity.this, R.string.location, R.string.searching_location).show();
            pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    setSearchingLocation(false);
                }
            });
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (Permissions.checkLocationPermission(MapsActivity.this)) {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                while (mLastLocation == null && isSearchingLocation()) {
                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pDialog.dismiss();
            if (mLastLocation != null) {
                position = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                setMyLastLocation(map);
                setLocation(position);
            }else{
                Toast.makeText(MapsActivity.this, R.string.cannot_find_location, Toast.LENGTH_LONG).show();
            }
            if (map != null){ map.clear(); }
        }
    }

}
