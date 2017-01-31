package com.entropy_factory.activismap.core.anotations;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.entropy_factory.activismap.core.anotations.LikeStatus.DISLIKE;
import static com.entropy_factory.activismap.core.anotations.LikeStatus.LIKE;
import static com.entropy_factory.activismap.core.anotations.LikeStatus.NEUTRAL;

/**
 * Created by ander on 11/11/16.
 */
@StringDef({DISLIKE, NEUTRAL, LIKE})
@Retention(RetentionPolicy.SOURCE)
public @interface LikeStatus {
    public static final String DISLIKE = "\ue004";
    public static final String NEUTRAL = "\ue006";
    public static final String LIKE = "\ue005";
}
