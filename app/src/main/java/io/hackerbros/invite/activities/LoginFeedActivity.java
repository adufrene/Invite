package io.hackerbros.invite.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Button;

import io.hackerbros.invite.login.LoginFragment;
import io.hackerbros.invite.R;
import io.hackerbros.invite.R;
import io.hackerbros.invite.event.AddEventFragment;
import io.hackerbros.invite.fragments.NewsFeedFragment;
import io.hackerbros.invite.util.SharedPrefsUtils;

import com.parse.ParseFacebookUtils;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;

public class LoginFeedActivity extends SimpleFragmentActivity implements View.OnClickListener {
    private Button addNewEventButton;
    private boolean isLaunchLogin_DEBUG = false; // FALSE will skip login screen
    private AddEventFragment eventFrag;

    public Fragment getFragment() {
        if (ParseUser.getCurrentUser() == null || ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            return LoginFragment.createFragment();
        } else {
            return new NewsFeedFragment();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_feed);
    }

    @Override
    public void onResume() {
        super.onResume();

        // setting onCreate returns null because it doesn't allow enough time to inflate view,
        // is there a better way?
        if (!isLaunchLogin_DEBUG) {
            addNewEventButton = (Button) findViewById(R.id.add_event_button);
            addNewEventButton.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_event_button) {
            addFragment(eventFrag = new AddEventFragment());
            addNewEventButton.setVisibility(View.GONE);
        }
    }

        /*
       if (SharedPrefsUtils.getBool(this, SharedPrefsUtils.LOGGED_IN_KEY, false))
        return new NewsFeedFragment();
       else
        return LoginFragment.createFragment();
        */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }

    @Override
    public void addEventSubmitCompleteCallback() {
        removeFragment(eventFrag);
    }
}
