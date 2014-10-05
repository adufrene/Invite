package io.hackerbros.invite.fragments;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.os.Bundle;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class EventMapFragment extends SupportMapFragment implements TitledFragment {

    public static final String BUNDLE_LATITUDE_KEY = "bundle_latitude_key";
    public static final String BUNDLE_LONGITUDE_KEY = "bundle_longitude_key";

    private LatLng currLoc;

    @Override
    public String getTitle() {
        return "Map";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);

        Bundle args = getArguments();
        double lat = args.getDouble(BUNDLE_LATITUDE_KEY);
        double lng = args.getDouble(BUNDLE_LONGITUDE_KEY);
        currLoc = new LatLng(lat, lng);
        initMap();
        return v; 
    }

    private void initMap() {
        UiSettings settings = getMap().getUiSettings();
        settings.setAllGesturesEnabled(true);
        settings.setMyLocationButtonEnabled(true);

        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(currLoc, 16));        
        getMap().addMarker(new MarkerOptions().position(currLoc));
    }
}
