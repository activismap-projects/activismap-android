package com.entropy_factory.activismap.core.db;

import android.content.res.Resources;

import com.entropy_factory.activismap.R;
import com.entropy_factory.activismap.app.ActivisApplication;
import com.entropy_factory.activismap.widget.ActivisView;

import static com.entropy_factory.activismap.app.ActivisApplication.INSTANCE;

/**
 * Created by ander on 30/09/16.
 */
public enum ActivisCategory implements ActivisView {

    AMNESTY, ANTICAPITALISM, ANTIMILITARISM, ASYLUM, FAIR_TRADE, EDUCATION, INTERNET,
    TERRITORY_DEFENSE, ECOLOGY, FEMINISM, PRO_INDEPENDENCE, INTEGRATION, MEMORY, ANIMAL_MOV, POLICY, HEALTH,
    SOLIDARITY, TREATED;

    public int getIcon() {
        switch (this) {
            case AMNESTY:
                return R.drawable.ic_amnesty;
            case ANTICAPITALISM:
                return R.drawable.ic_anticapitalism;
            case ANTIMILITARISM:
                return R.drawable.ic_antimilitarism;
            case ASYLUM:
                return R.drawable.ic_asylum;
            case FAIR_TRADE:
                return R.drawable.ic_fair_trade;
            case EDUCATION:
                return R.drawable.ic_education;
            case INTERNET:
                return R.drawable.ic_internet;
            case TERRITORY_DEFENSE:
                return R.drawable.ic_territory_defense;
            case ECOLOGY:
                return R.drawable.ic_ecology;
            case FEMINISM:
                return R.drawable.ic_feminism;
            case PRO_INDEPENDENCE:
                return R.drawable.ic_proindependence;
            case INTEGRATION:
                return R.drawable.ic_integration;
            case MEMORY:
                return R.drawable.ic_memory;
            case ANIMAL_MOV:
                return R.drawable.ic_animal_mov;
            case POLICY:
                return R.drawable.ic_policy;
            case HEALTH:
                return R.drawable.ic_health;
            case SOLIDARITY:
                return R.drawable.ic_solidarity;
            case TREATED:
                return R.drawable.ic_treated;
        }
        return 0;
    }

    public int getCategoryResource() {

        switch (this) {
            case AMNESTY:
                return R.array.amnesty;
            case ANTICAPITALISM:
                return R.array.anticapitalism;
            case ANTIMILITARISM:
                return R.array.antimilitarism;
            case ASYLUM:
                return R.array.asylum;
            case FAIR_TRADE:
                return R.array.fair_trade;
            case EDUCATION:
                return R.array.education;
            case INTERNET:
                return R.array.internet;
            case TERRITORY_DEFENSE:
                return R.array.territory_defense;
            case ECOLOGY:
                return R.array.ecology;
            case FEMINISM:
                return R.array.feminism;
            case PRO_INDEPENDENCE:
                return R.array.pro_independence;
            case INTEGRATION:
                return R.array.integration;
            case MEMORY:
                return R.array.memory;
            case ANIMAL_MOV:
                return R.array.animal_mov;
            case POLICY:
                return R.array.policy;
            case HEALTH:
                return R.array.health;
            case SOLIDARITY:
                return R.array.solidarity;
            case TREATED:
                return R.array.treated;
            default:
                return 0;
        }
    }

    public String[] getActivisClassification() {
        return INSTANCE.getResources().getStringArray(getCategoryResource());
    }

    public static ActivisCategory[] valueOf(String[] categories) {
        ActivisCategory[] activisCategories = new ActivisCategory[categories.length];
        for (int x = 0; x < categories.length; x++) {
            activisCategories[x] = valueOf(categories[x]);
        }

        return activisCategories;
    }
}
