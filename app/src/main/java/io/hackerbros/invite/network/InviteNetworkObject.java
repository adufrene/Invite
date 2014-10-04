package io.hackerbros.invite.network;

import com.parse.ParseObject;

import io.hackerbros.invite.data.Event;

public class InviteNetworkObject extends ParseObject {
    public static void submitEventObject(Event newEvent) {
        ParseObject newObj = new ParseObject("Events");
        newObj.add("Title", newEvent.getEventTitle());
        newObj.add("Description", newEvent.getEventDescription());
        newObj.add("isPublic", newEvent.isPublicEvent());
        newObj.saveInBackground();
    }
}
