package com.entropy_factory.activismap.core.db;

import com.entropy_factory.activismap.R;
import com.entropy_factory.activismap.widget.ActivisView;

import static com.entropy_factory.activismap.app.ActivisApplication.INSTANCE;

/**
 * Created by ander on 30/09/16.
 */
public enum ActivisCategory implements ActivisView {

    AMNESTY, ANTICAPITALISM, ANTIFASCISM, ANTIMILITARISM, FAIR_TRADE, COOPERATIVE, EDUCATION,
    TERRITORY_DEFENSE, ECOLOGY, FEMINISM, INTEGRATION, TREATED, MEMORY, ASYLUM, OCCUPATION, POLICY,
    HEALTH, TRADE_UNIONISM, RURAL_WORLD, NATIONAL_LIBERTY, SOLIDARITY, INTERNET, ANIMAL_MOV, UNKNOWN;

    public static final ActivisCategory[] AVAILABLE = {AMNESTY, ANTICAPITALISM, ANTIFASCISM, ANTIMILITARISM, FAIR_TRADE, COOPERATIVE, EDUCATION,
            TERRITORY_DEFENSE, ECOLOGY, FEMINISM, INTEGRATION, TREATED, MEMORY, ASYLUM, OCCUPATION, POLICY,
            HEALTH, TRADE_UNIONISM, RURAL_WORLD, NATIONAL_LIBERTY, SOLIDARITY, INTERNET, ANIMAL_MOV};

    public int getIcon() {
        switch (this) {
            case AMNESTY:
                return R.drawable.ic_amnesty;
            case ANTICAPITALISM:
                return R.drawable.ic_anticapitalism;
            case ANTIFASCISM:
                return R.drawable.ic_antifascism;
            case ANTIMILITARISM:
                return R.drawable.ic_antimilitarism;
            case FAIR_TRADE:
                return R.drawable.ic_fair_trade;
            case COOPERATIVE:
                return R.drawable.ic_cooperative;
            case EDUCATION:
                return R.drawable.ic_education;
            case TERRITORY_DEFENSE:
                return R.drawable.ic_territory_defense;
            case ECOLOGY:
                return R.drawable.ic_ecology;
            case FEMINISM:
                return R.drawable.ic_feminism;
            case INTEGRATION:
                return R.drawable.ic_integration;
            case TREATED:
                return R.drawable.ic_anti_tlc_fight;
            case MEMORY:
                return R.drawable.ic_memory;
            case ASYLUM:
                return R.drawable.ic_asylum;
            case OCCUPATION:
                return R.drawable.ic_occupation;
            case POLICY:
                return R.drawable.ic_policy;
            case HEALTH:
                return R.drawable.ic_health;
            case TRADE_UNIONISM:
                return R.drawable.ic_trade_uniosnism;
            case RURAL_WORLD:
                return R.drawable.ic_rural_world;
            case NATIONAL_LIBERTY:
                return R.drawable.ic_national_liberty;
            case SOLIDARITY:
                return R.drawable.ic_solidarity;
            case INTERNET:
                return R.drawable.ic_internet;
            case ANIMAL_MOV:
                return R.drawable.ic_animal;
        }
        return 0;
    }

    public int getCategoryResource() {

        switch (this) {
            case AMNESTY:
                return R.array.amnesty;
            case ANTICAPITALISM:
                return R.array.anticapitalism;
            case ANTIFASCISM:
                return R.array.antifascism;
            case ANTIMILITARISM:
                return R.array.antimilitarism;
            case FAIR_TRADE:
                return R.array.fair_trade;
            case COOPERATIVE:
                return R.array.cooperative;
            case EDUCATION:
                return R.array.education;
            case TERRITORY_DEFENSE:
                return R.array.territory_defense;
            case ECOLOGY:
                return R.array.ecology;
            case FEMINISM:
                return R.array.feminism;
            case INTEGRATION:
                return R.array.integration;
            case TREATED:
                return R.array.treated;
            case MEMORY:
                return R.array.memory;
            case ASYLUM:
                return R.array.asylum;
            case OCCUPATION:
                return R.array.occupation;
            case POLICY:
                return R.array.policy;
            case HEALTH:
                return R.array.health;
            case TRADE_UNIONISM:
                return R.array.trade_unionism;
            case RURAL_WORLD:
                return R.array.rural_world;
            case NATIONAL_LIBERTY:
                return R.array.national_liberty;
            case SOLIDARITY:
                return R.array.solidarity;
            case INTERNET:
                return R.array.internet;
            case ANIMAL_MOV:
                return R.array.animal_mov;
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
            ActivisCategory category = UNKNOWN;
            try {
                category = valueOf(categories[x]);
            } catch (Exception ignored) {}
            activisCategories[x] = category;
        }

        return activisCategories;
    }
}
