package com.entropy_factory.activismap.ui.base;

import android.view.View;

import com.entropy_factory.activismap.widget.OptionsView;

/**
 * Created by Andersson G. Acosta on 24/01/17.
 */
public abstract class AbstractOptionListener<T> implements OptionsView.OnOptionClickListener<T> {

    private static final String TAG = "AbstractOptionListener";

    @Override
    public void onOptionClick(View v, T option) {

    }

    @Override
    public boolean onOptionLongClick(View v, T option) {
        return false;
    }
}
