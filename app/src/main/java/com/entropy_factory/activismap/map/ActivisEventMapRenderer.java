package com.entropy_factory.activismap.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatDelegate;

import com.entropy_factory.activismap.core.item.ActivisItem;
import com.entropy_factory.activismap.util.Utils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import static android.R.attr.id;
import static com.entropy_factory.activismap.app.ActivisApplication.INSTANCE;

/**
 * Created by ander on 4/10/16.
 */
public class ActivisEventMapRenderer extends DefaultClusterRenderer<ActivisItem> {

    private static final String TAG = "ActivisEventMapRenderer";

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private BitmapDescriptor icon;
    private Context context;

    public ActivisEventMapRenderer(Context context, GoogleMap map, ClusterManager<ActivisItem> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
    }

    @Override
    protected void onBeforeClusterItemRendered(ActivisItem item, MarkerOptions markerOptions) {

        item.save();
        markerOptions.title(String.valueOf(item.getServerId()));

        Drawable vectorDrawable = context.getResources().getDrawable(item.getType().getIcon());

        int h = ((int) Utils.convertDpToPixels(context, 25));
        int w = ((int) Utils.convertDpToPixels(context, 25));
        vectorDrawable.setBounds(0, 0, w, h);
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        vectorDrawable.draw(canvas);

        icon = BitmapDescriptorFactory.fromBitmap(bm);
        markerOptions.icon(icon);

        super.onBeforeClusterItemRendered(item, markerOptions);
    }
}
