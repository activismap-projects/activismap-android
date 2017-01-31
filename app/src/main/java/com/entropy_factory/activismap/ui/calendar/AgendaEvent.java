package com.entropy_factory.activismap.ui.calendar;

import com.entropy_factory.activismap.core.db.ActivisCategory;
import com.entropy_factory.activismap.core.db.ActivisType;
import com.entropy_factory.activismap.core.item.ActivisItem;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.github.tibolte.agendacalendarview.models.DayItem;
import com.github.tibolte.agendacalendarview.models.WeekItem;
import com.google.android.gms.maps.model.LatLng;

import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ander on 14/11/16.
 */
public class AgendaEvent implements CalendarEvent, ActivisItem {

    private static final String TAG = "CalendarEvent";

    private ActivisItem item;
    private Calendar mInstanceDay;
    private WeekItem mWeekReference;
    private DayItem mDayReference;

    public AgendaEvent(ActivisItem item) {
        this.item = item;
    }

    @Override
    public String getDescription() {
        return item.getDescription();
    }

    @Override
    public String getStatus() {
        return item.getStatus();
    }

    @Override
    public ActivisCategory[] getCategories() {
        return item.getCategories();
    }

    @Override
    public ActivisType getType() {
        return item.getType();
    }

    @Override
    public String getImageUrl() {
        return item.getImageUrl();
    }

    @Override
    public int getLatitudeE6() {
        return 0;
    }

    @Override
    public int getLongitudeE6() {
        return 0;
    }

    @Override
    public double getLatitude() {
        return item.getLatitude();
    }

    @Override
    public double getLongitude() {
        return item.getLongitude();
    }

    @Override
    public long getStartDate() {
        return item.getStartDate();
    }

    @Override
    public long getEndDate() {
        return item.getEndDate();
    }

    @Override
    public long getCreatorId() {
        return item.getCreatorId();
    }

    @Override
    public long getParticipants() {
        return item.getParticipants();
    }

    @Override
    public long getLikes() {
        return item.getLikes();
    }

    @Override
    public long getDislikes() {
        return item.getDislikes();
    }

    @Override
    public boolean isSubscribed() {
        return item.isSubscribed();
    }

    @Override
    public OverlayItem toOverlayItem() {
        return item.toOverlayItem();
    }

    @Override
    public Long save() {
        return item.save();
    }

    @Override
    public long getServerId() {
        return item.getServerId();
    }

    @Override
    public long getCreatedDate() {
        return item.getCreatedDate();
    }

    @Override
    public String getIdentifier() {
        return item.getIdentifier();
    }

    @Override
    public long getId() {
        return item.getServerId();
    }

    @Override
    public void setId(long mId) {

    }

    @Override
    public Calendar getStartTime() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(item.getStartDate());
        return c;
    }

    @Override
    public void setStartTime(Calendar mStartTime) {

    }

    @Override
    public Calendar getEndTime() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(item.getEndDate());
        return c;
    }

    @Override
    public void setEndTime(Calendar mEndTime) {

    }

    @Override
    public String getTitle() {
        return item.getTitle();
    }

    @Override
    public void setTitle(String mTitle) {

    }

    @Override
    public Calendar getInstanceDay() {
        return mInstanceDay;
    }

    @Override
    public void setInstanceDay(Calendar mInstanceDay) {
        this.mInstanceDay = mInstanceDay;
        this.mInstanceDay.set(Calendar.HOUR, 0);
        this.mInstanceDay.set(Calendar.MINUTE, 0);
        this.mInstanceDay.set(Calendar.SECOND, 0);
        this.mInstanceDay.set(Calendar.MILLISECOND, 0);
        this.mInstanceDay.set(Calendar.AM_PM, 0);
    }

    @Override
    public DayItem getDayReference() {
        return mDayReference;
    }

    @Override
    public void setDayReference(DayItem mDayReference) {
        this.mDayReference = mDayReference;
    }

    @Override
    public WeekItem getWeekReference() {
        return mWeekReference;
    }

    @Override
    public void setWeekReference(WeekItem mWeekReference) {
        this.mWeekReference = mWeekReference;
    }

    @Override
    public AgendaEvent copy() {
        return new AgendaEvent(item);
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(getLatitude(), getLongitude());
    }

    public static List<CalendarEvent> from(List<? extends ActivisItem> itemList) {
        List<CalendarEvent> events = new ArrayList<>();
        for (ActivisItem i : itemList) {
            events.add(new AgendaEvent(i));
        }

        return events;
    }

    public static List<CalendarEvent> from(ActivisItem... itemList) {
        List<CalendarEvent> events = new ArrayList<>();
        for (ActivisItem i : itemList) {
            events.add(new AgendaEvent(i));
        }

        return events;
    }
}
