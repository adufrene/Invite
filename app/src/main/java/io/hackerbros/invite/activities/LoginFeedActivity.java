package io.hackerbros.invite.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.content.Intent;

import io.hackerbros.invite.login.LoginFragment;
import io.hackerbros.invite.R;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;

import io.hackerbros.invite.R;
import io.hackerbros.invite.fragments.NewsFeedFragment;

public class LoginFeedActivity extends SimpleFragmentActivity {
    public Fragment getFragment() {
        // if not logged in then return LoginFragment.createFragment();
        return new NewsFeedFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_feed);
        Parse.initialize(this, "JM1ObdhIVJuQSh1KogpvZRsU4nvcQkrcutOi9fO7", "s2Zd3ysZ5bL4UO8WDDCrj6djIheWjJHyMRSoYDn0");
        ParseFacebookUtils.initialize(getResources().getString(R.string.app_id));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }
}
