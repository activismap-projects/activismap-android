package com.entropy_factory.activismap.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by ander on 10/03/16.
 */
public class BesomTextView extends TextView {

    private static final String TAG = "FontTextView";

    public BesomTextView(Context context) {
        super(context);
    }

    public BesomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BesomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Typeface getTypeFace() {
        return Typefaces.get(getContext(), "fonts/besom.ttf");
    }

    @Override
    public void setTypeface(Typeface tf) {
        super.setTypeface(getTypeFace());
    }
}
