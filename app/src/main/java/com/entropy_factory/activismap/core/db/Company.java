package com.entropy_factory.activismap.core.db;

import android.util.Log;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.entropy_factory.activismap.core.db.base.AvatarEntity;
import com.entropy_factory.activismap.core.db.base.ProfilableEntity;
import com.entropy_factory.activismap.core.db.model.Nameable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Andersson G. Acosta on 12/01/17.
 */
@Table(name = "company")
public class Company extends ProfilableEntity implements Nameable {

    private static final String TAG = "Company";

    @Column(name = "role")
    private String role;

    public Company() {
        super();
    }

    @Override
    public Company setFriendlyName(String friendlyName) {
       setName(friendlyName);
        super.setFriendlyName(friendlyName);
        return this;
    }

    public Role getRole() {
        return Role.valueOf(role);
    }

    public Company setRole(String role) {
        this.role = role;
        return this;
    }

    public Company setRole(Role role) {
        this.role = role.toString();
        return this;
    }

    public boolean hasAnyRole() {
        Role role = getRole();
        return !role.equals(Role.ROLE_UNKNOWN);
    }

    public boolean isCurrentUserCompany() {
        return User.getUser().hasCurrentCompany() && User.getUser().getCurrentCompany().getServerId() == getServerId();
    }

    public static List<Company> getGrantedCompanies() {
        return new Select().from(Company.class).where("role <> '" + Role.ROLE_UNKNOWN + "'").execute();
    }

    public static Company findByInternalId(long id) {
        return new Select().from(Company.class).where("id = " + id).executeSingle();
    }

    public static Company findByRemoteId(Object id) {
        if (id != null) {
            if (id instanceof String) {
                return new Select().from(Company.class).where("identifier = '" + id.toString() + "'").executeSingle();
            } else if (id instanceof Long) {
                return new Select().from(Company.class).where("server_id = " + id).executeSingle();
            }
        }

        return null;

    }

    public static Company[] update(JSONArray jsonArray) throws JSONException {
        int lenght = jsonArray.length();
        Company[] companies = new Company[lenght];

        for (int x = 0; x < lenght; x++) {
            companies[x] = update(jsonArray.getJSONObject(x));
        }

        return companies;
    }

    public static Company update(JSONObject jsonObject) throws JSONException {
        long id = jsonObject.getLong("id");
        Company c = findByRemoteId(id);
        return update(c, jsonObject);
    }

    public static Company update(Company c, JSONObject data) throws JSONException {

        if (c == null) {
            String identifier = data.getString("identifier");
            long id = data.getLong("id");
            long created = data.getLong("created");
            c = new Company()
                    .setServerId(id)
                    .setIdentifier(identifier)
                    .setCreatedDate(created);
        }

        long updated = data.getLong("last_update");
        if (updated > c.getLastUpdate()) {
            Log.d(TAG, updated + " / " + c.getLastUpdate() + ", " + data.toString());
            c.setName(data.getString("name"))
                    .setEmail(data.getString("email"))
                    .setLastUpdate(updated);

            if (data.has("logo")) {
                c.setRemoteAvatar(data.getString("logo"));
            }

            c.save();
        }

        if (data.has("roles")) {
            JSONArray roles = data.getJSONArray("roles");
            int length = roles.length();
            String[] rolesString = new String[length];
            for (int x = 0; x < length; x++) {
                rolesString[x] = roles.getString(x);
            }

            Role userRole = Role.getMaxRole(rolesString);
            c.setRole(userRole);
        }

        c.save();

        return c;

    }
}
