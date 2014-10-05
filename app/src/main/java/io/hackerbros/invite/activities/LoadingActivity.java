package io.hackerbros.invite.activities;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;

import com.facebook.Request;
import com.facebook.HttpMethod;
import com.facebook.Response;

import com.parse.ParseFacebookUtils;

import io.hackerbros.invite.R;

public class LoadingActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        initDataStuff();
    }

    private void initDataStuff () {
        new Request(ParseFacebookUtils.getSession(), "/me/friends",
                null, HttpMethod.GET, new Request.Callback() {
                    public void onCompleted(Response response) {
                        Log.d("TAG", response.getRawResponse()); 
                        finishUp();
                    }
        }).executeAsync();
    }

    private void finishUp() {
        finish();
        startActivity(new Intent(this, NewsFeedActivity.class));
    }

}
