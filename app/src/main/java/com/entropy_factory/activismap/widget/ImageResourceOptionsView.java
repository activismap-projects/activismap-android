package com.entropy_factory.activismap.widget;

import android.content.Context;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.util.AttributeSet;

import com.entropy_factory.activismap.R;
import com.entropy_factory.activismap.core.db.ActivisCategory;

/**
 * Created by Andersson G. Acosta on 24/01/17.
 */
public class ImageResourceOptionsView extends OptionsView<Integer> {

    private static final String TAG = "CategoryView";

    public ImageResourceOptionsView(Context context) {
        super(context);
        initialize(null);
    }

    public ImageResourceOptionsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(attrs);
    }

    public ImageResourceOptionsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(attrs);
    }

    private void initialize(AttributeSet attrs) {
        clear();


        addOption(R.drawable.ic_gallery, R.string.from_gallery, 1);
        addOption(R.drawable.ic_internet_resource, R.string.from_internet, 2);
        setOptionsByPanel(2);
        //throw new RuntimeException("childs: " + Math.ceil(2 / getOptionsByPanel()));

    }
}
