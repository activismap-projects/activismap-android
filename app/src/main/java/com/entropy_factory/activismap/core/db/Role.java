package com.entropy_factory.activismap.core.db;

import android.support.annotation.StringRes;

import com.entropy_factory.activismap.R;

import java.util.ArrayList;

/**
 * Created by Andersson G. Acosta on 19/01/17.
 */
public enum Role {
    ROLE_SUPER_ADMIN, ROLE_ADMIN, ROLE_PUBLISHER, ROLE_UNKNOWN;

    @StringRes
    public int getStringResrouce() {
        switch (this) {
            case ROLE_SUPER_ADMIN:
                return R.string.role_super_admin;
            case ROLE_ADMIN:
                return R.string.role_admin;
            case ROLE_PUBLISHER:
                return R.string.role_publisher;
            default:
                return R.string.role_unknown;
        }
    }

    public static Role[] valueOf(String... roles) {
        ArrayList<Role> rolesList = new ArrayList<>();
        for (String r : roles) {
            rolesList.add(valueOf(r.toUpperCase()));
        }

        return rolesList.toArray(new Role[]{});
    }

    public static Role getMaxRole(String... roles) {
        return getMaxRole(valueOf(roles));
    }

    public static Role getMaxRole(Role... roles) {

        Role maxRole = ROLE_UNKNOWN;

        for (Role r : roles) {
            if (r.equals(ROLE_SUPER_ADMIN)) {
                return ROLE_SUPER_ADMIN;
            } else if (r.equals(ROLE_ADMIN) && !maxRole.equals(ROLE_SUPER_ADMIN)) {
                maxRole = ROLE_ADMIN;
            } else if (r.equals(ROLE_PUBLISHER) && maxRole.equals(ROLE_UNKNOWN)) {
                maxRole = ROLE_PUBLISHER;
            }

        }

        return maxRole;
    }


}
