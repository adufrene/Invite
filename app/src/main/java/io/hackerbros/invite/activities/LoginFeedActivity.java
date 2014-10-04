package io.hackerbros.invite.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Button;

import io.hackerbros.invite.login.LoginFragment;
import io.hackerbros.invite.R;

import com.parse.ParseFacebookUtils;

import io.hackerbros.invite.R;
import io.hackerbros.invite.event.AddEventFragment;
import io.hackerbros.invite.fragments.NewsFeedFragment;

public class LoginFeedActivity extends SimpleFragmentActivity {
   private Button addNewEventButton;
   private boolean isLaunchLogin_DEBUG = true;

   public Fragment getFragment() {
      // if not logged in then return LoginFragment.createFragment();
//      return LoginFragment.createFragment();
        return new NewsFeedFragment();
   }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }
}
