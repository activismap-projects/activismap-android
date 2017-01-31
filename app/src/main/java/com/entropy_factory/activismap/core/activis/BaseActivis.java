package com.entropy_factory.activismap.core.activis;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.entropy_factory.activismap.R;
import com.entropy_factory.activismap.app.ActivisApplication;
import com.entropy_factory.activismap.core.anotations.EventAction;
import com.entropy_factory.activismap.core.db.ActivisType;
import com.entropy_factory.activismap.core.db.User;
import com.entropy_factory.activismap.core.http.AbstractRequestStateListener;
import com.entropy_factory.activismap.core.http.handler.DialogResponseListener;
import com.entropy_factory.activismap.core.http.handler.OkResponseListener;
import com.entropy_factory.activismap.core.http.request.AddCommentRequestBuilder;
import com.entropy_factory.activismap.core.http.request.EventCommentsRequestBuilder;
import com.entropy_factory.activismap.core.http.request.LoginRequestBuilder;
import com.entropy_factory.activismap.core.http.request.RegisterRequestBuilder;
import com.entropy_factory.activismap.core.http.request.SearchActivisEventRequestBuilder;
import com.entropy_factory.activismap.core.http.request.UserAccountRequestBuilder;
import com.entropy_factory.activismap.core.http.request.EventActionRequestBuilder;
import com.entropy_factory.activismap.core.item.ActivisItem;
import com.entropy_factory.activismap.core.item.ActivisTempEvent;
import com.entropy_factory.activismap.core.item.Comment;
import com.entropy_factory.activismap.util.Task;
import com.zanjou.http.response.JsonResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.entropy_factory.activismap.core.http.request.UserAccountRequestBuilder.NO_ID;
import static com.entropy_factory.activismap.core.receiver.ActivisEventReceiver.ACTION_REFRESH;

/**
 * Created by ander on 3/10/16.
 */
public class BaseActivis {

    private static final String TAG = "Activis";

    protected Context context;

    public BaseActivis(Context context) {
        this.context = context;
    }

    public void register(String username, String password, String repassword, String email, String personName, final ActivisListener<User> listener) {

        RegisterRequestBuilder registerRequest = new RegisterRequestBuilder(username, password, repassword, email);
        registerRequest.setPersonName(personName);

        DialogResponseListener dListener = new DialogResponseListener(context, R.string.register, R.string.sending_data) {
            @Override
            public void onOkResponse(JSONObject jsonObject) throws JSONException {
                User u = User.update(jsonObject.getJSONObject("data"));
                if (listener != null) {
                    listener.onResponse(new ActivisResponse<>(u));
                }
            }

            @Override
            public void onParseError(JSONException e) {
                super.onParseError(e);
                if (listener != null) {
                    listener.onResponse(new ActivisResponse<User>(0, e.getMessage()));
                }
            }
        };

        registerRequest.build()
                .setRequestStateListener(dListener)
                .setResponseListener(dListener)
                .execute();
    }

    public void login(String username, String password, final ActivisListener<User> listener) {

        DialogResponseListener dListener = new DialogResponseListener(context, R.string.sign_in, R.string.logging) {

            @Override
            public void onOkResponse(JSONObject jsonObject) throws JSONException {
                String accessToken = jsonObject.getString("access_token");
                String refreshToken = jsonObject.getString("refresh_token");
                long expiration = jsonObject.getLong("expires_in");

                User u = User.getUser();
                if (u == null) {
                    u = new User();
                }
                u.setAccessToken(accessToken);
                u.setRefreshToken(refreshToken);
                u.setTokenExpiration(expiration);
                u.save();
                getAccount(listener);
            }

            @Override
            public void onParseError(JSONException e) {
                super.onParseError(e);
                if (listener != null) {
                    listener.onResponse(new ActivisResponse<User>(0, e.getMessage()));
                }
            }
        };

        new LoginRequestBuilder(username, password).build()
                .setRequestStateListener(dListener)
                .setResponseListener(dListener)
                .execute();
    }

    public void searchEvents() {
        searchEvents(null, null, null, null);
    }

    public void searchEvents(final ActivisListener<ActivisItem> listener) {
        searchEvents(null, listener, null, null);
    }

    public void searchEvents(ActivisType type) {
        searchEvents(type, null, null, null);
    }

    public void searchEvents(final ActivisListener<ActivisItem> listener, final Task<Void> onStart) {
        searchEvents(null, listener, onStart, null);
    }

    public void searchEvents(final ActivisListener<ActivisItem> listener, final Task<Void> onStart, final Task<Void> onFinish) {
        searchEvents(null, listener, onStart, onFinish);
    }

    public void searchEvents(ActivisType type, final ActivisListener<ActivisItem> listener, final Task<Void> onStart) {
        searchEvents(type, listener, onStart, null);
    }

