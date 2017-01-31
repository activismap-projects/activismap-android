package com.entropy_factory.activismap.core.http.request;

import android.text.TextUtils;

import com.entropy_factory.activismap.core.http.request.base.BasePostRequestBuilder;

import java.util.Map;

/**
 * Created by ander on 3/10/16.
 */
public class RegisterRequestBuilder extends BasePostRequestBuilder {

    private static final String TAG = "RegisterRequest";

    private String username;
    private String password;
    private String repassword;
    private String email;
    private String personName;

    public RegisterRequestBuilder(String username, String password, String repassword, String email) {
        this.username = username;
        this.password = password;
        this.repassword = repassword;
        this.email = email;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    @Override
    public String getURL() {
        return super.getURL() + "v1/public/register";
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> params = super.getParams();
        params.put("username", username);
        params.put("password", password);
        params.put("repassword", repassword);
        params.put("email", email);

        if (!TextUtils.isEmpty(personName)) {
            params.put("person_name", personName);
        }

        return params;
    }
}
