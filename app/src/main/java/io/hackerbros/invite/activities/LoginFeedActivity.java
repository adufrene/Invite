package io.hackerbros.invite.activities;

import android.app.Fragment;
import android.os.Bundle;

import io.hackerbros.invite.login.LoginFragment;

import com.parse.Parse;

public class LoginFeedActivity extends SimpleFragmentActivity {
  public Fragment getFragment() {
    return LoginFragment.createFragment();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Parse.initialize(this, "JM1ObdhIVJuQSh1KogpvZRsU4nvcQkrcutOi9fO7", "s2Zd3ysZ5bL4UO8WDDCrj6djIheWjJHyMRSoYDn0");
  }
}
