package com.entropy_factory.activismap.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.entropy_factory.activismap.R;
import com.entropy_factory.activismap.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andersson G. Acosta on 20/01/17.
 */
public class OptionsView<T> extends LinearLayout {

    private static final String TAG = "OptionsView";

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private int optonsByPanel = 4;
    private List<Option<T>> options;
    private int itemSpacing;
    private OnOptionClickListener<Option<T>> onOptionClickListener;
    private int itemPadding;
    private boolean multipleSelection;

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
            multipleSelection = a.getBoolean(R.styleable.OptionsView_multipleSelection, false);
            a.recycle();
        }

        setOrientation(VERTICAL);
        options = new ArrayList<>();

        addOption(R.drawable.ic_amnesty, "Option 1");
        addOption(R.drawable.ic_animal_mov, "Option 2");
        addOption(R.drawable.ic_anticapitalism, "Option 3");
        addOption(R.drawable.ic_antimilitarism, "Option 4");
    }

    private void invalidateView() {
        removeAllViews();

        int rows = (int) Math.ceil(options.size() / optonsByPanel);

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

            for (int y = 0; y < optonsByPanel; y++) {
                if (optionIndex >= options.size()) {
                    break;
                }

                final Option<T> option = options.get(optionIndex);

                LinearLayout optionLayout = new LinearLayout(getContext());
                optionLayout.setOrientation(VERTICAL);
                optionLayout.setLayoutParams(optionParams);
                optionLayout.setPadding(itemPadding, itemPadding , itemPadding, itemPadding);
                if (multipleSelection) {
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
                    textView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    optionLayout.addView(textView);
                    addOption = true;
                }

                if (addOption) {
                    optionLayout.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (multipleSelection) {
                                view.setPressed(!view.isPressed());
                            }

                            if (onOptionClickListener != null) {
                                onOptionClickListener.onOptionClick(view, option);
                            }
                        }
                    });

                    optionLayout.setOnLongClickListener(new OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            return onOptionClickListener != null && onOptionClickListener.onOptionLongClick(view, option);

                        }
                    });
                    rowLayout.addView(optionLayout);
                    option.id = Integer.valueOf(optionIndex);
                }

                optionIndex++;

            }

            addView(rowLayout);
        }
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

    public class Option<T> {
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
