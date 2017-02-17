package com.entropy_factory.activismap.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.entropy_factory.activismap.core.db.ActivisCategory;

/**
 * Created by Andersson G. Acosta on 24/01/17.
 */
public class CategoryView extends OptionsView<ActivisCategory> {

    private static final String TAG = "CategoryView";

    public CategoryView(Context context) {
        super(context);
        initialize(null);
    }

    public CategoryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(attrs);
    }

    public CategoryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(attrs);
    }

    private void initialize(AttributeSet attrs) {
        clear();
        ActivisCategory[] all = ActivisCategory.AVAILABLE;

        for (ActivisCategory ac : all) {
            addOption(ac.getIcon(), null, ac);
        }
    }
}
