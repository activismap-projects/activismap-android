package com.entropy_factory.activismap.ui.adapter;

import com.entropy_factory.activismap.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ander on 10/11/16.
 */
public class CommentItem {

    private static final String TAG = "CommentItem";

    private long date;
    private String comment;

    public CommentItem(long date, String comment) {
        this.date = date;
        this.comment = comment;
    }

    public long getDate() {
        return date;
    }

    public String getDateString() {
        return TimeUtils.getTimeString(date);
    }

    public String getComment() {
        return comment;
    }

    public static List<CommentItem> fake(int items) {
        List<CommentItem> list = new ArrayList<>();
        for (int x = 0; x < items; x++) {
            list.add(new CommentItem(System.currentTimeMillis() - (x+1) *20000, "Comentario nº " + x + ", pero es mas fake que cualquier otra cosa"));
        }

        return list;
    }
}
