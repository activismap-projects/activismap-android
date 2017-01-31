package com.entropy_factory.activismap.map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.List;

/**
 * Created by Andersson G. Acosta on 23/01/17.
 */
public class ActivisOverlay extends ItemizedIconOverlay<OverlayItem> {

    private static final String TAG = "ActivisOverlay";

    private OnTouchListener onTouchListener;

    public ActivisOverlay(List<OverlayItem> pList, Drawable pDefaultMarker, OnItemGestureListener<OverlayItem> pOnItemGestureListener, Context pContext) {
        super(pList, pDefaultMarker, pOnItemGestureListener, pContext);
    }

    public ActivisOverlay(List<OverlayItem> pList, OnItemGestureListener<OverlayItem> pOnItemGestureListener, Context pContext) {
        super(pList, pOnItemGestureListener, pContext);
    }

    public ActivisOverlay(Context pContext, List<OverlayItem> pList, OnItemGestureListener<OverlayItem> pOnItemGestureListener) {
        super(pContext, pList, pOnItemGestureListener);
    }

    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event, MapView mapView) {
        if (onTouchListener != null) {
            onTouchListener.onTouch(event, mapView);
        }

        return super.onTouchEvent(event, mapView);
    }

    @Override
    protected boolean onSingleTapUpHelper(int index, OverlayItem item, MapView mapView) {
        return super.onSingleTapUpHelper(index, item, mapView);
    }

    public interface OnTouchListener {
        boolean onTouch(MotionEvent event, MapView mapView);
    }
}
