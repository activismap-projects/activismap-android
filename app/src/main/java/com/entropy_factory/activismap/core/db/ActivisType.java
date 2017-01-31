package com.entropy_factory.activismap.core.db;

import android.content.res.Resources;

import com.entropy_factory.activismap.R;
import com.entropy_factory.activismap.app.ActivisApplication;
import com.entropy_factory.activismap.widget.ActivisView;

/**
 * Created by ander on 30/09/16.
 */
public enum ActivisType implements ActivisView{
    ASSEMBLY, LEARNING, TALKING, MANIFESTATION, FESTIVAL, MARKET;

    public int getIcon() {
        switch (this) {
            case ASSEMBLY:
                return R.drawable.ic_assembly;
            case LEARNING:
                return R.drawable.ic_learning;
            case TALKING:
                return R.drawable.ic_talking;
            case MANIFESTATION:
                return R.drawable.ic_manifestation;
            case FESTIVAL:
                return R.drawable.ic_festival;
            case MARKET:
                return R.drawable.ic_market;
            default:
                return 0;

        }
    }

    public int getTypeResource() {
        Resources resources = ActivisApplication.INSTANCE.getResources();
        String name = toString().toLowerCase();
        return resources.getIdentifier(name, "array", ActivisApplication.INSTANCE.getPackageName());
    }

    public String[] getActivisClassification() {
        return ActivisApplication.INSTANCE.getResources().getStringArray(getTypeResource());
    }

    public static ActivisType[] valueOf(String[] categories) {
        ActivisType[] activisTypes = new ActivisType[categories.length];
        for (int x = 0; x < categories.length; x++) {
            activisTypes[x] = valueOf(categories[x]);
        }

        return activisTypes;
    }
}
