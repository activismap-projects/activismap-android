package com.entropy_factory.activismap.core.anotations;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.entropy_factory.activismap.core.anotations.EventAction.DISLIKE;
import static com.entropy_factory.activismap.core.anotations.EventAction.LIKE;
import static com.entropy_factory.activismap.core.anotations.EventAction.NEUTRAL;
import static com.entropy_factory.activismap.core.anotations.EventAction.SUBSCRIBE;
import static com.entropy_factory.activismap.core.anotations.EventAction.UNSUBSCRIBE;

/**
 * Created by ander on 11/11/16.
 */
@StringDef({LIKE, DISLIKE, NEUTRAL, SUBSCRIBE, UNSUBSCRIBE})
@Retention(RetentionPolicy.SOURCE)
public @interface EventAction {
    String LIKE = "LIKE";
    String DISLIKE = "DISLIKE";
    String NEUTRAL = "NEUTRAL";
    String SUBSCRIBE = "SUBSCRIBE";
    String UNSUBSCRIBE = "UNSUBSCRIBE";
}
