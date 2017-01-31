package com.entropy_factory.activismap.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.entropy_factory.activismap.R;
import com.entropy_factory.activismap.core.activis.Activis;
import com.entropy_factory.activismap.core.anotations.CategoryIndex;
import com.entropy_factory.activismap.core.db.ActivisCategory;
import com.entropy_factory.activismap.core.db.ActivisType;
import com.entropy_factory.activismap.core.item.ActivisItem;
import com.entropy_factory.activismap.util.Utils;

import java.util.ArrayList;

import static com.entropy_factory.activismap.core.anotations.CategoryIndex.AUTO_INDEX;
import static com.entropy_factory.activismap.core.anotations.CategoryIndex.FIRST_CATEGORY;
import static com.entropy_factory.activismap.core.anotations.CategoryIndex.NO_INDEX;
import static com.entropy_factory.activismap.core.anotations.CategoryIndex.SECOND_CATEGORY;
import static com.entropy_factory.activismap.core.anotations.CategoryIndex.THIRD_CATEGORY;

/**
 * Created by ander on 10/11/16.
 */
public class ItemClassificationView extends LinearLayout {

    private static final String TAG = "ClassificationView";

    public interface OnClassificationClickListener {
        void onTypeClick(View v, ActivisType type);
        void onCategoryClick(View v, ActivisCategory category);
    }

    private final OnClickListener ONCLICK_LISTENER = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ActivisType type = null;
            ActivisCategory category = null;
            if (v.equals(typeView)) {
                type = ItemClassificationView.this.type;
            } else if (v.equals(categoryView1)) {
                category = ItemClassificationView.this.category1;
            } else if (v.equals(categoryView2)) {
                category = ItemClassificationView.this.category2;
            } else if (v.equals(categoryView3)) {
                category = ItemClassificationView.this.category3;
            }

            if (type != null && onClassificationClickListener != null) {
                onClassificationClickListener.onTypeClick(v, type);
            }

