package com.entropy_factory.activismap.ui.content;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.entropy_factory.activismap.R;
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
import com.entropy_factory.activismap.map.ActivisEventMapAdapter;
import com.entropy_factory.activismap.map.ActivisEventMapRenderer;
import com.entropy_factory.activismap.ui.base.AdvanceMapFragment;
import com.entropy_factory.activismap.ui.base.FragmentContext;
import com.entropy_factory.activismap.ui.base.MapStateListener;
import com.entropy_factory.activismap.util.TimeUtils;
import com.entropy_factory.activismap.widget.TimeSelectorView;
import com.entropy_factory.activismap.widget.ClassificationView;
import com.entropy_factory.activismap.widget.TypeClassificationView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Marker;
import com.zanjou.http.request.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.entropy_factory.activismap.core.receiver.ActivisEventReceiver.ACTION_CHANGE_CATEGORY;

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
    private long startDate;
    private long endDate;
    private List<ActivisItem> persistentEvents;


    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_map, null);
    }

    @Override
    public void afterInitialize(View view, Bundle savedInstanceState) {
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
                return false;
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.e(TAG, "OnMapReady");
        map = googleMap;
        if (map != null) {
            map.clear();
            map.setOnMarkerClickListener(this);

            mClusterManager = new ActivisClusterManager(getActivity(), map);

            ActivisEventMapRenderer renderer = new ActivisEventMapRenderer(getActivity(), map, mClusterManager);
            ActivisEventMapAdapter adapter = new ActivisEventMapAdapter(getActivity());

            mClusterManager.setRenderer(renderer);
            map.setOnCameraChangeListener(mClusterManager);
            map.setInfoWindowAdapter(adapter);

            map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    long id = Long.parseLong(marker.getTitle());
                    final ActivisEvent event = ActivisEvent.findByRemoteId(id);

                    Intent eventItent = new Intent(getActivity(), EventActivity.class);
                    eventItent.putExtra("id", id);
                    Log.d(TAG, "EVENT ID = " + event.getServerId());
                    startActivity(eventItent);
                }
            });
            MapStateListener msl = new MapStateListener(map, smf, getActivity()) {

                @Override
                public void onMapTouched() {
                    Log.d(TAG, "OnMapTouched");
                    categoryFilter.animate().translationX(100).setDuration(300);
                    timeFilter.animate().translationY(100).setDuration(300);
                }

                @Override
                public void onMapReleased() {
                    Log.d(TAG, "onMapReleased");
                    categoryFilter.animate().translationX(0).setDuration(300);
                    timeFilter.animate().translationY(0).setDuration(300);

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
        marker.showInfoWindow();
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter i = new IntentFilter(ActivisEventReceiver.ACTION_REFRESH);
        i.addAction(ACTION_CHANGE_CATEGORY);
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
