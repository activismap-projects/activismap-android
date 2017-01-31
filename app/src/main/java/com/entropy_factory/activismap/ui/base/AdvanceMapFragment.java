package com.entropy_factory.activismap.ui.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by ander on 29/09/16.
 */
public class AdvanceMapFragment extends SupportMapFragment {

    private static final String TAG = "AdvanceMapFragment";

    private View mOriginalContentView;
    private TouchableWrapper mTouchView;

    public void setTouchListener(TouchableWrapper.OnTouchListener onTouchListener) {
        mTouchView.setTouchListener(onTouchListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        mOriginalContentView = super.onCreateView(inflater, parent,
                savedInstanceState);

        mTouchView = new TouchableWrapper(getActivity());
        mTouchView.addView(mOriginalContentView);

        return mTouchView;
    }

    @Override
    public View getView() {
        return mOriginalContentView;
    }

}
