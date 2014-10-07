package io.hackbros.invite.data;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseClassName;

@ParseClassName("Rsvps")
public class Rsvp extends ParseObject {
    public static final String USER_ID_KEY = "UserId";
    public static final String EVENT_ID_KEY = "EventId";

    public String getUser() {
        return getString(USER_ID_KEY);
    }

    public Rsvp setUser(String userId) {
        put(USER_ID_KEY, userId);
        return this;
    }

    public String getEvent() {
        return getString(EVENT_ID_KEY);
    }

    public Rsvp setEvent(String eventId) {
        put(EVENT_ID_KEY, eventId);
        return this;
    }

    public static ParseQuery<Rsvp> getQuery() {
        return ParseQuery.getQuery(Rsvp.class);
    }
}
