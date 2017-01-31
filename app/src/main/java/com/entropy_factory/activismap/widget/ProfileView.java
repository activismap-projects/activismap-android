package com.entropy_factory.activismap.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.entropy_factory.activismap.R;
import com.entropy_factory.activismap.core.db.base.ProfilableEntity;
import com.entropy_factory.activismap.util.Utils;
import com.entropy_factory.activismap.view.Font;
import com.entropy_factory.activismap.view.FontTextView;

/**
 * Created by ander on 20/10/16.
 */
public class ProfileView extends LinearLayout {

    private static final String TAG = "ProfileView";

    private RemoteImageView avatar;
    private FontTextView name;
    private FontTextView detailText;

    public ProfileView(Context context) {
        super(context);
        initialize(null);
    }

    public ProfileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(attrs);
    }

    public ProfileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(attrs);
    }

    protected void initialize(AttributeSet attrs) {
        removeAllViews();
        setOrientation(HORIZONTAL);

        LayoutParams avatarParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 0.2f);
        LayoutParams nameParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 0.8f);
        nameParams.setMargins(Utils.convertDpToPixels(getContext(), 10), 0, 0, 0);

        avatar = new RemoteImageView(getContext());
        avatar.setImageResource(R.mipmap.ic_launcher);
        avatar.setLayoutParams(avatarParams);

        LinearLayout nameLayout = new LinearLayout(getContext());
        nameLayout.setLayoutParams(nameParams);
        nameLayout.setOrientation(VERTICAL);
        nameLayout.setGravity(Gravity.CENTER_VERTICAL);

        name = new FontTextView(getContext());
        name.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        name.setText("Zachary Hellman");
        name.setFont(Font.BOLD);

        detailText = new FontTextView(getContext());
        detailText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        detailText.setText("@zhellman");
        detailText.setFont(Font.BOLD);

        nameLayout.addView(name);
        nameLayout.addView(detailText);

        addView(avatar);
        addView(nameLayout);

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ProfileView);

            float nameSize = a.getDimensionPixelSize(R.styleable.ProfileView_textNameSize, Utils.convertDpToPixels(getContext(), 14));
            float usernameSize = a.getDimensionPixelSize(R.styleable.ProfileView_textDetailSize, Utils.convertDpToPixels(getContext(), 12));
            int nameColor = a.getColor(R.styleable.ProfileView_textNameColor, getResources().getColor(R.color.colorPrimaryDark));
            int usernameColor = a.getColor(R.styleable.ProfileView_textDetailColor, getResources().getColor(R.color.colorPrimary));
            boolean showName = a.getBoolean(R.styleable.ProfileView_showName, true);
            boolean showUsername = a.getBoolean(R.styleable.ProfileView_showUsername, true);
            a.recycle();

            name.setTextColor(nameColor);
            name.setTextSize(TypedValue.COMPLEX_UNIT_PX, nameSize);
            name.setVisibility(showName ? VISIBLE : GONE);
            detailText.setTextSize(TypedValue.COMPLEX_UNIT_PX, usernameSize);
            detailText.setTextColor(usernameColor);
            detailText.setVisibility(showUsername ? VISIBLE : GONE);
        }
    }

    public void setProfile(ProfilableEntity profile) {
        if (profile.hasAvatar()) {
            avatar.loadRemoteImage(profile.getRemoteAvatar());
        }
        name.setText(profile.getFriendlyName());
        detailText.setText(profile.getEmail());
    }

    public void setName(CharSequence name){
        this.name.setText(name);
    }

    public void setDetailText(CharSequence detail) {
        this.detailText.setText(detail);
    }

    public void setAvatar(String avatar) {
        this.avatar.loadRemoteImage(avatar);
    }
}
