package com.entropy_factory.activismap.widget;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.entropy_factory.activismap.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ander on 27/07/16.
 */
public class ProfileImageView extends CircleImageView {

    private static final String TAG = "RemoteImageView";


    public ProfileImageView(Context context) {
        super(context);
    }

    public ProfileImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProfileImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void loadRemoteImage(String url, @DrawableRes final int defaultIcon) {

        Glide.with(getContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(defaultIcon)
                .error(defaultIcon)
                .dontAnimate()
                .fitCenter()
                .into(this);
    }

    public void loadRemoteImage(String url) {
        loadRemoteImage(url, R.mipmap.ic_launcher);
    }

    @Override
    public void setAdjustViewBounds(boolean adjustViewBounds) {

    }
}
