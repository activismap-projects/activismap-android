package com.entropy_factory.activismap.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.entropy_factory.activismap.R;
import com.entropy_factory.activismap.util.Utils;

import java.util.ArrayList;
import java.util.List;

import static android.view.Gravity.CENTER;

/**
 * Created by Andersson G. Acosta on 20/01/17.
 */
public class OptionsView<T> extends LinearLayout {

    private static final String TAG = "OptionsView";

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private final OnTouchListener ITEM_LISTENER = new OnTouchListener() {

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            currentView = view;
            return GESTURE_DETECTOR.onTouchEvent(event);
        }
    };

    private final GestureDetector GESTURE_DETECTOR = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {

            Log.d(TAG, "View is pressed: " + currentView.isPressed());
            if (isMultipleSelectionEnabled() && (!isSelectionsReached() || currentView.isPressed())) {
                selections = currentView.isPressed() ? selections - 1 : selections + 1;

                if (isMultipleSelectionEnabled()) {
                    currentView.setPressed(!currentView.isPressed());
                }

                Option<T> option = (Option<T>) currentView.getTag();
                if (onOptionClickListener != null) {
                    onOptionClickListener.onOptionClick(currentView, option);
                }

                return true;
            } else {
                Option<T> option = (Option<T>) currentView.getTag();
                if (onOptionClickListener != null) {
                    onOptionClickListener.onOptionClick(currentView, option);
                }
            }

            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Option<T> option = (Option<T>) currentView.getTag();
            if (onOptionClickListener != null) {
                onOptionClickListener.onOptionLongClick(currentView, option);
            }
        }
    });

    private List<Option<T>> options;
    private OnOptionClickListener<Option<T>> onOptionClickListener;
    private View currentView;
    private int optionsByPanel = 4;
    private int itemSpacing;
    private int itemPadding;
    private int maxSelection;
    private int selections = 0;

    public OptionsView(Context context) {
        super(context);
        initialize(null);
    }

    public OptionsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(attrs);
    }

    public OptionsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(attrs);
    }

    private void initialize(AttributeSet attrs) {
        itemSpacing = Utils.convertDpToPixels(getContext(), 5);

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.OptionsView);

            itemSpacing = a.getDimensionPixelSize(R.styleable.OptionsView_itemSpacing, Utils.convertDpToPixels(getContext(), 5));
            itemPadding = a.getDimensionPixelSize(R.styleable.OptionsView_itemPadding, 0);
            maxSelection = a.getInt(R.styleable.OptionsView_maxSelection, 1);
            a.recycle();
        }

        setOrientation(VERTICAL);
        options = new ArrayList<>();

        addOption(R.drawable.ic_amnesty, "Option 1");
        addOption(R.drawable.ic_animal, "Option 2");
        addOption(R.drawable.ic_anticapitalism, "Option 3");
        addOption(R.drawable.ic_antimilitarism, "Option 4");
    }

    private void invalidateView() {
        removeAllViews();

        int rows = (int) Math.ceil(options.size() / optionsByPanel);

        int rowOrientation = getOrientation() == VERTICAL ? HORIZONTAL : VERTICAL;
        LayoutParams rowParams;
        LayoutParams optionParams;

        if (rowOrientation == HORIZONTAL) {
            rowParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0.1f);
            optionParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 0.1f);
        } else{
            rowParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 0.1f);
            optionParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0.1f);
        }

        optionParams.setMargins(itemSpacing, itemSpacing, itemSpacing, itemSpacing);

        int optionIndex = 0;
        for (int x = 0; x < rows; x++) {
            LinearLayout rowLayout = new LinearLayout(getContext());
            rowLayout.setOrientation(rowOrientation);
            rowLayout.setLayoutParams(rowParams);

            for (int y = 0; y < optionsByPanel; y++) {
                if (optionIndex >= options.size()) {
                    break;
                }

                final Option<T> option = options.get(optionIndex);

                LinearLayout optionLayout = new LinearLayout(getContext());
                optionLayout.setOrientation(VERTICAL);
                optionLayout.setLayoutParams(optionParams);
                optionLayout.setPadding(itemPadding, itemPadding , itemPadding, itemPadding);
                if (maxSelection > 1) {
                    optionLayout.setBackgroundResource(R.drawable.view_pressed_effect);
                }

                boolean addOption = false;
                boolean hasText = !TextUtils.isEmpty(option.text);
                if (option.icon != null) {
                    ImageView iconView = new ImageView(getContext());
                    iconView.setImageDrawable(option.icon);
                    iconView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, hasText ? ViewGroup.LayoutParams.WRAP_CONTENT : ViewGroup.LayoutParams.MATCH_PARENT));
                    optionLayout.addView(iconView);
                    addOption = true;
                }

                if (hasText) {
                    TextView textView = new TextView(getContext());
                    textView.setText(option.text);
                    textView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    textView.setGravity(CENTER);
                    optionLayout.addView(textView);
                    addOption = true;
                }

                if (addOption) {
                    optionLayout.setPressed(false);
                    optionLayout.setOnTouchListener(ITEM_LISTENER);
                    optionLayout.setTag(option);
                    rowLayout.addView(optionLayout);
                    option.id = Integer.valueOf(optionIndex);
                }

                optionIndex++;

            }

            addView(rowLayout);
        }
    }

    public void setOptionsByPanel(int optionsByPanel) {
        this.optionsByPanel = optionsByPanel;
        invalidateView();
    }

    public int getOptionsByPanel() {
        return optionsByPanel;
    }

    public void setOnOptionClickListener(OnOptionClickListener<Option<T>> onOptionClickListener) {
        this.onOptionClickListener = onOptionClickListener;
    }

    public void clear() {
        options.clear();
        invalidateView();
    }

    public void addOption(Option<T> option) {
        options.add(option);
        invalidateView();
    }

    public void addOption(Drawable icon, CharSequence text, T object) {
        Option<T> option = new Option<>();
        option.icon = icon;
        option.text = text;
        option.object = object;
        addOption(option);
    }

    public void addOption(Drawable icon, CharSequence text) {
        addOption(icon, text, null);
    }

    public void addOption(@DrawableRes int icon, CharSequence text) {
        addOption(getResources().getDrawable(icon), text, null);
    }

    public void addOption(@DrawableRes int icon, CharSequence text, T object) {
        addOption(getResources().getDrawable(icon), text, object);
    }

    public void addOption(Drawable icon, @StringRes int text) {
        addOption(icon, getContext().getString(text));
    }

    public void addOption(@DrawableRes int icon, @StringRes int text) {
        addOption(getResources().getDrawable(icon), getContext().getString(text));
    }

    public void addOption(@DrawableRes int icon, @StringRes int text, T object) {
        addOption(getResources().getDrawable(icon), getContext().getString(text), object);
    }

    public int getOptionsCount() {
        return options.size();
    }

    public int getMaxSelection() {
        return maxSelection;
    }

    public void setMaxSelection(int maxSelection) {
        if (maxSelection < 1) {
            maxSelection = 1;
        }

        this.maxSelection = maxSelection;
    }

    public boolean isMultipleSelectionEnabled() {
        return maxSelection > 1;
    }

    public int getSelections() {
        return selections;
    }

    public boolean isSelectionsReached() {
        return selections == maxSelection;
    }

    public static class Option<T> {
        public Drawable icon;
        public CharSequence text;
        public T object;
        public int id;
    }

    public interface OnOptionClickListener<T> {
        void onOptionClick(View v, T option);
        boolean onOptionLongClick(View v, T option);
    }
}