            if (category != null && onClassificationClickListener != null) {
                onClassificationClickListener.onCategoryClick(v, category);
            }
        }
    };

    private ImageView typeView;
    private ImageView categoryView1;
    private ImageView categoryView2;
    private ImageView categoryView3;

    private ActivisType type;
    private ActivisCategory category1;
    private ActivisCategory category2;
    private ActivisCategory category3;

    private OnClassificationClickListener onClassificationClickListener;

    public ItemClassificationView(Context context) {
        super(context);
        initialize(null);
    }

    public ItemClassificationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(attrs);
    }

    public ItemClassificationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(attrs);
    }

    private void initialize(AttributeSet attrs) {
        typeView = new ImageView(getContext());
        categoryView1 = new ImageView(getContext());
        categoryView2 = new ImageView(getContext());
        categoryView3 = new ImageView(getContext());

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ItemClassificationView);

            int icSize = a.getDimensionPixelSize(R.styleable.ItemClassificationView_itemSize, getWrapDimension());
            int icMargin = a.getDimensionPixelSize(R.styleable.ItemClassificationView_itemSpacing, Utils.convertDpToPixels(getContext(), 3));

            LayoutParams imageParams = new LayoutParams(icSize, icSize);

            if (getOrientation() == HORIZONTAL) {
                imageParams.setMargins(icMargin, 0, icMargin, 0);
            } else {
                imageParams.setMargins(0, icMargin, 0, icMargin);
            }

            typeView.setLayoutParams(imageParams);
            categoryView1.setLayoutParams(imageParams);
            categoryView2.setLayoutParams(imageParams);
            categoryView3.setLayoutParams(imageParams);

            a.recycle();
        }

        typeView.setOnClickListener(ONCLICK_LISTENER);
        categoryView1.setOnClickListener(ONCLICK_LISTENER);
        categoryView2.setOnClickListener(ONCLICK_LISTENER);
        categoryView3.setOnClickListener(ONCLICK_LISTENER);

        type = ActivisType.ASSEMBLY;
        category1 = ActivisCategory.AMNESTY;
        category2 = ActivisCategory.ANIMAL_MOV;
        category3 = ActivisCategory.FAIR_TRADE;
        invalidateViews();
    }

    public void clear() {
        type = null;
        category1 = null;
        category2 = null;
        category3 = null;
        invalidateViews();
    }

    private void invalidateViews() {
        removeAllViews();
        if (type != null) {
            typeView.setImageResource(type.getIcon());
            addView(typeView);
        }

        if (category1 != null) {
            categoryView1.setImageResource(category1.getIcon());
            addView(categoryView1);
        }

        if (category2 != null) {
            categoryView2.setImageResource(category2.getIcon());
            addView(categoryView2);
        }

        if (category3 != null) {
            categoryView3.setImageResource(category3.getIcon());
            addView(categoryView3);
        }
    }

    private int getWrapDimension() {
        return LayoutParams.WRAP_CONTENT;
    }

    public ActivisType getType() {
        return type;
    }

    public ItemClassificationView setType(ActivisType type) {
        this.type = type;
        invalidateViews();
        return this;
    }

    public ItemClassificationView setCategories(ActivisCategory category1, ActivisCategory category2, ActivisCategory category3) {
        this.category1 = category1;
        this.category2 = category2;
        this.category3 = category3;
        invalidateViews();
        return this;
    }

    public ItemClassificationView setCategory(ActivisCategory category) {
        return setCategory(category, AUTO_INDEX);
    }

    public ItemClassificationView setCategory(ActivisCategory category, @CategoryIndex int index) {
        if (!containsCategory(category)) {
            switch (index) {
                case FIRST_CATEGORY:
                    this.category1 = category;
                    break;
                case SECOND_CATEGORY:
                    this.category2 = category;
                    break;
                case THIRD_CATEGORY:
                    this.category3 = category;
                    break;
                case AUTO_INDEX:
                    if (category1 == null) {
                        category1 = category;
                    } else if (category2 == null) {
                        category2 = category;
                    } else if (category3 == null) {
                        category3 = category;
                    }
                    break;
            }
        }
        invalidateViews();

        return this;
    }

    public ItemClassificationView removeCategory(@CategoryIndex int index) {
        switch (index) {
            case FIRST_CATEGORY:
                this.category1 = null;
                break;
            case SECOND_CATEGORY:
                this.category2 = null;
                break;
            case THIRD_CATEGORY:
                this.category3 = null;
                break;
        }

        invalidateViews();
        return this;
    }

    public ItemClassificationView removeCategory(ActivisCategory category) {
        return removeCategory(getCategoryIndex(category));
    }

    public int getCategoryLength() {
        return getCategories().length;
    }

    public ActivisCategory[] getCategories() {
        ArrayList<ActivisCategory> list = new ArrayList<>();

        if (category1 != null) {
            list.add(category1);
        }

        if (category2 != null) {
            list.add(category2);
        }

        if (category3 != null) {
            list.add(category3);
        }

        return list.toArray(new ActivisCategory[]{});
    }

    public OnClassificationClickListener getOnClassificationClickListener() {
        return onClassificationClickListener;
    }

    public void setOnClassificationClickListener(OnClassificationClickListener onClassificationClickListener) {
        this.onClassificationClickListener = onClassificationClickListener;
    }

    public void setFrom(ActivisItem item) {
        type = item.getType();
        ActivisCategory[] categories = item.getCategories();

        if (categories.length >= 3) {
            category1 = categories[0];
            category2 = categories[1];
            category3 = categories[2];
        } else if (categories.length == 2) {
            category1 = categories[0];
            category2 = categories[1];
        } else if (categories.length == 1) {
            category1 = categories[0];
        }

        invalidateViews();
    }

    @CategoryIndex
    public int getCategoryIndex(ActivisCategory category) {
        if (category1 != null && category1.equals(category)) {
            return FIRST_CATEGORY;
        } else if (category2 != null && category2.equals(category)) {
            return SECOND_CATEGORY;
        } else if (category3 != null && category3.equals(category)) {
            return THIRD_CATEGORY;
        }

        return NO_INDEX;
    }

    public boolean containsCategory(ActivisCategory category) {
        return getCategoryIndex(category) >= 0;
    }

    public boolean hasCategories() {
        return getCategoryLength() > 0;
    }

    public boolean hasType() {
        return getType() != null;
    }
}
