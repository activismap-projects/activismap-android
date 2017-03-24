package com.entropy_factory.activismap.ui.content;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.entropy_factory.activismap.R;
import com.entropy_factory.activismap.app.Configuration;
import com.entropy_factory.activismap.core.activis.BaseActivis;
import com.entropy_factory.activismap.core.activis.ActivisListener;
import com.entropy_factory.activismap.core.activis.ActivisResponse;
import com.entropy_factory.activismap.core.db.ActivisCategory;
import com.entropy_factory.activismap.core.db.ActivisEvent;
import com.entropy_factory.activismap.core.db.ActivisType;
import com.entropy_factory.activismap.core.http.handler.OkResponseListener;
import com.entropy_factory.activismap.core.http.request.SearchActivisEventRequestBuilder;
import com.entropy_factory.activismap.core.item.ActivisItem;
import com.entropy_factory.activismap.core.item.ActivisTempEvent;
import com.entropy_factory.activismap.core.receiver.ActivisEventReceiver;
import com.entropy_factory.activismap.map.ActivisClusterManager;
import com.entropy_factory.activismap.map.ActivisEventMapRenderer;
import com.entropy_factory.activismap.ui.adapter.AddressAdapter;
import com.entropy_factory.activismap.ui.adapter.item.AddressItem;
import com.entropy_factory.activismap.ui.base.AdvanceMapFragment;
import com.entropy_factory.activismap.ui.base.FragmentContext;
import com.entropy_factory.activismap.ui.base.MapStateListener;
import com.entropy_factory.activismap.util.DialogFactory;
import com.entropy_factory.activismap.util.TimeUtils;
import com.entropy_factory.activismap.widget.RemoteImageView;
import com.entropy_factory.activismap.widget.TimeSelectorView;
import com.entropy_factory.activismap.widget.ClassificationView;
import com.entropy_factory.activismap.widget.TypeClassificationView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.zanjou.http.request.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

import static com.entropy_factory.activismap.core.receiver.ActivisEventReceiver.ACTION_CHANGE_CATEGORY;
import static com.entropy_factory.activismap.core.receiver.ActivisEventReceiver.ACTION_SELECT_ALL;

/**
 * Created by ander on 29/09/16.
 */
public class EventMapFragment extends FragmentContext implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, ActivisListener<ActivisItem>{

    private static final String TAG = "EventMapFragment";

    private final ActivisEventReceiver RECEIVER = new ActivisEventReceiver() {
        @Override
        public void onRefresh() {
            onMapReady(map);
        }

        @Override
        public void onCategory(ActivisCategory category) {
            searchCategory = category;
            search();
        }

        @Override
        public void onSelectAll() {
            searchCategory = null;
            search();
        }
    };

    private GoogleMap map;
    private ActivisClusterManager mClusterManager;
    private TimeSelectorView timeFilter;
    private TypeClassificationView categoryFilter;
    private AdvanceMapFragment smf;
    private BaseActivis baseActivis;
    private Handler handler;
    private SearchActivisEventRequestBuilder request;
    private Request requester;

