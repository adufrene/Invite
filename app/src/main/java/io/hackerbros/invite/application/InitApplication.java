package io.hackerbros.invite.application;

import android.app.Application;

import io.hackerbros.invite.R;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;

public class InitApplication extends Application {

  @Override
  public void onCreate() {
      super.onCreate();
      Parse.initialize(this, "JM1ObdhIVJuQSh1KogpvZRsU4nvcQkrcutOi9fO7", "s2Zd3ysZ5bL4UO8WDDCrj6djIheWjJHyMRSoYDn0");
      ParseFacebookUtils.initialize(getResources().getString(R.string.app_id));
  }
}
