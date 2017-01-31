package com.entropy_factory.activismap.core.db.base;

import android.text.TextUtils;
import android.webkit.URLUtil;

import com.activeandroid.annotation.Column;
import com.entropy_factory.activismap.core.db.model.Profilable;

import java.io.File;

/**
 * Created by ander on 11/08/16.
 */
public abstract class AvatarEntity extends BaseEntity implements Profilable {

    private static final String TAG = "AvatarEntity";

    @Column(name = "remote_avatar")
    protected String remoteAvatar;

    @Override
    public String getRemoteAvatar() {
        return remoteAvatar;
    }

    @Override
    public AvatarEntity setRemoteAvatar(String url) {
        remoteAvatar = url;
        return this;
    }

    @Override
    public boolean hasAvatar() {
        return URLUtil.isValidUrl(remoteAvatar);
    }
}