    private ActivisType searchType;
    private ActivisCategory searchCategory;
    private AsyncTask locationTask;
    private long startDate;
    private long endDate;
    private List<ActivisItem> persistentEvents;
    private boolean panelsVisible = true;
    private boolean setLocationPreference = true;

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_map, null);
    }

    @Override
    public void afterInitialize(View view, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        baseActivis = new BaseActivis(getActivity());
        request = new SearchActivisEventRequestBuilder();

        smf = (AdvanceMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        smf.getMapAsync(this);

        timeFilter = findViewById(R.id.time_filter);
        categoryFilter = findViewById(R.id.category_filter);
        categoryFilter.setOnClassificationClickListener(new ClassificationView.OnClassificationClickListener<ActivisType>() {
            @Override
            public void onClassificationClick(View v, ActivisType item) {
                searchType = item;
                search();
            }

            @Override
            public boolean onClassificationLongClick(View v, ActivisType item) {
                String text = TextUtils.join(", ", item.getActivisClassification());
                openTypeTooltip(v, text);
                return true;
            }
        });

        timeFilter.clear();
        timeFilter.addOption(R.string.events_this_week, TimeUtils.getWeekTime(), TimeUtils.getNextWeekTime());
        timeFilter.addOption(R.string.events_this_month, TimeUtils.getMonthTime(), TimeUtils.getNextMonthTime());
        timeFilter.addOption(R.string.events_three_months, TimeUtils.getMonthTime(), (TimeUtils.getNextMonthTime() + TimeUtils.MONTH * 3));

        timeFilter.setOnTimeClick(new TimeSelectorView.OnTimeClick() {
            @Override
            public void onTimeClick(View v, TimeSelectorView.TimeOption timeOption) {
                v.setPressed(true);
                endDate = timeOption.endDate;
                startDate = timeOption.startDate;
                search();
            }
        });

        search();
    }

    private void openTypeTooltip(View v, String text) {
        SimpleTooltip st = new SimpleTooltip.Builder(getActivity())
                .text(text)
                .textColor(Color.WHITE)
                .backgroundColor(getResources().getColor(R.color.colorSecondaryHint))
                .animated(true)
                .anchorView(v)
                .modal(true)
                .arrowColor(getResources().getColor(R.color.colorSecondaryHint))
                .build();
        st.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.e(TAG, "OnMapReady");
        map = googleMap;
        if (map != null) {
            map.clear();
            map.setOnMarkerClickListener(this);

            mClusterManager = new ActivisClusterManager(getActivity(), map);

            ActivisEventMapRenderer renderer = new ActivisEventMapRenderer(getActivity(), map, mClusterManager);

            mClusterManager.setRenderer(renderer);
            map.setOnCameraChangeListener(mClusterManager);

            MapStateListener msl = new MapStateListener(map, smf, getActivity()) {

                @Override
                public void onMapTouched() {
                    Log.d(TAG, "OnMapTouched");
                    hidePanels();
                }

                @Override
                public void onMapReleased() {
                    Log.d(TAG, "onMapReleased");
                    showPanels();
                }

                @Override
                public void onMapUnsettled() {
                    Log.e(TAG, "onMapUnsettled");
                }

                @Override
                public void onMapSettled() {
                    Log.e(TAG, "onMapUnsettled");
                }
            };

            msl.addCameraChangeListener(mClusterManager);
            msl.addCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {
                    Log.e(TAG, "onCameraChange");
                    search();
                }
            });

            if (persistentEvents != null && !persistentEvents.isEmpty()) {
                setEventsOnMap(persistentEvents);
                persistentEvents.clear();
            }

            if (setLocationPreference) {
                LatLng myLocation = Configuration.getInstance().getLocation();
                if (myLocation != null) {
                    moveCamera(myLocation, 7);
                }
                setLocationPreference = false;
            }
        }

    }

    private void tooglePanels() {
        if (panelsVisible) {
            hidePanels();
        } else {
            showPanels();
        }
    }
    private void showPanels() {
        if (!panelsVisible) {
            panelsVisible = true;
            categoryFilter.animate().translationY(0).setDuration(300);
            timeFilter.animate().translationY(0).setDuration(300);
        }
    }

    private void hidePanels() {
        if (panelsVisible) {
            panelsVisible = false;
            categoryFilter.animate().translationY(-100).setDuration(300);
            timeFilter.animate().translationY(150).setDuration(300);
        }
    }

    private void setDate(long duration) {
        startDate = System.currentTimeMillis();
        endDate = startDate + duration;
    }

    private void invalidateHandler() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }

        handler = new Handler();
    }

    private void delayRequestEvents() {
        invalidateHandler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                baseActivis.searchEvents(EventMapFragment.this);
            }
        }, 500);
    }

    private void invalidateRequester() {
        if (requester != null) {
            try {
                requester.cancel();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void search() {

        Log.e(TAG, "Searching ");
        invalidateRequester();

        request.setType(searchType);
        request.setEndDate(endDate);
        request.setStartDate(startDate);
        request.setCategory(searchCategory);

        if (map != null) {
            request.setArea(map.getProjection().getVisibleRegion().latLngBounds);
        }

        requester = request.build()
                .setResponseListener(new OkResponseListener() {
                    @Override
                    public void onOkResponse(JSONObject jsonObject) throws JSONException {
                        JSONArray data = jsonObject.getJSONArray("data");
                        List<ActivisItem> tempEvents = ActivisTempEvent.update(data);
                        setEventsOnMap(tempEvents);
                    }

                    @Override
                    public void onParseError(JSONException e) {
                        e.printStackTrace();
                    }
                });

        requester.execute();
    }

    private void setEventsOnMap(final List<ActivisItem> eventsOnMap) {
        if (map != null) {
            map.clear();
            Log.d(TAG, "Setting " + eventsOnMap.size() + " events");
            mClusterManager.clearItems();
            mClusterManager.addItems(eventsOnMap);
            mClusterManager.cluster();
        } else {
            persistentEvents = eventsOnMap;
        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        try {
            long id = Long.parseLong(marker.getTitle());
            final ActivisEvent event = ActivisEvent.findByRemoteId(id);

            View v = getLayoutInflater().inflate(R.layout.map_event_info, null);
            TextView title = (TextView) v.findViewById(R.id.title);
            TextView desc = (TextView) v.findViewById(R.id.desc);
            TextView date = (TextView) v.findViewById(R.id.info_date);
            RemoteImageView image = (RemoteImageView) v.findViewById(R.id.image);

            title.setText(event.getTitle());
            desc.setText(event.getDescription());
            date.setText(TimeUtils.getTimeString(event.getStartDate()) + " - " + TimeUtils.getTimeString(event.getEndDate()));
            image.loadRemoteImage(event.getImageUrl(), 0);

            MaterialDialog.Builder aDialog = DialogFactory.alert(getActivity(), event.getTitle(), v);
            aDialog.positiveText(R.string.open_event_details)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                            Intent eventItent = new Intent(getActivity(), EventActivity.class);
                            eventItent.putExtra("id", event.getServerId());
                            Log.d(TAG, "EVENT ID = " + event.getServerId());
                            startActivity(eventItent);
                        }
                    })
                    .negativeText(android.R.string.cancel)
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        } catch (Exception ignore) {
        } finally {
            moveCamera(marker.getPosition(), 7);
        }



        return true;
    }

    private void moveCamera(LatLng position, float zoom) {
        CameraPosition cp = new CameraPosition.Builder()
                .target(position)
                .zoom(zoom)
                .tilt(map.getCameraPosition().tilt)
                .bearing(map.getCameraPosition().bearing)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
    }

    private void showLocationDialog() {
        View v = getLayoutInflater().inflate(R.layout.location_dialog, null);

        final SwipeRefreshLayout srl = (SwipeRefreshLayout) v.findViewById(R.id.location_refresh);
        srl.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        RecyclerView list = (RecyclerView) v.findViewById(R.id.location_list);
        EditText locationText = (EditText) v.findViewById(R.id.location_name);

        final AddressAdapter addressAdapter = new AddressAdapter(new ArrayList<AddressItem>(), getActivity());
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setAdapter(addressAdapter);

        MaterialDialog.Builder lDialog = DialogFactory.alert(getActivity(), R.string.search_location, v);
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
                    moveCamera(position, 10);
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
                    final Geocoder gc = new Geocoder(getActivity(), Locale.getDefault());
                    return gc.getFromLocationName(search, 20);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_map, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_select_location:
                showLocationDialog();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume() {
        super.onResume();
        IntentFilter i = new IntentFilter(ActivisEventReceiver.ACTION_REFRESH);
        i.addAction(ACTION_CHANGE_CATEGORY);
        i.addAction(ACTION_SELECT_ALL);
        registerReceiver(RECEIVER, i);
    }

    @Override
    public void onPause() {
        unregisterReceiver(RECEIVER);
        super.onPause();
    }

    @Override
    public void onResponse(ActivisResponse<ActivisItem> response) {
        if (!response.hasError() && mClusterManager != null) {
            List<ActivisItem> events = response.getElements();
            setEventsOnMap(events);
        } else {
            delayRequestEvents();
        }
    }
}
