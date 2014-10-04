package io.hackerbros.invite.activities;

import android.app.Fragment;
import android.os.Bundle;

import io.hackerbros.invite.R;
import io.hackerbros.invite.fragments.NewsFeedFragment;

public class LoginFeedActivity extends SimpleFragmentActivity {
    public Fragment getFragment() {
        return new NewsFeedFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_feed);
    }


}
