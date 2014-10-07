package io.hackbros.invite.application;

import android.app.Application;
import android.util.Log;

import io.hackbros.invite.R;
import io.hackbros.invite.data.Event;
import io.hackbros.invite.data.Rsvp;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class InitApplication extends Application {
    private static final String TAG = InitApplication.class.getSimpleName();

  @Override
  public void onCreate() {
      super.onCreate();
      Parse.initialize(this, "JM1ObdhIVJuQSh1KogpvZRsU4nvcQkrcutOi9fO7", "s2Zd3ysZ5bL4UO8WDDCrj6djIheWjJHyMRSoYDn0");
      ParseFacebookUtils.initialize(getResources().getString(R.string.app_id));
      ParseObject.registerSubclass(Event.class);
      ParseObject.registerSubclass(Rsvp.class);

      ParsePush.subscribeInBackground("", new SaveCallback() {
          @Override
          public void done(ParseException e) {
              if (e != null) {
                  Log.d(TAG, "successfully subscribed to channel");
              }
              else {
                  Log.d(TAG, "failed to subscribe for push", e);
              }
          }
      });
  }
}
