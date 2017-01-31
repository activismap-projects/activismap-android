package com.entropy_factory.activismap.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.StringDef;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.entropy_factory.activismap.R;
import com.entropy_factory.activismap.core.anotations.LikeStatus;
import com.entropy_factory.activismap.util.Utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.entropy_factory.activismap.core.anotations.LikeStatus.DISLIKE;
import static com.entropy_factory.activismap.core.anotations.LikeStatus.LIKE;
import static com.entropy_factory.activismap.core.anotations.LikeStatus.NEUTRAL;

/**
 * Created by ander on 10/11/16.
 */
public class LikeView extends LinearLayout{

    private static final String TAG = "LikeView";

    public interface OnLikeClickListener {
        void onLikeClick(@LikeStatus String like);
    }

    private OnClickListener LISTENER = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String fireStatus = NEUTRAL;
            switch (status) {
                case DISLIKE:
                    if (v.equals(icon1)) {
                        fireStatus = LIKE;
                    } else {
                        fireStatus = NEUTRAL;
                    }
                    break;
                case NEUTRAL:
                    if (v.equals(icon1)) {
                        fireStatus = DISLIKE;
                    } else {
                        fireStatus = LIKE;
                    }
                    break;
                case LIKE:
                    if (v.equals(icon1)) {
                        fireStatus = DISLIKE;
                    } else {
                        fireStatus = NEUTRAL;
                    }
                    break;
            }

            if (onLikeClickListener != null) {
                onLikeClickListener.onLikeClick(fireStatus);
            }
        }
    };

    private String status = DISLIKE;
    private MaterialTextView icon1;
    private MaterialTextView icon2;
    private OnLikeClickListener onLikeClickListener;

    public LikeView(Context context) {
        super(context);
        initialize(null);
    }

    public LikeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(attrs);
    }

    public LikeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(attrs);
    }

    private void initialize(AttributeSet attrs) {
        if (attrs != null) {
            icon1 = new MaterialTextView(getContext(), attrs);
            icon2 = new MaterialTextView(getContext(), attrs);

            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LikeView);

            int status = a.getInteger(R.styleable.FontTextView_font, -1);
            switch (status) {
                case -1:
                    this.status = DISLIKE;
                    break;
                case 0:
                    this.status = NEUTRAL;
                    break;
                case 1:
                    this.status = LIKE;
            }

            a.recycle();
        } else {
            icon1 = new MaterialTextView(getContext());
            icon2 = new MaterialTextView(getContext());
        }

        icon1.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        LayoutParams icon2Params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if (getOrientation() == HORIZONTAL) {
            icon2Params.setMargins(Utils.convertDpToPixels(getContext(), 3), 0, 0, 0);
        }

        icon2.setLayoutParams(icon2Params);

        setPadding(
                Utils.convertDpToPixels(getContext(), 5),
                Utils.convertDpToPixels(getContext(), 5),
                Utils.convertDpToPixels(getContext(), 5),
                Utils.convertDpToPixels(getContext(), 5));

        icon1.setOnClickListener(LISTENER);
        icon2.setOnClickListener(LISTENER);
        addView(icon1);
        addView(icon2);
        invalidateStatus();
    }

    private void invalidateStatus() {
        switch (status) {
            case DISLIKE:
                icon1.setText(LIKE);
                icon2.setText(NEUTRAL);
                break;
            case LIKE:
                icon1.setText(DISLIKE);
                icon2.setText(NEUTRAL);
                break;
            case NEUTRAL:
                icon1.setText(DISLIKE);
                icon2.setText(LIKE);
                break;
        }
    }

    public LikeView setTextSize(float size) {
        icon1.setTextSize(size);
        icon2.setTextSize(size);
        return this;
    }

    public LikeView setTextColor(int color) {
        icon1.setTextColor(color);
        icon2.setTextColor(color);
        return this;
    }

    public LikeView setTextColor(ColorStateList color) {
        icon1.setTextColor(color);
        icon2.setTextColor(color);
        return this;
    }

    public LikeView setStatus(@LikeStatus String status) {
        this.status = status;
        invalidateStatus();
        return this;
    }

    public LikeView setBgColor(int color) {
        setBackgroundColor(color);
        return this;
    }

    public LikeView setBg(@DrawableRes int resId) {
        setBackgroundResource(resId);
        return this;
    }

    public LikeView setBg(Drawable drawable) {
        setBackgroundDrawable(drawable);
        return this;
    }

    public OnLikeClickListener getOnLikeClickListener() {
        return onLikeClickListener;
    }

    public LikeView setOnLikeClickListener(OnLikeClickListener onLikeClickListener) {
        this.onLikeClickListener = onLikeClickListener;
        return this;
    }
}
