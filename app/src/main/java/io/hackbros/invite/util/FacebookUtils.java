package io.hackbros.invite.util;

import java.util.HashSet;
import java.util.Collection;

public class FacebookUtils {

    private static String myFacebookId;
    private static HashSet<String> friendsList = new HashSet<String>();

    public static void setFacebookId(String id) {
        if (myFacebookId == null) {
            myFacebookId = id;
        }
    }

    public static String getFacebookId() {
        return myFacebookId;
    }

    public static void addFriend(String id) {
        friendsList.add(id);
    }

    public static void addFriends(Collection<String> ids) {
        friendsList.addAll(ids);
    }

    public static HashSet<String> getFriends() {
        return new HashSet<String>(friendsList);
    }

    public static HashSet<String> getFriendsAndMe() {
        HashSet<String> temp = getFriends();
        temp.add(getFacebookId());
        return temp;
    }
}
