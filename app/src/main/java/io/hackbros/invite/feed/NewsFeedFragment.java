package io.hackbros.invite.feed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import io.hackbros.invite.R;
import io.hackbros.invite.activities.EventActivity;
import io.hackbros.invite.activities.NewsFeedActivity;
import io.hackbros.invite.data.Event;
import io.hackbros.invite.data.InviteGeoLocation;
import io.hackbros.invite.data.Rsvp;
import io.hackbros.invite.fragments.TitledFragment;
import io.hackbros.invite.util.FacebookUtils;
import io.hackbros.invite.view_adapters.SpinnerListAdapter;

import com.parse.ParseUser;
import com.parse.ParseQueryAdapter;
import com.parse.ParseQuery;
import com.parse.ParseException;
import com.parse.FindCallback;

import com.facebook.widget.ProfilePictureView;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class NewsFeedFragment extends Fragment implements TitledFragment, SwipeRefreshLayout.OnRefreshListener {

    public static final String BUNDLE_FILTER_KEY = "bundle_filter_key";
    public static final int DEFAULT_SORT_DISTANCE = 25;

    public static String TAG = NewsFeedFragment.class.getSimpleName();

    public static enum FilterTypes {
        PUBLIC,
        FRIENDS,
        ALL
    }

    private static SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a - dd MMM yy");
    private ParseQueryAdapter<Event> eventAdapter;

    private NewsFeedActivity parentActivity;
    Spinner distanceSelector;

    public NewsFeedFragment() {
        // Required empty public constructor
    }

    private FilterTypes filter;
    private SwipeRefreshLayout srl;
    private HashSet<String> rsvpdEvents = new HashSet<String>();
    private ListView lv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_news_feed, container, false);
        parentActivity = (NewsFeedActivity) getActivity();

        srl = (SwipeRefreshLayout) v.findViewById(R.id.swipe_layout);
        srl.setOnRefreshListener(this);
        int firstColor = R.color.refresh_blue;
        int secondColor = R.color.background_color;
        srl.setColorScheme(firstColor, secondColor, firstColor, secondColor);
        Bundle args = getArguments();
        filter = (FilterTypes) args.getSerializable(BUNDLE_FILTER_KEY);

        lv = (ListView) v.findViewById(R.id.news_feed);
        ViewGroup feedHeader = (ViewGroup) inflater.inflate(R.layout.spinner_list_header, lv, false);
        lv.addHeaderView(feedHeader);

        ParseQuery rsvpQuery = Rsvp.getQuery();
        rsvpQuery.whereEqualTo(Rsvp.USER_ID_KEY, ParseUser.getCurrentUser().getObjectId());

        rsvpQuery.findInBackground(new FindCallback<Rsvp>() {
          @Override
          public void done(List<Rsvp> rsvps, ParseException e) {
              if (e == null) {
                  for (Rsvp rsvp : rsvps) {
                      rsvpdEvents.add(rsvp.getEvent());
                  }
              }
              else {
                Log.e(TAG, "Error fetching rsvps", e);
              }
              fetchEvents(DEFAULT_SORT_DISTANCE);
          }
        });

        initDistanceSpinner(v);

        return v;
    }

    private void initDistanceSpinner(View view) {
        final int[] distancesList = {1, 2, 5, 10, 25, 50};
        final SpinnerListAdapter adapter = new SpinnerListAdapter(parentActivity.getApplicationContext(), distancesList);
        Spinner spin = (Spinner) view.findViewById(R.id.distance_spinner);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fetchEvents(distancesList[i]);
                adapter.setCurSelected(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spin.setAdapter(adapter);
        spin.setSelection(SpinnerListAdapter.DEFUALT_SELECTED_DISTANCE_VAL);
    }

    public void fetchEvents(final int milesRestriction) {
        if(!parentActivity.mLocationClient.isConnected()) {
            return;
        }

        ParseQueryAdapter.QueryFactory<Event> factory = new ParseQueryAdapter.QueryFactory<Event>() {
            @Override
            public ParseQuery<Event> create() {
                double lat = parentActivity.mLocationClient.getLastLocation().getLatitude();
                double lng = parentActivity.mLocationClient.getLastLocation().getLongitude();
                ParseQuery<Event> query = Event.getQuery();
                if (filter == FilterTypes.PUBLIC)
                    query.whereEqualTo(Event.PUBLIC_EVENT_KEY, true);
                else if (filter == FilterTypes.FRIENDS)
                    query.whereContainedIn(Event.FB_ID_KEY, FacebookUtils.getFriendsAndMe());
                query.orderByAscending(Event.START_DATE_TIME_KEY)
                    .whereWithinMiles(Event.LOCATION_KEY, new InviteGeoLocation(lat, lng), milesRestriction);
                return query;
            }
        };

        // Add factory when date time is valid
        eventAdapter = new ParseQueryAdapter<Event>(getActivity(), factory) {
            @Override
            public View getItemView(final Event event, View view, ViewGroup parent) {
                srl.setRefreshing(false);
                if (view == null) {
                    view = View.inflate(getActivity(), R.layout.event_row, null);
                }

                ProfilePictureView ppv = (ProfilePictureView) view.findViewById(R.id.profile_image);
                ppv.setProfileId(event.getFacebookId());

                TextView title = (TextView) view.findViewById(R.id.event_title);
                TextView description = (TextView) view.findViewById(R.id.event_description);

                TextView location = (TextView) view.findViewById(R.id.event_location);
                TextView time = (TextView) view.findViewById(R.id.event_start_time);
                final TextView rsvpCount = (TextView) view.findViewById(R.id.rsvp_count);
                final Button rsvpButton = (Button) view.findViewById(R.id.event_rsvp_button);

                rsvpButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Rsvp().setEvent(event.getObjectId()).setUser(ParseUser.getCurrentUser().getObjectId()).saveInBackground();
                        event.incrementRsvp();
                        rsvpCount.setText(String.valueOf(event.getRsvpCount()));
                        event.saveInBackground();
                        rsvpButton.setClickable(false);
                        rsvpButton.setBackgroundResource(android.R.color.black);
                    }
                });

                if (rsvpdEvents.contains(event.getObjectId())) {
                        rsvpButton.setClickable(false);
                        rsvpButton.setBackgroundResource(android.R.color.black);
                }
                else {
                    rsvpButton.setClickable(true);
                    rsvpButton.setBackgroundResource(R.drawable.custom_rounded_button_style_blue);
                }

                title.setText(event.getEventTitle());
                description.setText(event.getEventDescription());
                location.setVisibility(View.GONE);
                time.setText(sdf.format(event.getStartDateTime()));
                rsvpCount.setText(String.valueOf(event.getRsvpCount()));

                return view;
            }
        };
        lv.setAdapter(eventAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (eventAdapter != null) {
//            eventAdapter.loadObjects();
//        }
    }

    @Override
    public String getTitle() {
        return "News Feed";
    }

    @Override
    public void onRefresh() {
        if (eventAdapter != null) {
            srl.setRefreshing(true);
            eventAdapter.loadObjects();
        }
    }

}
