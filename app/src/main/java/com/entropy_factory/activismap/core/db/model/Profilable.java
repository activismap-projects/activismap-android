package com.entropy_factory.activismap.core.db.model;

import java.io.File;

/**
 * Created by ander on 2/08/16.
 */
public interface Profilable extends Entity {

    String getRemoteAvatar();
    Entity setRemoteAvatar(String url);
    boolean hasAvatar();

}
