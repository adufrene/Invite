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
import io.hackerbros.invite.fragments.AddEventFragment;
import io.hackerbros.invite.fragments.NewsFeedFragment;

public class LoginFeedActivity extends SimpleFragmentActivity implements View.OnClickListener {
   private Button addNewEventButton;

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
    public void onResume() {
        super.onResume();

        // setting onCreate returns null because it doesn't allow enough time to inflate view,
        // is there a better way?
        addNewEventButton = (Button) findViewById(R.id.add_event_button);
        addNewEventButton.setOnClickListener(this);
    }

   @Override
   public void onClick(View v) {
      if (v.getId() == R.id.add_event_button) {
         addFragment(new AddEventFragment());
          addNewEventButton.setVisibility(View.GONE);
      }
   }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }
}
