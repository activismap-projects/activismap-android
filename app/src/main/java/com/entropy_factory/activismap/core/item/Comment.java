package com.entropy_factory.activismap.core.item;

import android.text.TextUtils;

import com.activeandroid.annotation.Column;
import com.entropy_factory.activismap.R;
import com.entropy_factory.activismap.core.db.model.Entity;
import com.entropy_factory.activismap.util.TimeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.entropy_factory.activismap.app.ActivisApplication.INSTANCE;

/**
 * Created by Andersson G. Acosta on 31/01/17.
 */
public class Comment implements Entity {

    private static final String TAG = "Comment";

    protected long serverId;
    protected long createdDate;
    protected long lastUpdate;
    protected String identifier;
    protected String comment;
    private ActivisItem activisItem;
    private String user;
    private String userAvatar;

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

    public long getLastUpdate() {
        return lastUpdate;
    }

    public String getComment() {
        return comment;
    }

    public String getUser() {
        if (TextUtils.isEmpty(user)) {
            return INSTANCE.getString(R.string.anonymous);
        }

        return user;
    }

    public String getUserAvatar() {
        if (TextUtils.isEmpty(userAvatar)) {
            return "http://api.adorable.io/avatars/285/" + Long.toString(getCreatedDate(), 36) + ".png";
        }
        return userAvatar;
    }

    public ActivisItem getActivisItem() {
        return activisItem;
    }

    public void setActivisItem(ActivisItem activisItem) {
        this.activisItem = activisItem;
    }

    public String getDateString() {
        return TimeUtils.getTimeString(getCreatedDate());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Comment)) {
            return false;
        }

        Comment c = (Comment) obj;
        return c.getServerId() == getServerId();
    }

    public static Comment from(JSONObject jsonObject) throws JSONException {
        Comment c = new Comment();
        c.serverId = jsonObject.getLong("id");
        c.createdDate = jsonObject.getLong("created");
        c.lastUpdate = jsonObject.getLong("last_update");
        c.identifier = jsonObject.getString("identifier");
        c.comment = jsonObject.getString("comment");

        if (jsonObject.has("user")) {
            JSONObject user = jsonObject.getJSONObject("user");
            c.user = user.getString("username");

            if (user.has("avatar")) {
                c.userAvatar = user.getString("avatar");
            }
        }

        return c;
    }

    public static List<Comment> from(JSONArray jsonArray) throws JSONException {
        ArrayList<Comment> comments = new ArrayList<>();

        for (int x = 0; x < jsonArray.length(); x++){
            comments.add(from(jsonArray.getJSONObject(x)));
        }

        return comments;
    }
}
