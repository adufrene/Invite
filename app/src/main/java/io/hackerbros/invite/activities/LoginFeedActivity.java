package io.hackerbros.invite.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.content.Intent;

import io.hackerbros.invite.login.LoginFragment;
import io.hackerbros.invite.R;

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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }
}
