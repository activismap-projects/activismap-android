package com.entropy_factory.activismap.core.db;

import android.util.Log;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.entropy_factory.activismap.core.db.base.ProfilableEntity;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ander on 21/04/16.
 */
@Table(name = "user")
public class User extends ProfilableEntity {

    private static final String TAG = "User";

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "token_expiration")
    private long tokenExpiration;

    @Column(name = "current_company")
    private Company currentCompany;

    public User() {
        super();
    }

    @Override
    public User setCreatedDate(long createdDate) {
        super.setCreatedDate(createdDate);
        return this;
    }

    @Override
    public User setIdentifier(String identifier) {
        super.setIdentifier(identifier);
        return this;
    }

    @Override
    public User setServerId(long serverId) {
        super.setServerId(serverId);
        return this;
    }

    @Override
    public User setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
        return this;
    }

    public Company getCurrentCompany() {

        if (currentCompany != null && !currentCompany.hasAnyRole()) {
            return null;
        }

        return currentCompany;
    }

    public User setCurrentCompany(Company currentCompany) {
        this.currentCompany = currentCompany;
        return this;
    }

    public boolean hasCurrentCompany() {
        return getCurrentCompany() != null;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public User setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public User setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public long getTokenExpiration() {
        return tokenExpiration;
    }

    public User setTokenExpiration(long tokenExpiration) {
        this.tokenExpiration = System.currentTimeMillis() + (tokenExpiration * 1000);
        return this;
    }

    public boolean isAccessTokenExpired() {
        return tokenExpiration <= System.currentTimeMillis();
    }

    public boolean isRefreshTokenExpired() {
        return (System.currentTimeMillis() - tokenExpiration) >= ( 1123200 * 1000); // 14 Days
    }

    public static User getUser() {
        return new Select().from(User.class).executeSingle();
    }

    public static User update(JSONObject jsonObject) throws JSONException {
        return update(User.getUser(), jsonObject);
    }

    public static User update(User user, JSONObject data) throws JSONException {

        if (user == null) {
            user = new User();
        }

        long updated = data.getLong("last_update");
        if (updated > user.getLastUpdate()) {
            Log.d(TAG, updated + " / " + user.getLastUpdate() + ", " + data.toString());
            user.setLastUpdate(updated);
            user.setCreatedDate(data.getLong("created"));
            user.setIdentifier(data.getString("identifier"));
            user.setFriendlyName(data.getString("username"));
            user.setServerId(data.getLong("id"));
            user.setName(data.getString("username"));
            user.setEmail(data.getString("email"));

            if (data.has("avatar")) {
                user.setRemoteAvatar(data.getString("avatar"));
            }

            user.save();
        }

        if (data.has("companies")) {
            Company[] companies = Company.update(data.getJSONArray("companies"));

            if (companies.length == 1) {
                user.setCurrentCompany(companies[0]);
            }
        }

        user.save();

        return user;
    }
}
