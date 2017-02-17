package com.entropy_factory.activismap.core.db;

import android.content.res.Resources;

import com.entropy_factory.activismap.R;
import com.entropy_factory.activismap.app.ActivisApplication;
import com.entropy_factory.activismap.core.activis.Activis;
import com.entropy_factory.activismap.widget.ActivisView;

/**
 * Created by ander on 30/09/16.
 */
public enum ActivisType implements ActivisView{
    ASSEMBLY, TALKING, FESTIVAL, POPULAR_PARTY, LEARNING, STRIKE, KAFETA, MANIFESTATION, MARKET, UNKNOWN;

    public static ActivisType[] AVAIALABLE = {ASSEMBLY, TALKING, FESTIVAL, POPULAR_PARTY, LEARNING, STRIKE, KAFETA, MANIFESTATION, MARKET};

    public int getIcon() {
        switch (this) {
            case ASSEMBLY:
                return R.drawable.ic_assembly;
            case TALKING:
                return R.drawable.ic_talking;
            case FESTIVAL:
                return R.drawable.ic_festival;
            case POPULAR_PARTY:
                return R.drawable.ic_popular_party;
            case LEARNING:
                return R.drawable.ic_learning;
            case STRIKE:
                return R.drawable.ic_strike;
            case KAFETA:
                return R.drawable.ic_kafeta;
            case MANIFESTATION:
                return R.drawable.ic_manifestation;
            case MARKET:
                return R.drawable.ic_market;
            default:
                return 0;

        }
    }

    public int getTypeResource() {
        switch (this) {
            case ASSEMBLY:
                return R.array.assembly;
            case TALKING:
                return R.array.talking;
            case FESTIVAL:
                return R.array.festival;
            case POPULAR_PARTY:
                return R.array.popular_party;
            case LEARNING:
                return R.array.learning;
            case STRIKE:
                return R.array.strike;
            case KAFETA:
                return R.array.kafeta;
            case MANIFESTATION:
                return R.array.manifestation;
            case MARKET:
                return R.array.market;
            default:
                return 0;
        }
    }

    public String[] getActivisClassification() {
        return ActivisApplication.INSTANCE.getResources().getStringArray(getTypeResource());
    }

    public static ActivisType[] valueOf(String[] types) {
        ActivisType[] activisTypes = new ActivisType[types.length];
        for (int x = 0; x < types.length; x++) {
            ActivisType type = UNKNOWN;

            try {
                type = valueOf(types[x]);
            } catch (Exception ignored) {}
            activisTypes[x] = type;
        }

        return activisTypes;
    }
}
