package com.entropy_factory.activismap.ui.content;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.entropy_factory.activismap.R;
import com.entropy_factory.activismap.core.activis.ActivisListener;
import com.entropy_factory.activismap.core.activis.ActivisResponse;
import com.entropy_factory.activismap.core.activis.BaseActivis;
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
import com.entropy_factory.activismap.map.ActivisOverlay;
import com.entropy_factory.activismap.ui.base.AdvanceMapFragment;
import com.entropy_factory.activismap.ui.base.FragmentContext;
import com.entropy_factory.activismap.ui.base.MapStateListener;
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
import org.osmdroid.api.IMapController;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ander on 29/09/16.
 */
public class EventStreetMapFragment extends FragmentContext implements ActivisListener<ActivisItem>{

    private static final String TAG = "EventMapFragment";

    private final ActivisEventReceiver RECEIVER = new ActivisEventReceiver() {
        @Override
        public void onRefresh() {
            search();
        }
    };

    private MapView map;
    private IMapController mapController;

    private View timeFilter;
    private TypeClassificationView categoryFilter;
    private BaseActivis baseActivis;
    private Handler handler;
    private SearchActivisEventRequestBuilder request;
    private Request requester;

    private ActivisType searchType;
    private long startDate;
    private long endDate;
    private boolean scrolled = false;
    private boolean zoomed = false;

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_street_map, null);
    }

    @Override
    public void afterInitialize(View view, Bundle savedInstanceState) {
        baseActivis = new BaseActivis(getActivity());
        request = new SearchActivisEventRequestBuilder();

        map = findViewById(R.id.map2);
        map.setMultiTouchControls(true);
        mapController = map.getController();
        mapController.setZoom(3);
        mapController.setCenter(new GeoPoint(0d, 0d));

        map.setMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
                scrolled = true;
                return true;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {
                zoomed = true;
                return true;
            }
        });

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
        search();
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
                baseActivis.searchEvents(EventStreetMapFragment.this);
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

        request.setArea(map.getProjection().getBoundingBox());

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
        Log.d(TAG, "Setting " + eventsOnMap.size() + " events");
        ArrayList<OverlayItem> anotherOverlayItemArray = new ArrayList<>();

        for (ActivisItem ai : eventsOnMap) {
            anotherOverlayItemArray.add(ai.toOverlayItem());
        }

        ActivisOverlay anotherItemizedIConOVerlay = new ActivisOverlay(getActivity(), anotherOverlayItemArray, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemSingleTapUp(int index, OverlayItem item) {
                long id = Long.parseLong(item.getUid());
                final ActivisEvent event = ActivisEvent.findByRemoteId(id);

                Intent eventIntent = new Intent(getActivity(), EventActivity.class);
                eventIntent.putExtra("id", id);
                Log.d(TAG, "EVENT ID = " + event.getServerId());
                startActivity(eventIntent);

                return false;
            }

            @Override
            public boolean onItemLongPress(int index, OverlayItem item) {
                return false;
            }
        });

        anotherItemizedIConOVerlay.setOnTouchListener(new ActivisOverlay.OnTouchListener() {
            @Override
            public boolean onTouch(MotionEvent event, MapView mapView) {
                Log.e(TAG, "OnTouched");
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        categoryFilter.post(new Runnable() {
                            @Override
                            public void run() {
                                categoryFilter.animate().translationX(100).setDuration(300);
                            }
                        });

                        timeFilter.post(new Runnable() {
                            @Override
                            public void run() {
                                timeFilter.animate().translationY(100).setDuration(300);;
                            }
                        });


                        if (scrolled || zoomed) {
                            scrolled = false;
                            zoomed = false;
                            search();
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        categoryFilter.animate().translationX(0).setDuration(300);
                        timeFilter.animate().translationY(0).setDuration(300);
                        if (scrolled || zoomed) {
                            scrolled = false;
                            zoomed = false;
                            search();
                        }
                        return true;
                }

                return false;
            }
        });

        anotherItemizedIConOVerlay.setEnabled(true);

        map.getOverlays().add(anotherItemizedIConOVerlay);

    }


    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(RECEIVER, new IntentFilter(ActivisEventReceiver.ACTION_REFRESH));
    }

    @Override
    public void onPause() {
        unregisterReceiver(RECEIVER);
        super.onPause();
    }

    @Override
    public void onResponse(ActivisResponse<ActivisItem> response) {
        if (!response.hasError()) {
            List<ActivisItem> events = response.getElements();
            setEventsOnMap(events);
        } else {
            delayRequestEvents();
        }
    }
}
