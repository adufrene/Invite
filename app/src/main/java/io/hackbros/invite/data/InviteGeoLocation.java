package io.hackbros.invite.data;

import com.parse.ParseGeoPoint;

public class InviteGeoLocation extends ParseGeoPoint {
    public InviteGeoLocation(double latitude, double longitude) {
        super(latitude, longitude);
    }
}