    public void searchEvents(ActivisType type, final ActivisListener<ActivisItem> listener, final Task<Void> onStart, final Task<Void> onFinish) {

        new SearchActivisEventRequestBuilder(type)
                .build()
                .setRequestStateListener(new AbstractRequestStateListener() {
                    @Override
                    public void onStart() {
                        if (onStart != null) {
                            onStart.doTask(null);
                        }
                    }

                    @Override
                    public void onFinish() {
                        if (onFinish != null) {
                            onFinish.doTask(null);
                        }

                        Intent i = new Intent(ACTION_REFRESH);
                        ActivisApplication.INSTANCE.sendBroadcast(i);
                    }
                }).setResponseListener(new JsonResponseListener() {
            @Override
            public void onOkResponse(JSONObject jsonObject) throws JSONException {
                List<ActivisItem> events = ActivisTempEvent.update(jsonObject.getJSONArray("data"));
                if (listener != null) {
                    ActivisResponse<ActivisItem> response = new ActivisResponse<>(events);
                    listener.onResponse(response);
                }
            }

            @Override
            public void onErrorResponse(JSONObject jsonObject) throws JSONException {
                if (listener != null) {
                    ActivisResponse<ActivisItem> response = new ActivisResponse<>(0, jsonObject.toString());
                    listener.onResponse(response);
                }
            }

            @Override
            public void onParseError(JSONException e) {
                if (listener != null) {
                    ActivisResponse<ActivisItem> response = new ActivisResponse<>(0, e.getMessage());
                    listener.onResponse(response);
                }
            }
        }).execute();
    }

    public void getAccount(final ActivisListener<User> listener) {
        new UserAccountRequestBuilder(NO_ID).build()
                .setResponseListener(new OkResponseListener() {
                    @Override
                    public void onOkResponse(JSONObject jsonObject) throws JSONException {
                        User p = User.update(jsonObject.getJSONObject("data"));
                        if (listener != null) {
                            ActivisResponse<User> response = new ActivisResponse<>(p);
                            listener.onResponse(response);
                        }
                    }

                    @Override
                    public void onParseError(JSONException e) {
                        e.printStackTrace();
                    }
                }).execute();
    }

    public void makeEventAction(ActivisItem item, @EventAction String action, final ActivisListener<ActivisItem> listener) {
        new EventActionRequestBuilder(item, action).build()
                .setResponseListener(new OkResponseListener() {
                    @Override
                    public void onOkResponse(JSONObject jsonObject) throws JSONException {
                        ActivisTempEvent event = ActivisTempEvent.update(jsonObject.getJSONObject("data"));
                        if (listener != null) {
                            ActivisResponse<ActivisItem> response = new ActivisResponse<ActivisItem>(event);
                            listener.onResponse(response);
                        }
                    }
                }).execute();
    }

    public void subscribe(ActivisItem item, final ActivisListener<ActivisItem> listener) {
        makeEventAction(item, EventAction.SUBSCRIBE, listener);
    }

    public void unsubscribe(ActivisItem item, final ActivisListener<ActivisItem> listener) {
        makeEventAction(item, EventAction.UNSUBSCRIBE, listener);
    }

    public void like(ActivisItem item, final ActivisListener<ActivisItem> listener) {
        makeEventAction(item, EventAction.LIKE, listener);
    }

    public void dislike(ActivisItem item, final ActivisListener<ActivisItem> listener) {
        makeEventAction(item, EventAction.DISLIKE, listener);
    }

    public void neutralLike(ActivisItem item, final ActivisListener<ActivisItem> listener) {
        makeEventAction(item, EventAction.NEUTRAL, listener);
    }

    public void getComments(ActivisItem item, int limit, int offset, final ActivisListener<Comment> listener) {
        new EventCommentsRequestBuilder(item)
                .setLimit(limit)
                .setOffset(offset)
                .build()
                .setResponseListener(new OkResponseListener() {
                    @Override
                    public void onOkResponse(JSONObject jsonObject) throws JSONException {
                        List<Comment> c = Comment.from(jsonObject.getJSONArray("data"));
                        if (listener != null) {
                            ActivisResponse<Comment> response = new ActivisResponse<>(c);
                            listener.onResponse(response);
                        }
                    }
                }).execute();
    }

    public void getComments(ActivisItem item, final ActivisListener<Comment> listener) {
        getComments(item, 20, 0, listener);
    }

    public void addComment(ActivisItem item, String comment, final ActivisListener<Comment> listener) {
        new AddCommentRequestBuilder(item, comment)
                .build()
                .setResponseListener(new OkResponseListener() {
                    @Override
                    public void onOkResponse(JSONObject jsonObject) throws JSONException {
                        Comment c = Comment.from(jsonObject.getJSONObject("data"));
                        if (listener != null) {
                            ActivisResponse<Comment> response = new ActivisResponse<>(c);
                            listener.onResponse(response);
                        }
                    }
                }).execute();
    }
}
