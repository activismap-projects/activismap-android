package com.entropy_factory.activismap.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.entropy_factory.activismap.R;
import com.entropy_factory.activismap.core.item.Comment;
import com.entropy_factory.activismap.widget.ProfileView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ander on 13/04/16.
 */
public class CommentAdapter extends RecyclerAdapter<CommentAdapter.BaseViewHolder, Comment> {

    private static final String TAG = "CommentAdapter";

    public static final Comparator<Comment> COMMENT_SORTER = new Comparator<Comment>() {
        @Override
        public int compare(Comment comment, Comment t1) {
            return comment.getCreatedDate() < t1.getCreatedDate() ? 1 : -1 ;
        }
    };

    public CommentAdapter(Activity activity, List<Comment> list) {
        super(activity, list);
    }

    @Override
    public void addItem(Comment comment) {
        if (!contains(comment)) {
            super.addItem(comment);
            Log.d(TAG, "Item added!" + comment.getServerId());
        }

        sort(COMMENT_SORTER);
    }

    @Override
    public void addAll(Collection<? extends Comment> comments) {
        ArrayList<Comment> toAdd = new ArrayList<>();
        for (Comment c : comments) {
            if (!contains(c)) {
                toAdd.add(c);
                Log.d(TAG, "Item added!" + c.getServerId());
            }
        }

        super.addAll(toAdd);
        sort(COMMENT_SORTER);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = getLayoutInflater().inflate(R.layout.comment_row, parent, false);
        return new BaseViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        Comment item = getItem(position);

        holder.profileView.setName(item.getUser());
        holder.profileView.setAvatar(item.getUserAvatar());
        holder.text.setText(item.getComment());
        holder.date.setText(item.getDateString());
    }

    protected class BaseViewHolder extends RecyclerView.ViewHolder {
        protected TextView text;
        protected TextView date;
        protected ProfileView profileView;

        public BaseViewHolder(View v) {
            super(v);
            text = (TextView) v.findViewById(R.id.comment_text);
            date = (TextView) v.findViewById(R.id.comment_date);
            profileView = (ProfileView) v.findViewById(R.id.profile);
        }
    }
}
