package com.entropy_factory.activismap.ui.calendar;

import android.view.View;
import android.widget.TextView;

import com.entropy_factory.activismap.R;
import com.entropy_factory.activismap.widget.ItemClassificationView;
import com.entropy_factory.activismap.widget.ProfileImageView;
import com.entropy_factory.activismap.widget.RemoteImageView;
import com.github.tibolte.agendacalendarview.render.EventRenderer;

/**
 * Created by ander on 14/11/16.
 */
public class ActivisRenderer extends EventRenderer<AgendaEvent> {

    private static final String TAG = "ActivisRenderer";

    @Override
    public void render(View view, AgendaEvent event) {
        RemoteImageView image = (RemoteImageView) view.findViewById(R.id.image);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView description = (TextView) view.findViewById(R.id.description);
        ItemClassificationView classificationView = (ItemClassificationView) view.findViewById(R.id.classification);

        image.loadRemoteImage(event.getImageUrl());
        title.setText(event.getTitle());
        description.setText(event.getDescription());
        classificationView.setFrom(event);
    }

    @Override
    public int getEventLayout() {
        return R.layout.calendar_row;
    }
}
