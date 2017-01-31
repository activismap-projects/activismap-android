package com.entropy_factory.activismap.core.db.base;

import com.activeandroid.annotation.Column;
import com.entropy_factory.activismap.core.db.model.Entity;

/**
 * Created by ander on 18/07/16.
 */
public class BaseEntity extends BaseModel implements Entity{

    private static final String TAG = "BaseEntity";

    @Column(name = "server_id")
    protected long serverId;

    @Column (name = "created_date")
    protected long createdDate;

    @Column (name = "last_update")
    protected long lastUpdate;

    @Column (name = "identifier", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    protected String identifier;

    @Override
    public long getServerId() {
        return serverId;
    }

    @Override
    public long getCreatedDate() {
        return createdDate;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    public <T extends BaseEntity> T setServerId(long serverId) {
        this.serverId = serverId;
        return (T) this;
    }

    public <T extends BaseEntity> T setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
        return (T) this;
    }

    public <T extends BaseEntity> T setIdentifier(String identifier) {
        this.identifier = identifier;
        return (T) this;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public <T extends BaseEntity> T setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
        return (T) this;
    }


}
