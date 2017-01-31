package com.entropy_factory.activismap.widget;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.widget.CardView;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.entropy_factory.activismap.R;
import com.entropy_factory.activismap.util.Utils;

import java.util.ArrayList;
import java.util.List;

import static android.view.Gravity.CENTER;

/**
 * Created by Andersson G. Acosta on 23/01/17.
 */
public class TimeSelectorView extends CardView {

    private static final String TAG = "TimeSelector";

    private List<TimeOption> timeOptions;
    private LinearLayout container;
    private OnTimeClick onTimeClick;
    private View lastPressedView;

    public TimeSelectorView(Context context) {
        super(context);
        initialize(null);
    }

    public TimeSelectorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(null);
    }

    public TimeSelectorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(null);
    }

    private void initialize(AttributeSet attrs) {
        setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setRadius(Utils.convertDpToPixels(getContext(), 5));

        timeOptions = new ArrayList<>();

        container = new LinearLayout(getContext());
        container.setOrientation(LinearLayout.HORIZONTAL);
        container.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(container);

        addOption("Week", 1, 2);
        addOption("Month", 1, 2);
        addOption("Year", 1, 2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_UP:
                return true;
        }

        return false;
    }

    private void invalidateView() {
        container.removeAllViews();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f);

        int length = timeOptions.size();

        for (int x = 0; x < length; x++) {
            final TimeOption to = timeOptions.get(x);
            TextView tView = new TextView(getContext());
            tView.setLayoutParams(params);
            tView.setGravity(CENTER);
            tView.setText(to.text);
            tView.setPadding(0, Utils.convertDpToPixels(getContext(), 10), 0, Utils.convertDpToPixels(getContext(), 10));
            tView.setBackgroundResource(R.drawable.view_pressed_effect);

            tView.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    // show interest in events resulting from ACTION_DOWN
                    if (event.getAction() == MotionEvent.ACTION_DOWN) return true;

                    // don't handle event unless its ACTION_UP so "doSomething()" only runs once.
                    if (event.getAction() != MotionEvent.ACTION_UP) return false;


                    view.setPressed(true);
                    if (onTimeClick != null) {
                        onTimeClick.onTimeClick(view, to);
                    }

                    if (lastPressedView != null) {
                        lastPressedView.setPressed(false);
                    }

                    lastPressedView = view;

                    return true;
                }
            });

            container.addView(tView);
        }
    }

    public void clear() {
        timeOptions.clear();
        invalidateView();
    }

    public void setOnTimeClick(OnTimeClick onTimeClick) {
        this.onTimeClick = onTimeClick;
    }

    public void addOption(CharSequence text, long startDate, long endDate) {
        TimeOption option = new TimeOption();
        option.endDate = endDate;
        option.startDate = startDate;
        option.text = text;
        timeOptions.add(option);
        invalidateView();
    }

    public void addOption(@StringRes int resId, long startDate, long endDate) {
        addOption(getResources().getString(resId), startDate, endDate);
    }

    public interface OnTimeClick {
        void onTimeClick(View v, TimeOption timeOption);
    }

    public class TimeOption {
        public long startDate;
        public long endDate;
        public CharSequence text;
    }
}
