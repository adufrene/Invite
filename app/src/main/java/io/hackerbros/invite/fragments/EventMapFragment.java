package io.hackerbros.invite.fragments;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.os.Bundle;
import android.content.IntentSender;
import android.widget.Toast;
import android.util.Log;
import android.location.Location;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import io.hackerbros.invite.data.Event;
import io.hackerbros.invite.util.FacebookUtils;

import org.json.JSONObject;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import com.parse.ParseQuery;
import com.parse.ParseException;
import com.parse.FindCallback;
import com.parse.ParseUser;
import com.parse.ParseGeoPoint;

public class EventMapFragment extends SupportMapFragment implements TitledFragment, GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private static final String TAG = EventMapFragment.class.getSimpleName();

    private LocationClient mLocationClient;
    private ArrayList<LocationStruct> otherEvents = new ArrayList<LocationStruct>();

    public static final String BUNDLE_LATITUDE_KEY = "bundle_latitude_key";
    public static final String BUNDLE_LONGITUDE_KEY = "bundle_longitude_key";

    @Override
    public void onConnected(Bundle dataBundle) {
        if (getMap() != null) {
            initMap();
        }
    }

    @Override
    public void onDisconnected() {
        Toast.makeText(getActivity(), "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(
                        getActivity(),
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
            }
            catch (IntentSender.SendIntentException e) {
                Log.e(TAG, e.getClass().getSimpleName(), e);    
            }
        }
        else {
            Log.e(TAG, "No Resolution");
        }
    }

    @Override
    public String getTitle() {
        return "Map";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getActivity(), this, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);
        

        initMap();
        return v; 
    }

    @Override
    public void onStart() {
        super.onStart();
        mLocationClient.connect();
    }

    @Override
    public void onDestroy() {
        if (mLocationClient.isConnected()) {
            mLocationClient.disconnect();
        }
        super.onDestroy();
    }

    private void initMap() {
        if (!mLocationClient.isConnected()) {
            return;
        }

        Location location = mLocationClient.getLastLocation();
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        LatLng currLoc = new LatLng(lat, lng);

        ParseQuery query = ParseQuery.or(Arrays.asList(Event.getQuery().whereEqualTo(Event.PUBLIC_EVENT_KEY, true), 
                    Event.getQuery().whereContainedIn(Event.USERNAME_KEY, FacebookUtils.getFriendsAndMe())));
        ParseGeoPoint currLocation = new ParseGeoPoint(lat, lng);
        query.whereWithinMiles(Event.LOCATION_KEY, currLocation, 10); 
        query.findInBackground(new FindCallback<Event>() {
            public void done(List<Event> events, ParseException e) {
                if (e == null) {
                    for (Event event : events) {
                        ParseGeoPoint pt = event.getLocation();

                        plotPoint(new LocationStruct(new LatLng(pt.getLatitude(), pt.getLongitude()), event.getEventTitle()));
                    }
                }
                else {
                    Log.d(TAG, "Failure", e);

                }
            }
        });
        UiSettings settings = getMap().getUiSettings();
        settings.setAllGesturesEnabled(true);
        settings.setMyLocationButtonEnabled(true);

        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(currLoc, 16));        
        getMap().addMarker(new MarkerOptions().position(currLoc));
    }

    private void plotPoint(LocationStruct ls) {
        GoogleMap gMap = getMap();
        if (gMap == null) {
            return;
        }

        gMap.addMarker(new MarkerOptions()
                .position(ls.place)
                .title(ls.eventTitle)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
    }

    private static class LocationStruct {
        public LatLng place;
        public String eventTitle;

        public LocationStruct(LatLng place, String eventTitle) {
            this.place = place;
            this.eventTitle = eventTitle;
        }
    }
}
