package com.entropy_factory.activismap.ui.content;

import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.entropy_factory.activismap.R;
import com.entropy_factory.activismap.core.activis.BaseActivis;
import com.entropy_factory.activismap.core.activis.ActivisListener;
import com.entropy_factory.activismap.core.activis.ActivisResponse;
import com.entropy_factory.activismap.core.anotations.LikeStatus;
import com.entropy_factory.activismap.core.db.ActivisCategory;
import com.entropy_factory.activismap.core.db.ActivisEvent;
import com.entropy_factory.activismap.core.db.ActivisType;
import com.entropy_factory.activismap.core.item.ActivisItem;
import com.entropy_factory.activismap.core.item.Comment;
import com.entropy_factory.activismap.ui.adapter.CommentAdapter;
import com.entropy_factory.activismap.util.IntentUtils;
import com.entropy_factory.activismap.util.TimeUtils;
import com.entropy_factory.activismap.util.Utils;
import com.entropy_factory.activismap.widget.ItemClassificationView;
import com.entropy_factory.activismap.view.LikeView;
import com.entropy_factory.activismap.widget.ProfileView;
import com.entropy_factory.activismap.widget.ProfileImageView;
import com.entropy_factory.activismap.widget.RemoteImageView;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class EventActivity extends AppCompatActivity implements ActivisListener<ActivisItem> {

    private static final String TAG = "EventActivity";

    private final ActivisListener<Comment> COMMENT_LISTENER = new ActivisListener<Comment>() {
        @Override
        public void onResponse(ActivisResponse<Comment> response) {
            if (adapter == null) {
                adapter = new CommentAdapter(EventActivity.this);
                commentList.setAdapter(adapter);
            }

            adapter.addAll(response.getElements());


        }
    };

    private RemoteImageView eventImage;
    private ItemClassificationView classification;
    private ProfileView companyProfile;

    private TextView eventDescription;
    private TextView eventTime;
    private TextView eventLocation;
    private TextView eventPersons;
    private TextView eventLikes;
    private TextView eventDislikes;
    private TextView likeIcon;
    private TextView subscribeIcon;
    private TextView actionComment;
    private EditText commentBox;
    private View openNavigation;
    private RecyclerView commentList;

    private CollapsingToolbarLayout toolbarLayout;

    private ActivisEvent item;
    private SimpleTooltip st;

    @LikeStatus
    private String likeStatus = LikeStatus.NEUTRAL;
    private BaseActivis client;
    private CommentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        client = new BaseActivis(this);

        eventImage = (RemoteImageView) findViewById(R.id.event_image);
        eventDescription = (TextView) findViewById(R.id.event_description);
        eventTime = (TextView) findViewById(R.id.event_time);
        eventLocation = (TextView) findViewById(R.id.event_location);
        eventPersons = (TextView) findViewById(R.id.event_persons);
        eventLikes = (TextView) findViewById(R.id.event_likes);
        eventDislikes = (TextView) findViewById(R.id.event_dislikes);
        openNavigation = findViewById(R.id.open_navigation);
        likeIcon = (TextView)  findViewById(R.id.like_icon);
        subscribeIcon = (TextView)  findViewById(R.id.subscribe_icon);
        classification = (ItemClassificationView)  findViewById(R.id.classification);
        companyProfile = (ProfileView) findViewById(R.id.company_profile);

        actionComment = (TextView) findViewById(R.id.action_comment);
        commentBox = (EditText) findViewById(R.id.comment_box);

        commentList = (RecyclerView)findViewById(R.id.list);
        commentList.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        commentList.setLayoutManager(new LinearLayoutManager(this));

        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            long id = extras.getLong("id");
            ActivisEvent item = ActivisEvent.findByRemoteId(id);
            setItem(item);
            Log.d(TAG, "EVENT ID = " + id);
        }

        likeIcon.setText(likeStatus);
        likeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLikeTooltip(v);
            }
        });

        classification.setOnClassificationClickListener(new ItemClassificationView.OnClassificationClickListener() {
            @Override
            public void onTypeClick(View v, ActivisType type) {
                openTypeTooltip(v, TextUtils.join("\n", type.getActivisClassification()));
            }

            @Override
            public void onCategoryClick(View v, ActivisCategory category) {
                openTypeTooltip(v, TextUtils.join("\n", category.getActivisClassification()));
            }
        });

        actionComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean visible = commentBox.getVisibility() == View.VISIBLE;

                String text = commentBox.getText().toString();
                if (visible && !TextUtils.isEmpty(text)) {
                    client.addComment(item, text, COMMENT_LISTENER);
                    commentBox.setText("");
                    commentBox.setVisibility(View.GONE);
                } else {
                    commentBox.setVisibility(visible ? View.GONE : View.VISIBLE);
                }
            }
        });

        commentBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(charSequence)) {
                    actionComment.setText("\ue0d8");
                } else {
                    actionComment.setText("\ue163");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        subscribeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.isSubscribed()) {
                    client.unsubscribe(item, EventActivity.this);
                    item.setSubscribed(false);
                } else {
                    client.subscribe(item, EventActivity.this);
                    item.setSubscribed(true);
                }

                item.save();
                setSubscribeIcon();

            }
        });

        setSubscribeIcon();
    }

    private void setSubscribeIcon() {
        subscribeIcon.setText(item.isSubscribed() ? "\ue7fd" : "\ue7ff");
    }

    private void openTypeTooltip(View v, String text) {
        st = new SimpleTooltip.Builder(this)
                .text(text)
                .backgroundColor(Color.WHITE)
                .animated(true)
                .anchorView(v)
                .modal(true)
                .gravity(Gravity.START)
                .arrowColor(Color.WHITE)
                .build();
        st.show();
    }

    private void openLikeTooltip(View v) {
        st = new SimpleTooltip.Builder(this)
                .contentView(new LikeView(this)
                        .setTextSize(Utils.convertDpToPixels(this, 20))
                        .setTextColor(getResources().getColor(R.color.like_dark))
                        .setStatus(likeStatus)
                        .setOnLikeClickListener(new LikeView.OnLikeClickListener() {
                            @Override
                            public void onLikeClick(@LikeStatus String likeStatus) {
                                Log.e(TAG, "onLikeClick");
                                likeIcon.setText(likeStatus);
                                EventActivity.this.likeStatus = likeStatus;
                                st.dismiss();

                                switch (likeStatus) {
                                    case LikeStatus.LIKE:
                                        client.like(item, EventActivity.this);
                                        break;
                                    case LikeStatus.DISLIKE:
                                        client.dislike(item, EventActivity.this);
                                        break;
                                    case LikeStatus.NEUTRAL:
                                        client.neutralLike(item, EventActivity.this);
                                        break;
                                }
                            }
                        })
                        .setBg(R.drawable.shape_white_rounded), 0)
                .anchorView(v)
                .modal(true)
                .gravity(Gravity.TOP)
                .arrowColor(Color.WHITE)
                .build();
        st.show();
    }

    private void setItem(final ActivisEvent item) {
        if (item != null) {
            this.item = item;
            client.getComments(item, COMMENT_LISTENER);

            toolbarLayout.setTitle(item.getTitle());
            eventImage.loadRemoteImage(item.getImageUrl());
            eventDescription.setText(item.getDescription());
            eventTime.setText(getString(R.string.date_construct, TimeUtils.getTimeString(item.getStartDate()), TimeUtils.getTimeString(item.getEndDate())));
            eventLocation.setText(Utils.locationToString(item.getLatitude(), item.getLongitude()));
            eventPersons.setText(Utils.humanReadable(item.getParticipants()));
            eventLikes.setText(Utils.humanReadable(item.getLikes()));
            eventDislikes.setText(Utils.humanReadable(item.getDislikes()));
            companyProfile.setProfile(item.getCompany());
            openNavigation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentUtils.openNavigation(EventActivity.this, item.getPosition());
                }
            });

            classification.setFrom(item);


        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResponse(ActivisResponse<ActivisItem> response) {
        if (!response.hasError()) {
            ActivisItem ai = response.getElementAt(0);
            ai.save();
            setItem(ActivisEvent.findByRemoteId(ai.getServerId()));
        }
    }
}
