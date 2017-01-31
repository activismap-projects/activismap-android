package com.entropy_factory.activismap.core.activis;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.entropy_factory.activismap.R;
import com.entropy_factory.activismap.core.db.ActivisEvent;
import com.entropy_factory.activismap.core.db.ActivisType;
import com.entropy_factory.activismap.core.db.Company;
import com.entropy_factory.activismap.core.db.User;
import com.entropy_factory.activismap.core.http.handler.DialogResponseListener;
import com.entropy_factory.activismap.core.http.request.CreateEventRequestBuilder;
import com.entropy_factory.activismap.ui.tool.CompanyListActivity;
import com.entropy_factory.activismap.ui.tool.LoginActivity;
import com.entropy_factory.activismap.ui.tool.RegisterActivity;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import static com.entropy_factory.activismap.ui.tool.CompanyListActivity.PICK_COMPANY;
import static com.entropy_factory.activismap.ui.tool.LoginActivity.LOGIN_USER;
import static com.entropy_factory.activismap.ui.tool.RegisterActivity.REGISTER_USER;

/**
 * Created by Andersson G. Acosta on 20/01/17.
 */
public class Activis extends BaseActivis {

    private static final String TAG = "Activis";

    public Activis(Activity context) {
        super(context);
    }

    public void openRegisterScreen() {
        Intent registerIntent = new Intent(context, RegisterActivity.class);
        ((Activity) context).startActivityForResult(registerIntent, REGISTER_USER);
    }

    public void openLoginScreen() {
        Intent registerIntent = new Intent(context, LoginActivity.class);
        ((Activity) context).startActivityForResult(registerIntent, LOGIN_USER);
    }

    public void openCompanyScreen() {
        Intent companyIntent = new Intent(context, CompanyListActivity.class);
        ((Activity) context).startActivityForResult(companyIntent, PICK_COMPANY);
    }

    public void createEvent(User user, String title, String description, long sDate, long eDate, String categories, ActivisType type, LatLng location, ActivisListener<ActivisEvent> listener) {
        createEvent(user, title, description, sDate, eDate, categories, type, location, null, listener);
    }

    public void createEvent(User user, String title, String description, long sDate, long eDate, String categories, ActivisType type, LatLng location, File image, final ActivisListener<ActivisEvent> listener) {

        if (user == null) {
            openRegisterScreen();
            return;
        } else if (user.isAccessTokenExpired()) {
            openLoginScreen();
            return;
        } else if (!user.hasCurrentCompany()) {
            List<Company> grantedCompanies = Company.getGrantedCompanies();
            openCompanyScreen();
            return;
            /*if (grantedCompanies.isEmpty()) {
                Toast.makeText(context, R.string.no_granted_companies, Toast.LENGTH_LONG).show();
                return;
            } else  {

                return;
            }*/
        }

        CreateEventRequestBuilder cer = new CreateEventRequestBuilder(user.getCurrentCompany(), title, description, categories, type, location, sDate, eDate);

        if (image != null) {
            cer.setImage(image);
        }

        DialogResponseListener dListener = new DialogResponseListener(context, R.string.new_event, R.string.creating_event) {
            @Override
            public void onOkResponse(JSONObject jsonObject) throws JSONException {
                jsonObject = jsonObject.getJSONObject("data");
                ActivisEvent ae = ActivisEvent.update(jsonObject);

                if (listener != null) {
                    listener.onResponse(new ActivisResponse<>(ae));
                }
            }

            @Override
            public void onParseError(JSONException e) {
                super.onParseError(e);
                if (listener != null) {
                    listener.onResponse(new ActivisResponse<ActivisEvent>(0, e.getMessage()));
                }
            }
        };

        cer.build()
                .setRequestStateListener(dListener)
                .setResponseListener(dListener)
                .execute();
    }
}
