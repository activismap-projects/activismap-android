package com.entropy_factory.activismap.core.anotations;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.entropy_factory.activismap.core.anotations.CategoryIndex.AUTO_INDEX;
import static com.entropy_factory.activismap.core.anotations.CategoryIndex.FIRST_CATEGORY;
import static com.entropy_factory.activismap.core.anotations.CategoryIndex.NO_INDEX;
import static com.entropy_factory.activismap.core.anotations.CategoryIndex.SECOND_CATEGORY;
import static com.entropy_factory.activismap.core.anotations.CategoryIndex.THIRD_CATEGORY;

/**
 * Created by ander on 11/11/16.
 */
@IntDef({FIRST_CATEGORY, SECOND_CATEGORY, THIRD_CATEGORY, AUTO_INDEX, NO_INDEX})
@Retention(RetentionPolicy.SOURCE)
public @interface CategoryIndex {
    int NO_INDEX = -1;
    int FIRST_CATEGORY = 0;
    int SECOND_CATEGORY = 1;
    int THIRD_CATEGORY = 2;
    int AUTO_INDEX = 4;
}
