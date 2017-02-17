package com.entropy_factory.activismap.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.entropy_factory.activismap.core.db.ActivisType;

/**
 * Created by ander on 11/11/16.
 */
public class TypeClassificationView extends ClassificationView<ActivisType> {

    private static final String TAG = "TypeClassificationView";

    public TypeClassificationView(Context context) {
        super(context);
        addAll(ActivisType.AVAIALABLE);
    }

    public TypeClassificationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        addAll(ActivisType.AVAIALABLE);
    }

    public TypeClassificationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addAll(ActivisType.AVAIALABLE);
    }
}
