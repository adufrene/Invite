package io.hackbros.invite.data;

import java.util.Date;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;

@ParseClassName("Events")
public class Event extends ParseObject {
    public static final String EVENT_TITLE_KEY = "Title";
    public static final String EVENT_DESCRIPTION_KEY = "Description";
    public static final String DATE_TIME_KEY = "Timestamp";
    public static final String START_DATE_TIME_KEY = "StartTime";
    public static final String END_DATE_TIME_KEY = "EndTime";
    public static final String LOCATION_KEY = "Location";
    public static final String PUBLIC_EVENT_KEY = "isPublic";
    public static final String FB_ID_KEY = "FacebookId";
    public static final String RSVP_COUNT_KEY = "RsvpCount";

    public String getEventTitle() {
        return getString(EVENT_TITLE_KEY);
    }

    public void setEventTitle(String eventTitle) {
        put(EVENT_TITLE_KEY, eventTitle);
    }

    public String getEventDescription() {
        return getString(EVENT_DESCRIPTION_KEY); 
    }

    public void setEventDescription(String eventDescription) {
        put(EVENT_DESCRIPTION_KEY, eventDescription);
    }

    public Date getDateTime() {
        return new Date(getLong(DATE_TIME_KEY));
    }

    public void setDateTime(Date dateTime) {
        put(DATE_TIME_KEY, dateTime.getTime());
    }

    public Date getStartDateTime() {
        return new Date(getLong(START_DATE_TIME_KEY));
    }

    public void setStartDateTime(Date dateTime) {
        put(START_DATE_TIME_KEY, dateTime.getTime());
    }

    public Date getEndDateTime() {
        return new Date(getLong(END_DATE_TIME_KEY));
    }

    public void setEndDateTime(Date dateTime) {
        put(END_DATE_TIME_KEY, dateTime.getTime());
    }

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint(LOCATION_KEY);
    }

    public void setLocation(InviteGeoLocation location) {
        put(LOCATION_KEY, location);
    }

    public boolean isPublicEvent() {
        return getBoolean(PUBLIC_EVENT_KEY);
    }

    public void setPublicEvent(boolean publicEvent) {
        put(PUBLIC_EVENT_KEY, publicEvent);
    }

    public String getFacebookId() {
        return getString(FB_ID_KEY);
    }

    public void setFacebookId(String id) {
        put(FB_ID_KEY, id);
    }

    public int getRsvpCount() {
        return getInt(RSVP_COUNT_KEY);
    }

    public void setRsvpCount(int count) {
        put(RSVP_COUNT_KEY, count);
    }

    public void incrementRsvp() {
        increment(RSVP_COUNT_KEY);
    }

    public void decrementRsvp() {
        increment(RSVP_COUNT_KEY, new Number() {
            @Override
            public double doubleValue() {
                return -1.0;
            }

            @Override
            public float floatValue() {
                return -1.0f;
            }

            @Override
            public int intValue() {
                return -1;
            }

            @Override
            public long longValue() {
                return -1;
            }
        });
    }

    public static ParseQuery<Event> getQuery() {
        return ParseQuery.getQuery(Event.class);
    }
}
