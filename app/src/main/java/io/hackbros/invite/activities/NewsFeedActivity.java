package io.hackbros.invite.activities;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import io.hackbros.invite.R;
import io.hackbros.invite.fragments.TitledFragment;
import io.hackbros.invite.fragments.EventMapFragment;
import io.hackbros.invite.feed.NewsFeedFragment;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;

public class NewsFeedActivity extends InviteFragmentActivity {

    private static final String TAG = NewsFeedActivity.class.getSimpleName();
    private static final String DIALOG_ERROR = "dialog_error";

    private NewsFeedPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    NewsFeedFragment frag1;
    NewsFeedFragment frag2;
    EventMapFragment frag3;

    private boolean connected = false;

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CONNECTION_FAILURE_RESOLUTION_REQUEST:
                switch(resultCode) {
                    case Activity.RESULT_OK:
                        break;
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.news_feed_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_profile:
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
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

    private void showErrorDialog(int errorCode) {
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "errordialog");
    }

    private class NewsFeedPagerAdapter extends FragmentPagerAdapter {
        private Fragment[] fragments;
        public NewsFeedPagerAdapter(FragmentManager fm) {
            super(fm);

            frag1 = new NewsFeedFragment();
            Bundle args1 = new Bundle();
            args1.putSerializable(NewsFeedFragment.BUNDLE_FILTER_KEY, NewsFeedFragment.FilterTypes.PUBLIC);
            frag1.setArguments(args1);
            frag1.setRetainInstance(true);

            frag2 = new NewsFeedFragment();
            Bundle args2 = new Bundle();
            args2.putSerializable(NewsFeedFragment.BUNDLE_FILTER_KEY, NewsFeedFragment.FilterTypes.FRIENDS);
            frag2.setArguments(args2);
            frag2.setRetainInstance(true);

            frag3 = new EventMapFragment();
            frag3.setRetainInstance(true);
            
            fragments = new Fragment[] { frag1, frag2, frag3 };

            if(connected == true) {
                frag1.fetchEvents();
                frag2.fetchEvents();
                frag3.initMap();
            }
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
                   return "Public Feed";
                case 1:
                   return "Friends Feed";
                case 2:
                   return "Map";
            }
            return null;
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

    @Override
    public void onConnected(Bundle bundle) {
        super.onConnected(bundle);
        connected = true;

        if (frag1 != null && frag1.isAdded()) {
            frag1.fetchEvents();
        }

        if (frag2 != null && frag2.isAdded()) {
            frag2.fetchEvents();
        }

        if (frag3 != null && frag3.isAdded()) {
            frag3.initMap();
        }
    }
}
