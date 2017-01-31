package com.entropy_factory.activismap.ui.adapter.item;

import android.animation.Animator;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.entropy_factory.activismap.R;
import com.entropy_factory.activismap.core.db.base.ProfilableEntity;
import com.entropy_factory.activismap.widget.ProfileView;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.helpers.AnimatorHelper;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

import static com.entropy_factory.activismap.app.ActivisApplication.INSTANCE;

/**
 * Created by ander on 21/10/16.
 */
public class ProfileItem<T extends ProfilableEntity> extends AbstractFlexibleItem<ProfileItem.ViewHolder> {

    private static final String TAG = "ProfileItem";

    private T profile;

    public ProfileItem(T profile) {
        this.profile = profile;
    }

    public T getProfile() {
        return profile;
    }

    public void setProfile(T profile) {
        this.profile = profile;
    }

    @Override
    public boolean equals(Object o) {
        ProfilableEntity p;
        if (o instanceof ProfilableEntity) {
            p = (ProfilableEntity) o;
            return profile.equals(p);
        } else if (o instanceof ProfileItem) {
            p = ((ProfileItem) o).getProfile();
            return profile.equals(p);
        }
        return false;
    }

    @Override
    public ViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        View v = inflater.inflate(getLayoutRes(), parent);
        return new ViewHolder(v, adapter);
    }

    @Override
    public void bindViewHolder(final FlexibleAdapter adapter, ViewHolder holder, final int position, List payloads) {
        holder.profileView.setProfile(profile);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.company_row;
    }

    public static class ViewHolder extends FlexibleViewHolder {

        public ProfileView profileView;

        public ViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            initialize(view);
        }

        public ViewHolder(View view, FlexibleAdapter adapter, boolean stickyHeader) {
            super(view, adapter, stickyHeader);
            initialize(view);
        }

        private void initialize(View view) {
            profileView = (ProfileView) view.findViewById(R.id.profile);
        }

        @Override
        public void scrollAnimators(@NonNull List<Animator> animators, int position, boolean isForward) {
            if (position % 2 != 0)
                AnimatorHelper.slideInFromRightAnimator(animators, itemView, mAdapter.getRecyclerView(), 0.5f);
            else
                AnimatorHelper.slideInFromLeftAnimator(animators, itemView, mAdapter.getRecyclerView(), 0.5f);
        }
    }
}
