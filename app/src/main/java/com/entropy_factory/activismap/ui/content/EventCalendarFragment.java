package com.entropy_factory.activismap.ui.content;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.entropy_factory.activismap.R;
import com.entropy_factory.activismap.core.db.ActivisEvent;
import com.entropy_factory.activismap.ui.calendar.ActivisRenderer;
import com.entropy_factory.activismap.ui.calendar.AgendaEvent;
import com.entropy_factory.activismap.ui.base.FragmentContext;
import com.github.tibolte.agendacalendarview.AgendaCalendarView;
import com.github.tibolte.agendacalendarview.CalendarPickerController;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.github.tibolte.agendacalendarview.models.DayItem;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by ander on 29/09/16.
 */
public class EventCalendarFragment extends FragmentContext implements CalendarPickerController {

    private static final String TAG = "ActivitiesFragment";

    private AgendaCalendarView agenda;
    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, null);
    }

    @Override
    public void afterInitialize(View view, Bundle savedInstanceState) {
        agenda = findViewById(R.id.agenda);

        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();

        maxDate.add(Calendar.MONTH, 3);

        List<CalendarEvent> eventList = AgendaEvent.from(ActivisEvent.getSubscribed());

        agenda.init(eventList, minDate, maxDate, Locale.getDefault(), this);
        agenda.addEventRenderer(new ActivisRenderer());
    }

    @Override
    public void onDaySelected(DayItem dayItem) {

    }

    @Override
    public void onEventSelected(CalendarEvent event) {
        Intent eventIntent = new Intent(getActivity(), EventActivity.class);
        eventIntent.putExtra("id", event.getId());
        startActivity(eventIntent);
    }

    @Override
    public void onScrollToDate(Calendar calendar) {

    }
}
