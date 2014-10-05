package io.hackerbros.invite.activities;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.app.Activity;
import android.app.Dialog;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.View;
import android.content.Intent;
import android.util.Log;
import android.location.Location;
import android.widget.Toast;
import android.content.IntentSender;

import io.hackerbros.invite.R;
import io.hackerbros.invite.fragments.TitledFragment;
import io.hackerbros.invite.fragments.ProfileFragment;
import io.hackerbros.invite.fragments.EventMapFragment;
import io.hackerbros.invite.feed.NewsFeedFragment;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;

public class NewsFeedActivity extends FragmentActivity implements 
    GooglePlayServicesClient.ConnectionCallbacks,
    GooglePlayServicesClient.OnConnectionFailedListener {

    private static final String TAG = NewsFeedActivity.class.getSimpleName();
    private static final String DIALOG_ERROR = "dialog_error";

    private NewsFeedPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private static LocationClient mLocationClient;

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        mLocationClient = new LocationClient(this, this, this);

        findViewById(R.id.add_event_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewsFeedActivity.this, EventActivity.class));
            }
        });

        mPagerAdapter = new NewsFeedPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                getActionBar().setSelectedNavigationItem(position);
            }
        });
        mViewPager.setAdapter(mPagerAdapter);

        final ActionBar actionBar = getActionBar();

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

            }

            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

            }
        };

        for (int i = 0; i < 3; ++i) {
            actionBar.addTab(
                    actionBar.newTab()
                        .setText(mPagerAdapter.getPageTitle(i))
                        .setTabListener(tabListener));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLocationClient.connect();
    }

    @Override
    protected void onStop() {
        mLocationClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CONNECTION_FAILURE_RESOLUTION_REQUEST:
                switch(resultCode) {
                    case Activity.RESULT_OK:
                        break;
                }
        }
    }

    private boolean servicesConnected() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (ConnectionResult.SUCCESS == resultCode) {
            return true;
        }
        else {
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, CONNECTION_FAILURE_RESOLUTION_REQUEST);

            if (errorDialog != null) {
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(errorDialog);
                errorFragment.show(getSupportFragmentManager(), "Location Updates");
            }
        }
        return false;
    }

    @Override
    public void onConnected(Bundle dataBundle) {
        mPagerAdapter.setLatLng(mLocationClient.getLastLocation());
    }

    @Override
    public void onDisconnected() {
        Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
            }
            catch (IntentSender.SendIntentException e) {
                Log.e(TAG, e.getClass().getSimpleName(), e);    
            }
        }
        else {
            showErrorDialog(connectionResult.getErrorCode());
        }
    }

    private void showErrorDialog(int errorCode) {
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "errordialog");
    }

    private static class NewsFeedPagerAdapter extends FragmentPagerAdapter {
        private Fragment[] fragments;
        public NewsFeedPagerAdapter(FragmentManager fm) {
            super(fm);
            
            fragments = new Fragment[] { new NewsFeedFragment(), new ProfileFragment(), new EventMapFragment() };
        }

        @Override
        public Fragment getItem(int i) {
            return fragments[i];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                   return "News Feed";
                case 1:
                   return "Profile";
                case 2:
                   return "Map";
            }
            return null;
        }

        public void setLatLng(Location loc) {
            double lat = loc.getLatitude();
            double lng = loc.getLongitude();
            Bundle args = new Bundle();
            args.putDouble(EventMapFragment.BUNDLE_LATITUDE_KEY, lat);
            args.putDouble(EventMapFragment.BUNDLE_LONGITUDE_KEY, lng);
            fragments[2].setArguments(args);
        }
    }

    public static class ErrorDialogFragment extends DialogFragment {
        private Dialog mDialog;
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }
}
