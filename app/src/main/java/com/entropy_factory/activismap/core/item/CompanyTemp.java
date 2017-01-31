package com.entropy_factory.activismap.core.item;

import com.entropy_factory.activismap.core.db.Company;
import com.entropy_factory.activismap.core.db.Role;
import com.entropy_factory.activismap.core.db.model.Nameable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Andersson G. Acosta on 12/01/17.
 */

public class CompanyTemp implements Nameable, Serializable {

    private static final String TAG = "Company";
    private static final long serialVersionUID = 4043709652828311593L;

    private long serverId;
    private long createdDate;
    private long lastUpdate;
    private String identifier;
    private String remoteAvatar;
    private String name;
    private String email;
    private String role;

    @Override
    public String getFriendlyName() {
        return name;
    }

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

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getRemoteAvatar() {
        return remoteAvatar;
    }

    public void setRemoteAvatar(String remoteAvatar) {
        this.remoteAvatar = remoteAvatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        if (role == null) {
            return Role.ROLE_UNKNOWN;
        }

        return Role.valueOf(role);
    }

    public CompanyTemp setRole(String role) {
        this.role = role;
        return this;
    }

    public CompanyTemp setRole(Role role) {
        this.role = role.toString();
        return this;
    }

    public Long save() {
        Company company = Company.findByRemoteId(getServerId());

        if (company == null) {
            company = new Company();
        }

        company.setServerId(getServerId());
        company.setIdentifier(getIdentifier());
        company.setCreatedDate(getCreatedDate());
        company.setLastUpdate(getLastUpdate());
        company.setName(getName());
        company.setEmail(getEmail());
        company.setRemoteAvatar(getRemoteAvatar());
        company.setRole(getRole());

        return company.save();
    }


    public static CompanyTemp[] update(JSONArray jsonArray) throws JSONException {
        int lenght = jsonArray.length();
        CompanyTemp[] companies = new CompanyTemp[lenght];

        for (int x = 0; x < lenght; x++) {
            companies[x] = update(jsonArray.getJSONObject(x));
        }

        return companies;
    }

    public static CompanyTemp update(JSONObject data) throws JSONException {

        CompanyTemp c = new CompanyTemp();
        String identifier = data.getString("identifier");
        long id = data.getLong("id");
        long created = data.getLong("created");
        long lastUpdate = data.getLong("last_update");

        c.setServerId(id);
        c.setIdentifier(identifier);
        c.setCreatedDate(created);
        c.setName(data.getString("name"));
        c.setEmail(data.getString("email"));
        c.setLastUpdate(lastUpdate);

        if (data.has("logo")) {
            c.setRemoteAvatar(data.getString("logo"));
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

        return c;

    }
}
