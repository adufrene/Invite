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
import io.hackerbros.invite.util.SharedPrefsUtils;
import io.hackerbros.invite.activities.NewsFeedActivity;

import com.parse.ParseFacebookUtils;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;

public class LoginFeedActivity extends SimpleFragmentActivity {
    private Button addNewEventButton;
    private AddEventFragment eventFrag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ParseUser.getCurrentUser() != null && !ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            startActivity(new Intent(this, NewsFeedActivity.class));
        }
    }

    public Fragment getFragment() {
        return LoginFragment.createFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }

    public void addEventSubmitCompleteCallback() {
        removeFragment(eventFrag);
    }
}
