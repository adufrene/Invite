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

public class LoginFeedActivity extends SimpleFragmentActivity {
    private Button addNewEventButton;
    private AddEventFragment eventFrag;

    public Fragment getFragment() {
        if (ParseUser.getCurrentUser() == null || ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            return LoginFragment.createFragment();
        } else {
            return new NewsFeedFragment();
        }
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
