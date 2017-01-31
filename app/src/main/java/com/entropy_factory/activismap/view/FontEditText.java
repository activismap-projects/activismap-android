package com.entropy_factory.activismap.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import com.entropy_factory.activismap.R;

/**
 * Created by ander on 10/03/16.
 */
public class FontEditText extends EditText implements Font {

    private static final String TAG = "FontTextView";

    private int font;

    public FontEditText(Context context) {
        super(context);
        setFont(EXTRA_LIGHT);
    }

    public FontEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(attrs);
    }

    public FontEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(attrs);
    }

    private void initialize(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FontEditText);

        setFont(a.getInteger(R.styleable.FontEditText_fontEditText, LIGHT));
        a.recycle();
    }

    public void setFont(int font) {
        this.font = font;
        setTypeface(null);
    }

    public Typeface getTypeFace() {
        switch (font) {
            case EXTRA_LIGHT:
                return Typefaces.get(getContext(), "fonts/Dosis-ExtraLight.otf");
            case LIGHT:
                return Typefaces.get(getContext(), "fonts/Dosis-Light.otf");
            case SEMI_BOLD:
                return Typefaces.get(getContext(), "fonts/Dosis-SemiBold.otf");
            case BOLD:
                return Typefaces.get(getContext(), "fonts/Dosis-Bold.otf");
            case BLACK:
                return Typefaces.get(getContext(), "fonts/Dosis-ExtraBold.otf");
            case MEDIUM:
                return Typefaces.get(getContext(), "fonts/Dosis-Medium.otf");
            default:
                return Typefaces.get(getContext(), "fonts/Dosis-Regular.otf");
        }
    }
    @Override
    public void setTypeface(Typeface tf) {
        super.setTypeface(getTypeFace());
    }
}
