package com.entropy_factory.activismap.core.db.base;

import com.activeandroid.annotation.Column;
import com.entropy_factory.activismap.core.db.model.Nameable;

/**
 * Created by ander on 2/08/16.
 */
public class ProfilableEntity extends AvatarEntity implements Nameable {

    private static final String TAG = "ProfilableEntity";

    @Column(name = "name")
    private String name;

    @Column(name = "friendly_name")
    private String friendlyName;

    @Column(name = "email")
    private String email;

    public ProfilableEntity setName(String name) {
        this.name = name;
        this.friendlyName = name;
        return this;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getFriendlyName() {
        return friendlyName;
    }

    public <T extends ProfilableEntity> T setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
        return (T) this;
    }

    public String getEmail() {
        return email;
    }

    public <T extends ProfilableEntity> T setEmail(String email) {
        this.email = email;
        return (T) this;
    }
}
