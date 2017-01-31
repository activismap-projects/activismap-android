package com.entropy_factory.activismap.util;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.entropy_factory.activismap.ui.base.AbstractOptionListener;

/**
 * Created by Andersson G. Acosta on 31/01/17.
 */
public abstract class DialogOptionLinstener<T> extends AbstractOptionListener<T> {

    private static final String TAG = "DialogOptionClickLinstener";

    private AlertDialog dialog;

    public DialogOptionLinstener(AlertDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public final boolean onOptionLongClick(View v, T option) {
        return onOptionLongClick(dialog, v, option);
    }

    @Override
    public final  void onOptionClick(View v, T option) {
        onOptionClick(dialog, v, option);
    }

    public boolean onOptionLongClick(DialogInterface dialogInterface, View v, T option) {
        return false;
    }

    public void onOptionClick(DialogInterface dialogInterface, View v, T option) {

    }
}
