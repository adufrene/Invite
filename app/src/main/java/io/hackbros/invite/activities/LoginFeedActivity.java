package io.hackbros.invite.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Button;

import io.hackbros.invite.login.LoginFragment;
import io.hackbros.invite.R;
import io.hackbros.invite.R;
import io.hackbros.invite.event.AddEventFragment;
import io.hackbros.invite.util.SharedPrefsUtils;
import io.hackbros.invite.activities.NewsFeedActivity;

import com.parse.ParseFacebookUtils;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;

public class LoginFeedActivity extends SimpleFragmentActivity {
    private Button addNewEventButton;
    private AddEventFragment eventFrag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ParseUser.getCurrentUser() != null && !ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())
                && ParseFacebookUtils.getSession() != null) {
            startActivity(new Intent(this, LoadingActivity.class));
            finish();
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
}
