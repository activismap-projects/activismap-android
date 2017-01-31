package com.entropy_factory.activismap.core.activis;

import com.entropy_factory.activismap.core.db.base.BaseEntity;
import com.entropy_factory.activismap.core.db.model.Entity;

/**
 * Created by ander on 3/10/16.
 */

public interface ActivisListener<Element extends Entity> {

    void onResponse(ActivisResponse<Element> response);
}
