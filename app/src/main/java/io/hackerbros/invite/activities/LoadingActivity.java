package io.hackerbros.invite.activities;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;

import com.facebook.Request;
import com.facebook.HttpMethod;
import com.facebook.Response;
import com.facebook.model.GraphUser;

import com.parse.ParseFacebookUtils;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import io.hackerbros.invite.R;
import io.hackerbros.invite.util.FacebookUtils;

public class LoadingActivity extends Activity {
    private static final String TAG = LoadingActivity.class.getSimpleName();

    boolean friendsLoaded = false;
    boolean meLoaded = false;

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
                        Log.d(TAG, response.getRawResponse()); 
                        try {
                            JSONObject obj = new JSONObject(response.getRawResponse());
                            JSONArray friendsArr = obj.getJSONArray("data");
                            for (int i = 0; i < friendsArr.length(); ++i) {
                                FacebookUtils.addFriend(friendsArr.getJSONObject(i).getString("id"));
                            }
                        }
                        catch (JSONException e) {
                            Log.e(TAG, "JSONException", e);
                        }
                        friendsLoaded = true;
                        finishUp();
                    }
        }).executeAsync();

        Request.newMeRequest(ParseFacebookUtils.getSession(), new Request.GraphUserCallback() {
            @Override
            public void onCompleted(GraphUser user, Response response) {
                FacebookUtils.setFacebookId(user.getId());
                Log.d(TAG, "Id: " + user.getId());
                meLoaded = true;
                finishUp();
            }
        }).executeAsync();
    }

    private void finishUp() {
        if (friendsLoaded && meLoaded) {
            finish();
            startActivity(new Intent(this, NewsFeedActivity.class));
        }
    }

}
